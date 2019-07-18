package com.example.apatormapbox

import android.app.Application
import com.example.apatormapbox.viewmodels.SolarViewModel
import com.mapbox.mapboxsdk.Mapbox
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(
                listOf(module {
                    viewModel { SolarViewModel() }
                })
            )
        }
        Mapbox.getInstance(this, resources.getString(R.string.mapbox_access_token))
    }
}