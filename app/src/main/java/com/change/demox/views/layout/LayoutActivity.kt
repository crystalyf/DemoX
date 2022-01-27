package com.change.demox.views.layout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CompoundButton
import android.widget.Toast
import com.change.demox.R
import kotlinx.android.synthetic.main.activity_layout.*

class LayoutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layout)
        initView()
    }

    private fun initView(){
        sw_state.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                //true
                Toast.makeText(this,isChecked.toString(),Toast.LENGTH_SHORT).show()
            } else {
                //false
                Toast.makeText(this,isChecked.toString(),Toast.LENGTH_SHORT).show()
            }
        }
    }
}