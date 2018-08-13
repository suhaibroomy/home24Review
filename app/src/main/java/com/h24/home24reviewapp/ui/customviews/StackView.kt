package com.h24.home24reviewapp.ui.customviews

import android.animation.*
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.request.ImageRequest
import com.h24.home24reviewapp.R
import com.h24.home24reviewapp.util.MAX_CARDS

/**
 * StackView provides functionality to add views in a stack
 */
class StackView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var scaleDiff = 0.02f
    private var translationDiff = 12
    private var maxAllowedCards = MAX_CARDS
    private val containers = ArrayList<View>()
    private var addAfterAnimation: String? = null
    /**
     * Use this variable to check if the any view is in animating state
     */
    var animating = false

    enum class SwipeDirection {
        LEFT, RIGHT
    }

    init {
        attrs?.let {
            val a = context.obtainStyledAttributes(it, R.styleable.StackView)
            scaleDiff = a.getFloat(R.styleable.StackView_scaleDiff, scaleDiff)
            maxAllowedCards = a.getInt(R.styleable.StackView_maxCards, MAX_CARDS)
            translationDiff = a.getDimensionPixelSize(R.styleable.StackView_translationDiff, translationDiff)
            a.recycle()
        }
    }

    /**
     * adds the view to the back of the stack and loads up the image
     * @param card view to be added to the stack
     * @param uri Image Uri to be loaded
     */
    private fun addCard(card: View, uri: String) {
        if (containers.size >= maxAllowedCards) {
            return
        }

        val imageView = card.findViewById<SimpleDraweeView>(R.id.image_view)
        val controller = Fresco.newDraweeControllerBuilder()
                .setOldController(imageView.controller)
                .setImageRequest(ImageRequest.fromUri(uri))
                .setTapToRetryEnabled(true)
                .build()
        imageView.controller = controller

        card.translationX = 0f
        card.translationY = 0f
        card.scaleX = 1f
        card.scaleY = 1f
        card.rotation = 0f

        addView(card, 0)
        containers.add(card)

        update(0f)
    }



    /**
     * adds a new view to the back of the stack and loads up the image
     * @param uri Image Uri to be loaded
     */
    fun addCard(uri: String) {
        if (animating) {
            addAfterAnimation = uri
        }
        val view = LayoutInflater.from(context).inflate(R.layout.card_stack, this, false)
        addCard(view, uri)
    }



    /**
     * updates the position of all items in the stack according to the translation percentage of the top
     * @param percentX translationX percent of the top View
     */
    private fun update(percentX: Float) {

        for (i in 1 until containers.size) {
            val view = containers[i]

            val currentScale = 1f - i * scaleDiff
            val nextScale = 1f - (i - 1) * scaleDiff
            val percent = currentScale + (nextScale - currentScale) * Math.abs(percentX)
            view.scaleX = percent
            view.scaleY = percent

            val currentTranslationY = i * translationDiff

            val nextTranslationY = (i - 1) * translationDiff

            val translationY = currentTranslationY - Math.abs(percentX) * (currentTranslationY - nextTranslationY)
            view.translationY = translationY
        }
    }

    /**
     * swipes the card according to the direction and adds a new card at the back of the stack if provided
     * @param direction swipe direction
     * @param nextCardUri Uri of the new card to be loaded. No card will be added if it is null
     */
    fun swipe(direction: SwipeDirection, nextCardUri: String?) {
        if (animating) {
            return
        }
        animating = true
        var rotationValue = 10f
        var translationXValue = 2000f
        var translationYValue = 500f
        if (direction == SwipeDirection.LEFT) {
            rotationValue = -rotationValue
            translationXValue = -translationXValue
            translationYValue = -translationYValue
        }
        if (!containers.isEmpty()) {
            val target = containers[0]

            val rotation = ObjectAnimator.ofPropertyValuesHolder(
                    target, PropertyValuesHolder.ofFloat("rotation", rotationValue))
            rotation.duration = 200
            val translateX = ObjectAnimator.ofPropertyValuesHolder(
                    target, PropertyValuesHolder.ofFloat("translationX", 0f, translationXValue))
            val translateY = ObjectAnimator.ofPropertyValuesHolder(
                    target, PropertyValuesHolder.ofFloat("translationY", 0f, translationYValue))
            translateX.startDelay = 100
            translateY.startDelay = 100
            translateX.duration = 500
            translateY.duration = 500
            val cardAnimationSet = AnimatorSet()
            cardAnimationSet.playTogether(rotation, translateX, translateY)

            cardAnimationSet.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animator: Animator?) {
                    animating = false
                    removeView(target)
                    containers.removeAt(0)
                    nextCardUri?.let {
                        addCard(target, it)
                    } ?: run {
                        addAfterAnimation?.let {
                            addAfterAnimation = null
                            addCard(it)
                        }
                    }

                }

            })
            cardAnimationSet.setInterpolator { input ->
                update(getPercentX(target))
                input
            }
            cardAnimationSet.start()
        }
    }

    private fun getPercentX(view: View): Float {
        var percent = 2f * (view.translationX) / width
        if (percent > 1) {
            percent = 1f
        }
        if (percent < -1) {
            percent = -1f
        }
        return percent
    }
}