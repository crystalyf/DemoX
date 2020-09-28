package com.change.demox.views.edittext.widget

import android.content.Context
import android.graphics.PorterDuff
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.View
import android.view.View.OnFocusChangeListener
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import com.change.demox.R
import kotlinx.android.synthetic.main.common_edit_text_with_error_message.view.*

/**
 * 输入时带删除按钮的EditText （自定义组件）
 *
 */
class InputEditTextView : RelativeLayout {
    var deleteBtnClickRunnable: (() -> Unit)? = null
    lateinit var view: View

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        initView()
    }

    fun initView() {
        //用inflate的方式去加载 ：
        view = View.inflate(context, R.layout.common_edit_text_with_error_message, this)
        view.input_edit_text?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                view.img_delete?.visibility = if (s == null || s.isEmpty()) View.GONE else View.VISIBLE
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
        view.input_edit_text.onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                number_is_incorrect_message.visibility = View.GONE
            }
        }
        view.img_delete?.setOnClickListener {
            onDeleteBtn()
        }
    }

    private fun onDeleteBtn() {
        view.input_edit_text.setText("")
        deleteBtnClickRunnable?.invoke()
    }

    fun setupView(tag: String, errorText: String, text: String = "") {
        setText(text)
        setErrorText(errorText)
        setTag(tag)
    }

    fun setTag(tag: String) {
        view.textview_tag.text = tag
    }

    fun setText(text: String) {
        view.input_edit_text.setText(text)
    }

    fun setTextHint(hint: String) {
        view.input_edit_text.hint = hint
    }

    fun setErrorText(text: String) {
        view.error_text.text = text
    }

    fun setHasError(hasError: Boolean) {
        if (hasError) {
            view.input_edit_text.background.mutate()
                    .setColorFilter(ContextCompat.getColor(this.context, R.color.colorRed), PorterDuff.Mode.SRC_ATOP)
            number_is_incorrect_message.visibility = View.VISIBLE
        } else {
            view.input_edit_text.background.mutate()
                    .setColorFilter(
                            ContextCompat.getColor(this.context, R.color.colorBlue), PorterDuff.Mode.SRC_ATOP)
            view.number_is_incorrect_message.visibility = View.INVISIBLE
        }
    }

    fun getInput(): String {
        return view.input_edit_text.text.toString()
    }

    fun setPasswordStyle() {
        view.input_edit_text.transformationMethod = PasswordTransformationMethod.getInstance()
        view.input_edit_text.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
    }

    fun setNumberPasswordStyle() {
        view.input_edit_text.transformationMethod = PasswordTransformationMethod.getInstance()
        view.input_edit_text.inputType = InputType.TYPE_CLASS_NUMBER
    }

    fun setNumberStyle() {
        view.input_edit_text.inputType = InputType.TYPE_CLASS_PHONE
    }

    fun setInputFilter() {
        view.input_edit_text.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(11))
    }

    fun addTextWatcher(watcher: TextWatcher) {
        view.input_edit_text.addTextChangedListener(watcher)
    }

    fun setTextFocusListener(listener: OnFocusChangeListener) {
        view.input_edit_text.onFocusChangeListener = listener
    }

    fun setSelection(position: Int) {
        view.input_edit_text.setSelection(position)
    }

    fun setMaxLength(maxLength: Int) {
        view.input_edit_text.filters = arrayOf(InputFilter.LengthFilter(maxLength))
    }

    fun setEditTextNull() {
        view.input_edit_text.text = null
    }
}