package com.change.demox.views.firebase.dynamiclink

import android.os.Bundle
import androidx.activity.viewModels
import com.change.base.BaseActivity
import com.change.demox.R
import com.change.demox.extension.getViewModelFactory
import kotlinx.android.synthetic.main.activity_firebase_dynamic_link.*
import kotlinx.android.synthetic.main.activity_firebase_twitter.*


class FirebaseDynamicLinkActivity : BaseActivity() {
    val viewModel by viewModels<FirebaseDynamicViewModel> { getViewModelFactory() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_firebase_dynamic_link)
        initClickListener()
    }

    private fun initClickListener() {
        fl_create_dynamic_link.setOnClickListener {
            //创建动态链接
            viewModel.createDynamicLink()
        }
    }

}