package com.change.demox.views.webview

import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.change.demox.R
import kotlinx.android.synthetic.main.activity_web_view_try.*

/**
 * Webview-> 常规
 *
 */
class WebViewCommonActivity : AppCompatActivity() {

    val webUrl = "https://www.baidu.com/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view_common)
        initView()
    }

    private fun initView() {
        webview.webChromeClient = WebChromeClient()
        webview.webViewClient = WebViewClient()
        webview.loadUrl(webUrl)
    }
}