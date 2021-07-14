package com.change.demox

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.change.demox.camera.TakePhotoViewModel
import com.change.demox.pdf.PDFBookDocumentViewModel
import com.change.demox.remote.RetrofitManager
import com.change.demox.repository.ITopRepository
import com.change.demox.usecase.PDFBookDocumentUseCase
import com.change.demox.utils.SharedPreferences
import com.change.demox.views.bottomsheet.BottomSheetViewModel
import com.change.demox.views.firebase.auth.FirebaseAuthViewModel
import com.change.demox.views.firebase.dynamiclink.FirebaseDynamicViewModel
import com.change.demox.views.flexboxlayout.TopicDetailViewModel
import com.change.demox.views.recyclerview.figillustration.FigViewModel
import com.change.demox.views.recyclerview.paging.delete.PagingDeleteViewModel
import com.change.demox.views.recyclerview.paging.delete.usecase.GetBooksUseCase
import com.change.demox.views.recyclerview.paging.delete.usecase.repository.ISearchRepository
import com.change.demox.views.recyclerview.paging.onlyshow.PagingViewModel
import com.change.demox.views.recyclerview.paging.onlyshow.usecase.GetPagingHomeDataUseCase
import com.change.demox.views.recyclerview.paging.onlyshow.usecase.repository.IDataRepository
import com.change.demox.views.tutorial.TutorialViewModel

class ViewModelFactory constructor(
        private val sharePref: SharedPreferences,
        private val topRepository: ITopRepository,
        private val dataRepository: IDataRepository,
        private val searchRepository: ISearchRepository
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
                    isAssignableFrom(PagingDeleteViewModel::class.java) ->
                        PagingDeleteViewModel(GetBooksUseCase(searchRepository))
                    isAssignableFrom(TutorialViewModel::class.java) ->
                        TutorialViewModel()
                    isAssignableFrom( TopicDetailViewModel::class.java) ->
                        TopicDetailViewModel()
                    isAssignableFrom(FigViewModel::class.java) ->
                        FigViewModel()
                    isAssignableFrom(FirebaseAuthViewModel::class.java) ->
                        FirebaseAuthViewModel()
                    isAssignableFrom(FirebaseDynamicViewModel::class.java) ->
                        FirebaseDynamicViewModel()
                    isAssignableFrom(TakePhotoViewModel::class.java) ->
                        TakePhotoViewModel()
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
                iDataRepository: IDataRepository,
                iSearchRepository: ISearchRepository
        ): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class) {
                    if (INSTANCE == null) {
                        INSTANCE =
                                ViewModelFactory(
                                        sharePref,
                                        iTopRepository,
                                        iDataRepository,
                                        iSearchRepository
                                )
                        RetrofitManager.sharedPreferences = sharePref
                    }
                }
            }
            return INSTANCE!!
        }
    }
}
