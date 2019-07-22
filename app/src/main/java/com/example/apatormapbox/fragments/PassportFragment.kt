package com.example.apatormapbox.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.anychart.APIlib
import com.anychart.AnyChart
import com.example.apatormapbox.R
import com.example.apatormapbox.models.dbentities.StationDetailsEntity
import com.example.apatormapbox.objects.SetPassportDate
import com.example.apatormapbox.viewmodels.SolarViewModel
import kotlinx.android.synthetic.main.fragment_paszport.view.*
import org.koin.android.viewmodel.ext.android.viewModel

class PassportFragment : Fragment() {

    private val solarViewModel: SolarViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_paszport, container, false)

        // Pobranie ID z bundle oraz zainicjowanie bazy danych -->
        val id = arguments!!.getString("stationId")
        solarViewModel.fetchStationDetails(id!!)
        solarViewModel.stationDetails.observe(this, Observer {
            refViews(it, view)
        })

        view.back_btn_PF.setOnClickListener {
            Navigation.findNavController(view).popBackStack()
        }

        return view
    }

    fun refViews(stationDetailsEntity: StationDetailsEntity, view: View) {

        val lat = stationDetailsEntity.lat
        val lon = stationDetailsEntity.lon
        val elev = stationDetailsEntity.elev
        val tz = stationDetailsEntity.tz
        val location = stationDetailsEntity.location
        val city = stationDetailsEntity.city
        val state = stationDetailsEntity.state
        val distance = stationDetailsEntity.distance
        val acAnnual = stationDetailsEntity.acAnnual
        val solradAnnual = stationDetailsEntity.solradAnnual
        val capacityFactor = stationDetailsEntity.capacityFactor

        // implementacja zmiennych data wykonuj przez objects/SetPassportDate/setData<Poa,DC,AC,SOL>
        val dataPoa = SetPassportDate.setDataPoa(stationDetailsEntity.poaMonthly)
        val dataDC = SetPassportDate.setDataDC(stationDetailsEntity.dcMonthly)
        val dataAC = SetPassportDate.setDataAC(stationDetailsEntity.acMonthly)
        val dataSOL = SetPassportDate.setDataSOL(stationDetailsEntity.solradMonthly)

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
    }
}

