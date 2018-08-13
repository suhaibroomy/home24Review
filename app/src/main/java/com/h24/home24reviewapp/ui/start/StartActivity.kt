package com.h24.home24reviewapp.ui.start

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.view.animation.DecelerateInterpolator
import com.h24.home24reviewapp.R
import com.h24.home24reviewapp.ui.selection.SelectionActivity
import kotlinx.android.synthetic.main.activity_start.*

class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        val viewModel = ViewModelProviders.of(this).get(StartViewModel::class.java)

        if (viewModel.shouldAnimate()) {
            startWelcomeAnimation()
        }

        fab_container.setOnClickListener {
            val intent = Intent(this@StartActivity, SelectionActivity::class.java)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this@StartActivity, fab_container, ViewCompat.getTransitionName(fab_container)!!)
            ActivityCompat.startActivity(this@StartActivity, intent, options.toBundle())
        }
    }


    /**
     * Performs the starting animation
     */
    private fun startWelcomeAnimation() {
        tv_welcome_msg.alpha = 0f
        tv_welcome_msg.scaleX = 0.9f
        tv_welcome_msg.scaleY = 0.9f
        tv_welcome_msg.animate().setStartDelay(600).setDuration(800).setInterpolator(DecelerateInterpolator()).scaleX(1f).scaleY(1f).alpha(1f)

        tv_instruction_msg.alpha = 0f
        tv_instruction_msg.scaleX = 0.9f
        tv_instruction_msg.scaleY = 0.9f
        tv_instruction_msg.animate().setStartDelay(1400).setDuration(800).setInterpolator(DecelerateInterpolator()).scaleX(1f).scaleY(1f).alpha(1f)

        fab_container.scaleX = 0f
        fab_container.scaleY = 0f
        fab_container.animate().scaleY(1f).scaleX(1f).setStartDelay(2200).setDuration(800).interpolator = DecelerateInterpolator()
    }

}
