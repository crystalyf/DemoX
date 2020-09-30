package com.change.demox

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.change.demox.pdf.PDFBookDocumentViewModel
import com.change.demox.remote.RetrofitManager
import com.change.demox.repository.ITopRepository
import com.change.demox.usecase.PDFBookDocumentUseCase
import com.change.demox.utils.SharedPreferences

class ViewModelFactory constructor(
        private val sharePref: SharedPreferences,
        private val topRepository: ITopRepository,
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
            with(modelClass) {
                when {
                    isAssignableFrom(PDFBookDocumentViewModel::class.java) ->
                        PDFBookDocumentViewModel(PDFBookDocumentUseCase(topRepository))
                    else ->
                        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
                }
            } as T
    
    companion object {
        @Volatile
        var INSTANCE: ViewModelFactory? = null

        fun getInstance(
                sharePref: SharedPreferences,
                iTopRepository: ITopRepository,
        ): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class) {
                    if (INSTANCE == null) {
                        INSTANCE =
                                ViewModelFactory(
                                        sharePref,
                                        iTopRepository
                                )
                        RetrofitManager.sharedPreferences = sharePref
                    }
                }
            }
            return INSTANCE!!
        }
    }
}
