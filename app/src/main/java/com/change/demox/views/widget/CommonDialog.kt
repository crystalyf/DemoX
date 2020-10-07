/*
 * CommonDialog.kt
 *
 * Created by kangzewei on 2019/12/05.
 * Copyright © 2019年 Eole. All rights reserved.
 */

package com.change.demox.views.widget

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.change.demox.R


/**
 * 共通のダイアログ
 *
 * @param T
 */
@Suppress("UNCHECKED_CAST")
open class CommonDialog<out T : CommonDialog<T>> : DialogFragment() {
    private val mSavedDialogStateTag = "android:savedDialogState"
    private var mTitle: String? = null
    private var mMessage: String? = null
    private var mPositiveButton: String? = null
    private var mNegativeButton: String? = null
    private var mCancelOutside = false
    private var mCancelable: Boolean? = null

    @LayoutRes
    private var mLayoutID: Int = 0

    //カスタムビュー
    var customView: View? = null
        private set

    private var mOnOkClickListener: DialogInterface.OnClickListener? =
            DialogInterface.OnClickListener { _, _ -> dismissAllowingStateLoss() }
    private var mOnCancelListener: DialogInterface.OnCancelListener? = null
    private var mOnDismissListener: DialogInterface.OnDismissListener? =
            DialogInterface.OnDismissListener {
                fragmentManager?.let {
                    if (!isRemoving && !isDetached) dismissAllowingStateLoss()
                }
            }
    private var mCustomViewListener: SetupCustomViewListener? = null
    private var mOnDialogCreatedListener: OnDialogCreatedListener? = null


    internal open val builder: AlertDialog.Builder
        get() = AlertDialog.Builder(requireContext(), R.style.commonDialogStyle)

    interface SetupCustomViewListener {
        fun setupCustomView(customView: View?)
    }

    interface OnDialogCreatedListener {
        fun onDialogCreated(dialog: Dialog)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (!TextUtils.isEmpty(mPositiveButton)) {
            outState.putString(POSITIVE_BUTTON, mPositiveButton)
        }

        if (!TextUtils.isEmpty(mNegativeButton)) {
            outState.putString(NEGATIVE_BUTTON, mNegativeButton)
        }

        if (!TextUtils.isEmpty(mTitle)) {
            outState.putString(TITLE, mTitle)
        }

        if (!TextUtils.isEmpty(mMessage)) {
            outState.putString(MESSAGE, mMessage)
        }

        if (mLayoutID != 0) {
            outState.putInt(LAYOUT_ID, mLayoutID)
        }

    }

