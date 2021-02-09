package com.change.demox.views.firebase

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.change.demox.R
import com.change.demox.views.firebase.auth.FirebaseAuthActivity
import com.change.demox.views.firebase.dynamiclink.FirebaseDynamicLinkActivity
import kotlinx.android.synthetic.main.activity_firebase_root.*


class FirebaseRootActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_firebase_root)
        initView()
    }

    private fun initView() {
        btn_firebase_twitter.setOnClickListener {
            val intent = Intent(this, FirebaseAuthActivity::class.java)
            startActivity(intent)
        }
        btn_firebase_dynamic_link.setOnClickListener {
            val intent = Intent(this, FirebaseDynamicLinkActivity::class.java)
            startActivity(intent)
        }
    }
}