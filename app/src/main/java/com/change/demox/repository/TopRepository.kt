/*
 * TopRepository.kt
 *
 * Created by shitianming on 2020/06/03.
 * Copyright © 2020年 Kubota-PAD. All rights reserved.
 */

package com.change.demox.repository

import android.content.Context
import com.change.demox.remote.RetrofitManager

class TopRepository(private val context: Context) : ITopRepository {

    companion object {
        private var instance: ITopRepository? = null
        fun instance(
                context: Context
        ): ITopRepository {
            if (instance == null) {
                instance =
                        TopRepository(
                                context
                        )
            }
            return instance!!
        }
    }

    override suspend fun downloadPDFByLink(
            url: String,
            fileName: String
    ): String {
        return RetrofitManager.downloadPDF(url, fileName)
    }

}