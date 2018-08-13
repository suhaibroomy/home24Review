package com.h24.home24reviewapp.di

import android.content.Context
import com.h24.home24reviewapp.repo.Repository
import dagger.Component
import javax.inject.Singleton

/**
 * ViewModelInjector is a Component which injects core dependencies
 * @requires AppModule and RepositoryModule
 */
@Singleton
@Component(modules = [AppModule::class, RepositoryModule::class])
interface CoreComponent {

    fun context(): Context

    fun repo(): Repository
}