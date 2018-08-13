package com.h24.home24reviewapp.extensions

import com.h24.home24reviewapp.model.ResponseStatus
import io.reactivex.subjects.PublishSubject

/**
 * Extension function to push a failed event with an exception to the observing responseStatus
 * */
fun <T> PublishSubject<ResponseStatus<T>>.failed(e: Throwable) {
    with(this){
        loading(false)
        onNext(ResponseStatus.failure(e))
    }
}

/**
 * Extension function to push  a success event with data to the observing responseStatus
 * */
fun <T> PublishSubject<ResponseStatus<T>>.success(t: T) {
    with(this){
        loading(false)
        onNext(ResponseStatus.success(t))
    }
}

/**
 * Extension function to push the loading status to the observing responseStatus
 * */
fun <T> PublishSubject<ResponseStatus<T>>.loading(isLoading: Boolean) {
    this.onNext(ResponseStatus.loading(isLoading))
}