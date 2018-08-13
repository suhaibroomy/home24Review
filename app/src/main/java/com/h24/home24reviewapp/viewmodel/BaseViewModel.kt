package com.h24.home24reviewapp.viewmodel

import android.arch.lifecycle.ViewModel
import com.h24.home24reviewapp.H24App
import com.h24.home24reviewapp.di.DaggerViewModelInjector
import com.h24.home24reviewapp.di.ViewModelInjector
import com.h24.home24reviewapp.ui.review.ReviewViewModel
import com.h24.home24reviewapp.ui.selection.SelectionViewModel
import io.reactivex.disposables.CompositeDisposable

/**
 * BaseViewModel that auto Injects the required dependencies provided that corresponding
 * child ViewModel injection is added in ViewModelInjectors
 */
abstract class BaseViewModel : ViewModel() {
    protected val compositeDisposable = CompositeDisposable()

    private val injector: ViewModelInjector = DaggerViewModelInjector
            .builder()
            .coreComponent(H24App.coreComponent)
            .build()

    init {
        inject()
    }

    /**
     * Injects the required dependencies
     */
    private fun inject() {
        when (this) {
            is SelectionViewModel -> injector.inject(this)
            is ReviewViewModel -> injector.inject(this)
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}