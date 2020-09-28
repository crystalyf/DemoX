package com.change.demox.views.edittext

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.change.demox.R
import com.change.demox.views.recyclerview.RecyclerActivity
import com.change.demox.views.spinner.SpinnerActivity
import com.change.demox.views.textview.TextViewActivity
import kotlinx.android.synthetic.main.activity_edit_text.*
import kotlinx.android.synthetic.main.activity_view.*


class EditTextActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_text)
        initView()
    }

    private fun initView() {
        btn_edit_1.setOnClickListener {
            val intent = Intent(this, EditTextAddDeleteActivity::class.java)
            startActivity(intent)
        }
    }
}