package com.change.demox.views.edittext.widget

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import androidx.appcompat.widget.AppCompatEditText

/**
 * 输入时删除字符串两侧的空格TextWatcher
 */
class TrimTextWatcher(ed: AppCompatEditText) : TextWatcher {
    private val editText = ed
    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
    }

    override fun afterTextChanged(s: Editable) {
        val sourceStr = s.toString()
        val trimStr = s.toString().trim()
        if (!TextUtils.equals(sourceStr, trimStr)) {
            editText.removeTextChangedListener(this)
            editText.setText(trimStr)
            //将光标移到文本的末尾
            editText.setSelection(trimStr.length)
            editText.addTextChangedListener(this)
        }
    }
}