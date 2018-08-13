package com.h24.home24reviewapp.util

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Build
import android.support.annotation.ColorRes
import android.support.annotation.RequiresApi
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.AccelerateDecelerateInterpolator


interface OnRevealAnimationListener {
    fun onRevealHide()

    fun onRevealShow()
}

/**
 * Performs reveal animation on the passed view and provides callbacks to the listener
 * @param view View that has to be animated
 * @param color color for reveal Animation. This shoud be a valid color resource
 * @param x PivotX for the view
 * @param y PivotY for the view
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
fun animateRevealShow(view: View, startRadius: Int, @ColorRes color: Int,
                      x: Int, y: Int, listener: OnRevealAnimationListener) {
    val finalRadius = Math.hypot(view.width.toDouble(), view.height.toDouble()).toFloat()

    val anim = ViewAnimationUtils.createCircularReveal(view, x, y, startRadius.toFloat(), finalRadius)
    anim.duration = 300
    anim.startDelay = 100
    anim.interpolator = AccelerateDecelerateInterpolator()
    anim.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationStart(animation: Animator) {
            view.setBackgroundColor(ContextCompat.getColor(view.context, color))
        }

        override fun onAnimationEnd(animation: Animator) {
            view.visibility = View.VISIBLE
            listener.onRevealShow()
        }
    })
    anim.start()
}