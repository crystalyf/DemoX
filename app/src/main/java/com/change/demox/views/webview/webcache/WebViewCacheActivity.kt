package com.change.demox.views.webview.webcache

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
import com.change.demox.views.webview.webcache.component.config.FastCacheMode
import kotlinx.android.synthetic.main.activity_web_view_try.*
import kotlinx.android.synthetic.main.activity_webview_cache.*


class WebViewCacheActivity : AppCompatActivity() {

    val TAG = "FastWebView"

    //var url = "https://www.pref.saitama.lg.jp/a0311/bouhansupporter/index.html"

    var url = "https://v01-ib.valuedirect.nri.co.jp/sp_im/IBGate/sD02101CT/PD/2#DSD0210150"
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
        settings.setAppCacheEnabled(true)
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true
        settings.allowFileAccess = true
        settings.useWideViewPort = true
        settings.loadWithOverviewMode = true
        settings.defaultTextEncodingName = "UTF-8"
        webview_cache.webChromeClient = WebChromeClient()
        webview_cache.webViewClient = mWebViewClient

        //   webview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webview_cache.setCacheMode(FastCacheMode.FORCE)
        webview_cache.loadUrl(url)


        webview_cache.setDownloadListener { url, userAgent, contentDisposition, mimetype, contentLength ->
            Log.v("url:DownloadListener", "进入setDownloadListener，mimetype :$mimetype")
            // PDF document use external browser
            if (TextUtils.isEmpty(mimetype)) {
                return@setDownloadListener
            }
            if (mimetype.equals("application/pdf")) {
                Log.v("url:DownloadListener", "进入setDownloadListener，mimetype :$mimetype")
                //调用系统中已经内置的浏览器进行下载：
                showExternalApplicationDialog(url)
            }
        }


        btn_refresh.setOnClickListener {
            webview_cache.reload()
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
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            Log.v("shouldOverrideUrl:", url)
            //点击图片区域之后，防止webview跳转
            return false
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            Log.v("url:", url!!)
        }
    }

    private fun startBrowser(context: Context, url: String?) {
        if (TextUtils.isEmpty(url)) {
            return
        }
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        val content_url = Uri.parse(url)
        intent.data = content_url
        try {
            context.startActivity(intent)
        } catch (e: Exception) {

        }
    }

}