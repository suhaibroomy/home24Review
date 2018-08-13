package com.h24.home24reviewapp.ui.selection

import android.arch.lifecycle.MutableLiveData
import android.view.View
import com.h24.home24reviewapp.model.ResponseStatus
import com.h24.home24reviewapp.model.ArticleModel
import com.h24.home24reviewapp.model.UpdateCardEvent
import com.h24.home24reviewapp.repo.Repository
import com.h24.home24reviewapp.util.MAX_CARDS
import com.h24.home24reviewapp.util.NUM_OF_ARTICLES_TO_LOAD
import com.h24.home24reviewapp.viewmodel.BaseViewModel
import io.reactivex.disposables.Disposable
import javax.inject.Inject

/**
 * ViewModel for SelectionActivity
 */
class SelectionViewModel : BaseViewModel() {

    /**
     * Injects the required Repository in this ViewModel.
     */
    @Inject
    lateinit var repo: Repository

    private lateinit var subscription: Disposable

    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()
    val errorVisibility: MutableLiveData<Int> = MutableLiveData()
    val endVisibility: MutableLiveData<Int> = MutableLiveData()
    val buttonState: MutableLiveData<Boolean> = MutableLiveData()
    val navigate: MutableLiveData<Boolean> = MutableLiveData()
    val statusText: MutableLiveData<String> = MutableLiveData()
    val cardEvent: MutableLiveData<UpdateCardEvent> = MutableLiveData()

    private var data: List<ArticleModel>? = null
    private var currentIndex = 0
    private var reviewedIndex = 0

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

    fun onActivityCreated() {
        loadArticles()
    }

    /**
     * Load Articles from the repository and notifies the listeners
     */
    private fun loadArticles() {
        if (data == null) {
            buttonState.value = false
            subscription = repo.status.subscribe { status ->
                when (status) {
                    is ResponseStatus.Progress -> {
                        loadingVisibility.value = if (status.loading) View.VISIBLE else View.GONE
                    }
                    is ResponseStatus.Success -> {
                        buttonState.value = true

                        data = status.data

                        notifyCardsLoaded()
                        subscription.dispose()
                    }
                    is ResponseStatus.Failure -> {
                        errorVisibility.value = View.VISIBLE
                        subscription.dispose()
                    }
                }
            }
            repo.loadArticles(NUM_OF_ARTICLES_TO_LOAD)
        } else {
            notifyCardsLoaded()
        }
    }

    /**
     * Notifies the listener about the cards to be displayed initially
     */
    private fun notifyCardsLoaded() {
        val nextCards = mutableListOf<ArticleModel>()
        for (i in reviewedIndex until (reviewedIndex + MAX_CARDS)) {
            if (i >= data!!.size) {
                break
            }
            currentIndex = i
            nextCards.add(data!![i])
        }
        cardEvent.value = UpdateCardEvent.add(nextCards)
        statusText.value = "$reviewedIndex/${data!!.size} Reviewed"
    }


    /**
     * Propagates Like Event
     */
    fun onLiked() {
        processEvent(true)
    }

    /**
     * Propagates Unlike Event
     */
    fun onUnliked() {
        processEvent(false)
    }

    /**
     * Processes the like and unlike events, updates all indexes with new values and notifies
     * all the required observer for state change
     */
    private fun processEvent(liked: Boolean) {
        data?.let {
            if (reviewedIndex < it.size) {

                it[currentIndex].isLiked = liked
                var nextCard: ArticleModel? = null
                if (currentIndex < it.size - 1) {
                    currentIndex++
                    nextCard = it[currentIndex]
                }

                reviewedIndex++
                statusText.value = "$reviewedIndex/${data!!.size} Reviewed"
                cardEvent.value =  if (liked) UpdateCardEvent.swipeRight(nextCard) else UpdateCardEvent.swipeLeft(nextCard)
            }

            if (reviewedIndex == it.size) {
                endVisibility.value = View.VISIBLE
                buttonState.value = false
                statusText.value = ""
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        subscription.dispose()
    }

    /**
     * Tries to load articles from repo again and notifies the UI to update state
     */
    fun retryFetch() {
        errorVisibility.value = View.GONE
        loadArticles()
    }

    /**
     * Checks if all the articles are reviews and notifies the View to naviagate to next screen
     */
    fun onNavigationRequested() {
        navigate.value = reviewedIndex == data?.size
    }
}

