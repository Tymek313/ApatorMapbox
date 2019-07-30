package com.example.apatormapbox.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.*
import android.widget.Toast
import androidx.annotation.UiThread
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
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
import com.example.apatormapbox.models.earthquakes.Earthquakes
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
import com.mapbox.mapboxsdk.log.Logger
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.expressions.Expression
import com.mapbox.mapboxsdk.style.expressions.Expression.*
import com.mapbox.mapboxsdk.style.layers.CircleLayer
import com.mapbox.mapboxsdk.style.layers.HeatmapLayer
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.*
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonOptions
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import kotlinx.android.synthetic.main.fragment_map.view.*
import org.koin.android.viewmodel.ext.android.viewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.net.URI
import java.net.URL
import java.time.LocalDate
import java.time.format.DateTimeFormatter


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
    //globalne stałe do trzesnien ziemi
    private var from = getDate(1)
    private var to = getDate(0)
    private val minMagnitude = 4.3
    private lateinit var earthquakes: Earthquakes
    private val EARTHQUAKE_SOURCE_ID = "earthquakes"
    private val HEATMAP_LAYER_ID = "earthquakes-heat"
    private val HEATMAP_LAYER_SOURCE = "earthquakes"
    private val CIRCLE_LAYER_ID = "earthquakes-circle"

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
        setGeoJson()

        //przygotowanie toolbara
        (activity as MainActivity).apply {
            setHasOptionsMenu(true)
            setSupportActionBar(view.mapToolbar)
            supportActionBar.apply {
                this?.setHomeAsUpIndicator(R.drawable.ic_settings)
                this?.setDisplayHomeAsUpEnabled(true)
            }
        }

        mapView = view.mapView
        mapView.getMapAsync {
            onMapReady(it)
            onBackToMap()
        }

        solarViewModel.fetchStationsFromDb()
        return view
    }

    private fun fetchEarthquakesFromApi(startTime: String, endTime: String, magnitude: Double) {
        val api = Apifactory.solarApi
        api.getEarthquakes(startTime, endTime, magnitude).enqueue(object : Callback<Earthquakes> {
            override fun onFailure(call: Call<Earthquakes>, t: Throwable) {
                Logger.d("EARTH", t.message)
                t.printStackTrace()
            }

            override fun onResponse(call: Call<Earthquakes>, response: Response<Earthquakes>) {
                val endangeredSolars = ArrayList<StationBasicEntity>()
                solarViewModel.stations.observe(this@MapFragment, Observer { stationBasicEntityList ->
                    response.body()!!.features.forEach { feature ->
                        stationBasicEntityList.forEach { stationBasicEntity ->
                            if (
                                distanceInKmBetweenEarthCoordinates(
                                    stationBasicEntity.lat!!,
                                    stationBasicEntity.lon!!,
                                    feature.geometry.coordinates[1],
                                    feature.geometry.coordinates[0]
                                )
                                < 300
                            ) {
                                endangeredSolars.add(stationBasicEntity)
                            }
                        }
                    }
                    notifySolarDanger(endangeredSolars.distinct())
                })
            }
        })
    }

    private fun degreesToRadians(degrees: Double): Double {
        return degrees * Math.PI / 180
    }

    fun distanceInKmBetweenEarthCoordinates(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        var earthRadiusKm = 6371

        var dLat = degreesToRadians(lat2 - lat1)
        var dLon = degreesToRadians(lon2 - lon1)

        val nLat1 = degreesToRadians(lat1)
        val nLat2 = degreesToRadians(lat2)

        var a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(nLat1) * Math.cos(nLat2)
        var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        return earthRadiusKm * c

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

    private fun onDataChanged(stationBasicEntities: List<StationBasicEntity>?) {
        if (stationBasicEntities == null) {
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
        activity!!.runOnUiThread {
            setGeoJson()
        }
    }

    @UiThread
    private fun setGeoJson() {
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
        mapboxMap.uiSettings.apply {
            val topMargin =
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80f, resources.displayMetrics).toInt()
            val rightMargin =
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15f, resources.displayMetrics).toInt()
            compassGravity = Gravity.END
            setCompassMargins(0, topMargin, rightMargin, 0)
        }
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
        ) { style ->
            onStyleLoaded(style)
            val choice = PreferenceManager.getDefaultSharedPreferences(context)
                .getString(getString(R.string.time_window_preference), "1")
            when (choice) {
                "1" -> {
                    from = getDate(1)
                }
                "2" -> {
                    from = getDate(2)
                }
                "3" -> {
                    from = getDate(3)
                }
                "4" -> {
                    from = getDate(4)
                }
                "5" -> {
                    from = getDate(5)
                }
                "6" -> {
                    from = getDate(6)
                }
                "7" -> {
                    from = getDate(7)
                }
                else -> {
                    Timber.d("Błąd wyboru zakresu danych")
                }
            }

            val localDate = LocalDate.now().minusDays(0)
            Timber.d("${localDate.year}-${localDate.month}-${localDate.dayOfMonth} $localDate")
//            addClusteredGeoJsonSource(style)
            addEarthquakeSource(style)
            addHeatmapLayer(style)
            fetchEarthquakesFromApi(from, to, 4.3)
        }

    }

    private fun addHeatmapLayer(loadedMapStyle: Style) {
        val layer = HeatmapLayer(HEATMAP_LAYER_ID, EARTHQUAKE_SOURCE_ID)
        layer.maxZoom = 8.0F
        layer.sourceLayer = HEATMAP_LAYER_SOURCE
        layer.setProperties(
            heatmapColor(
                interpolate(
                    linear(), heatmapDensity(),
                    literal(0), rgba(33, 102, 172, 0),
                    literal(0.2), rgb(103, 169, 207),
                    literal(0.4), rgb(209, 229, 240),
                    literal(0.6), rgb(253, 219, 199),
                    literal(0.8), rgb(239, 138, 98),
                    literal(1), rgb(178, 24, 43)
                )
            ),

            heatmapOpacity(0.8F)
        )

        if (loadedMapStyle.getLayer("earthquakes-heat") != null) {
            loadedMapStyle.removeLayer("earthquakes-heat")
        }

        loadedMapStyle.addLayerAbove(layer, "waterway-label")
    }

    private fun addEarthquakeSource(loadedMapStyle: Style) {
        val EARTHQUAKE_SOURCE_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=$from&endtime=$to&minmagnitude=$minMagnitude"
        try {
            loadedMapStyle.addSource(GeoJsonSource(EARTHQUAKE_SOURCE_ID, URL(EARTHQUAKE_SOURCE_URL)))
        } catch (malformedURLException: Exception) {
            Timber.e(malformedURLException, "That's not an url...")
        }
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
                    if (FeaturesHelper.isLocationEnabled(context!!)) {
                        val location = mapboxMap.locationComponent.lastKnownLocation!!
                        val position = CameraPosition.Builder()
                            .target(LatLng(location.latitude, location.longitude))
                            .zoom(6.0)
                            .tilt(0.0)
                            .build()
                        mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 1250)
                    } else {
                        Toast.makeText(context, R.string.location_disabled, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun getDate(days: Int): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return LocalDate.now().minusDays(days.toLong()).format(formatter)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.map_toolbar_menu, menu)
        val item = menu.findItem(R.id.switchMap)
        item.setActionView(R.layout.switch_layout)
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
                if (FeaturesHelper.isConnectedToNetwork(context!!)) {
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

    private fun addClusteredGeoJsonSource(loadedMapStyle: Style) {
        val EARTHQUAKE_SOURCE_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=$from&endtime=$to&minmagnitude=$minMagnitude"
        loadedMapStyle.addSource(
            GeoJsonSource(
                "earthquakes",
                URI(EARTHQUAKE_SOURCE_URL),
                GeoJsonOptions()
                    .withCluster(true)
                    .withClusterMaxZoom(15)
                    .withClusterRadius(20)
            )
        )

        val unclustered = CircleLayer("unclustered-points", "earthquakes")
        unclustered.setProperties(
            circleColor(Color.parseColor("#FBB03B")),
            circleRadius(20f),
            circleBlur(1f)
        )
        unclustered.setFilter(neq(get("cluster"), literal(true)))
        loadedMapStyle.addLayerBelow(unclustered, "building")
    }

    private fun notifySolarDanger(solars: List<StationBasicEntity>) {
        if (!solars.isNullOrEmpty()) {

            val builder = NotificationCompat.Builder(context!!, getString(R.string.notifiaction_channel_id))
                .setSmallIcon(R.drawable.mapbox_logo_icon)
                .setContentTitle("EarthQuake Warning")
                .setContentText("Solar stations in danger")
                .setLargeIcon(
                    DrawableToBitmapHelper.drawableToBitmap(
                        ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.baseline_priority_high_24,
                            null
                        )!!
                    )
                )
                .setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText(notificationList(solars))
                )
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            with(NotificationManagerCompat.from(context!!)) {
                // notificationId is a unique int for each notification that you must define
                notify(1, builder.build())
            }

        }
    }

    private fun notificationList(solars: List<StationBasicEntity>): String {
        var text = ""
        if (solars.size > 7) {
            for (i in 0..6) {
                text += "#${i + 1}: ${solars[i].id} \n\r "
            }
            text += "and ${solars.size - 7} more..."
        } else {
            solars.forEachIndexed { index, stationBasicEntity ->
                text += "#${index + 1}: ${stationBasicEntity.id}\n\r "
            }
        }
        return text
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
