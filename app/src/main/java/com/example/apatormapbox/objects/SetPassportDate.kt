package com.example.apatormapbox.objects

import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry

object SetPassportDate{
    fun setDataPoa(value: List<Double?>?) : ArrayList<DataEntry>{
        val dataPoa = arrayListOf<DataEntry>().also {
            for(i in 0..11){
                it.add(ValueDataEntry("$${i + 1}", value!![i]))
            }
        }
        return dataPoa
    }
    fun setDataDC(value: List<Double?>?) : ArrayList<DataEntry> {
        val dataDC = arrayListOf<DataEntry>().also {
            for(i in 0..11){
                it.add(ValueDataEntry("$${i + 1}", value!![i]))
            }
        }
        return dataDC
    }
    fun setDataAC(value: List<Double?>?) : ArrayList<DataEntry> {
        val dataAC = arrayListOf<DataEntry>().also {
            for(i in 0..11){
                it.add(ValueDataEntry("$${i + 1}", value!![i]))
            }
        }
        return dataAC
    }
    fun setDataSOL(value: List<Double?>?) : ArrayList<DataEntry> {
        val dataSOL = arrayListOf<DataEntry>().also {
            for(i in 0..11){
                it.add(ValueDataEntry("${i + 1}", value!![i]))
            }
        }
        return dataSOL
    }
}
