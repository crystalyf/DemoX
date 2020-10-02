package com.change.demox.views.bottomsheet

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.change.demox.R
import com.change.demox.views.bottomsheet.BottomSheetActivity
import com.change.demox.views.bottomsheet.infragment.BottomSheetFragmentActivity
import com.change.demox.views.edittext.EditTextActivity
import com.change.demox.views.recyclerview.RecyclerActivity
import com.change.demox.views.spinner.SpinnerActivity
import com.change.demox.views.textview.TextViewActivity
import kotlinx.android.synthetic.main.activity_bottom_sheet_root.*
import kotlinx.android.synthetic.main.activity_view.*


class BottomSheetRootActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_sheet_root)
        initView()
    }

    private fun initView() {
        btn_bottomsheet1.setOnClickListener {
            val intent = Intent(this, BottomSheetActivity::class.java)
            startActivity(intent)
        }
        btn_bottomsheet2.setOnClickListener {
            val intent = Intent(this, BottomSheetFragmentActivity::class.java)
            startActivity(intent)
        }
    }
}