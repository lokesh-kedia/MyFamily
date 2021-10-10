package com.d4d5.myfamily

import android.app.Application

class MyFamilyApplication:Application() {

    override fun onCreate() {
        super.onCreate()

        SharedPref.init(this)
    }
}