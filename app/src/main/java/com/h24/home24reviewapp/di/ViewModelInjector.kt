package com.h24.home24reviewapp.di

import com.h24.home24reviewapp.ui.review.ReviewViewModel
import com.h24.home24reviewapp.ui.selection.SelectionViewModel
import dagger.Component

/**
 * ViewModelInjector is a Component which injects required dependencies into the specified ViewModels.
 * @requires CoreComponent Dependency
 */
@ViewModelScope
@Component(dependencies = [CoreComponent::class])
interface ViewModelInjector {
    /**
     * Injects required dependencies into the specified SelectionViewModel.
     * @param selectionViewModel SelectionViewModel in which to inject the dependencies
     */
    fun inject(selectionViewModel: SelectionViewModel)

    /**
     * Injects required dependencies into the specified ReviewViewModel.
     * @param reviewViewModel ReviewViewModel in which to inject the dependencies
     */
    fun inject(reviewViewModel: ReviewViewModel)

}