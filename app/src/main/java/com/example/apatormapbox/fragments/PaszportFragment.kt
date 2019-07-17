package com.example.apatormapbox.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.anychart.APIlib
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry

import com.example.apatormapbox.R
import kotlinx.android.synthetic.main.fragment_paszport.view.*

class PaszportFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_paszport, container, false)

        // zmieniaj tutaj!
        val lat = 40.0099983215332
        val lon = -105.0199966430664
        val elev = 1581.8399658203125
        val tz = -7
        val location = "None"
        val city = ""
        val state = "Colorado"
        val distance = 2029
        val dataPoa = arrayListOf<DataEntry>().also {
            it.add(ValueDataEntry("1", 1000))
            it.add(ValueDataEntry("2", 100))
            it.add(ValueDataEntry("3", 1300))
        }
        val dataDC = arrayListOf<DataEntry>().also {
            it.add(ValueDataEntry("1", 21000))
            it.add(ValueDataEntry("2", 3100))
            it.add(ValueDataEntry("3", 41300))
        }
        val dataAC = arrayListOf<DataEntry>().also {
            it.add(ValueDataEntry("1", 621000))
            it.add(ValueDataEntry("2", 73100))
            it.add(ValueDataEntry("3", 481300))
        }
        val dataSOL = arrayListOf<DataEntry>().also {
            it.add(ValueDataEntry("1", 69210900))
            it.add(ValueDataEntry("2", 793100))
            it.add(ValueDataEntry("3", 4981300))
        }

        //przygotowane wczytanie danych i wyswietlnie danych --->
        //////////////////////////////////////////////////////////////////////////////////////////
        view.distance_value_PF2.setText(distance)
        view.state_value_PF2.setText(state)
        view.city_value_PF.setText(city)
        view.location_value_PF.setText(location)
        view.tz_value_PF.setText(tz.toString())
        view.elev_value_PF.setText(elev.toString())
        view.lon_value_PF.setText(lon.toString())
        view.lat_value_PF.setText(lat.toString())

        //////////////////////////////////////////////////////////////////////////////////////////
        val anyChartViewPOA = view.poa_monthly_ACV_PF
        APIlib.getInstance().setActiveAnyChartView(anyChartViewPOA)
        val piePoa = AnyChart.column()
        piePoa.title("Monthly plane of array irradiance values")
        piePoa.data(dataPoa)
        anyChartViewPOA.setChart(piePoa)

        //////////////////////////////////////////////////////////////////////////////////////////
        val anyChartViewDC = view.dc_monthly_ACV_PF
        APIlib.getInstance().setActiveAnyChartView(anyChartViewDC)
        val pieDC = AnyChart.column()
        pieDC.title("Monthly DC array output")
        pieDC.data(dataDC)
        anyChartViewDC.setChart(pieDC)

        //////////////////////////////////////////////////////////////////////////////////////////
        val anyChartViewAC = view.ac_monthly_ACV_PF
        APIlib.getInstance().setActiveAnyChartView(anyChartViewAC)
        val pieAC = AnyChart.column()

        pieAC.title("Monthly AC system output")
        pieAC.data(dataAC)
        anyChartViewAC.setChart(pieAC)

        //////////////////////////////////////////////////////////////////////////////////////////
        val anyChartViewSOL = view.solrad_monthly_ACV_PF
        APIlib.getInstance().setActiveAnyChartView(anyChartViewSOL)
        val pieSOL = AnyChart.column()
        pieSOL.title("Monthly solar radiation values")
        pieSOL.data(dataSOL)
        anyChartViewSOL.setChart(pieSOL)

        //////////////////////////////////////////////////////////////////////////////////////////

        // Powrot do mapy...
        view.back_btn_PF.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_paszportFragment_to_mapFragment)
        }

        return view
    }


}
