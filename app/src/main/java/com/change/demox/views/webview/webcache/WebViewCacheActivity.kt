package com.change.demox.views.webview.webcache

import android.os.Bundle
import android.util.Log
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import com.change.demox.R
import com.change.demox.views.webview.webcache.component.config.FastCacheMode
import kotlinx.android.synthetic.main.activity_webview_cache.*

/**
 * Js调用Android
 *
 * 点击的网页JS会调用Android本画面代码
 *
 */
class WebViewCacheActivity : AppCompatActivity() {

    val TAG = "FastWebView"

    var url = "https://www.pref.saitama.lg.jp/a0311/bouhansupporter/index.html"
   // var url = "http://www.huoyanzn.com/"
   // var url = "https://www.seiburailway.jp/railways/tourist/chinese/"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview_cache)
        initView()
    }

    private fun initView() {
        val settings: WebSettings = webview_cache.settings
//        settings.javaScriptEnabled = true
        settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
        settings.displayZoomControls = false
        settings.setAppCacheEnabled(true)

        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true
        settings.allowFileAccess = true
        settings.useWideViewPort = true
        settings.loadWithOverviewMode = true
        settings.setSupportZoom(false)
        settings.builtInZoomControls = false
        settings.displayZoomControls = false
        settings.defaultTextEncodingName = "UTF-8"
        webview_cache.webChromeClient = WebChromeClient()
        webview_cache.webViewClient = mWebViewClient

        //   webview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webview_cache.setCacheMode(FastCacheMode.FORCE)
        webview_cache.loadUrl(url)

        btn_refresh.setOnClickListener {
            webview_cache.reload()
        }
    }

    private val mWebViewClient: WebViewClient = object : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            //点击图片区域之后，防止webview跳转
            return true
        }
    }

}