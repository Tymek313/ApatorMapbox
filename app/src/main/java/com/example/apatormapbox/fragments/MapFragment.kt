package com.example.apatormapbox.fragments

import android.annotation.SuppressLint
import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.example.apatormapbox.R
import com.example.apatormapbox.activities.MainActivity
import com.example.apatormapbox.helpers.AppConstants
import com.example.apatormapbox.helpers.DrawableToBitmap
import com.example.apatormapbox.helpers.Permissions
import com.example.apatormapbox.models.dbentities.StationBasicEntity
import com.example.apatormapbox.viewmodels.SolarViewModel
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdate
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.geometry.LatLngBounds
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.LocationComponentOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import kotlinx.android.synthetic.main.change_localization.*
import kotlinx.android.synthetic.main.fragment_map.view.*
import org.koin.android.viewmodel.ext.android.viewModel


class MapFragment : Fragment() {

    companion object {
        private const val STATION_POINTS_LAYER = "STATION_POINTS"
    }

    private lateinit var mapView: MapView
    private val solarViewModel: SolarViewModel by viewModel()
    private val geoJsonSource = GeoJsonSource("SOURCE_ID")
    private lateinit var mapboxMap: MapboxMap
    var latMarker: Double? = null
    var lonMarker: Double? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_map, container, false)
        if(arguments != null){
            latMarker = arguments!!.getDouble("lat")
            lonMarker = arguments!!.getDouble("lon")
            Log.d("MapFragment: ", "$latMarker")
            Log.d("MapFragment: ", "$lonMarker")
        }

        Permissions.handlePermission(
            this,
            context!!,
            Manifest.permission.ACCESS_FINE_LOCATION,
            AppConstants.PermissionConstants.LOCATION_PERMISSION.value
        )

        view.locate_device_btn.setOnClickListener {
            onLocationBtnClick(it)
        }
        mapView = view.mapView

        //PRZYGOTOWANIE TOOLBARA
        (activity as MainActivity).apply {
            setHasOptionsMenu(true)
            setSupportActionBar(view.mapToolbar)
            supportActionBar.apply {
                this?.setHomeAsUpIndicator(R.drawable.ic_settings)
                this?.setDisplayHomeAsUpEnabled(true)
            }
        }

        mapView = view.mapView

        mapView.apply {
            getMapAsync {
                onMapReady(it)
                onMapClick()
            }
            onCreate(savedInstanceState)
        }



        solarViewModel.fetchStationsFromDb()
        solarViewModel.stations.observe(this, Observer {
            onDataChanged(it)
        })

        return view
    }

    /*
     * ON DATA CHANGE
     */
    fun onMapClick(){
        if(latMarker != null || lonMarker != null){
            val position = CameraPosition.Builder()
                .target(LatLng(latMarker!!, lonMarker!!))
                .zoom(6.0)
                .tilt(00.0)
                .build()
            mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 1250)
        }else{
            val position = CameraPosition.Builder()
                .target(LatLng(90.0, -100.0))
                .zoom(0.0)
                .tilt(00.0)
                .build()
            mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 1250)
        }
    }
    fun onDataChanged(stationBasicEntities: List<StationBasicEntity>) {
        //Log.d("pobrano stacje", it.toString())
        val symbols = ArrayList<Feature>()
        stationBasicEntities.forEach {
            val feature = Feature.fromGeometry(Point.fromLngLat(it.lon!!, it.lat!!))
            feature.addStringProperty("id", it.id)
            symbols.add(feature)
        }
        geoJsonSource.setGeoJson(FeatureCollection.fromFeatures(symbols))
    }

    fun onMarkerClick(point: LatLng): Boolean {
        val touchPoint = mapboxMap.projection.toScreenLocation(point)
        val features = mapboxMap.queryRenderedFeatures(touchPoint, STATION_POINTS_LAYER)
        if (features.isNotEmpty()) {
            val selectedFeature = features[0]
            val bundle = Bundle()
            bundle.putString("stationId", selectedFeature.getStringProperty("id"))
            Navigation.findNavController(activity!!, R.id.navHost).navigate(R.id.paszportFragment, bundle)
            //Toast.makeText(context, selectedFeature.getStringProperty("id"), Toast.LENGTH_SHORT).show()
        }
        return true
    }

    fun onMapReady(mapboxMap: MapboxMap) {
        this.mapboxMap = mapboxMap
        mapboxMap.addOnMapClickListener {
            onMarkerClick(it)
        }
        val markerBitmap =
            DrawableToBitmap.drawableToBitmap(ResourcesCompat.getDrawable(resources, R.drawable.ic_marker, null)!!)!!
        mapboxMap.setStyle(
            Style.Builder().fromUrl("mapbox://styles/mapbox/cjf4m44iw0uza2spb3q0a7s41")
                .withSource(geoJsonSource)
                .withImage("ICON_ID", markerBitmap)
                .withLayer(
                    SymbolLayer(STATION_POINTS_LAYER, "SOURCE_ID")
                        .withProperties(
                            PropertyFactory.iconImage("ICON_ID"),
                            iconAllowOverlap(true),
                            iconOffset(arrayOf(0f, -9f))
                        )
                )
        ) { style -> onStyleLoaded(style) }
    }

    @SuppressWarnings("MissingPermission")
    private fun enableLocationComponent(loadedMapStyle: Style) {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(context!!)) {

            // Get an instance of the component
            val locationComponent = mapboxMap.locationComponent

            // Activate with options
            locationComponent.activateLocationComponent(
                LocationComponentActivationOptions.builder(context!!, loadedMapStyle).build()
            )

            // Enable to make component visible
            locationComponent.isLocationComponentEnabled = true
/*
            // Set the component's camera mode
            locationComponent.caomeraM(CameraMode.TRACKING);

            // Set the component's render mode
            locationComponent.renderMode = RenderMode.COMPASS*/
        } else {

        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (grantResults.isEmpty()) {
            permissionRejected()
        }

        when (grantResults[0]) {
            PackageManager.PERMISSION_DENIED -> permissionRejected()
            PackageManager.PERMISSION_GRANTED -> mapboxMap.getStyle {
                enableLocationComponent(it)
            }
        }
    }

    fun permissionRejected() {
        Toast.makeText(context, "Localisation permissions are necessary", Toast.LENGTH_SHORT).show()
        activity!!.finish()
    }

    @SuppressLint("MissingPermission")
    fun onStyleLoaded(style: Style) {
        // logika wyswietlenia markera z aktualna lokalizacja
        // brak dodanego zapytania o uprawnienia do lokalizacji, aktualnie trzeba dac te uprawnienia recznie (o ile nie działa odrazu)
        if (PermissionsManager.areLocationPermissionsGranted(context)) {
            val customLocationComponentOptions = LocationComponentOptions.builder(context!!)
                .trackingGesturesManagement(true)
                .accuracyColor(ContextCompat.getColor(context!!, R.color.mapbox_gray))
                .build()
            if (mapboxMap.style != null) {
                val locationComponentActivationOptions =
                    LocationComponentActivationOptions.builder(context!!, style)
                        .locationComponentOptions(customLocationComponentOptions)
                        .build()
                mapboxMap.locationComponent.apply {
                    activateLocationComponent(locationComponentActivationOptions)
                    isLocationComponentEnabled = true
                }
            }
        }
    }

    fun onLocationBtnClick(view: View?) {
        when (view?.id) {
            // przycisk lokalizacji, ustawienie kamery na aktualnej lokalizacji
            // pobieram lokalizacje do location ->
            // nastepnie mapuje ja na CameraPosition ->
            // i ustawiam pozycje kamery na aktualna lokalizacje
            R.id.locate_device_btn -> {
                //Navigation.findNavController(view).navigate(R.id.action_mapFragment_to_paszportFragment)
                val location = mapboxMap.locationComponent.lastKnownLocation!!
                val position = CameraPosition.Builder()
                    .target(LatLng(location.latitude, location.longitude))
                    .zoom(6.0)
                    .tilt(00.0)
                    .build()
                mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 1250)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.map_toolbar_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                Navigation.findNavController(activity!!, R.id.navHost).navigate(R.id.settingsFragment)
                Log.d("Map Fragment: ", "Przejście do ustawień")
                true
            }
            R.id.sync -> {
                Log.d("Map Fragment: ", "Synchronizacja")
                solarViewModel.fetchStationsFromApi(40, -105)
                true

//                // Implementacja okna dialogowego - zmiana planow
//                val mDialogView = LayoutInflater.from(context).inflate(R.layout.change_localization, null)
//                val mBuilder = AlertDialog.Builder(context)
//                    .setView(mDialogView)
//                    .setTitle("Change Localization")
//                val mAlertDialog = mBuilder.show()
//
//                mDialogView.change_btn_CL.setOnClickListener {
//                    val longitude = mDialogView.longitude_CL.text.toString()
//                    val latitude = mDialogView.latitude_CL.text.toString()
//                    mAlertDialog.dismiss()
//                }
//                mDialogView.cancel_btn_CL.setOnClickListener {
//                    mAlertDialog.dismiss()
//                }

                solarViewModel.fetchStationsFromApi(40, -105)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    //Cykle życia Mapbox
    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mapView.onDestroy()
    }
}
