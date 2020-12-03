package com.example.itda

import android.app.Application
import com.example.itda.familydiaryfolder.PreferenceUtil

class MyApplication : Application() {
    companion object { lateinit var prefs: PreferenceUtil }
    override fun onCreate() {
        prefs = PreferenceUtil(applicationContext)
        super.onCreate()

    }
}

