package com.change.demox.views.dialog

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.change.demox.R
import com.change.demox.views.bottomsheet.BottomSheetRootActivity
import com.change.demox.views.dialog.dialogfragment.DialogFragmentActivity
import com.change.demox.views.edittext.EditTextActivity
import com.change.demox.views.imageview.ImageViewActivity
import com.change.demox.views.recyclerview.RecyclerActivity
import com.change.demox.views.slideview.SlideViewActivity
import com.change.demox.views.spinner.SpinnerActivity
import com.change.demox.views.textview.TextViewActivity
import kotlinx.android.synthetic.main.activity_dialog.*
import kotlinx.android.synthetic.main.activity_view.*


class DialogActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialog)
        initView()
    }

    private fun initView() {
        btn_dialog_fragment.setOnClickListener {
            val intent = Intent(this, DialogFragmentActivity::class.java)
            startActivity(intent)
        }

    }
}