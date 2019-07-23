package com.example.apatormapbox.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.preference.PreferenceManager
import com.example.apatormapbox.R
import com.example.apatormapbox.activities.MainActivity
import com.example.apatormapbox.helpers.*
import com.example.apatormapbox.models.dbentities.StationBasicEntity
import com.example.apatormapbox.viewmodels.SolarViewModel
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.LocationComponentOptions
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.expressions.Expression
import com.mapbox.mapboxsdk.style.expressions.Expression.get
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.*
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonOptions
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import kotlinx.android.synthetic.main.fragment_map.view.*
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber


class MapFragment : Fragment() {

    companion object {
        private const val STATION_POINTS_LAYER = "STATION_POINTS"
        private const val GEO_JSON_SOURCE_ID = "GEO_JSON_SOURCE_ID"
        private const val MARKER_ICON_ID = "MARKER_ICON_ID"
        private val GEO_JSON_OPTIONS = GeoJsonOptions()
            .withCluster(true)
            .withClusterMaxZoom(14)
            .withClusterRadius(10)
    }

    private lateinit var mapView: MapView
    private val solarViewModel: SolarViewModel by viewModel()
    private var geoJsonSource: GeoJsonSource = GeoJsonSource(GEO_JSON_SOURCE_ID)
    private lateinit var mapboxMap: MapboxMap
    //zapamietane ustwienia mapy aby je przywrocic przy powrocie z paszportu
    private var latMarker: Double? = null
    private var lonMarker: Double? = null
    private var mapZoom: Double = 0.0
    //globalna lista symboli bo postValue z viewModelu nadpisuje stare dane
    private val symbols = ArrayList<Feature>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        solarViewModel.stations.observe(this, Observer {
            onDataChanged(it)
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_map, container, false)
        if (arguments != null) {
            latMarker = arguments!!.getDouble("lat")
            lonMarker = arguments!!.getDouble("lon")
        }

        view.locate_device_btn.setOnClickListener {
            onLocationBtnClick(it)
        }

        geoJsonSource = GeoJsonSource(GEO_JSON_SOURCE_ID, GEO_JSON_OPTIONS)
        geoJsonSource.setGeoJson(FeatureCollection.fromFeatures(symbols))

        //przygotowanie toolbara
        (activity as MainActivity).apply {
            setHasOptionsMenu(true)
            setSupportActionBar(view.mapToolbar)
            supportActionBar.apply {
                this?.setHomeAsUpIndicator(R.drawable.ic_settings)
                this?.setDisplayHomeAsUpEnabled(true)
            }
        }

        mapView = view.mapView.apply {
            getMapAsync {
                onMapReady(it)
                onBackToMap()
            }
            onCreate(savedInstanceState)
        }

        solarViewModel.fetchStationsFromDb()
        return view
    }

