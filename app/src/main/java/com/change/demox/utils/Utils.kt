package com.change.demox.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 * Utilメソッド
 */
object Utils {

    /**
     * hideSoftKeyboard
     *
     * @param activity
     */
    fun hideSoftKeyboard(activity: Activity) {
        if (activity.currentFocus != null && activity.currentFocus!!.windowToken != null) {
            val manager =
                    activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            manager.hideSoftInputFromWindow(
                    activity.currentFocus!!
                            .windowToken, InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }

    fun showSoftKeyboard(view: View, activity: Activity) {
        val manager =
                activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        manager.showSoftInput(
                view, InputMethodManager.SHOW_IMPLICIT
        )
    }

}