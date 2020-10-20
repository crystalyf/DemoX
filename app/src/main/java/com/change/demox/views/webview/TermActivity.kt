package com.change.demox.views.webview

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.change.demox.R
import com.change.demox.databinding.ActivityTermBinding
import com.change.demox.views.webview.widget.CommonWebView

/**
 * 免责规约（WebView画面）
 *
 * 加载网页URL的过程中转圈提醒，加载好了之后关闭转圈
 *
 */
class TermActivity : AppCompatActivity() {

    private var dataBinding: ActivityTermBinding? = null
    private var isAgreeBtnCanClick = true
    private var isLoadError = false

    override fun onCreate(savedInstanceState: Bundle?) {
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_term)
        super.onCreate(savedInstanceState)
        initView()
        initWeb()
    }

    private fun initView() {
        dataBinding?.btnAgree?.isEnabled = false
        dataBinding?.btnAgree?.setTextColor(resources.getColor(R.color.colorGray))
        dataBinding?.btnAgree?.setOnClickListener {
            Toast.makeText(this, "跳转到下一画面", Toast.LENGTH_SHORT).show()
        }
        dataBinding?.webLayout?.getErrorButton()?.setOnClickListener {
            isLoadError = false
            dataBinding?.webLayout?.getErrorLayout()?.visibility = View.GONE
            dataBinding?.webLayout?.load()
            dataBinding?.webLayout?.getWebView()?.clearWebView()
        }
        dataBinding?.cbTerm?.isClickable = false
        dataBinding?.cbTerm?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                if (isAgreeBtnCanClick) {
                    dataBinding?.btnAgree?.isEnabled = true
                    dataBinding?.btnAgree?.setTextColor(resources.getColor(R.color.white))
                } else {
                    dataBinding?.btnAgree?.isEnabled = true
                    dataBinding?.btnAgree?.setTextColor(resources.getColor(R.color.white))
                }
            } else {
                dataBinding?.btnAgree?.isEnabled = false
                dataBinding?.btnAgree?.setTextColor(resources.getColor(R.color.colorGray))
            }
        }
    }

    private fun initWeb() {
        dataBinding?.webLayout?.setStateListener(object : CommonWebView.WebViewStateChangeListener {
            override fun onLoadStart() {
                //
            }

            override fun onLoadFinish() {
                if (!isLoadError) {
                    dataBinding?.cbTerm?.isClickable = true
                }
            }

            override fun onTitleChange(title: String?) {
                //
            }

            override fun onProgress(progress: Int) {
                //
            }

            override fun onDisConnectError(url: String?) {
                isAgreeBtnCanClick = false
                isLoadError = true
                dataBinding?.cbTerm?.isClickable = false
            }

            override fun onLoadingError(url: String?) {
                isAgreeBtnCanClick = false
                isLoadError = true
                dataBinding?.cbTerm?.isClickable = false
            }

        })
        //加载免责规约的URL
        dataBinding?.webLayout?.loadUrl("https://www.baidu.com/")
    }

//按硬件返回键退到后台
//    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            moveTaskToBack(true)
//            return true
//        }
//        return super.onKeyDown(keyCode, event)
//    }
}