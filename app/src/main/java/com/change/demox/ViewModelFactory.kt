package com.change.demox

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.change.demox.pdf.PDFBookDocumentViewModel
import com.change.demox.remote.RetrofitManager
import com.change.demox.repository.ITopRepository
import com.change.demox.usecase.PDFBookDocumentUseCase
import com.change.demox.utils.SharedPreferences
import com.change.demox.views.bottomsheet.BottomSheetViewModel
import com.change.demox.views.recyclerview.paging.PagingViewModel
import com.change.demox.views.recyclerview.paging.usecase.GetPagingHomeDataUseCase
import com.change.demox.views.recyclerview.paging.usecase.repository.IDataRepository

class ViewModelFactory constructor(
        private val sharePref: SharedPreferences,
        private val topRepository: ITopRepository,
        private val dataRepository: IDataRepository,
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
            with(modelClass) {
                when {
                    isAssignableFrom(PDFBookDocumentViewModel::class.java) ->
                        PDFBookDocumentViewModel(PDFBookDocumentUseCase(topRepository))
                    isAssignableFrom(BottomSheetViewModel::class.java) ->
                        BottomSheetViewModel()
                    isAssignableFrom(PagingViewModel::class.java) ->
                        PagingViewModel(GetPagingHomeDataUseCase(dataRepository))
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
                iDataRepository: IDataRepository
        ): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class) {
                    if (INSTANCE == null) {
                        INSTANCE =
                                ViewModelFactory(
                                        sharePref,
                                        iTopRepository,
                                        iDataRepository
                                )
                        RetrofitManager.sharedPreferences = sharePref
                    }
                }
            }
            return INSTANCE!!
        }
    }
}
