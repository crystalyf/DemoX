package com.change.demox.views.slideview

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.change.demox.R
import kotlinx.android.synthetic.main.activity_slide_view.*


class SlideViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_slide_view)
        initView()
    }

    private fun initView() {
        slide_accept.onSlideDone = {
            Toast.makeText(this@SlideViewActivity, "已接听", Toast.LENGTH_SHORT).show()
        }
    }
}