package com.h24.home24reviewapp.repo

import com.h24.home24reviewapp.api.Home24Service
import com.h24.home24reviewapp.model.ResponseStatus
import com.h24.home24reviewapp.extensions.failed
import com.h24.home24reviewapp.extensions.loading
import com.h24.home24reviewapp.extensions.success
import com.h24.home24reviewapp.model.ArticleModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import javax.inject.Singleton

/**
 * Repository handles all the data related task, it can act as a mediator between data accessor and data provider modules
 */
@Singleton
class Repository(private val home24Service: Home24Service) {

    val status = PublishSubject.create<ResponseStatus<List<ArticleModel>>>()
    private var list: List<ArticleModel>? = null

    /**
     * Loads articles if not present from the service and publishes the data on a stream
     * @param limit number of articles to be fetched
     */
    fun loadArticles(limit: Int) {
        list?.let {
            status.success(list!!)
        } ?: run {
            val queryMap = HashMap<String, String>()
            queryMap["appDomain"] = "1"
            queryMap["locale"] = "de_DE"
            queryMap["offset"] = "0"
            queryMap["limit"] = limit.toString()

            home24Service.getArticles(queryMap)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { status.loading(true) }
                    .doOnTerminate { status.loading(false) }
                    .subscribe(
                            { result ->
                                list = result._embedded.articles
                                status.success(list!!)
                            },
                            { error -> status.failed(error) }
                    )
        }
    }
}