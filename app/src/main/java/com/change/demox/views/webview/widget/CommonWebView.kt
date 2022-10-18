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
//        settings.builtInZoomControls = false
     //   webViewClient = null
        webChromeClient = null
        clearCache(true)
        clearSslPreferences()
        clearFormData()
        clearView()
        destroyDrawingCache()
        destroy()
    }

    internal inner class ChromeClient : WebChromeClient() {

        /**
         * 根据这个回调，可以控制进度条的进度（包括显示与隐藏）。一般情况下，想要达到100%的进度需要的时间较长（特别是首次加载），
         * 用户长时间等待进度条不消失必定会感到焦虑，影响体验。其实当progress达到80的时候，加载出来的页面已经基本可用了。事实上，
         * 国内厂商大部分都会提前隐藏进度条，让用户以为网页加载很快
         *
         * @param view
         * @param newProgress
         */
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            if (mListener != null) {
                mListener?.onProgress(newProgress)
            }
            super.onProgressChanged(view, newProgress)
        }

        /**
         * 用来设置标题。需要注意的是，在部分Android系统版本中可能会回调多次这个方法，而且有时候回调的title是一个url，
         * 客户端可以针对这种情况进行特殊处理，避免在标题栏显示不必要的链接
         *
         * @param view
         * @param title
         */
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

        /**
         * 加载页面结束回调
         *
         * @param view
         * @param url
         */
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

        /**
         * 如果遇到了重定向，或者点击了页面中的a标签实现页面跳转，那么会回调这个方法。可以说这个是WebView里面最重要的回调之一
         *
         * shouldOverrideUrlLoading 这个方法的返回值 ：

        return true： 表示当前url即使是重定向url也不会再执行（除了在return true之前使用webview.loadUrl(url)除外，因为这个会重新加载）
        return false： 表示由系统执行url，直到不再执行此方法，即加载完重定向的ur（即具体的url，不再有重定向）。
         *
         * @param view
         * @param url
         * @return
         */
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器（ Utils.startBrowser（）方法打开浏览器）
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

        /**
         * 加载页面的过程中发生了错误，会回调这个方法。主要是http错误以及ssl错误。在这两个回调中，我们可以进行异常上报，监控异常页面、过期页面，及时反馈给运营或前端修改。
         * 在处理ssl错误时，遇到不信任的证书可以进行特殊处理，例如对域名进行判断，针对自己公司的域名“放行”，防止进入丑陋的错误证书页面。也可以与Chrome一样，
         * 弹出ssl证书疑问弹窗，给用户选择的余地
         *
         * @param view
         * @param request
         * @param error
         */
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