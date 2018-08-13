package com.h24.home24reviewapp.ui.selection

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.transition.Transition
import android.view.View
import com.h24.home24reviewapp.R
import com.h24.home24reviewapp.model.UpdateCardEvent
import com.h24.home24reviewapp.ui.customviews.StackView
import com.h24.home24reviewapp.ui.review.ReviewActivity
import com.h24.home24reviewapp.util.OnRevealAnimationListener
import com.h24.home24reviewapp.util.animateRevealHide
import com.h24.home24reviewapp.util.animateRevealShow
import com.h24.home24reviewapp.util.showToast
import kotlinx.android.synthetic.main.activity_selection.*

class SelectionActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var viewModel: SelectionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selection)

        viewModel = ViewModelProviders.of(this).get(SelectionViewModel::class.java)

        if (viewModel.shouldAnimate()) {
            setupEnterAnimation()
        } else {
            root_layout.alpha = 1f
            root_layout.setBackgroundColor(ContextCompat.getColor(this, R.color.bg_color))
        }

        registerViewModelObservers()

        btn_review.setOnClickListener(this)
        iv_back.setOnClickListener(this)
        btn_like.setOnClickListener(this)
        btn_unlike.setOnClickListener(this)
        btn_retry.setOnClickListener(this)

        viewModel.onActivityCreated()
    }

    /**
     * Registering observers for related liveData from viewModel
     */
    private fun registerViewModelObservers() {
        viewModel.loadingVisibility.observe(this, Observer { visibility ->
            visibility?.let {
                progress_bar.visibility = it
            }
        })

        viewModel.cardEvent.observe(this, Observer { data ->
            data?.let {
                when (it) {
                    is UpdateCardEvent.Add -> {
                        for (model in it.articles) {
                            card_stack_view.addCard(model.getMediaUrl())
                        }
                    }
                    is UpdateCardEvent.SwipeRight -> card_stack_view.swipe(StackView.SwipeDirection.RIGHT, it.articleModel?.getMediaUrl())
                    is UpdateCardEvent.SwipeLeft -> card_stack_view.swipe(StackView.SwipeDirection.LEFT, it.articleModel?.getMediaUrl())
                }
            }
        })

        viewModel.buttonState.observe(this, Observer { state ->
            state?.let {
                if (state) {
                    btn_like.setBackgroundResource(R.drawable.ic_like)
                    btn_unlike.setBackgroundResource(R.drawable.ic_unlike)

                } else {
                    btn_like.setBackgroundResource(R.drawable.ic_like_disabled)
                    btn_unlike.setBackgroundResource(R.drawable.ic_unlike_disabled)
                }
                btn_like.isEnabled = state
                btn_unlike.isEnabled = state
            }
        })

        viewModel.errorVisibility.observe(this, Observer { visibility ->
            visibility?.let {
                btn_retry.clearAnimation()
                group_error.visibility = it
            }
        })

        viewModel.endVisibility.observe(this, Observer { visibility ->
            visibility?.let {
                group_end.visibility = it
            }
        })

        viewModel.statusText.observe(this, Observer { text ->
            text?.let {
                tv_status.text = it
            }
        })

        viewModel.navigate.observe(this, Observer { navigate ->
            navigate?.let {
                if (navigate) {
                    val intent = Intent(this@SelectionActivity, ReviewActivity::class.java)
                    ActivityCompat.startActivity(this@SelectionActivity, intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this@SelectionActivity).toBundle())
                } else {
                    showToast(getString(R.string.review_all_message))
                }
            }
        })
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.btn_retry -> viewModel.retryFetch()
            R.id.iv_back -> performExit()
            R.id.btn_unlike -> {
                if (!card_stack_view.animating) {
                    viewModel.onUnliked()
                }
            }
            R.id.btn_like -> {
                if (!card_stack_view.animating) {
                    viewModel.onLiked()
                }
            }
            R.id.btn_review -> viewModel.onNavigationRequested()
        }
    }

    /**
     * This method sets up enter animation and makes the root view visible after reveal animation
     */
    private fun setupEnterAnimation() {
        window.sharedElementEnterTransition.addListener(object : Transition.TransitionListener {
            override fun onTransitionEnd(p0: Transition?) {
                val cx = (activity_root.left + activity_root.right) / 2
                val cy = (activity_root.top + activity_root.bottom) / 2
                animateRevealShow(activity_root, fab_container.width / 2, R.color.bg_color, cx,
                        cy, object : OnRevealAnimationListener {
                    override fun onRevealShow() {
                        fab_container.visibility = View.GONE
                        root_layout.animate().alpha(1f).duration = 300
                    }

                    override fun onRevealHide() {
                    }
                })
            }

            override fun onTransitionResume(p0: Transition?) {
            }

            override fun onTransitionPause(p0: Transition?) {
            }

            override fun onTransitionCancel(p0: Transition?) {
            }

            override fun onTransitionStart(p0: Transition?) {
            }

        })
    }

    override fun onBackPressed() {
        performExit()
    }

    fun performExit() {
        fab_container.visibility = View.VISIBLE
        animateRevealHide(activity_root, R.color.bg_color, fab_container.width / 2,
                object : OnRevealAnimationListener {

                    override fun onRevealShow() {
                    }

                    override fun onRevealHide() {
                        root_layout.alpha = 0f
                        ActivityCompat.finishAfterTransition(this@SelectionActivity);
                    }
                })
    }

}
