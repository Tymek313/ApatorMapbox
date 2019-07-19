package com.example.apatormapbox.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.anychart.APIlib
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.example.apatormapbox.R
import kotlinx.android.synthetic.main.fragment_paszport.view.*

class PassportFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_paszport, container, false)

        // zmieniaj tutaj! - chwilowe dane testowe... - najlepiej nadac inna wartosc stałym aby nie grzebać w kodzie
        val lat = 40.0099983215332
        val lon = -105.0199966430664
        val elev = 1581.8399658203125
        val tz = -7
        val location = "None"
        val city = ""
        val state = "Colorado"
        val distance = 2029
        val acAnnual = 6575.31884765625
        val solradAnnual = 5.618043899536133
        val capacityFactor = 18.765178680419922

        // implementacja zmiennych data wykonuj przez objects/SetPassportDate/setData<Poa,DC,AC,SOL>
        val dataPoa: ArrayList<DataEntry> = arrayListOf<DataEntry>().also {
            for(i in 1..12){
                it.add(ValueDataEntry("$i", 1))
            }
        }
        val dataDC = arrayListOf<DataEntry>().also {
            for(i in 1..12){
                it.add(ValueDataEntry("$i", 1))
            }
        }
        val dataAC = arrayListOf<DataEntry>().also {
            for(i in 1..12){
                it.add(ValueDataEntry("$i", 1))
            }
        }
        val dataSOL = arrayListOf<DataEntry>().also {
            for(i in 1..12){
                it.add(ValueDataEntry("$i", 1))
            }
        }

        //przygotowane wczytanie danych i wyswietlnie danych --->
        //////////////////////////////////////////////////////////////////////////////////////////
        // sekcja ustawienia value - okno tekstu z wartosciami -->
        view.distance_value_PF.text = distance.toString()
        view.state_value_PF.text = state
        view.city_value_PF.text = city
        view.location_value_PF.text = location
        view.tz_value_PF.text = tz.toString()
        view.elev_value_PF.text = elev.toString()
        view.lon_value_PF.text = lon.toString()
        view.lat_value_PF.text = lat.toString()
        view.ac_annual_value_PF.text = acAnnual.toString()
        view.solrad_annual_value_PF.text = solradAnnual.toString()
        view.capacity_factor_value_PF.text = capacityFactor.toString()

        //////////////////////////////////////////////////////////////////////////////////////////
        // sekcja wykresow -->
        val anyChartViewPOA = view.poa_monthly_ACV_PF
        APIlib.getInstance().setActiveAnyChartView(anyChartViewPOA)
        val piePoa = AnyChart.column()
        piePoa.title("Monthly plane of array irradiance values")
        piePoa.data(dataPoa)
        anyChartViewPOA.setChart(piePoa)

        val anyChartViewDC = view.dc_monthly_ACV_PF
        APIlib.getInstance().setActiveAnyChartView(anyChartViewDC)
        val pieDC = AnyChart.column()
        pieDC.title("Monthly DC array output")
        pieDC.data(dataDC)
        anyChartViewDC.setChart(pieDC)

        val anyChartViewAC = view.ac_monthly_ACV_PF
        APIlib.getInstance().setActiveAnyChartView(anyChartViewAC)
        val pieAC = AnyChart.column()

        pieAC.title("Monthly AC system output")
        pieAC.data(dataAC)
        anyChartViewAC.setChart(pieAC)

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

