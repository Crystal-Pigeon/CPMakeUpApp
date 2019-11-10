package com.crystalpigeon.makeupapp

import android.app.Application
import com.crystalpigeon.makeupapp.di.AppComponent
import com.crystalpigeon.makeupapp.di.AppModule
import com.crystalpigeon.makeupapp.di.DaggerAppComponent

class MakeUpApp : Application() {

    val component: AppComponent by lazy {
        DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }

    companion object {
        @JvmStatic
        lateinit var app: MakeUpApp
    }

    override fun onCreate() {
        super.onCreate()
        app = this
    }
}