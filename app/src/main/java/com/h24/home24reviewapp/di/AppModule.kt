package com.h24.home24reviewapp.di

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Module which provides context Dependency
 * @param context Application context
 */
@Module
class AppModule(val context: Context) {

    /**
     * Provides application Context
     * @return ApplicationContext
     */
    @Provides
    @Singleton
    fun providesContext(): Context {
        return context
    }
}