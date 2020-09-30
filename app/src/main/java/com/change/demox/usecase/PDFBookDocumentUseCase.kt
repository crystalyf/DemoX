/*
 * PDFBookDocumentUseCase.kt
 *
 * Created by wangxin on 2020/07/08.
 * Copyright © 2020年 Kubota-PAD. All rights reserved.
 */

package com.change.demox.usecase

import com.change.demox.repository.ITopRepository

/**
 * PDF ドキュメント閲覧画面 use case
 *
 * @property iTopRepository
 */
class PDFBookDocumentUseCase(
        private val iTopRepository: ITopRepository
) {

    suspend fun loadPDFByLink(url: String, fileName: String): String? {
        return iTopRepository.downloadPDFByLink(url, fileName)
    }

}