package com.h24.home24reviewapp.ui.review

import android.support.v7.content.res.AppCompatResources
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.request.ImageRequest
import com.h24.home24reviewapp.H24App
import com.h24.home24reviewapp.R
import com.h24.home24reviewapp.model.ArticleModel

/**
 * Adapter for recycler of ReviewActivity, it can handle grid and list layout
 * @param layoutType LayoutType(GRID/LIST)
 */
class ReviewAdapter(private var layoutType: ReviewViewModel.LayoutType) : RecyclerView.Adapter<ReviewViewHolder>() {

    private val TYPE_GRID = 0
    private val TYPE_LIST = 1

    private lateinit var articleList: List<ArticleModel>
    private var layoutInflater: LayoutInflater = LayoutInflater.from(H24App.coreComponent.context())

    override fun getItemViewType(position: Int): Int {
        return when (layoutType) {
            ReviewViewModel.LayoutType.GRID -> TYPE_GRID
            ReviewViewModel.LayoutType.LIST -> TYPE_LIST
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ReviewViewHolder {
        return when (viewType) {
            TYPE_GRID -> ReviewViewHolder(layoutInflater.inflate(R.layout.item_article_grid, viewGroup, false))
            else -> ReviewViewHolder(layoutInflater.inflate(R.layout.item_article_list, viewGroup, false))
        }
    }

    override fun getItemCount(): Int {
        return if (::articleList.isInitialized) articleList.size else 0
    }

    override fun onBindViewHolder(viewHolder: ReviewViewHolder, position: Int) {
        viewHolder.onBind(articleList[position])
    }

    /**
     * Updates the articles List and notifies the adapter
     * @param articleList updated articles list
     */
    fun updateArticleList(articleList: List<ArticleModel>) {
        this.articleList = articleList
        notifyDataSetChanged()
    }

    /**
     * Updates the layoutType and notifies the adapter
     * @param value udpated LayoutType Value
     */
    fun updateLayoutType(value: ReviewViewModel.LayoutType) {
        if (layoutType == value) {
            return
        }
        layoutType = value
        notifyDataSetChanged()
    }
}

class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val tvTitle = itemView.findViewById<TextView?>(R.id.tv_title)
    private val tvBrand = itemView.findViewById<TextView?>(R.id.tv_brand)
    private val tvPrice = itemView.findViewById<TextView?>(R.id.tv_price)
    private val ivMainImage = itemView.findViewById<SimpleDraweeView>(R.id.iv_main)
    private val ivLiked = itemView.findViewById<ImageView>(R.id.iv_liked)
    private val icLikeDrawable = AppCompatResources.getDrawable(itemView.context, R.drawable.ic_like)
    private val icUnlikeDrawable = AppCompatResources.getDrawable(itemView.context, R.drawable.ic_unlike)

    fun onBind(model: ArticleModel) {
        tvTitle?.text = model.title
        tvBrand?.text = model.getBrand()
        tvPrice?.text = model.getPrice()

        val controller = Fresco.newDraweeControllerBuilder()
                .setOldController(ivMainImage.controller)
                .setImageRequest(ImageRequest.fromUri(model.getMediaUrl()))
                .setTapToRetryEnabled(true)
                .build()
        ivMainImage.controller = controller

        ivLiked.setImageDrawable(if (model.isLiked) icLikeDrawable else icUnlikeDrawable)
    }

}