package com.h24.home24reviewapp.api

import com.h24.home24reviewapp.model.ResponseModel
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * REST API access point
 */
interface Home24Service {

    @GET("articles")
    fun getArticles(@Query("offset") offset: Int, @Query("limit") limit: Int): Observable<ResponseModel>
}
