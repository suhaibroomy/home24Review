package com.h24.home24reviewapp.model

/**
 * All the models needed to parse api response are in this class
 */
data class ResponseModel(val _embedded: ArticleResponseModel)

data class ArticleResponseModel (val articles: ArrayList<ArticleModel>)

data class ArticleModel(val title: String, val sku: String, val media: ArrayList<MediaModel>,
                        val brand: BrandModel, val price: PriceModel, var isLiked: Boolean = false) {
    override fun equals(other: Any?): Boolean {
        return (other as? ArticleModel)?.sku.equals(sku)
    }

    override fun hashCode(): Int {
        return sku.hashCode()
    }

    fun getMediaUrl(): String {
        return if (media.isEmpty()) "" else media[0].uri
    }

    fun getPrice(): String {
        return "${price.amount} ${price.currency}"
    }

    fun getBrand(): String {
        return brand.title
    }
}

data class PriceModel (val amount: String, val currency: String)

data class BrandModel (val title: String)

data class MediaModel(val uri: String)
