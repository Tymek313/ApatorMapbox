package com.example.apatormapbox.fragments


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.apatormapbox.R
import com.example.apatormapbox.activities.MainActivity
import kotlinx.android.synthetic.main.fragment_map.view.*

class MapFragment : Fragment(), View.OnClickListener {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_map, container, false)
        val activity = (activity as MainActivity)
        activity.setSupportActionBar(view.mapToolbar)
        activity.supportActionBar.apply {
            this?.setHomeAsUpIndicator(R.drawable.ic_settings)
            this?.setDisplayHomeAsUpEnabled(true)
        }
        view.locate_device_btn.setOnClickListener(this)
        setHasOptionsMenu(true)
        return view
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.locate_device_btn -> {
                //Log.d("locate", "locate")
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
                true
            }
            R.id.sync -> {
                //Log.d("sync", "sync")
                //TODO wykonaj synchronizację
                false
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
