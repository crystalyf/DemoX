package com.change.demox.views.dialog.dialogfragment.widget

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.fragment.app.DialogFragment
import com.change.demox.R
import com.change.demox.utils.ViewUtils

/**
 *  Android比较推荐采用DialogFragment实现对话框，它完全能够实现Dialog的所有需求，并且还能复用Fragment的生命周期管理，被后台杀死后，可以恢复重建。
 *
 */
class BottomDialogFragment : DialogFragment() {

    //按下cancel按钮之后的动作
    private var cancelAction: (() -> Unit)? = null
    private var deleteAction: ((dialog: DialogFragment) -> Unit)? = null
    private var deleteButton: TextView? = null

    //按下硬件返回键之后的动作
    private var onBackKey: (() -> Unit)? = null

    fun setCancelAction(cancelAction: () -> Unit): BottomDialogFragment {
        this.cancelAction = cancelAction
        return this
    }

    fun setOnBackKey(onBackKey: () -> Unit): BottomDialogFragment {
        this.onBackKey = onBackKey
        return this
    }

    fun setDeleteAction(deleteAction: (dialog: DialogFragment) -> Unit): BottomDialogFragment {
        this.deleteAction = deleteAction
        return this
    }

    companion object {
        const val SAVED_DIALOG_STATE_TAG = "android:savedDialogState"
    }

    override fun onActivityCreated(@Nullable savedInstanceState: Bundle?) {
        if (showsDialog) {
            showsDialog = false
        }
        super.onActivityCreated(savedInstanceState)
        showsDialog = true
        val view = view
        if (view != null) {
            check(view.parent == null) { "DialogFragment can not be attached to a container view" }
            dialog!!.setContentView(view)
        }
        val activity: Activity? = activity
        if (activity != null) {
            dialog!!.setOwnerActivity(activity)
        }
        if (savedInstanceState != null) {
            val dialogState = savedInstanceState.getBundle(SAVED_DIALOG_STATE_TAG)
            if (dialogState != null) {
                dialog!!.onRestoreInstanceState(dialogState)
            }
        }
    }

    fun setDeleteButtonEnable(isEnable: Boolean) {
        deleteButton?.isEnabled = isEnable
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val view =
                LayoutInflater.from(requireContext()).inflate(R.layout.layout_bottom_delete_item, null)

        val cancelButton: TextView = view.findViewById(R.id.button_cancel)
        deleteButton = view.findViewById(R.id.button_delete)
        deleteButton?.isEnabled = true
        deleteButton?.setOnClickListener {
            deleteAction?.invoke(this)
        }

        cancelButton.setOnClickListener {
            cancelAction?.invoke()
            dismiss()
        }
        dialog.setContentView(view)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                onBackKey?.invoke()
                return@setOnKeyListener true
            }

            false
        }
        val window = dialog.window
        if (window != null) {
            val params = window.attributes
            //设置底部显示
            params.gravity = Gravity.BOTTOM
            window.setBackgroundDrawableResource(R.color.white)
            params.width = WindowManager.LayoutParams.MATCH_PARENT
            params.height = ViewUtils.dip2px(requireContext(), 64f).toInt()
            // flag can touch beside dialog window
            window.addFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
            )
            window.setDimAmount(0f)
            window.attributes = params

        }
        return dialog
    }

    override fun onDestroyView() {
        deleteAction = null
        cancelAction = null
        onBackKey = null
        super.onDestroyView()
    }
}