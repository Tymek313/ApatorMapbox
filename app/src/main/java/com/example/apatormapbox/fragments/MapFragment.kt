package com.example.apatormapbox.fragments


import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.apatormapbox.R
import com.example.apatormapbox.activities.MainActivity
import com.example.apatormapbox.helpers.DrawableToBitmap
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.Point
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


class MapFragment : Fragment(), View.OnClickListener, OnMapReadyCallback {

    private lateinit var mapView: MapView
    private lateinit var markerBitmap: Bitmap

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_map, container, false)
        view.locate_device_btn.setOnClickListener(this)

        (activity as MainActivity).apply {
            setSupportActionBar(view.mapToolbar)
            supportActionBar.apply {
                this?.setHomeAsUpIndicator(R.drawable.ic_settings)
                this?.setDisplayHomeAsUpEnabled(true)
            }
        }

        markerBitmap =
            DrawableToBitmap.drawableToBitmap(ResourcesCompat.getDrawable(resources, R.drawable.ic_marker, null)!!)!!

        mapView = view.mapView.apply {
            getMapAsync(this@MapFragment)
            onCreate(savedInstanceState)
        }

        setHasOptionsMenu(true)
        return view
    }

    override fun onMapReady(mapboxMap: MapboxMap) {
        val symbols = ArrayList<Feature>()
        symbols.add(Feature.fromGeometry(Point.fromLngLat(21.016758, 52.218822)))
        mapboxMap.setStyle(
            Style.Builder().fromUrl("mapbox://styles/mapbox/cjf4m44iw0uza2spb3q0a7s41")
                .withSource(
                    GeoJsonSource(
                        "SOURCE_ID",
                        FeatureCollection.fromFeatures(symbols)
                    )
                )
                .withImage(
                    "ICON_ID", markerBitmap
                )
                .withLayer(
                    SymbolLayer("LAYER_ID", "SOURCE_ID").withProperties(
                        PropertyFactory.iconImage("ICON_ID"),
                        iconAllowOverlap(true),
                        iconOffset(arrayOf(0f, -9f))
                    )
                )
        )
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.locate_device_btn -> {
                Log.d("locate", "Lokalizacja")
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
                //TODO wykonaj synchronizację
                false
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
