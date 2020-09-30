/*
 * ITopRepository.kt
 *
 * Created by xingjunchao on 2020/09/29.
 * Copyright © 2020年 Kubota-PAD. All rights reserved.
 */

package com.change.demox.repository

interface ITopRepository {

    suspend fun downloadPDFByLink(url: String, fileName: String): String

}