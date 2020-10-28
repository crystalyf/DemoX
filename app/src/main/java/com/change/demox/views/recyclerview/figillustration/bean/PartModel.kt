/*
 * PartModel.kt
 *
 * Created by xingjunchao on 2020/06/17.
 * Copyright © 2020年 Kubota-PAD. All rights reserved.
 */
package com.change.demox.views.recyclerview.figillustration.bean

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 *
 * @property compatibility_type 互換性区分パーツの互換性を表す。レスポンスは、下記の数字を文字列として返す。2: 相互互換3: 互換性無し4: 上位互換5: 下位互換
 * @property frt
 * @property fui ブック内のパーツFUI情報
 * @property illustrations
 * @property is_memo_addable
 * @property is_sb_addable
 * @property maintenances FRS作業項目一覧
 * @property memos パーツメモ
 * @property part_id パーツ ID
 * @property part_name 品名
 * @property part_number 品番
 * @property quantity 数量
 * @property recommended_part_ids
 * @property reference_number1 図番 イラストに記載された番号のこと。パーツ情報とイラスト上のパーツの対応を表現する。
 * @property reference_number2 図番 イラストに記載された番号のこと。パーツ情報とイラスト上のパーツの対応を表現する。
 * @property remarks 補足、備考など
 * @property revision_number
 * @property selected_maintenance_id
 * @property selected_maintenance_name
 * @property serial_number 機番情報
 * @property service_bulletins
 * @property weight 重量
 * @property weight_unit 重量単位
 * @property figId  figId(カートの一覧に使用)
 * @property isHaveMemoPermissions MemoPermissionsか? true: はい   false:ない(カートの一覧に使用)
 * @property isHaveSbPermissions service_bulletinsか? true: はい   false:ない(カートの一覧に使用)
 * @property modelName  モデル名(カートの一覧に使用)
 * @property figName  figName(カートの一覧に使用)
 * @property isSelected 選択されたかどうか、true: はい   false:ない
 */
@Parcelize
data class PartModel(

        var compatibility_type: String? = null,
        val frt: String? = null,
        val fui: Fui? = null,
        val illustrations: List<Illustrations>? = null,
        val is_memo_addable: Boolean? = null,
        val is_sb_addable: Boolean? = null,
        val maintenances: List<String>? = null,
        var memos: List<Memo>? = null,
        var part_id: Int? = null,
        var part_name: String? = null,
        var part_number: String? = null,
        var quantity: Int? = null,
        val recommended_part_ids: List<String>? = null,
        var reference_number1: String? = null,
        val reference_number2: String? = null,
        var remarks: String? = null,
        val revision_number: String? = null,
        val selected_maintenance_id: Int? = null,
        val selected_maintenance_name: String? = null,
        var serial_number: String? = null,
        var service_bulletins: List<ServiceBulletins>? = null,
        var weight: Double? = null,
        var weight_unit: String? = null,
        var figId: Int? = null,
        var isHaveMemoPermissions: Boolean? = null,
        var isHaveSbPermissions: Boolean? = null,
        var modelName: String? = null,
        var figName: String? = null,
        var isSelected: Boolean = false
) : Parcelable {

}

@Parcelize
data class Fui(
        val capacity: String?,
        val capacity_unit_name: String?,
        val related_figs: String?,
        val section_name: String?,
        val services: String?
) : Parcelable

@Parcelize
data class Illustrations(
        val illustration_id: Int,
        val positions: List<Position>
) : Parcelable

@Parcelize
data class Position(
        val end_x: Int,
        val end_y: Int,
        val start_x: Int,
        val start_y: Int
) : Parcelable

@Parcelize
data class Memo(
        val is_editable: Boolean?,
        val items: List<Item>?,
        val memo_id: Int?,
        val modified_by: String?,
        val open_ranges: OpenRanges?,
        val start_date: String?,
        val end_date: String?,
        val type: Int?
) : Parcelable

@Parcelize
data class Item(
        val content: String?,
        val file_id: Int?,
        val file_name: String?,
        val file_size: Int?,
        val language: String?,
        val title: String?
) : Parcelable

@Parcelize
data class OpenRanges(
        val group: Group?,
        @SerializedName("private")
        val _private: Private?,
        val role: Role?,
        val type: String?
) : Parcelable

@Parcelize
data class Group(
        val groups: String?,
        val selected: Boolean?
) : Parcelable

@Parcelize
data class Private(
        val selected: Boolean?
) : Parcelable

@Parcelize
data class Role(
        val roles: String?,
        val selected: Boolean?
) : Parcelable

@Parcelize
data class ServiceBulletins(
        val is_editable: Boolean?,
        val items: List<Item>?,
        val modified_by: String?,
        val open_ranges: OpenRanges?,
        val service_bulletin_id: Int?,
        val start_date: String?,
        val end_date: String?,
        val type: Int?
) : Parcelable


