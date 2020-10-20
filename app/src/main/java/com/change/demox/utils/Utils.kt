package com.change.demox.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.net.URI

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

    /**
     * Start External Browser
     */
    fun startBrowser(context: Context?, url: String?) {
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        val contentUrl = Uri.parse(url)
        intent.data = contentUrl
        try {
            context?.startActivity(intent)
        } catch (e: Exception) {
        }
    }

    /**
     * judge the url
     *
     * @param url
     * @return
     */
    fun isUrlNeedShowInBrowser(url: String?): Boolean {
        if (TextUtils.isEmpty(url)) {
            return false
        }
        val uri = URI.create(url)
        if (TextUtils.equals("BuildConfig.UR_DOMAIN", uri.authority)) {
            return false
        }
        return true
    }

    fun hasNetWork(context: Context): Boolean {
        val mConnectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = mConnectivityManager.activeNetworkInfo ?: return false
        return networkInfo.isConnected
    }

}