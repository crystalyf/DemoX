/*
 * dd.kt
 *
 * Created by shitianming on 2020/06/10.
 * Copyright © 2020年 Kubota-PAD. All rights reserved.
 */

package com.change.demox.views.recyclerview.paging.delete.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * BookModel是一个responseBean
 */
@Parcelize
data class BookModel(
        val books: List<Book>,
        val total: Int
) : Parcelable

@Parcelize
data class Book(
        val book_id: Int,
        val categories: List<Category>,
        val category_division_code: Int,
        val category_id: Int,
        val distributor_code: String,
        val is_favorite: Boolean,
        val model_name: String,
        val options: List<BOption>?,
        val remarks: String,
        var isChecked: Boolean? = false
) : Parcelable {
    fun isSameWith(other: Book): Boolean {
        return book_id == other.book_id
                && categories == other.categories
                && category_division_code == other.category_division_code
                && category_id == other.category_id
                && is_favorite == other.is_favorite
                && model_name == other.model_name
                && options == other.options
                && remarks == other.remarks
                && isChecked == other.isChecked
    }
}

@Parcelize
data class Category(
        val category_division_code: Int,
        val category_id: Int,
        val category_name: String,
        val parent_category_id: Int,
        val sort_orders: List<Int>
) : Parcelable

@Parcelize
data class BOption(
        val ParentBookID: Int,
        val amount_of_memos: Int,
        val amount_of_service_bulletins: Int,
        val book_id: Int,
        val is_favorite: Boolean,
        val model_name: String,
        val remarks: String
) : Parcelable