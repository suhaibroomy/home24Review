package com.h24.home24reviewapp.ui.review

import android.arch.lifecycle.MutableLiveData
import com.h24.home24reviewapp.model.ArticleModel
import com.h24.home24reviewapp.model.ResponseStatus
import com.h24.home24reviewapp.repo.Repository
import com.h24.home24reviewapp.util.MAX_OF_ARTICLES_TO_LOAD
import com.h24.home24reviewapp.viewmodel.BaseViewModel
import javax.inject.Inject

/**
 * ViewModel for ReviewActivity
 */
class ReviewViewModel : BaseViewModel() {

    enum class LayoutType {
        GRID, LIST
    }

    /**
     * Injects the required Repository in this ViewModel.
     */
    @Inject
    lateinit var repo: Repository

    val layoutType: MutableLiveData<LayoutType> = MutableLiveData()
    val data: MutableLiveData<List<ArticleModel>> = MutableLiveData()

    init {
        compositeDisposable.add(repo.status.subscribe { status ->
            when (status) {
                is ResponseStatus.Progress -> {
                }
                is ResponseStatus.Success -> {
                    data.value = status.data
                }
                is ResponseStatus.Failure -> {
                }
            }
        })

        layoutType.value = LayoutType.GRID
        loadArticles()
    }

    /**
     * Loads articles from the repository and notifies the listeners
     */
    private fun loadArticles() {
        repo.loadArticles(0, MAX_OF_ARTICLES_TO_LOAD)
    }

    /**
     * toggles the layout type of recyclerView and notifies the listener
     */
    fun toggleLayoutType() {
        layoutType.value = if (layoutType.value == LayoutType.LIST) LayoutType.GRID else LayoutType.LIST
    }
}