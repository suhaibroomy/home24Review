package com.h24.home24reviewapp.ui.review

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.content.res.AppCompatResources
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.h24.home24reviewapp.R
import com.h24.home24reviewapp.util.RecyclerViewItemOffset
import kotlinx.android.synthetic.main.activity_review.*

class ReviewActivity : AppCompatActivity() {

    private lateinit var viewModel: ReviewViewModel
    private lateinit var adapter: ReviewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)

        setSupportActionBar(toolbar)

        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
            it.setDisplayShowTitleEnabled(false)
            it.setHomeAsUpIndicator(AppCompatResources.getDrawable(this, R.drawable.ic_back))
        }

        viewModel = ViewModelProviders.of(this).get(ReviewViewModel::class.java)

        registerViewModelObservers()

        adapter = ReviewAdapter(viewModel.layoutType.value!!)

        recycler_view.layoutManager = GridLayoutManager(this, 2)
        recycler_view.addItemDecoration(RecyclerViewItemOffset(resources.getDimensionPixelSize(R.dimen.rv_item_offset), 2))
        recycler_view.adapter = adapter

        btn_toggle.setOnClickListener {
            viewModel.toggleLayoutType()
        }
    }

    /**
     * Registering observers for related liveData from viewModel
     */
    private fun registerViewModelObservers() {
        viewModel.data.observe(this, Observer { data ->
            data?.let {
                adapter.updateArticleList(it)
            }
        })

        viewModel.layoutType.observe(this, Observer { layoutType ->
            layoutType?.let {
                var scrollPosition = 0

                if (recycler_view.layoutManager is LinearLayoutManager) {
                    scrollPosition = (recycler_view.layoutManager as LinearLayoutManager)
                            .findFirstCompletelyVisibleItemPosition()
                } else if (recycler_view.layoutManager is GridLayoutManager) {
                    scrollPosition = (recycler_view.layoutManager as LinearLayoutManager)
                            .findFirstCompletelyVisibleItemPosition()
                }

                when (viewModel.layoutType.value) {
                    ReviewViewModel.LayoutType.GRID -> {
                        recycler_view.layoutManager = GridLayoutManager(this, 2)
                        btn_toggle.setImageResource(R.drawable.ic_list)

                    }
                    ReviewViewModel.LayoutType.LIST -> {
                        recycler_view.layoutManager = LinearLayoutManager(this)
                        btn_toggle.setImageResource(R.drawable.ic_grid)
                    }
                }
                adapter.updateLayoutType(viewModel.layoutType.value!!)
                recycler_view.scrollToPosition(scrollPosition)
            }
        })

    }

    override fun onSupportNavigateUp(): Boolean {
        ActivityCompat.finishAfterTransition(this)
        return true
    }
}
