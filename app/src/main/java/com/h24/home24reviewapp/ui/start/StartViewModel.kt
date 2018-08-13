package com.h24.home24reviewapp.ui.start

import android.arch.lifecycle.ViewModel

/**
 * ViewModel for startActivity
 */
class StartViewModel : ViewModel() {
    private var animate = true

    /**
     * check for starting animation.
     */
    fun shouldAnimate(): Boolean {
        if (animate) {
            animate = false
            return true
        } else {
            return false
        }
    }
}