    /**
     * SDKのデフォルト処理にListenerを設定処理があるので、メモリリーク頻繁に発生する
     * overrideしてListenerを設定の処理を取り消して修正する
     *
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        if (showsDialog) {
            showsDialog = false
        }
        super.onActivityCreated(savedInstanceState)
        showsDialog = true

        val view = view
        if (view != null) {
            if (view.parent != null) {
                throw IllegalStateException(
                        "DialogFragment can not be attached to a container view"
                )
            }
            dialog?.setContentView(view)
        }
        if (activity != null) {
            dialog?.setOwnerActivity(requireActivity())
        }
        if (savedInstanceState != null) {
            val dialogState = savedInstanceState.getBundle(mSavedDialogStateTag)
            if (dialogState != null) {
                dialog?.onRestoreInstanceState(dialogState)
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = builder.setOnDismissListener(mOnDismissListener)

        if (mOnCancelListener != null) {
            builder.setOnCancelListener(mOnCancelListener)
        }

        if (savedInstanceState != null) {
            mPositiveButton = savedInstanceState.getString(POSITIVE_BUTTON)
            mNegativeButton = savedInstanceState.getString(NEGATIVE_BUTTON)
            mTitle = savedInstanceState.getString(TITLE)
            mMessage = savedInstanceState.getString(MESSAGE)
            mLayoutID = savedInstanceState.getInt(LAYOUT_ID)
        }
        if (!TextUtils.isEmpty(mPositiveButton)) {
            builder.setPositiveButton(mPositiveButton, mOnOkClickListener)
        }

        if (!TextUtils.isEmpty(mNegativeButton)) {
            builder.setNegativeButton(mNegativeButton) { _, _ -> dismiss() }
        }

        if (!TextUtils.isEmpty(mTitle)) {
            builder.setTitle(mTitle)
        }

        if (!TextUtils.isEmpty(mMessage)) {
            builder.setMessage(mMessage)
        }

        if (mLayoutID != 0) {
            customView = LayoutInflater.from(context).inflate(mLayoutID, null)
            builder.setView(customView)
            if (mCustomViewListener != null) {
                mCustomViewListener!!.setupCustomView(customView)
            }
        } else if (customView != null) {
            builder.setView(customView)
            if (mCustomViewListener != null) {
                mCustomViewListener!!.setupCustomView(customView)
            }
        }

        val dialog = builder.create()
        dialog.setCanceledOnTouchOutside(mCancelOutside)
        if (mCancelable != null) {
            dialog.setCancelable(mCancelable!!)
        } else if (!TextUtils.isEmpty(mNegativeButton)) {
            dialog.setCancelable(true)
        } else {
            dialog.setCancelable(false)
        }

        if (mOnDialogCreatedListener != null) {
            mOnDialogCreatedListener!!.onDialogCreated(dialog)
        }

        return dialog
    }

    override fun onDestroyView() {
        if (view is ViewGroup) {
            (view as ViewGroup).removeAllViews()
        }
        super.onDestroyView()
        clearListeners()
    }

    override fun onDetach() {
        super.onDetach()
        clearListeners()
    }

    fun clearListeners() {
        dialog?.let {
            it as AlertDialog
            it.setOnShowListener(null)
            it.setOnCancelListener(null)
            it.setOnDismissListener(null)
            it.setOnShowListener(null)
            it.setOnKeyListener(null)
            it.setButton(DialogInterface.BUTTON_POSITIVE, "", null, null)
            it.setButton(DialogInterface.BUTTON_NEGATIVE, "", null, null)
        }

        mOnOkClickListener = null
        mOnDismissListener = null
        mCustomViewListener = null
        mOnDialogCreatedListener = null
    }

    fun title(title: String): T {
        mTitle = title
        return this as T
    }

    fun message(message: String): T {
        mMessage = message
        return this as T
    }

    fun positiveTitle(positiveButton: String): T {
        mPositiveButton = positiveButton
        return this as T
    }

    fun negativeTitle(negativeButton: String): T {
        mNegativeButton = negativeButton
        return this as T
    }

    fun customLayoutID(@LayoutRes layoutId: Int): CommonDialog<*> {
        mLayoutID = layoutId
        return this as T
    }

    fun customView(view: View): T {
        customView = view
        return this as T
    }

    fun isCancelOutside(cancelOutside: Boolean): T {
        mCancelOutside = cancelOutside
        return this as T
    }

    fun isCancelable(cancelable: Boolean): T {
        mCancelable = cancelable
        return this as T
    }

    fun onPositiveListener(onOkClickListener: DialogInterface.OnClickListener): T {
        mOnOkClickListener = onOkClickListener
        return this as T
    }

    fun onCancelListener(onCancelListener: DialogInterface.OnCancelListener): T {
        mOnCancelListener = onCancelListener
        return this as T
    }

    fun onCustomViewListener(listener: SetupCustomViewListener): T {
        mCustomViewListener = listener
        return this as T
    }

    fun onDialogCreatedListener(listener: OnDialogCreatedListener): T {
        mOnDialogCreatedListener = listener
        return this as T
    }

    fun show(fragmentManager: FragmentManager) {
        dialog?.window
                ?.decorView
                ?.viewTreeObserver
                ?.addOnWindowAttachListener(object : ViewTreeObserver.OnWindowAttachListener {
                    override fun onWindowAttached() {}
                    override fun onWindowDetached() {
                        clearListeners()
                    }
                })
        show(fragmentManager, this.toString())
    }

    companion object {

        const val POSITIVE_BUTTON = "mPositiveButton"
        const val NEGATIVE_BUTTON = "mNegativeButton"
        const val TITLE = "title"
        const val MESSAGE = "message"
        const val LAYOUT_ID = "layoutID"
    }
}
