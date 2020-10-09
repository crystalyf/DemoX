package com.change.demox.views.imageview

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.change.demox.R
import com.change.demox.views.imageview.viewTag.ViewTagActivity
import kotlinx.android.synthetic.main.activity_image_view.*

class ImageViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_view)
        initView()
    }

    private fun initView() {
        btn_view_tag.setOnClickListener {
            //图片指定位置增加标记
            val intent = Intent(this, ViewTagActivity::class.java)
            startActivity(intent)
        }
        btn_gesture.setOnClickListener {
            //手势操作（手势滑动,手势缩放，双击缩放）
            val intent = Intent(this, ViewTagActivity::class.java)
            startActivity(intent)
        }
    }
}