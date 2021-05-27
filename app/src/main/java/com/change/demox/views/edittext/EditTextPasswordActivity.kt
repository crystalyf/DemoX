package com.change.demox.views.edittext

import android.os.Bundle
import android.text.TextUtils
import android.text.method.DigitsKeyListener
import android.text.method.PasswordTransformationMethod
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.change.demox.R
import com.change.demox.databinding.ActivityEditTextPasswordBinding
import com.change.demox.databinding.ActivityRecyclerCeilingBinding
import com.change.demox.utils.EventObserver
import com.change.demox.utils.ViewUtils
import com.change.demox.views.edittext.widget.TrimTextWatcher
import com.change.demox.views.recyclerview.ceiling.RecyclerCeilingViewModel
import kotlinx.android.synthetic.main.activity_edit_text_password.*

/**
 * EditText的转换显示： 输入密码，输入时去除空格
 */
class EditTextPasswordActivity : AppCompatActivity() {

    private lateinit var viewModel: EditTextViewModel
    private var dataBinding: ActivityEditTextPasswordBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_edit_text_password)
        viewModel = ViewModelProviders.of(this).get(EditTextViewModel::class.java)
        initView()
    }

    private fun initView() {
        dataBinding?.viewModel = viewModel
        dataBinding?.lifecycleOwner = this
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
        //输入时去除前后空格的EditText
        edit_trim.addTextChangedListener(TrimTextWatcher(edit_trim))

        viewModel.clickEmailButton.observe(this, EventObserver {
          Toast.makeText(this,"按钮被点击",Toast.LENGTH_SHORT).show()
        })
    }
}