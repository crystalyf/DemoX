/*
 * TopRepository.kt
 *
 * Created by shitianming on 2020/06/03.
 * Copyright © 2020年 Kubota-PAD. All rights reserved.
 */

package com.change.demox.views.recyclerview.paging.delete.usecase.repository

import android.content.Context
import com.change.demox.remote.RetrofitManager
import com.change.demox.views.recyclerview.paging.delete.bean.BookModel

class SearchRepository(private val context: Context) : ISearchRepository {

    companion object {
        private var instance: ISearchRepository? = null
        fun instance(
                context: Context
        ): ISearchRepository {
            if (instance == null) {
                instance =
                        SearchRepository(
                                context
                        )
            }
            return instance!!
        }
    }

    /**
     * ブック一覧
     *
     * @param offset 偏移量
     * @return
     */
    override suspend fun getBooks(offset: Int): BookModel? {
        return RetrofitManager.getBooks(offset)
    }
}