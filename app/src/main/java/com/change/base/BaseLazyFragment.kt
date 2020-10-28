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
abstract class BaseLazyFragment<T : ViewDataBinding>(useDataBinding: Boolean = true) : Fragment() {

    // 视图加载完成标志位
    private var isViewCreated = false

    // 视图对用户可见标志位
    private var isUserVisible = false

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

        //afterView()
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // fragment视图加载完成，设置视图加载完成标志位
        isViewCreated = true
        // 可以开启懒加载
        lazyLoad()
    }

    // 该方法是在onCreate方法之前调用的
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            isUserVisible = true
            lazyLoad()
        } else {
            isUserVisible = false
        }
    }

    private fun lazyLoad() {
        /**
         * 牢记懒加载的2个重要条件：
         * 1、fragment视图已经加载完成
         * 2、fragment视图对用户可见了
         *
         * 所以，根据2个标志位进行判断
         */
        if (isViewCreated && isVisible) {
            afterView()
            // 数据加载后重置标志位，避免重复加载
            isUserVisible = false
            isViewCreated = false
        }
    }

    @LayoutRes
    abstract fun layoutId(): Int

    protected abstract fun loadData()
    abstract fun afterView()


    override fun onDestroyView() {
        if (_useBinding) {
            hideProgress()
            dataBinding!!.unbind()
            dataBinding = null
        }
        rootView = null
        super.onDestroyView()
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