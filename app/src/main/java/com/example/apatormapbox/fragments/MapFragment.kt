package com.example.apatormapbox.fragments

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.apatormapbox.R
import com.example.apatormapbox.activities.MainActivity
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.LocationComponentOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.Style
import kotlinx.android.synthetic.main.fragment_map.view.*

class MapFragment : Fragment(), View.OnClickListener {

    private lateinit var mapView: MapView
    private lateinit var location: Location


    @SuppressLint("MissingPermission")
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

        mapView = view.mapView.apply {
            getMapAsync {
                it.setStyle(Style.MAPBOX_STREETS) { style ->
                    if (PermissionsManager.areLocationPermissionsGranted(view.context)) {
                        val customLocationComponentOptions = LocationComponentOptions.builder(view.context)
                            .trackingGesturesManagement(true)
                            .accuracyColor(ContextCompat.getColor(view.context, R.color.mapboxGreen))
                            .build()
                        if (it.style != null) {
                            val locationComponentActivationOptions =
                                LocationComponentActivationOptions.builder(view.context, style)
                                    .locationComponentOptions(customLocationComponentOptions)
                                    .build()
                            it.locationComponent.apply {
                                activateLocationComponent(locationComponentActivationOptions)
                                isLocationComponentEnabled = true
                                cameraMode = CameraMode.TRACKING
                                renderMode = RenderMode.COMPASS
                            }
                        }
                        location = it.locationComponent.lastKnownLocation!!
                    }
                }
            }


        }
        mapView.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        return view
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.locate_device_btn -> {
                Navigation.findNavController(view).navigate(R.id.action_mapFragment_to_paszportFragment)
                Log.d("locate", "Lokalizacja")
                //TODO zlokalizuj użytkownika
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.map_toolbar_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                Navigation.findNavController(activity!!, R.id.navHost).navigate(R.id.settingsFragment)
                Log.d("ustawienia", "Przejście do ustawień")
                true
            }
            R.id.sync -> {
                Log.d("sync", "Synchronizacja")

//                // Implementacja okna dialogowego
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
