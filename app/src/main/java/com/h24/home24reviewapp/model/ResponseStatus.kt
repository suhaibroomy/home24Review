package com.h24.home24reviewapp.model

/**
 * ResponseStatus is class to represent all the possible states of
 * Api response. It can be passed on to the listeners to notify stats
 */
sealed class ResponseStatus<T> {
    data class Progress<T>(var loading: Boolean) : ResponseStatus<T>()
    data class Success<T>(var data: T) : ResponseStatus<T>()
    data class Failure<T>(val e: Throwable) : ResponseStatus<T>()

    companion object {
        fun <T> loading(isLoading: Boolean): ResponseStatus<T> = Progress(isLoading)

        fun <T> success(data: T): ResponseStatus<T> = Success(data)

        fun <T> failure(e: Throwable): ResponseStatus<T> = Failure(e)
    }
}