/*
 * FigViewPagerAdapter.kt
 *
 * Created by xingjunchao on 2020/06/17.
 * Copyright © 2020年 Kubota-PAD. All rights reserved.
 */

package com.change.demox.views.recyclerview.figillustration.adapter

import android.content.res.Configuration
import android.os.Parcelable
import android.text.TextUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.change.demox.views.recyclerview.figillustration.FigFragment
import com.change.demox.views.recyclerview.figillustration.bean.ThumbnailModel

/**
 * fig ViewPagerAdapter
 * @param fm
 */
class FigIllustrationViewPagerAdapter(
        private val fm: FragmentManager?,
        private val illustrationList: MutableList<ThumbnailModel>?,
        private val isFromLocal: Boolean,
        private val selectedPartsId: List<Int?>? = null,
        private val orientation: Int
) : FragmentStatePagerAdapter(fm!!, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        val fragment = fm?.fragments?.find {
            (it is FigFragment) && (
                    TextUtils.equals(
                            it.figTag,
                            position.toString() + orientation.toString()
                    ))
        }

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            val oldFragment = fm?.fragments?.find {
                (it is FigFragment) && (
                        TextUtils.equals(
                                it.figTag,
                                position.toString() + Configuration.ORIENTATION_LANDSCAPE.toString()
                        ))
            }
            if (oldFragment != null) {
                fm?.beginTransaction()?.remove(oldFragment)?.commit()
            }
        } else {
            val oldFragment =
                    fm?.fragments?.find {
                        (it is FigFragment) && (
                                TextUtils.equals(
                                        it.figTag,
                                        position.toString() + Configuration.ORIENTATION_PORTRAIT.toString()
                                ))
                    }
            if (oldFragment != null) {
                fm?.beginTransaction()?.remove(oldFragment)?.commit()
            }
        }
        return fragment ?: FigFragment.newInstance(
                illustrationList?.get(position),
                isFromLocal,
                tag = position.toString() + orientation.toString(),
                selectedPartsId = selectedPartsId?.get(position)
        )
    }

    override fun getCount(): Int {
        return illustrationList?.size ?: 0
    }

    override fun saveState(): Parcelable? {
        return null
    }
}