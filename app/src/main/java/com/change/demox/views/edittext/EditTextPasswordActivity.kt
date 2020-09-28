package com.change.demox.views.edittext

import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import androidx.appcompat.app.AppCompatActivity
import com.change.demox.R
import com.change.demox.utils.ViewUtils
import kotlinx.android.synthetic.main.activity_edit_text_password.*


class EditTextPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_text_password)
        initView()
    }

    private fun initView() {
        /**
         *
         * setTransformationMethod
         * 这个方法是用来设置其中text的转换显示
         *
         * 标准：
         * HideReturnsTransformationMethod隐藏回车

        SingleLineTransformationMethod不能用换行回车

        PasswordTransformationMethod密码类型
         *
         */
        edit_big.transformationMethod = ViewUtils.BiggerDotPasswordTransformationMethod
        edit_normal.transformationMethod = PasswordTransformationMethod.getInstance()
    }
}