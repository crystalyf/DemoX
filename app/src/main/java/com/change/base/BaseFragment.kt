package com.change.base

import android.app.Dialog
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.change.demox.R
import com.change.demox.databinding.FragmentFigIllustrationBinding
import com.change.demox.utils.SharedPreferences
import timber.log.Timber

/**
 * アプリ中使用のBaseFragment
 * LifeCycleのDebugログを出力
 * Fragmentの共通処理もここにおく
 *
 */
abstract class BaseFragment<T : ViewDataBinding>(useDataBinding: Boolean = true) : Fragment() {
    /**
     * データ保存操作の対象
     */
    val preferences by lazy { SharedPreferences(requireContext()) }

    var dataBinding: T? = null

    /** 通信中インジケーター */
    private var mProgressDialog: Dialog? = null

    private var isShowingProgress = false

    var mProgress: View? = null


    private val _useBinding = useDataBinding

    var rootView: FrameLayout? = null


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        if (_useBinding) {
            dataBinding = DataBindingUtil.inflate(inflater, layoutId(), container, false)
            dataBinding!!.lifecycleOwner = this
        } else
            inflater.inflate(layoutId(), container, false)

        afterView()
        if (isShowingProgress) {
            showProgress()
        }
        return if (_useBinding) {
            if (layoutId() == R.layout.fragment_fig_illustration) {
                if (rootView == null) {
                    rootView = FrameLayout(requireActivity())
                }
                rootView?.addView(dataBinding!!.root)
                rootView!!
            } else {
                dataBinding!!.root
            }
        } else super.onCreateView(inflater, container, savedInstanceState)
    }

    abstract fun afterView()

    open fun shouldChangeLayout() = false

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (layoutId() != R.layout.fragment_fig_illustration) {
            return
        }
        (dataBinding as FragmentFigIllustrationBinding).recyclerViewPart.adapter = null

        rootView?.removeAllViews()
        dataBinding = null
        dataBinding = DataBindingUtil.inflate(LayoutInflater.from(context), layoutId(), null, false)
        rootView?.addView(dataBinding!!.root)
        dataBinding?.executePendingBindings()
        dataBinding!!.lifecycleOwner = this
    }


    @LayoutRes
    abstract fun layoutId(): Int

    /**
     * この画面はインジケータタイプの場合
     *
     * @return trueの場合はインジケータタイプ、falseはHUD タイプの場合
     */
    open fun isCustomViewError(): Boolean = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroyView() {
        if (_useBinding) {
            hideProgress()
            dataBinding!!.unbind()
            dataBinding = null
        }
        rootView = null
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onDetach() {
        super.onDetach()
    }

    private fun showProgress() {
        if (this.isDetached) {
            return
        }
        if (mProgress == null) {
            mProgress = View.inflate(requireContext(), R.layout.layout_inside_loading, null)
            val para = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            )
            mProgress!!.layoutParams = para
            (dataBinding?.root as ViewGroup).addView(mProgress)
        } else {
            try {
                (dataBinding?.root as ViewGroup).addView(mProgress)
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    private fun hideProgress() {
        if (mProgress != null) {
            (dataBinding?.root as ViewGroup).removeView(mProgress)
            mProgress = null
        }
    }
}