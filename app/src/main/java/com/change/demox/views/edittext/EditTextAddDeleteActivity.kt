package com.change.demox.views.edittext

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.change.demox.R
import com.change.demox.extension.getPhoneNumber
import com.change.demox.extension.insertSpaceInPhoneNumber
import kotlinx.android.synthetic.main.activity_edit_text_add_delete.*


class EditTextAddDeleteActivity : AppCompatActivity() {

    private var mNumberSelectionPosition = 0
    private var mLastInputNumber = ""
    private var mIsNumberInputStartReal = true
    val NUMBER_INDEX = "02"
    val NUMBER_CONTENT_MAX_SIZE = 11

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_text_add_delete)
        initView()
    }

    private fun initView() {
        setupViews()
    }

    private fun setupViews() {
        initEditText()
        setupNameInputView()
        setupPhoneNumberView()
        ok_btn.isEnabled = isAllInputCorrectly()
    }

    private fun initEditText() {
        user_name.setupView(
                getString(R.string.contact_add_name_tag),
                "",
                ""
        )
        phone_number.setupView(
                getString(R.string.contact_add_phone_tag),
                "",
                ""
        )
    }

    /**
     * OK button 状态 （判断输入是否皆无错误）
     *
     * @return
     */
    private fun isAllInputCorrectly(): Boolean {
        return user_name.getInput().isNotEmpty() && checkPhoneNumber()
    }

    private fun checkPhoneNumber(): Boolean {
        var result = false
        if (phone_number.getInput().length == NUMBER_CONTENT_MAX_SIZE) {
            if (TextUtils.equals(phone_number.getInput().substring(0, 2), NUMBER_INDEX)) {
                result = true
            }
        }
        return result
    }

    private fun setupNameInputView() {
        user_name.addTextWatcher(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                ok_btn.isEnabled = isAllInputCorrectly()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    private fun setupPhoneNumberView() {
        phone_number.setNumberStyle()
        phone_number.setInputFilter()
        phone_number.addTextWatcher(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                ok_btn.isEnabled = isAllInputCorrectly()

                val etContent = phone_number.getInput()
                if (TextUtils.isEmpty(etContent)) {
                    return
                }
                val newContent = etContent.getPhoneNumber().insertSpaceInPhoneNumber()
                mLastInputNumber = newContent
                if (etContent != newContent) {
                    mIsNumberInputStartReal = false
                    phone_number.setText(newContent)
                    phone_number.setSelection(if (mNumberSelectionPosition > newContent.length) newContent.length else mNumberSelectionPosition)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!mIsNumberInputStartReal) {
                    //from setText(),but not keyboard input. do nothing
                    mIsNumberInputStartReal = true
                    return
                }

                var editTextContent = phone_number.getInput()
                if (TextUtils.isEmpty(editTextContent) || TextUtils.isEmpty(mLastInputNumber)) {
                    return
                }
                editTextContent = editTextContent.getPhoneNumber().insertSpaceInPhoneNumber()
                if (editTextContent.length <= mLastInputNumber.length) {
                    //delete char
                    mNumberSelectionPosition = start
                } else {
                    //add char
                    if (editTextContent[start].toString() in setOf("-")) {
                        //selection position is before '-' or ','
                        mNumberSelectionPosition = start + 2
                    } else {
                        mNumberSelectionPosition = start + 1
                    }
                }
            }
        })
    }
}