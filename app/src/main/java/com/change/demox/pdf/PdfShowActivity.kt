package com.change.demox.pdf

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.change.base.BaseActivity
import com.change.demox.R
import com.change.demox.extension.getViewModelFactory
import com.change.demox.utils.EventObserver
import com.change.demox.views.recyclerview.ceilingTwo.RecyclerCeilingTwoViewModel
import com.github.barteksc.pdfviewer.util.FitPolicy
import kotlinx.android.synthetic.main.activity_pdf_show.*
import timber.log.Timber
import java.io.File

/**
 * PDF在线显示，本质上是先下载PDF文件到本地，然后再显示PDF本地文件。做法是：Retrofit下载大文件（PDF），然后用PdfViewer组件加载PDF显示
 *
 * 组件github: https://github.com/barteksc/AndroidPdfViewer
 *
 */
class PdfShowActivity : BaseActivity() {

    private lateinit var viewModel: PDFBookDocumentViewModel

    //private val pdfUrl = "https://publicd6tn2upk7h3ce.blob.core.windows.net/oldmodel/07909-00396-1.pdf?se=2020-09-29T04%3A02%3A02Z&sig=PFylT6AX71kaTXB0bFh3er07eoGrF6BmZvW3icNFS8c%3D&sp=r&spr=https&sr=b&sv=2015-04-05"
    private val pdfUrl = "https://staticzcjb.weibangong.com/pdf/business_license.pdf"
    private val fileName = "部品表(1)"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf_show)
        viewModel = viewModels<PDFBookDocumentViewModel> { getViewModelFactory() }.value
        observeApiErrorEvent(viewModel)
        initView()
    }

    private fun initView() {
        viewModel.loadPDF(pdfUrl, fileName)
        viewModel.loadPDFAction.observe(this, EventObserver {
            viewLoadPDF(it)
        })
    }

    private fun viewLoadPDF(path: String) {
        pdf_view?.fromFile(File(path))
                ?.enableSwipe(true)
                ?.enableDoubletap(true)
                ?.spacing(16)
                ?.swipeHorizontal(true)
                ?.pageSnap(true)
                ?.autoSpacing(true)
                ?.pageFling(true)
                ?.pageFitPolicy(FitPolicy.BOTH)
                ?.onLoad {
                    Timber.d("load success")
                }
                ?.onError {
                    Timber.d("load error")
                }
                ?.onPageChange { page, pageCount ->
                    Timber.d("load page:$page,pageCount:$pageCount")
                }
                ?.onPageScroll { page, positionOffset ->
                    Timber.d("load page:$page,positionOffset:$positionOffset")
                }
                ?.onPageError { page, t ->
                    Timber.d("load page:$page,exp:$t")
                }
                ?.enableAntialiasing(true)
                ?.load()
    }
}