package com.change.demox.themecolor.waytwo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.change.demox.R
import com.change.demox.utils.SharedPreferences
import kotlinx.android.synthetic.main.activity_dynamic_theme_color.*

class DynamicThemeColorActivity : Activity(), View.OnClickListener {

    /**
     * データ保存操作の対象
     */
    private val preferences by lazy { SharedPreferences(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dynamic_theme_color)
        initView()
    }

    private fun initView() {
        btn_theme1?.setOnClickListener(this)
        btn_theme2?.setOnClickListener(this)
        btn_jump?.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_theme1 -> {
                preferences.isOrangeThemeColor = false
                recreate()
            }
            R.id.btn_theme2 -> {
                preferences.isOrangeThemeColor = true
                recreate()
            }
            R.id.btn_jump -> {
                val intent = Intent(this, SecondActivity::class.java)
                startActivity(intent)
            }
            else -> {
            }
        }
    }
}