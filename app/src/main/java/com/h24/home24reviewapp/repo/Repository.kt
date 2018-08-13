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
    private val dataList = mutableListOf<ArticleModel>()

    /**
     * Loads articles if not present from the service and publishes the data on a stream
     * @param limit number of articles to be fetched
     */
    fun loadArticles(offset: Int, limit: Int) {
        if (offset + limit <= dataList.size) {
            status.success(dataList.subList(offset, offset + limit))
        } else {
            val queryMap = HashMap<String, String>()
            queryMap["appDomain"] = "1"
            queryMap["locale"] = "de_DE"
            queryMap["offset"] = dataList.size.toString()
            queryMap["limit"] = limit.toString()

            home24Service.getArticles(queryMap)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { status.loading(true) }
                    .doOnTerminate { status.loading(false) }
                    .subscribe(
                            { result ->
                                dataList.addAll(result._embedded.articles)
                                status.success(dataList.subList(offset, offset + limit))
                            },
                            { error -> status.failed(error) }
                    )
        }
    }
}