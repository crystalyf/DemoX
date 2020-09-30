/*
 * BaseActivity.kt
 *
 * Created by kangzewei on 2020/05/29.
 * Copyright © 2020年 Kubota-PAD. All rights reserved.
 */

package com.change.base

import android.app.Dialog
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import com.change.demox.R
import com.change.demox.application.MyApplication
import com.change.demox.utils.EventObserver
import com.change.demox.utils.Utils

/**
 * Activityのベースクラス、共通処理はベースクラスにおく
 * LifeCycleのtraceログを出力する
 */
abstract class BaseActivity() :
        AppCompatActivity() {

    /**
     * データ保存操作の対象
     */
    var preferences = MyApplication.instance?.preferences

    /** 通信中インジケーター */
    private var mProgressDialog: Dialog? = null


    /**
     * を押してソフトキーボードを非表示にします
     *
     * @param event touch event
     * @return このtouch eventを消費するかどうか
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            Utils.hideSoftKeyboard(this)
        }
        return super.onTouchEvent(event)
    }

    fun observeApiErrorEvent(viewModel: BaseViewModel) {

        viewModel.showProgress.observe(this, EventObserver {
            if (it) {
                showProgress()
            } else {
                hideProgress()
            }
        })
    }


    /**
     * 通信中のインジケーターを表示する
     *
     */
    private fun showProgress() {
        if (this.isFinishing) {
            return
        }
        if (mProgressDialog == null) {
            mProgressDialog = Dialog(this, R.style.commonContentTransparent)
            mProgressDialog!!.setContentView(R.layout.view_common_dialog_progress)
            mProgressDialog!!.setCancelable(false)
            mProgressDialog!!.show()
        }
    }

    /**
     * 通信中のインジケーターを消す
     *
     */
    private fun hideProgress() {
        if (mProgressDialog != null) {
            mProgressDialog!!.dismiss()
            mProgressDialog = null
        }
    }
}