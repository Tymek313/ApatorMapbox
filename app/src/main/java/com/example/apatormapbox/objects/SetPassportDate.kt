package com.example.apatormapbox.objects

import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry

object SetPassportDate{
    fun setDataPoa(value: List<Double?>?) : ArrayList<DataEntry>{
        val dataPoa = arrayListOf<DataEntry>().also {
            for(i in 0..11){
                it.add(ValueDataEntry(month(i), value!![i]))
            }
        }
        return dataPoa
    }
    fun setDataDC(value: List<Double?>?) : ArrayList<DataEntry> {
        val dataDC = arrayListOf<DataEntry>().also {
            for(i in 0..11){
                it.add(ValueDataEntry(month(i), value!![i]))
            }
        }
        return dataDC
    }
    fun setDataAC(value: List<Double?>?) : ArrayList<DataEntry> {
        val dataAC = arrayListOf<DataEntry>().also {
            for(i in 0..11){
                it.add(ValueDataEntry(month(i), value!![i]))
            }
        }
        return dataAC
    }

    fun setDataSOL(value: List<Double?>?) : ArrayList<DataEntry> {
        val dataSOL = arrayListOf<DataEntry>().also {
            for(i in 0..11){
                it.add(ValueDataEntry(month(i), value!![i]))
            }
        }
        return dataSOL
    }
    private fun month(i: Int): String {
        return when (i) {
            0 -> "I"
            1 -> "II"
            2 -> "III"
            3 -> "IV"
            4 -> "V"
            5 -> "VI"
            6 -> "VII"
            7 -> "VIII"
            8 -> "IX"
            9 -> "X"
            10 -> "XI"
            11 -> "XII"
            else -> ""
        }
    }
}
