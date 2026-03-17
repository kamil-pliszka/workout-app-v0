package com.pl.myworkoutapp.androidapp

import android.app.Application
import com.pl.myworkoutapp.di.initKoin
import org.koin.android.ext.koin.androidContext

class WorkoutApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@WorkoutApplication)
        }
    }
}