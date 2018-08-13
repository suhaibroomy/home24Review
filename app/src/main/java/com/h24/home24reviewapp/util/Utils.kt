package com.h24.home24reviewapp.util

import android.support.annotation.UiThread
import android.text.TextUtils
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.h24.home24reviewapp.H24App

/**
 * Show Toast with text gravity as Gravity.CENTER_HORIZONTAL
 * @param message Message to be shown in Toast
 */
@UiThread
fun showToast(message: String?) {
    if (message == null || TextUtils.isEmpty(message)) {
        return
    }

    val toast = Toast.makeText(H24App.coreComponent.context(), message, Toast.LENGTH_LONG)
    var toastTxv: TextView? = null
    val view = toast.view
    if (view is TextView) {
        toastTxv = view
    } else if (view is ViewGroup) {
        for (i in 0 until view.childCount) {
            val child = view.getChildAt(i)
            if (child is TextView) {
                toastTxv = child
                break
            }
        }
    }
    if (toastTxv != null) {
        toastTxv.gravity = Gravity.CENTER_HORIZONTAL
    }
    toast.show()
}