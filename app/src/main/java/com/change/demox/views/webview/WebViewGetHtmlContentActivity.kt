package com.change.demox.views.webview

import android.os.Bundle
import android.util.Log
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import com.change.demox.R
import com.change.demox.application.MyApplication
import kotlinx.android.synthetic.main.activity_web_view_js.*

/**
 * webView获取html元素的属性
 *
 */
class WebViewGetHtmlContentActivity : AppCompatActivity() {

    val url = "http://www.cocoachina.com/articles/65680"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview_get_html)
        initView()
    }

    private fun initView() {
        val settings: WebSettings = webview_js.settings
        settings.javaScriptEnabled = true
        settings.builtInZoomControls = true
        settings.displayZoomControls = false
        settings.loadsImagesAutomatically = true
        webview_js.setInitialScale(100)
        webview_js.webChromeClient = WebChromeClient()
        webview_js.webViewClient = mWebViewClient
        //第二个参数要和JS语句里的一样
        webview_js.addJavascriptInterface(JavaScriptInterface(), "jsAndroid")
        webview_js.loadUrl(url)
    }

    private val mWebViewClient: WebViewClient = object : WebViewClient() {
        override fun onPageFinished(view: WebView?, url: String?) {
            //写JS查询指定下标meta的content.
//            view?.loadUrl(
//                    "javascript:window.jsAndroid.getMetaSrc(document.getElementsByTagName('meta')[2].content);")

            //写JS查询指定name字段的meta的content.
            view?.loadUrl(
                    "javascript:for (i = 0; i < document.getElementsByTagName('meta').length; i++) { if(document.getElementsByTagName('meta')[i].name=='keywords'){ window.jsAndroid.getMetaSrc(document.getElementsByTagName('meta')[i].content) }}"
            )

            super.onPageFinished(view, url)
        }
    }

    /**
     * 查询出的结果src，会参数传递到getMetaSrc（）内
     *
     */
    internal class JavaScriptInterface {
        @JavascriptInterface
        fun getMetaSrc(src: String) {
            Log.v("Meta", src)
            MyApplication.instance?.showToast("目标Meta Content:$src")
        }
    }
}