/*
 * ITopRepository.kt
 *
 * Created by shitianming on 2020/06/03.
 * Copyright © 2020年 Kubota-PAD. All rights reserved.
 */

package com.change.demox.views.recyclerview.paging.delete.usecase.repository

import com.change.demox.views.recyclerview.paging.delete.bean.BookModel

interface ISearchRepository {

    suspend fun getBooks(offset: Int): BookModel?
}