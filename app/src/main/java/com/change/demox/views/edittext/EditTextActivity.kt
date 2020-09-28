package com.change.demox.views.edittext

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.change.demox.R
import kotlinx.android.synthetic.main.activity_edit_text.*

/**
 * EditText控件：
 *
 * 1。默认选中状态时，横线，光标是colorPrimary,未选中时横线是灰色
 * 2。点击屏幕其他位置不会自动取消focus状态，如果需要解除选中状态，需要在onFocusChangeListener去写逻辑
 *
 */
class EditTextActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_text)
        initView()
    }

    private fun initView() {
        btn_edit_1.setOnClickListener {
            val intent = Intent(this, EditTextAddDeleteActivity::class.java)
            startActivity(intent)
        }
        btn_edit_2.setOnClickListener {
            val intent = Intent(this, EditTextPasswordActivity::class.java)
            startActivity(intent)
        }
    }
}