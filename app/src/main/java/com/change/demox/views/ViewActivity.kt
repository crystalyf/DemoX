package com.change.demox.views

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.change.demox.R
import com.change.demox.views.recyclerview.banner.BannerRootActivity
import com.change.demox.views.bottomsheet.BottomSheetRootActivity
import com.change.demox.views.browser.BrowserActivity
import com.change.demox.views.chart.ChartActivity
import com.change.demox.views.collapsingtoolbarlayout.ProfileHomePageActivity
import com.change.demox.views.customview.CustomViewActivity
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
import kotlinx.android.synthetic.main.activity_view.*


class ViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view)
        initView()
    }

    private fun initView() {
        btn_spinner.setOnClickListener {
            val intent = Intent(this, SpinnerActivity::class.java)
            startActivity(intent)
        }
        btn_recyclerview.setOnClickListener {
            val intent = Intent(this, RecyclerActivity::class.java)
            startActivity(intent)
        }
        btn_layout.setOnClickListener {
            val intent = Intent(this, LayoutActivity::class.java)
            startActivity(intent)
        }
        btn_textview.setOnClickListener {
            val intent = Intent(this, TextViewActivity::class.java)
            startActivity(intent)
        }
        btn_edittext.setOnClickListener {
            val intent = Intent(this, EditTextActivity::class.java)
            startActivity(intent)
        }
        btn_bottomsheet.setOnClickListener {
            val intent = Intent(this, BottomSheetRootActivity::class.java)
            startActivity(intent)
        }
        btn_imageview.setOnClickListener {
            val intent = Intent(this, ImageViewActivity::class.java)
            startActivity(intent)
        }
        btn_webview.setOnClickListener {
            val intent = Intent(this, WebViewActivity::class.java)
            startActivity(intent)
        }
        btn_slide_view.setOnClickListener {
            val intent = Intent(this, SlideViewActivity::class.java)
            startActivity(intent)
        }
        btn_dialog.setOnClickListener {
            val intent = Intent(this, DialogActivity::class.java)
            startActivity(intent)
        }
        btn_banner.setOnClickListener {
            val intent = Intent(this, BannerRootActivity::class.java)
            startActivity(intent)
        }
        btn_qrcode.setOnClickListener {
            val intent = Intent(this, CustomScanActivity::class.java)
            startActivity(intent)
        }
        btn_firebase.setOnClickListener {
            val intent = Intent(this, FirebaseRootActivity::class.java)
            startActivity(intent)
        }
        btn_collapsing_toolbar_layout.setOnClickListener {
            val intent = Intent(this, ProfileHomePageActivity::class.java)
            startActivity(intent)
        }
        btn_start_googlemap.setOnClickListener {
            val intent = Intent(this, GoogleMapStartActivity::class.java)
            startActivity(intent)
        }
        btn_pie_chart.setOnClickListener {
            val intent = Intent(this, ChartActivity::class.java)
            startActivity(intent)
        }
        btn_browser.setOnClickListener {
            val intent = Intent(this, BrowserActivity::class.java)
            startActivity(intent)
        }
        btn_custom_view.setOnClickListener {
            val intent = Intent(this, CustomViewActivity::class.java)
            startActivity(intent)
        }
    }
}