package com.change.demox.views.webview.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.net.http.SslError
import android.os.Build
import android.text.TextUtils
import android.util.AttributeSet
import android.view.MotionEvent
import android.webkit.*
import com.change.demox.utils.Utils


class CommonWebView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null
) : WebView(context, attrs) {

    private var lastTime = 0L
    private var mListener: WebViewStateChangeListener? = null
    private var mContext: Context? = null
    private var mScrollInterface: ScrollInterface? = null

    interface ScrollInterface {
        fun onSChanged(scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int)
    }

    interface WebViewStateChangeListener {
        fun onLoadStart()
        fun onLoadFinish()
        fun onTitleChange(title: String?)
        fun onProgress(progress: Int)
        fun onDisConnectError(url: String?)
        fun onLoadingError(url: String?)
    }

    init {
        this.mContext = context
        initWeb()
    }

    fun setOnCustomScrollChangeListener(mInterface: ScrollInterface) {
        mScrollInterface = mInterface
    }

    fun setOnStateChangeListener(stateListener: WebViewStateChangeListener) {
        mListener = stateListener
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        if (mScrollInterface != null) {
            mScrollInterface?.onSChanged(l, t, oldl, oldt)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            val currentTime = System.currentTimeMillis()
            val intervalTime = currentTime - lastTime
            if (intervalTime < 300) {
                lastTime = currentTime
                return true
            } else {
                lastTime = currentTime
            }
        } else if (event?.action == MotionEvent.ACTION_MOVE) {
            //手指move的时候隐藏软键盘
        }
        return super.onTouchEvent(event)
    }

    @SuppressLint("SetJavaScriptEnabled")
    fun initWeb() {
        webChromeClient = ChromeClient()
        webViewClient = Client()
        settings.javaScriptEnabled = true
        settings.useWideViewPort = true
        settings.loadWithOverviewMode = true
        settings.domStorageEnabled = true
        settings.builtInZoomControls = false
        settings.setSupportZoom(false)
        settings.displayZoomControls = false
    }

    /**
     * 清除webView cookie
     *
     * @param url
     * @param cookie
     */
    fun syncCookie(url: String, cookie: String) {
        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(cookie)) {
            return
        }
        val cookieManager = CookieManager.getInstance()
        cookieManager.setAcceptCookie(true)
        val uri = Uri.parse(url)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.removeAllCookies { value ->
                cookieManager.setAcceptCookie(true)
                cookieManager.setCookie(uri.host, cookie)
                cookieManager.flush()
            }
        } else {
            cookieManager.removeAllCookie()
            cookieManager.setAcceptCookie(true)
            cookieManager.setCookie(uri.host, cookie)
            CookieSyncManager.getInstance().sync()
        }
    }

    /**
     * 清空webView
     *
     */
    fun clearWebView() {
        clearView()
        removeAllViews()
    }

    /**
     * 清空webview设置
     *
     */
    fun releaseWebView() {
        stopLoading()
        settings.builtInZoomControls = false
        webViewClient = null
        webChromeClient = null
        clearCache(true)
        clearSslPreferences()
        clearFormData()
        clearView()
        destroyDrawingCache()
        destroy()
    }

    internal inner class ChromeClient : WebChromeClient() {

        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            if (mListener != null) {
                mListener?.onProgress(newProgress)
            }
            super.onProgressChanged(view, newProgress)
        }

        override fun onReceivedTitle(view: WebView?, title: String?) {
            super.onReceivedTitle(view, title)
            if (mListener != null) {
                mListener?.onTitleChange(title)
            }
        }
    }

    internal inner class Client : WebViewClient() {

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            clearWebView()
            if (mListener != null) {
                mListener?.onLoadStart()
            }
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            if (!Utils.hasNetWork(context)) {
                if (mListener != null) {
                    mListener?.onDisConnectError(view?.url)
                }
            }
            if (mListener != null) {
                mListener?.onLoadFinish()
            }
        }

        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
            return if (Utils.isUrlNeedShowInBrowser(url)) {
                Utils.startBrowser(mContext, url)
                false
            } else {
                true
            }
        }

        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
            return if (Utils.isUrlNeedShowInBrowser(request?.url.toString())) {
                Utils.startBrowser(mContext, request?.url.toString())
                false
            } else {
                true
            }
        }

        override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
            super.onReceivedError(view, request, error)
            if (request?.isForMainFrame != false) {
                if (mListener != null) {
                    mListener?.onLoadingError(view?.url)
                }
            }
        }

        override fun onReceivedError(view: WebView?, errorCode: Int, description: String?, failingUrl: String?) {
            super.onReceivedError(view, errorCode, description, failingUrl)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return
            }
            if (mListener != null) {
                mListener?.onLoadingError(view?.url)
            }
        }

        // Http error > 400 ,this method will be called
        override fun onReceivedHttpError(
                view: WebView?,
                request: WebResourceRequest?,
                errorResponse: WebResourceResponse?
        ) {
            super.onReceivedHttpError(view, request, errorResponse)
            if (request?.isForMainFrame != false) {
                if (mListener != null && !TextUtils.equals(
                                errorResponse?.reasonPhrase,
                                "Unauthorized"
                        )
                ) {
                    mListener?.onLoadingError(view?.url)
                }
            }
        }

        override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
            super.onReceivedSslError(view, handler, error)

        }
    }

}