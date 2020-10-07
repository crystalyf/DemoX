package com.change.demox.views.recyclerview.paging.delete

import android.os.Bundle
import com.change.base.BaseActivity
import com.change.demox.R
import com.change.demox.views.recyclerview.paging.onlyshow.PagingFragment


class PagingDeleteActivity : BaseActivity() {

    private val tag = "pagingDelete"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paging_delete)
        initView()
    }

    private fun initView() {
        var fragmentResult = supportFragmentManager.findFragmentByTag(tag)
        if (fragmentResult == null) {
            fragmentResult = PagingDeleteFragment()
            supportFragmentManager
                    .beginTransaction()
                    .add(
                            R.id.frame_container,
                            fragmentResult,
                            tag
                    )
                    .commit()
        }
    }
}