package com.change.demox.views.firebase

import android.os.Bundle
import androidx.activity.viewModels
import com.change.base.BaseActivity
import com.change.demox.R
import com.change.demox.extension.getViewModelFactory
import kotlinx.android.synthetic.main.activity_firebase_twitter.*


class FirebaseTwitterActivity : BaseActivity() {
    val viewModel by viewModels<FirebaseTwitterViewModel> { getViewModelFactory() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_firebase_twitter)
        initClickListener()
    }

    private fun initClickListener() {
        sign_in_with_google.setOnClickListener {
            //todo sign in action
            viewModel.refreshTwitterAuthentication()
        }

        sign_in_with_twiiter.setOnClickListener {
            //sign in action
            viewModel.doTwitterAuthentication(this@FirebaseTwitterActivity)
        }
    }

}