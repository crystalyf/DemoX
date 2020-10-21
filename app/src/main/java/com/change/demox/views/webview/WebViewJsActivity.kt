package com.change.demox.views.webview

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import com.change.demox.R
import com.change.demox.utils.Utils
import kotlinx.android.synthetic.main.activity_web_view_js.*
import kotlinx.android.synthetic.main.layout_webview.*

/**
 * Js调用Android
 *
 * 点击的网页JS会调用Android本画面代码
 *
 */
class WebViewJsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view_js)
        initView()
    }

    private fun initView() {
        val settings: WebSettings = webview_js.settings
        settings.javaScriptEnabled = true
        settings.builtInZoomControls = true
        settings.displayZoomControls = false
        settings.loadsImagesAutomatically = true
        webview_js.setInitialScale(100)
        //通过 addJavascriptInterface 方法进行添加对象映射
        webview_js.addJavascriptInterface(JavaScriptInterface(), "JavaScriptInterface")
        webview_js.webChromeClient = WebChromeClient()
        webview_js.webViewClient = mWebViewClient
        webview_js.loadUrl("file:///android_asset/routemap.html")
    }

    private val mWebViewClient: WebViewClient = object : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            //点击图片区域之后，防止webview跳转
            return true
        }
    }

    /**
     * 前端代码嵌入js：
     * imageClick 名应和js函数方法名一致
     *
     * @param src 图片的链接
     */
    internal class JavaScriptInterface {
        @JavascriptInterface
        fun station(name: String?) {
            if (TextUtils.isEmpty(name)) {
                return
            }
            Log.v("showContent", name)
        }
    }
}