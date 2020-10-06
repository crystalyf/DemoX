package com.change.demox.views.recyclerview.paging.onlyshow

import android.os.Bundle
import com.change.base.BaseActivity
import com.change.demox.R
import com.change.demox.views.recyclerview.paging.onlyshow.PagingFragment


class PagingFragmentActivity : BaseActivity() {

    private val tag = "pagingFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paging_fragment)
        initView()
    }

    private fun initView() {
        var fragmentResult = supportFragmentManager.findFragmentByTag(tag)
        if (fragmentResult == null) {
            fragmentResult = PagingFragment()
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