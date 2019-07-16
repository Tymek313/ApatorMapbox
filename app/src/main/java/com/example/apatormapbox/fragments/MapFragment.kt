package com.example.apatormapbox.fragments


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.apatormapbox.R
import com.example.apatormapbox.activities.MainActivity
import kotlinx.android.synthetic.main.fragment_map.view.*

class MapFragment : Fragment() {

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
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.map_toolbar_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.home -> {
                Navigation.findNavController(activity!!, R.id.navHost)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
