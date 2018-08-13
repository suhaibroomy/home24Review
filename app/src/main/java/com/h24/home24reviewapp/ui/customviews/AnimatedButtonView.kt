package com.h24.home24reviewapp.ui.customviews

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.v7.content.res.AppCompatResources
import android.support.v7.widget.AppCompatButton
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.h24.home24reviewapp.R

/**
 * Subclass of Button which provides animate button functionality on touch
 */
class AnimatedButtonView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = android.support.v7.appcompat.R.attr.buttonStyle) : AppCompatButton(context, attrs, defStyleAttr) {
    private val downAnimation: Animation by lazy { AnimationUtils.loadAnimation(getContext(), R.anim.scale_down) }
    private val upAnimation: Animation by lazy { AnimationUtils.loadAnimation(getContext(), R.anim.scale_up) }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isEnabled) {
            return super.onTouchEvent(event)
        }
        return when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (!downAnimation.hasStarted() || downAnimation.hasEnded()) {
                    clearAnimation()
                    startAnimation(downAnimation)
                }
                super.onTouchEvent(event)
                true
            }
            MotionEvent.ACTION_MOVE, MotionEvent.ACTION_UP -> {
                if (isPressed && (!upAnimation.hasStarted() || upAnimation.hasEnded())) {
                    clearAnimation()
                    startAnimation(upAnimation)
                }
                super.onTouchEvent(event)
                true
            }
            else -> super.onTouchEvent(event)
        }
    }
}
