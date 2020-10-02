package com.change.demox.views.bottomsheet

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.change.base.BaseActivity
import com.change.demox.R
import com.change.demox.extension.getViewModelFactory
import com.change.demox.utils.EventObserver
import com.change.demox.views.recyclerview.ceilingTwo.RecyclerCeilingTwoViewModel
import com.github.barteksc.pdfviewer.util.FitPolicy
import kotlinx.android.synthetic.main.activity_bottom_sheet.*
import kotlinx.android.synthetic.main.activity_pdf_show.*
import timber.log.Timber
import java.io.File


class BottomSheetActivity : BaseActivity() {

    private lateinit var viewModel: BottomSheetViewModel

    private var list: List<String>? = null

    private fun initData() {
        list = ArrayList()
        for (i in 1..10) {
            (list as ArrayList<String>).add("我是条目$i")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_sheet)
        initData()
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rv_machine_part.layoutManager = layoutManager
        rv_machine_part.itemAnimator = DefaultItemAnimator()
        rv_machine_part.addItemDecoration(
                DividerItemDecoration(
                        this,
                        DividerItemDecoration.VERTICAL
                )
        )
        val adapter = KotlinRecycleAdapter(this, list)
        rv_machine_part.adapter = adapter
    }

}