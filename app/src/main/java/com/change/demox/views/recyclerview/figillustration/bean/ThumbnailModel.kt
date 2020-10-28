/*
 * ThumbnailItem.kt
 *
 * Created by xingjunchao on 2020/06/15.
 * Copyright © 2020年 Kubota-PAD. All rights reserved.
 */

package com.change.demox.views.recyclerview.figillustration.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * ブック_サムネイル
 *
 * @property featureName 機能名称
 * @property booklet 冊子総称名称
 * @property figComment Fig 注釈
 * @property figId Fig ID
 * @property figLinkUrl 部品表URLリンク
 * @property figName Fig 名
 * @property figNumber　Fig
 * @property illustrations　Fig イラスト情報
 * @property isFavorite お気に入りフラグ、true: お気に入り登録済 false: お気に入り未登録
 * @property updatedAt
 */
@Parcelize
data class ThumbnailModel(
    var featureName: String = "",
    var booklet: String? = "",
    var figComment: String? = "",
    var figId: Int? = null,
    var figLinkUrl: FigLinkUrl? = null,
    var figName: String? = "",
    var figNumber: String? = "",
    var illustrations: List<Illustration>? = null,
    var isFavorite: Boolean? = false,
    var updatedAt: String? = ""
) : Parcelable {
    constructor(fig: Fig) : this() {
        booklet = fig.booklet
        figComment = fig.fig_comment
        figId = fig.fig_id
        figName = fig.fig_name
        figLinkUrl = fig.fig_link_url
        figNumber = fig.fig_number
        illustrations = fig.illustrations
        isFavorite = fig.is_favorite
        updatedAt = fig.updated_at
    }
}

@Parcelize
data class FigLinkUrl(
        val url_type: Int,
        val urls: List<Url>?
) : Parcelable


@Parcelize
data class Url(
        val url: String,
        val url_comment: String
) : Parcelable

@Parcelize
data class Illustration(
        val high_quality_image_download_link: String,
        val illustration_id: Int,
        val low_quality_image_download_link: String
) : Parcelable

@Parcelize
data class Fig(
        val booklet: String,
        val fig_comment: String,
        val fig_id: Int,
        val fig_link_url: FigLinkUrl,
        val fig_name: String,
        val fig_number: String,
        val illustrations: List<Illustration>,
        val is_favorite: Boolean,
        val updated_at: String
) : Parcelable