package com.change.demox.views.dialog.bottomsheetdialog.widget

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ListView
import com.change.demox.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.bottom_dialog_view.view.*

/**
 * item click callback
 */
private typealias OnSheetItemClickCallBack = (String, Int, ActionPickerView) -> Unit

/**
 * footer click callback
 */
private typealias OnSheetCloseClickCallBack = (ActionPickerView) -> Unit

/**
 * 自定义底部弹出选择Dialog (BottomSheetDialog)
 */
class ActionPickerView(context: Context, souceData: List<String?>, sheetItemListener: OnSheetItemClickCallBack, sheetCloseListener: OnSheetCloseClickCallBack, currentPosition: Int) : BottomSheetDialog(context, R.style.BottomSheetDialogTheme) {
    var mSouceData = souceData
    var mContext = context
    var mSheetItemListener = sheetItemListener
    var mSheetCloseListener = sheetCloseListener
    var mPosition: Int = currentPosition

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = LayoutInflater.from(mContext).inflate(R.layout.bottom_dialog_view, null)

        val listView = view.findViewById<ListView>(R.id.list_view)

        val startAdapter = BottomSheetAdapter(mContext, mSouceData, R.layout.util_bottomsheet_view)
        listView.adapter = startAdapter

        mPosition.let { startAdapter.setChecked(it) }
        startAdapter.notifyDataSetInvalidated()
        setContentView(view)

        listView.setOnItemClickListener { _, _, position, _ ->
            mPosition = position
            startAdapter.setChecked(mPosition)
            startAdapter.notifyDataSetInvalidated()
            mSheetItemListener(mSouceData[mPosition]!!, mPosition, this)
            dismiss()
        }

        /**
         * 列表视图还原位置
         */
        listView.postDelayed({
            listView.setSelection(mPosition)
        }, 100)

        view.footer_view.setOnClickListener {
            mSheetCloseListener(this)
            dismiss()
        }

    }

    override fun setContentView(view: View) {
        super.setContentView(view)
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

        /**
         * 防止BottomSheetBehavior 关闭对话框
         */
        val behavior = BottomSheetBehavior.from(this.delegate.findViewById<FrameLayout>(R.id.design_bottom_sheet) as View)
        behavior.isHideable = false
    }
}

