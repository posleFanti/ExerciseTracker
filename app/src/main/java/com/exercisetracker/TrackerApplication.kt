package com.exercisetracker

import android.app.Application
import com.exercisetracker.data.AppContainer
import com.exercisetracker.data.AppDataContainer

class TrackerApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}