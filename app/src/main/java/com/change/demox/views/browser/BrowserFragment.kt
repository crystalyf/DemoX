package com.change.demox.views.browser

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.change.demox.R
import com.change.demox.views.recyclerview.banner.component.BaseFragment
import kotlinx.android.synthetic.main.fragment_browser.*


/**  web浏览器
 *
 *
原生web浏览器，按浏览器的返回键：

浏览器的返回键受浏览器应用程序的控制，按浏览器的返回键会回到上一个进入的浏览器画面，如果在根浏览器画面再次点击浏览器返回键，那么就会关闭浏览器回到APP


按硬件返回键：
原生按浏览器的返回键会回到上一个进入的浏览器画面，如果在根浏览器画面再次点击back key，那么就会关闭浏览器回到APP
 *
 *
 * */

class BrowserFragment : BaseFragment() {

    // private val WEB_PAGE_URL = "https://www.seiburailway.jp/railways/tourist/chinese/"
    private val WEB_PAGE_URL = "https://www.surugabank.co.jp/surugabank/kojin/open/"

    override fun getLayoutId(): Int = R.layout.fragment_browser

    override fun initView() {
        btn_open_browser.setOnClickListener {
            startBrowser(activity, WEB_PAGE_URL)
        }
    }

    override fun lazyLoad() {

    }

    /**
     * Start External Browser
     */
    private fun startBrowser(context: Context?, url: String?) {
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        val contentUrl = Uri.parse(url)
        intent.data = contentUrl
        try {
            context?.startActivity(intent)
        } catch (e: Exception) {

        }
    }


}
