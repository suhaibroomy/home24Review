package com.h24.home24reviewapp.di

import com.h24.home24reviewapp.api.Home24Service
import com.h24.home24reviewapp.repo.Repository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Module which provides Repository dependency
 * @requires NetworkModule dependency
 */

@Module(includes = [NetworkModule::class])
class RepositoryModule {
    /**
     * Provides the Repository instance.
     * @param service Home24Service implementation to used with repo
     * @return Repository instance.
     */
    @Provides
    @Singleton
    fun providesRepository(service: Home24Service): Repository {
        return Repository(service)
    }
}