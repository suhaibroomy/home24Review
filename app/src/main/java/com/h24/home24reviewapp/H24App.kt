package com.h24.home24reviewapp

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco
import com.h24.home24reviewapp.di.AppModule
import com.h24.home24reviewapp.di.CoreComponent
import com.h24.home24reviewapp.di.DaggerCoreComponent
import io.reactivex.plugins.RxJavaPlugins

class H24App: Application() {

    companion object {
        lateinit var coreComponent: CoreComponent
    }

    override fun onCreate() {
        super.onCreate()
        initDI()
        Fresco.initialize(this)
        RxJavaPlugins.setErrorHandler { error ->
            //Unexpected RXJAVA errors should be handled here to prevent crashes
            error.printStackTrace()
        }
    }

    private fun initDI() {
        coreComponent = DaggerCoreComponent.builder().appModule(AppModule(this)).build()
    }
}