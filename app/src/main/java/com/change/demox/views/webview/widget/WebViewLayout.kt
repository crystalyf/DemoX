package com.change.demox.views.webview.widget

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.change.demox.databinding.LayoutWebviewBinding

/**
 * webView的自定义组件（包含了等待加载，错误布局）
 *
 * 自定义组件和自定义View最大的区别就是：
 * 自定义组件是画好布局文件然后自定义组件的class引进来布局，自定义View布局全是代码写的：onDraw,onLayout等等
 *
 */
class WebViewLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var binding: LayoutWebviewBinding? = null
    private var mContext: Context? = null
    private var webViewUrl = ""
    private var listener: WebViewTitleChangeListener? = null
    private var stateListener: CommonWebView.WebViewStateChangeListener? = null
    private var stopProgressFlag = false

    interface WebViewTitleChangeListener {
        fun onTitleChange(title: String?)
    }

    init {
        val inflate = LayoutInflater.from(context)
        binding = LayoutWebviewBinding.inflate(inflate, this, true)
        mContext = context
        initView()
    }

    fun setTitleListener(listener: WebViewTitleChangeListener) {
        this.listener = listener
    }

    fun setStateListener(listener: CommonWebView.WebViewStateChangeListener) {
        stateListener = listener
    }

    fun getWebView(): CommonWebView? {
        return binding?.webView
    }

    fun getErrorButton(): TextView? {
        return binding?.errorLayout?.errorBtn
    }

    fun getErrorLayout(): View? {
        return binding?.errorLayout?.errorLayout
    }

    fun loadUrl(url: String) {
        binding?.webView?.loadUrl(url)
        webViewUrl = url
        stopProgressFlag = false
    }

    fun load() {
        binding?.webView?.loadUrl(webViewUrl)
        stopProgressFlag = false
    }

    private fun initView() {
        binding?.errorLayout?.errorBtn?.setOnClickListener {
            binding?.errorLayout?.errorLayout?.visibility = View.GONE
            load()
            binding?.webView?.clearWebView()
        }

        binding?.webView?.setOnStateChangeListener(object : CommonWebView.WebViewStateChangeListener {
            override fun onLoadStart() {
                if (stateListener != null) {
                    stateListener?.onLoadStart()
                }
            }

            override fun onLoadFinish() {
                if (stateListener != null) {
                    stateListener?.onLoadFinish()
                }
            }

            override fun onTitleChange(title: String?) {
                if (listener != null) {
                    listener?.onTitleChange(title ?: "")
                }
                if (stateListener != null) {
                    stateListener?.onTitleChange(title ?: "")
                }
            }

            override fun onProgress(progress: Int) {
                var progressCopy = progress
                //如果是错误回调 ,设置progress的值为100
                if (stopProgressFlag) {
                    progressCopy = 100
                }
                if (progressCopy < 100) {
                    binding?.webProgress?.visibility = View.VISIBLE
                    //显示progress circle, 隐藏 webview
                    binding?.pbCircle?.visibility = View.VISIBLE
                    binding?.webView?.visibility = View.GONE
                    binding?.webProgress?.progress = progress
                } else {
                    binding?.webProgress?.visibility = View.GONE
                    //隐藏 progress circle，显示 webview
                    binding?.pbCircle?.visibility = View.GONE
                    binding?.webView?.visibility = View.VISIBLE
                    if (progress > 100) {
                        stopProgressFlag = false
                    }
                }
                if (stateListener != null) {
                    stateListener?.onProgress(progress)
                }
            }

            override fun onDisConnectError(url: String?) {
                if (!TextUtils.isEmpty(url)) {
                    webViewUrl = url.toString()
                }
                binding?.errorLayout?.errorMsg?.text = "请在通讯良好的地方重新加载"
                binding?.errorLayout?.errorLayout?.visibility = View.VISIBLE
                if (stateListener != null) {
                    stateListener?.onDisConnectError(url)
                }
                stopProgressFlag = true
            }

            override fun onLoadingError(url: String?) {
                if (!TextUtils.isEmpty(url)) {
                    webViewUrl = url.toString()
                }
                binding?.errorLayout?.errorMsg?.text = "对服务器的访问可能拥塞或维护不足。\n请稍等片刻，然后重试"
                binding?.errorLayout?.errorLayout?.visibility = View.VISIBLE
                if (stateListener != null) {
                    stateListener?.onLoadingError(url)
                }
                stopProgressFlag = true
            }
        })
    }

}