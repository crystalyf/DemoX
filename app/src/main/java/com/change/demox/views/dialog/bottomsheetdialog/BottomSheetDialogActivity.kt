package com.change.demox.views.dialog.bottomsheetdialog

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.change.demox.R
import com.change.demox.databinding.ActivitySelectBinding
import com.change.demox.utils.EventObserver
import com.change.demox.views.dialog.bottomsheetdialog.widget.ActionPickerView

/**
 * 自定义点击底部Dialog选择
 */

class BottomSheetDialogActivity : AppCompatActivity() {

    private lateinit var viewModel: BottomSheetDialogViewModel
    private var dataBinding: ActivitySelectBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_select)
        viewModel = ViewModelProviders.of(this).get(BottomSheetDialogViewModel::class.java)
        initView()
    }

    private fun initView() {
        dataBinding?.viewModel = viewModel
        dataBinding?.lifecycleOwner = this

        viewModel.makeDialogContent()
        //selectText设置第一个默认选中
        viewModel.categoryNameText.postValue(viewModel.dialogItemList[0])
        viewModel.selectTextClick.observe(this, EventObserver {
            bottomSheetDialog(viewModel.dialogItemList)
        })
    }

    private fun bottomSheetDialog(listData: List<String>) {
        val dialogView = ActionPickerView(this@BottomSheetDialogActivity, listData, { categoryName, position, _ ->
            viewModel.categoryNamePosition = position
            viewModel.categoryNameText.postValue(categoryName)
            //ボタンの状態
            dataBinding?.executePendingBindings()
        }, { }, viewModel.categoryNamePosition)
        dialogView.show()
    }
}