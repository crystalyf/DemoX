package com.change.demox.views.customview

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.change.demox.R
import com.change.demox.views.recyclerview.banner.BannerRootActivity
import com.change.demox.views.bottomsheet.BottomSheetRootActivity
import com.change.demox.views.browser.BrowserActivity
import com.change.demox.views.chart.ChartActivity
import com.change.demox.views.collapsingtoolbarlayout.ProfileHomePageActivity
import com.change.demox.views.customview.linechart.LineChartViewActivity
import com.change.demox.views.dialog.DialogActivity
import com.change.demox.views.edittext.EditTextActivity
import com.change.demox.views.firebase.FirebaseRootActivity
import com.change.demox.views.googlemap.GoogleMapStartActivity
import com.change.demox.views.imageview.ImageViewActivity
import com.change.demox.views.layout.LayoutActivity
import com.change.demox.views.qrcode.scan.CustomScanActivity
import com.change.demox.views.recyclerview.RecyclerActivity
import com.change.demox.views.slideview.SlideViewActivity
import com.change.demox.views.spinner.SpinnerActivity
import com.change.demox.views.textview.TextViewActivity
import com.change.demox.views.webview.WebViewActivity
import kotlinx.android.synthetic.main.activity_custom_view.*
import kotlinx.android.synthetic.main.activity_view.*


class CustomViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_view)
        initView()
    }

    private fun initView() {
        btn_custom_line_chart.setOnClickListener {
            val intent = Intent(this, LineChartViewActivity::class.java)
            startActivity(intent)
        }
    }
}