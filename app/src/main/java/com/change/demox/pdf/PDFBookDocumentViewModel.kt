/*
 * PDFBookDocumentViewModel.kt
 *
 * Created by wangxin on 2020/07/08.
 * Copyright © 2020年 Kubota-PAD. All rights reserved.
 */

package com.change.demox.pdf

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.change.base.BaseViewModel
import com.change.demox.usecase.PDFBookDocumentUseCase
import com.change.demox.utils.Event

/**
 * PDF ドキュメント閲覧画面 use case
 *
 */
class PDFBookDocumentViewModel(private val useCase: PDFBookDocumentUseCase) : BaseViewModel() {

    private val _loadPDFAction = MutableLiveData<Event<String>>()
    val loadPDFAction: LiveData<Event<String>> = _loadPDFAction

    /**
     * PDFを取得
     *
     * @param url  PDF link ,在浏览器上能直接打开PDF的url
     * @param fileName  PDF名
     */
    fun loadPDF(url: String, fileName: String) {
        runPipeline(pipelineBlock = {
            val result = useCase.loadPDFByLink(url, fileName)
            if (result != null) {
                _loadPDFAction.postValue(Event(result))
            }
        }, errorHandler = {

        })
    }
}