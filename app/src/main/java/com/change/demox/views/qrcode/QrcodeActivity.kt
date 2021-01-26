package com.change.demox.views.qrcode

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.change.demox.R
import com.change.demox.views.qrcode.scan.CustomScanActivity
import kotlinx.android.synthetic.main.activity_qrcode.*

class QrcodeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qrcode)
        initView()
    }

    private fun initView() {
        btn_sao_qr.setOnClickListener {
            val intent = Intent(this, CustomScanActivity::class.java)
            startActivity(intent)
        }
    }
}