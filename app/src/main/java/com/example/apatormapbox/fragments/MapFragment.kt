package com.example.apatormapbox.fragments

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.example.apatormapbox.R
import com.example.apatormapbox.activities.MainActivity
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.LocationComponentOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.example.apatormapbox.helpers.DrawableToBitmap
import com.example.apatormapbox.models.dbentities.StationBasicEntity
import com.example.apatormapbox.viewmodels.SolarViewModel
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.geometry.LatLngBounds
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import kotlinx.android.synthetic.main.fragment_map.view.*
import org.koin.android.viewmodel.ext.android.viewModel


class MapFragment : Fragment(), View.OnClickListener, OnMapReadyCallback, Observer<List<StationBasicEntity>> {

    private lateinit var mapView: MapView
    private lateinit var mapboxMap: MapboxMap
    private lateinit var position: CameraPosition
    private val solarViewModel: SolarViewModel by viewModel()
    private val geoJsonSource = GeoJsonSource("SOURCE_ID")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_map, container, false)
        view.locate_device_btn.setOnClickListener(this)
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

        mapView.apply {
            getMapAsync(this@MapFragment)
            onCreate(savedInstanceState)
        }

        solarViewModel.fetchStationsFromDb()
        solarViewModel.stations.observe(this, this)

        return view
    }

    /*
     * ON DATA CHANGE
     */
    override fun onChanged(stationBasicEntities: List<StationBasicEntity>) {
        //Log.d("pobrano stacje", it.toString())
        val symbols = ArrayList<Feature>()
        stationBasicEntities.forEach {
            symbols.add(Feature.fromGeometry(Point.fromLngLat(it.lon!!, it.lat!!)))
        }
        geoJsonSource.setGeoJson(FeatureCollection.fromFeatures(symbols))
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(mapboxMap: MapboxMap) {
        this.mapboxMap = mapboxMap
        // wyswietlenie markera pkt. stacji na mapie
        val markerBitmap =
            DrawableToBitmap.drawableToBitmap(ResourcesCompat.getDrawable(resources, R.drawable.ic_marker, null)!!)!!
        mapboxMap.setStyle(
            Style.Builder().fromUrl("mapbox://styles/mapbox/cjf4m44iw0uza2spb3q0a7s41")
                .withSource(geoJsonSource)
                .withImage("ICON_ID", markerBitmap)
                .withLayer(
                    SymbolLayer("LAYER_ID", "SOURCE_ID")
                        .withProperties(
                            PropertyFactory.iconImage("ICON_ID"),
                            iconAllowOverlap(true),
                            iconOffset(arrayOf(0f, -9f))
                        )
                )
        ) { style ->
            // logika wyswietlenia markera z aktualna lokalizacja
            // brak dodanego zapytania o uprawnienia do lokalizacji, aktualnie trzeba dac te uprawnienia recznie (o ile nie działa odrazu)
            if (PermissionsManager.areLocationPermissionsGranted(this.context)) {
                val customLocationComponentOptions = LocationComponentOptions.builder(this.context!!)
                    .trackingGesturesManagement(true)
                    .accuracyColor(ContextCompat.getColor(this.context!!, R.color.mapboxGreen))
                    .build()
                if (mapboxMap.style != null) {
                    val locationComponentActivationOptions =
                        LocationComponentActivationOptions.builder(this.context!!, style)
                            .locationComponentOptions(customLocationComponentOptions)
                            .build()
                    mapboxMap.locationComponent.apply {
                        activateLocationComponent(locationComponentActivationOptions)
                        isLocationComponentEnabled = true
                        cameraMode = CameraMode.TRACKING
                        renderMode = RenderMode.COMPASS
                    }
                }
                // wyciagniecie lokalizacji urzytkownika i zmapowanie na CameraPosition oraz przypisanie do zmiennej lokalnej
                val location = mapboxMap.locationComponent.lastKnownLocation!!
                position = CameraPosition.Builder()
                    .target(LatLng(location.latitude, location.longitude))
                    .zoom(10.0)
                    .tilt(20.0)
                    .build()
            }
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.locate_device_btn -> {
                Navigation.findNavController(view).navigate(R.id.action_mapFragment_to_paszportFragment)
                mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 10)
                //TODO zlokalizuj użytkownika
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
                Log.d("ustawienia", "Przejście do ustawień")
                true
            }
            R.id.sync -> {
                Log.d("sync", "Synchronizacja")

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
