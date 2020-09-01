package com.change.demox.views

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.change.demox.R
import kotlinx.android.synthetic.main.activity_spinner.*


class SpinnerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spinner)
        initView()
    }

    private fun initView() {
        val list = ArrayList<String>()
        list.add("W7")
        list.add("W8")
        list.add("W9")
        //构造ArrayAdapter
        val adapter = ArrayAdapter(this,
                R.layout.simple_spinner_item, list)
        //设置下拉样式以后显示的样式
        adapter.setDropDownViewResource(R.layout.my_drop_down_item)
        field_item_spinner_content.adapter = adapter
    }
}