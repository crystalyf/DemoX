/*
 * Constants.kt
 *
 * Created by kangzewei on 2020/05/29.
 * Copyright © 2020年 Kubota-PAD. All rights reserved.
 */

package com.change.demox.utils

object Constants {

    enum class ApiErrorType {
        /** ネットワーク異常 */
        NetworkError,

        BadRequest,

        TooManyCartItems,

        /** 認証エラー */
        AuthError,

        /** レスポンスは正常に返すが、異常データがある */
        InvalidParameter,

        /** パスワードが有効期限切れ　*/
        PasswordExpired,

        /** 権限なしエラー　*/
        PermissionError,

        IPAddressRejected,

        InternalServerError,

        ServiceUnavailable,

        /** メンテナンスエラー　*/
        Maintenance,

        ExternalServiceUnavailable,

        /** 上記以外 */
        OtherError
    }

}