    private fun onBackToMap() {
        if (latMarker != null || lonMarker != null) {
            val position = CameraPosition.Builder()
                .target(LatLng(latMarker!!, lonMarker!!))
                .zoom(mapZoom)
                .tilt(0.0)
                .build()
            mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 1250)
        } else {
            val position = CameraPosition.Builder()
                .target(LatLng(90.0, -100.0))
                .zoom(0.0)
                .tilt(0.0)
                .build()
            mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 1250)
        }
    }

    private fun onDataChanged(stationBasicEntities: List<StationBasicEntity>) {
        if (stationBasicEntities.isEmpty()) {
            Toast.makeText(context, getString(R.string.api_key_error_message), Toast.LENGTH_SHORT).show()
            return
        }
        if (symbols.isNotEmpty()) {
            symbols.clear()
        }
        stationBasicEntities.forEach {
            val feature = Feature.fromGeometry(Point.fromLngLat(it.lon!!, it.lat!!))
            feature.addStringProperty("id", it.id)
            feature.addNumberProperty("lon", it.lon)
            feature.addNumberProperty("lat", it.lat)
            symbols.add(feature)
        }
        geoJsonSource.setGeoJson(FeatureCollection.fromFeatures(symbols))
    }

    private fun onMarkerClick(point: LatLng): Boolean {
        val touchPoint = mapboxMap.projection.toScreenLocation(point)
        val features = mapboxMap.queryRenderedFeatures(touchPoint, STATION_POINTS_LAYER)

        if (features.isNotEmpty()) {
            val selectedFeature = features[0]
            if (selectedFeature.getBooleanProperty("cluster") == true) {
                val cameraPosition = CameraPosition.Builder()
                    .zoom(mapboxMap.cameraPosition.zoom + 2)
                    .target(point)
                    .build()
                mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 1250)
            } else {
                lonMarker = selectedFeature.getNumberProperty("lon") as Double
                latMarker = selectedFeature.getNumberProperty("lat") as Double
                mapZoom = mapboxMap.cameraPosition.zoom
                val bundle = Bundle()
                bundle.putString("stationId", selectedFeature.getStringProperty("id"))
                Navigation.findNavController(activity!!, R.id.navHost).navigate(R.id.paszportFragment, bundle)
            }
        }
        return true
    }

    private fun onMapReady(mapboxMap: MapboxMap) {
        this.mapboxMap = mapboxMap

        mapboxMap.addOnMapClickListener {
            onMarkerClick(it)
        }
        val markerBitmap =
            DrawableToBitmapHelper.drawableToBitmap(
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.ic_marker,
                    null
                )!!
            )!!
        mapboxMap.setStyle(
            Style.Builder().fromUrl(getString(R.string.map_url))
                .withSource(geoJsonSource)
                .withImage(MARKER_ICON_ID, markerBitmap)
                .withLayer(
                    SymbolLayer(STATION_POINTS_LAYER, GEO_JSON_SOURCE_ID)
                        .withProperties(
                            iconImage(MARKER_ICON_ID),
                            iconAllowOverlap(true),
                            iconOffset(arrayOf(0f, -9f)),
                            textField(Expression.toString(get("point_count"))),
                            textOffset(arrayOf(0f, 0.5f))
                        )
                )
        ) { style -> onStyleLoaded(style) }
    }

    @SuppressLint("MissingPermission")
    private fun onStyleLoaded(style: Style) {
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

    @SuppressWarnings("MissingPermission")
    private fun enableLocationComponent(loadedMapStyle: Style) {
        if (PermissionsManager.areLocationPermissionsGranted(context!!)) {
            val locationComponent = mapboxMap.locationComponent
            locationComponent.activateLocationComponent(
                LocationComponentActivationOptions.builder(context!!, loadedMapStyle).build()
            )
            locationComponent.isLocationComponentEnabled = true
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            AppConstants.LOCATION_PERMISSION -> {
                when (grantResults[0]) {
                    PackageManager.PERMISSION_GRANTED -> mapboxMap.getStyle { enableLocationComponent(it) }
                }
            }
        }
    }

    private fun onLocationBtnClick(view: View) {
        when (view.id) {
            R.id.locate_device_btn -> {
                val isGranted = PermissionsHelper.handlePermission(
                    this,
                    context!!,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    AppConstants.LOCATION_PERMISSION
                )
                if (isGranted) {
                    val location = mapboxMap.locationComponent.lastKnownLocation!!
                    val position = CameraPosition.Builder()
                        .target(LatLng(location.latitude, location.longitude))
                        .zoom(6.0)
                        .tilt(0.0)
                        .build()
                    mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 1250)
                }
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
                Timber.d("Przejście do ustawień")
                true
            }
            R.id.sync -> {
                Timber.d("Synchronizacja")
                if (ConnectivityHelper.isConnectedToNetwork(context!!)) {
                    solarViewModel.fetchAllStationsFromApi()
                    PreferenceManager.getDefaultSharedPreferences(context)
                        .edit()
                        .putString(
                            getString(R.string.sync_preference),
                            "${getString(R.string.last_sync)}: ${DateHelper.getToday()}"
                        )
                        .apply()
                } else {
                    Toast.makeText(context, R.string.no_internet_connection, Toast.LENGTH_SHORT).show()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    //Cykle życia Mapbox
    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }
}
