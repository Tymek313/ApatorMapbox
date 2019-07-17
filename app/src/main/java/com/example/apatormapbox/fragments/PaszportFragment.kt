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
        /////////////////////////////////////////////
        val anyChartViewPOA = view.poa_monthly_ACV_PF
        APIlib.getInstance().setActiveAnyChartView(anyChartViewPOA)
        val piePoa = AnyChart.column()
        val dataPoa = arrayListOf<DataEntry>().also {
            it.add(ValueDataEntry("1", 1000))
            it.add(ValueDataEntry("2", 100))
            it.add(ValueDataEntry("3", 1300))
        }
        piePoa.title("Monthly plane of array irradiance values")
        piePoa.data(dataPoa)
        anyChartViewPOA.setChart(piePoa)
        /////////////////////////////////////////////
        val anyChartViewDC = view.dc_monthly_ACV_PF
        APIlib.getInstance().setActiveAnyChartView(anyChartViewDC)
        val pieDC = AnyChart.column()
        val dataDC = arrayListOf<DataEntry>().also {
            it.add(ValueDataEntry("1", 21000))
            it.add(ValueDataEntry("2", 3100))
            it.add(ValueDataEntry("3", 41300))
        }
        pieDC.title("Monthly DC array output")
        pieDC.data(dataDC)
        anyChartViewDC.setChart(pieDC)
        /////////////////////////////////////////////
        val anyChartViewAC = view.ac_monthly_ACV_PF
        APIlib.getInstance().setActiveAnyChartView(anyChartViewAC)
        val pieAC = AnyChart.column()
        val dataAC = arrayListOf<DataEntry>().also {
            it.add(ValueDataEntry("1", 621000))
            it.add(ValueDataEntry("2", 73100))
            it.add(ValueDataEntry("3", 481300))
        }
        pieAC.title("Monthly AC system output")
        pieAC.data(dataAC)
        anyChartViewAC.setChart(pieAC)
        /////////////////////////////////////////////
        val anyChartViewSOL = view.solrad_monthly_ACV_PF
        APIlib.getInstance().setActiveAnyChartView(anyChartViewSOL)
        val pieSOL = AnyChart.column()
        val dataSOL = arrayListOf<DataEntry>().also {
            it.add(ValueDataEntry("1", 69210900))
            it.add(ValueDataEntry("2", 793100))
            it.add(ValueDataEntry("3", 4981300))
        }
        pieSOL.title("Monthly solar radiation values")
        pieSOL.data(dataSOL)
        anyChartViewSOL.setChart(pieSOL)



        // Powrot do mapy...
        view.back_btn_PF.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_paszportFragment_to_mapFragment)
        }

        return view
    }


}
