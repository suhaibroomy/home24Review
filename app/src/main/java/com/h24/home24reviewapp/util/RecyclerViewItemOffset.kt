package com.h24.home24reviewapp.util

import android.graphics.Rect
import android.support.annotation.IntRange
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View

class RecyclerViewItemOffset
/**
 * constructor
 *
 * @param itemOffset  desirable itemOffset size in px between the views in the recyclerView
 * @param spanSize spanSize for GridLayout
 */
(@param:IntRange(from = 0) private val itemOffset: Int, private val spanSize: Int) : RecyclerView.ItemDecoration() {

    /**
     * Set margins for the items inside the recyclerView while considering the layoutManager
     */
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (view.layoutParams is GridLayoutManager.LayoutParams) {
            if (spanSize == (view.layoutParams as GridLayoutManager.LayoutParams).spanSize) {
                outRect.set(0, itemOffset, 0, itemOffset)
                return
            }
            val modulus = (view.layoutParams as GridLayoutManager.LayoutParams).spanIndex % spanSize
            if (modulus == 0) {
                outRect.set(itemOffset * 2, itemOffset, itemOffset, itemOffset)
            } else if (modulus == spanSize - 1) {
                outRect.set(itemOffset, itemOffset, itemOffset * 2, itemOffset)
            } else {
                outRect.set(itemOffset, itemOffset, itemOffset, itemOffset)
            }
        } else {
            outRect.set(0, itemOffset, 0, itemOffset)
        }
    }
}
