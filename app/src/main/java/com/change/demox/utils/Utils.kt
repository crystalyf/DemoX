package com.change.demox.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.change.demox.BuildConfig
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import java.net.URI
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import kotlin.coroutines.suspendCoroutine

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

    /**
     * 長いダイナミックリンクを作成する
     *
     * @param deepLink ディープリンク
     * @return 長いダイナミックリンク
     */
    fun buildDynamicLongLink(deepLink: Uri): String {
        val builder = FirebaseDynamicLinks.getInstance()
                .createDynamicLink()
                .setDomainUriPrefix(BuildConfig.DYNAMIC_LINK_URIPREFIX)
                .setAndroidParameters(
                        DynamicLink.AndroidParameters.Builder().build()
                )
//                .setIosParameters(
//                        DynamicLink.IosParameters.Builder(BuildConfig.IOS_BUNDLE_ID).setAppStoreId(
//                                BuildConfig.IOS_APP_STORE_ID
//                        ).build()
//                )
                .setLink(deepLink)
        //dynamic linkを作成する
        val link = builder.buildDynamicLink()
        return URLDecoder.decode(link.uri.toString(), StandardCharsets.UTF_8.name())
    }

    /**
     * 短いダイナミックリンクを作成する
     *
     * @param deepLink ディープリンク
     * @return 短いダイナミックリンク
     */
    suspend fun buildDynamicShortLink(deepLink: Uri): String? {
        var resultShort: String? = null
        suspendCoroutine<Result<String>?> {
            FirebaseDynamicLinks.getInstance().createDynamicLink()
                    .setLongLink((Uri.parse(buildDynamicLongLink(deepLink))))
                    .buildShortDynamicLink()
                    .addOnSuccessListener { result ->
                        // dynamic short linkを作成する
                        resultShort = result.shortLink.toString()
                    }.addOnFailureListener { e ->
                        // Exception
                        Log.v("error",e.toString())
                    }
        }
        return resultShort
    }
}