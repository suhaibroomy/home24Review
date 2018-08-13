package com.h24.home24reviewapp.api

import com.h24.home24reviewapp.model.ResponseModel
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.QueryMap
import java.util.*

/**
 * REST API access point
 */
interface Home24Service {

    @GET("articles")
    fun getArticles(@QueryMap map: HashMap<String, String>): Observable<ResponseModel>
}
