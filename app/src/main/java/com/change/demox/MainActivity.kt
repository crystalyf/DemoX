package com.change.demox

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.change.demox.themecolor.waytwo.DynamicThemeColorActivity
import com.change.demox.ucrop.UcropActivity
import com.change.demox.views.ViewActivity
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        initClickListener()
    }

    private fun initClickListener() {
        btn_dynamic_theme.setOnClickListener(this)
        btn_lecenses.setOnClickListener(this)
        btn_ucrop.setOnClickListener(this)
        btn_views.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_dynamic_theme -> {
                val intent = Intent(this, DynamicThemeColorActivity::class.java)
                startActivity(intent)
            }
            R.id.btn_lecenses -> {
                val intent2 = Intent(this, OssLicensesMenuActivity::class.java)
                startActivity(intent2)
            }
            R.id.btn_ucrop -> {
                val intent3 = Intent(this, UcropActivity::class.java)
                startActivity(intent3)
            }
            R.id.btn_views -> {
                val intent3 = Intent(this, ViewActivity::class.java)
                startActivity(intent3)
            }
            else -> {
            }
        }
    }
}