package com.change.demox.views.textview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.change.demox.R
import kotlinx.android.synthetic.main.activity_text_view.*

class TextViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_view)
        initView()
    }

    private fun initView(){

//      lin_root.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
//          contact_name.isSelected = hasFocus
//      }
//
//        contact_name.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
//            contact_name.isSelected = hasFocus
//        }
    }
}