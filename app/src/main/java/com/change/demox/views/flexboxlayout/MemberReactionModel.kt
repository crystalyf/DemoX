/*
 * MemberReactionModell.kt
 *
 * Created by jilingwen on 2020/01/08.
 * Copyright © 2019年 Eole. All rights reserved.
 */

package com.change.demox.views.flexboxlayout

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

typealias ApiMemberReactionModel = MemberReactionModel


/**
 * 反応
 * @property read 読了フラグ
 * @property answered 回答済みフラグ(0:未回答, 1:回答済み)
 * @property member 紐づくメンバー
 * @property isHaveQuestion アンケートはありますか(false: なし, true:あり)
 */
@Parcelize
data class MemberReactionModel(
    var read: Int,
    var member: MemberLightModel,
    var answered: Int,
    var isHaveQuestion: Boolean
) : Parcelable