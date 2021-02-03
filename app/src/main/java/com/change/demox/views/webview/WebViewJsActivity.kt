package com.change.demox.views.webview

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import com.change.demox.R
import kotlinx.android.synthetic.main.activity_web_view_js.*

/**
 * Js调用Android
 *
 * 点击的网页JS会调用Android本画面代码
 *
 * 交互逻辑是：js文件的点击回调方法里，调用安卓端的方法名，就能通知安卓端进入android代码的回调里。
 *
JS：
$(document).ready(function(){
$("area").off('click').on('click', function(){
//点击以后的JS->android ,调用Android中JavaScriptInterface.station()
JavaScriptInterface.station($(this).prop('alt'));
});
})


Android：
internal class JavaScriptInterface {
@JavascriptInterface
fun station(name: String?) {
if (TextUtils.isEmpty(name)) {
return
}
Log.v("showContent", name)
}
}
如此，点击触发JS执行后，Android的station方法里就能接收到了。
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
        //网页文件在本地
        // webview_js.loadUrl("file:///android_asset/routemap.html")
        //网页文件在远端服务器
         webview_js.loadUrl("http://47.105.132.26:22123/routemap.html")

        //webview_js.loadUrl("https://v01-ib.valuedirect.nri.co.jp/sp_im/IBGate/sD02101CT/PD/2#DSD0210150")
    }


    private val mWebViewClient: WebViewClient = object : WebViewClient() {

        /**
         * url重定向会执行此方法以及点击页面某些链接也会执行此方法
         * 通俗的说，当返回true时，你点任何链接都是失效的，需要你自己跳转。return false时webview会自己跳转。
         */
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            //点击图片区域之后，防止webview跳转
            return true
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            Log.v("url:", url)
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