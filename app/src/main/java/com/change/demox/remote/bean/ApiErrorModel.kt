/*
 * ApiErrorModel.kt
 *
 * Created by kangzewei on 2020/06/02.
 * Copyright © 2020年 Kubota-PAD. All rights reserved.
 */

package com.change.demox.remote.bean

import com.change.demox.utils.Constants
import com.google.gson.annotations.SerializedName

open class ApiErrorModel(
        var title: String? = null,
        var code: String? = null,
        var message: String? = null,
        var detail: String? = null,
        @SerializedName("password_reset_token")
        var passwordResetToken: String? = null,
        @SerializedName("invalid-params")
        var invalidParams: List<ChangePasswordErrorModel>? = null,
        var error: Throwable? = null
) {
    fun getErrorType(): Constants.ApiErrorType {
        return when (code) {
            "E400-1" -> {
                //InvalidParameter
                Constants.ApiErrorType.InvalidParameter
            }
            "E400-2" -> {
                //BadRequest
                Constants.ApiErrorType.BadRequest
            }
            "E400-3" -> {
                //TooManyCartItems
                Constants.ApiErrorType.TooManyCartItems
            }
            "E401-1" -> {
                //Unauthorized
                Constants.ApiErrorType.AuthError
            }
            "E401-2" -> {
                //PasswordExpired
                Constants.ApiErrorType.PasswordExpired
            }
            "E403-1" -> {
                //Forbidden
                Constants.ApiErrorType.PermissionError
            }
            "E403-2" -> {
                //IPAddressRejected
                Constants.ApiErrorType.IPAddressRejected
            }
            "E500-1" -> {
                //InternalServerError
                Constants.ApiErrorType.InternalServerError
            }
            "E503-1" -> {
                //ServiceUnavailable
                Constants.ApiErrorType.ServiceUnavailable
            }
            "E503-2" -> {
                //Maintenance
                Constants.ApiErrorType.Maintenance
            }
            "E503-3" -> {
                //ExternalServiceUnavailable
                Constants.ApiErrorType.ExternalServiceUnavailable
            }
            // Custom error
            "NETWORK_ERROR" -> {
                Constants.ApiErrorType.NetworkError
            }
            else -> {
                Constants.ApiErrorType.OtherError
            }
        }
    }

    data class ChangePasswordErrorModel(
            var name: String?,
            var reason: String?
    )

    companion object {
        fun getNetWorkErrorModel(e: Throwable? = null): ApiErrorModel {
            return ApiErrorModel(
                    "NETWORK_ERROR",
                    "NETWORK_ERROR",
                    "NETWORK_ERROR",
                    "NETWORK_ERROR",
                    "NETWORK_ERROR",
                    arrayListOf(),
                    e
            )
        }

        fun getMaintenanceModel(e: Throwable? = null): ApiErrorModel {
            return ApiErrorModel(
                    "Maintenance",
                    "E503-2",
                    "Maintenance",
                    "Maintenance",
                    "Maintenance",
                    arrayListOf(),
                    e
            )
        }
    }
}