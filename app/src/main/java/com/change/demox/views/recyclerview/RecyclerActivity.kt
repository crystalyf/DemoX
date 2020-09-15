package com.change.demox.views.recyclerview

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.change.demox.R
import kotlinx.android.synthetic.main.activity_recycler.*

class RecyclerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler)
        initView()
    }

   private fun initView(){
        btn_category.setOnClickListener {
            val intent = Intent(this, RecyclerviewCategoryActivity::class.java)
            startActivity(intent)
        }
    }
}