package com.change.demox.views.webview

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.webkit.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.change.demox.R
import kotlinx.android.synthetic.main.activity_web_view_try.*

/**
 * Webview-> 点击下载监听
 *
 */
class WebViewTryActivity : AppCompatActivity() {

    val webUrl = "https://www.eiken.or.jp/eiken/apply/"
    // val webUrl = "http://www.dqjsw.com.cn/down/8509.html"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view_try)
        initView()
    }

    private fun initView() {
        val settings: WebSettings = webview.settings
        settings.builtInZoomControls = true
        settings.displayZoomControls = false
        settings.loadsImagesAutomatically = true
        webview.setInitialScale(100)
        webview.webChromeClient = WebChromeClient()
        webview.webViewClient = mWebViewClient
        webview.loadUrl(webUrl)


        /**
         * 点击这个8509网页上下载APK的按钮，就触发下载监听了
         */
        webview.setDownloadListener { url, userAgent, contentDisposition, mimetype, contentLength ->
            Log.v("url:DownloadListener", "进入setDownloadListener，mimetype :$mimetype")
            // PDF document use external browser
            if (TextUtils.isEmpty(mimetype)) {
                return@setDownloadListener
            }
//            if (mimetype.equals("application/pdf")) {
            Log.v("url:DownloadListener", "进入setDownloadListener，mimetype :$mimetype")
            //调用系统中已经内置的浏览器，打开外部浏览器进行下载：
            showExternalApplicationDialog(url)
//            }
        }
    }

    private fun showExternalApplicationDialog(data: String) {
        AlertDialog.Builder(this)
                .setTitle("标题")
                .setMessage("信息")
                .setPositiveButton("OK", { dialog, which ->
                    dialog.dismiss()
                    startBrowser(this, data)
                })
                .setNegativeButton("Cancel", { dialog, which -> dialog.dismiss() })
                .show()
    }


    private val mWebViewClient: WebViewClient = object : WebViewClient() {

        /**
         * url重定向会执行此方法以及点击页面某些链接也会执行此方法
         * 通俗的说，当返回true时，你点任何链接都是失效的，需要你自己跳转。return false时webview会自己跳转。
         *
         *
         * 如果loadUrl方法执行之后，没有内部重定向到新的URL，那么这个函数是不会走的，直接走onPageFinished
         *
         */
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            Log.v("shouldOverrideUrl:", url)
            //点击图片区域之后，防止webview跳转
            return false
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            Log.v("url:", url)
        }

        override fun onReceivedHttpError(view: WebView?, request: WebResourceRequest?, errorResponse: WebResourceResponse) {
            Log.v("url:", errorResponse.statusCode.toString())
            if (errorResponse.statusCode >= 500) {
            }
        }
    }


    private fun startBrowser(context: Context, url: String?) {
        if (TextUtils.isEmpty(url)) {
            return
        }
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        val contentUrl = Uri.parse(url)
        intent.data = contentUrl
        try {
            context.startActivity(intent)
        } catch (e: Exception) {

        }
    }


}