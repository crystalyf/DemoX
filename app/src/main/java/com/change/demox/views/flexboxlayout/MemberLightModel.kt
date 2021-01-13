/*
 * MemberReactionModell.kt
 *
 * Created by jilingwen on 2020/01/08.
 * Copyright © 2019年 Eole. All rights reserved.
 */

package com.change.demox.views.flexboxlayout

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

typealias ApiMemberLightModel = MemberLightModel


/**
 * 反応
 * @property id メンバーid
 * @property name メンバー名
 * @property picture プロフィール画像
 * @property role 紐づくメンバー
 */
@Parcelize
data class MemberLightModel(
    var id: String,
    var name: String,
    var picture: String,
    var role: Int
) : Parcelable