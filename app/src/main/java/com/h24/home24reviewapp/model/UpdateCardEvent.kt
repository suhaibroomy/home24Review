package com.h24.home24reviewapp.model

/**
 * Event Details for like, unlike and add articles
 */
sealed class UpdateCardEvent {
    data class Add(val articles: List<ArticleModel>) : UpdateCardEvent()
    data class SwipeRight(var articleModel: ArticleModel?) : UpdateCardEvent()
    data class SwipeLeft(var articleModel: ArticleModel?) : UpdateCardEvent()

    companion object {
        fun add(articles: List<ArticleModel>): UpdateCardEvent = Add(articles)

        fun swipeRight(articleModel: ArticleModel?): UpdateCardEvent = SwipeRight(articleModel)

        fun swipeLeft(articleModel: ArticleModel?): UpdateCardEvent = SwipeLeft(articleModel)
    }
}