package com.change.demox.views.firebase

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.change.demox.R
import com.change.demox.views.webview.webcache.WebViewCacheActivity
import kotlinx.android.synthetic.main.activity_firebase_root.*
import kotlinx.android.synthetic.main.activity_web_view.*


class FirebaseRootActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_firebase_root)
        initView()
    }

    private fun initView() {
        btn_firebase_twitter.setOnClickListener {
            val intent = Intent(this, FirebaseTwitterActivity::class.java)
            startActivity(intent)
        }
    }
}