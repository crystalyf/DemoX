/*
 * ScalableImageView.kt
 *
 * Created by xingjunchao on 2020/06/17.
 * Copyright © 2020年 Kubota-PAD. All rights reserved.
 */

package com.change.demox.views.recyclerview.figillustration.widget

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.OverScroller
import androidx.core.content.ContextCompat
import androidx.core.math.MathUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.change.demox.R
import com.change.demox.utils.SharedPreferences
import com.change.demox.views.recyclerview.figillustration.FigViewModel
import com.change.demox.views.recyclerview.figillustration.bean.PartModel
import com.change.demox.views.recyclerview.figillustration.bean.Position
import kotlin.math.max
import kotlin.math.min

/**
 * イラストの画像を表示のCustomView
 *
 */
class ScalableImageView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    /**
     * 画像はローディング完成
     *
     */
    interface ProgressListener {
        fun loadFinish()
    }

    /**
     * データ保存操作の対象
     */
    private val preferences by lazy { context?.applicationContext?.let { SharedPreferences(it) } }

    /**
     * 画像中のパーツをクリックする
     */
    var onDeviceClicked: (partIdList: MutableList<Int>?, referenceNumber: String?) -> Unit =
            { _, _ -> }

    private var viewModel: FigViewModel? = null

    private var mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mImage: Bitmap? = null
    private var mImageWidth = 0
    private var mImageHeight = 0
    private var mMinScale = 0f
    private var mAllMinScale = 0f
    private var mMaxScale = 0f

    private var mImageScale = 1
    private var mPointResetFlag = false

    private var mBeginScale = 1f
    private var mPreScale = 0f

    @SuppressLint("AnimatorKeep")
    private var mCurrentScale = 0f
        set(value) {
            mPreScale = field
            field = value
            offsetWhenScale(
                    mFocusX,
                    mFocusY,
                    if (viewModel?.currentScale != 0F) mCurrentScale else mPreScale,
                    mCurrentScale
            )
            invalidate()
        }

    private var mOffsetX = 0f
    private var mOffsetY = 0f
    private var mFocusX = 0f
    private var mFocusY = 0f

    private var mRectPadding = 3.toPx()

    //tools
    private var mScroller = OverScroller(context)
    private lateinit var mScaleAnimator: ObjectAnimator
    private lateinit var mGestureDetector: GestureDetector
    private lateinit var mScaleDetector: ScaleGestureDetector

    //data
    private var mIsHighQuality = false
    private var mImageUrl: String = ""
    private var mOriginalData: MutableList<PartModel> = mutableListOf()
    private var mDataList: ArrayList<LocaleModel> = arrayListOf()
    private var mIsFromLocal = false
    private var mBookId = 0

    var mIllustrationId: Int? = null
    private var mSelectedData: List<LocaleModel>? = null
    private var mHighLightData: List<ScalableImageView.LocaleModel>? = null
    private var listener: ProgressListener? = null

    private var fingerCount: Int? = 0

    init {
        initPaint()
        initAnimator()
        initGestureDetector()
    }

    private fun Int.toPx(): Int {
        return (Resources.getSystem().displayMetrics.scaledDensity * this).toInt()
    }

    /**
     * データソースを設定
     * @param url 画像のURL
     * @param illustrationId illustrationId
     * @param isHighQuality true：高品質  false：低品質
     * @param isFromLocal true: local data
     */
    fun setDataSource(
            url: String?,
            illustrationId: Int,
            isHighQuality: Boolean,
            isFromLocal: Boolean,
            bookId: Int,
            viewModel: FigViewModel
    ) {
        mIsHighQuality = isHighQuality
        mImageUrl = url ?: ""
        mIllustrationId = illustrationId
        mIsFromLocal = isFromLocal
        mBookId = bookId
        this.viewModel = viewModel
    }

    fun restoreImageViewParams() {
        if ((mOffsetX == 0F || mOffsetX == Float.POSITIVE_INFINITY) && (mOffsetY == 0F || mOffsetY == Float.POSITIVE_INFINITY) &&
                (mCurrentScale - mMinScale <= 0.001)
        ) {
            return
        }
        viewModel?.offsetX = mOffsetX
        viewModel?.offsetY = mOffsetY
        viewModel?.currentScale = if (mCurrentScale - mMinScale > 0.001) mCurrentScale else 0f
        viewModel?.illustrationId = mIllustrationId ?: 0
    }

    fun setProgress(listener: ProgressListener?) {
        this.listener = listener
    }

    fun setParts(data: MutableList<PartModel>?, selectedIds: List<Int>, highLightIds: List<Int>) {
        if (data == null) {
            return
        }
        mOriginalData.clear()
        mOriginalData.addAll(data)
        originalDataTransferToLocaleModel()
        mDataList.forEach {
            it.translateRect()
        }
        if (!mPointResetFlag && mImageScale > 1) {
            mDataList.forEach {
                it.fixLocalModel()
            }
        }
        setSelectedDataIds(selectedIds.toMutableList())
        setHighLightDataIds(highLightIds.toMutableList())
    }

    /**
     * ハイライトを設定する
     *
     * @param ids ハイライト表示されたパーツID
     */
    fun setHighLightDataIds(ids: MutableList<Int>) {
        mHighLightData = mDataList.filter {
            ids.contains(it.partId)
        }
        invalidate()
    }

    /**
     * 選択されているポイントを設定する
     *
     * @param ids ハイライト表示されたパーツID
     */
    fun setSelectedDataIds(ids: MutableList<Int>) {
        mSelectedData = mDataList.filter {
            ids.contains(it.partId)
        }
        invalidate()
    }

    private fun resetPoint() {
        val selectedIds = mSelectedData?.map { it.partId }
        mSelectedData = mDataList.filter {
            selectedIds?.contains(it.partId) == true
        }
        val highLightIds = mHighLightData?.map { it.partId }
        mHighLightData = mDataList.filter {
            highLightIds?.contains(it.partId) == true
        }
    }

    /**
     * flingアクションを処理するためにpostInvalidateOnAnimation（）の後に呼び出されます
     */
    override fun computeScroll() {
        //フリングが終わったかどうかを判断する
        if (mScroller.computeScrollOffset()) {
            mOffsetX = mScroller.currX.toFloat()
            mOffsetY = mScroller.currY.toFloat()
            postInvalidateOnAnimation()
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (mMinScale == 0f) return
        canvas?.apply {
            //オフセット値を変更して特定の範囲に制限する
            if (viewModel != null && viewModel?.illustrationId == mIllustrationId &&
                    ((viewModel?.offsetX != Float.POSITIVE_INFINITY && viewModel?.offsetX != 0F) ||
                            (viewModel?.offsetY != Float.POSITIVE_INFINITY && viewModel?.offsetY != 0F) ||
                            (viewModel?.currentScale != 0f))
            ) {
                mOffsetX = viewModel?.offsetX ?: 0F
                mOffsetY = viewModel?.offsetY ?: 0F
                mCurrentScale = viewModel?.currentScale ?: 0F
                viewModel?.offsetX = 0F
                viewModel?.offsetY = 0F
                viewModel?.currentScale = 0F
                viewModel?.illustrationId = 0
            }
            fixOffset()
            translate(mOffsetX, mOffsetY)
            scale(mCurrentScale, mCurrentScale, width / 2f, height / 2f)
            drawBitmap(
                    mImage!!,
                    (measuredWidth - mImageWidth) / 2f,
                    (measuredHeight - mImageHeight) / 2f,
                    mPaint
            )
            // border
            mPaint.style = Paint.Style.STROKE
            if (preferences?.isOrangeThemeColor!!) {
                mPaint.color = ContextCompat.getColor(context, R.color.colorBlue)
            } else {
                mPaint.color = ContextCompat.getColor(context, R.color.colorRed)
            }
            resetPoint()
            mHighLightData?.forEach {
                mPaint.strokeWidth = 2.toPx().toFloat()
                drawRect(it.translateRect, mPaint)
            }
            //選択したマークをクリック
            mSelectedData?.forEach {
                it.let {
                    // border
                    mPaint.style = Paint.Style.FILL
                    if (preferences?.isOrangeThemeColor!!) {
                        mPaint.color = ContextCompat.getColor(context, R.color.colorAccent)
                    } else {
                        mPaint.color = ContextCompat.getColor(context, R.color.colorBlue)
                    }
                    drawRect(it.translateRect, mPaint)
                    //stroke
                    mPaint.style = Paint.Style.STROKE
                    mPaint.strokeWidth = 4.toPx().toFloat()
                    if (preferences?.isOrangeThemeColor!!) {
                        mPaint.color = ContextCompat.getColor(context, R.color.colorThemeOrange)
                    } else {
                        mPaint.color = ContextCompat.getColor(context, R.color.colorThemeGreen)
                    }
                    drawRect(it.translateRect, mPaint)
                }
            }

        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        //イベントは最初にmScaleDetectorに渡されます。falseが返された場合、それは2本指操作ではないことを意味し、次にイベントはmGestureDetectorに渡されて処理されます。
        var res = mScaleDetector.onTouchEvent(event)
        fingerCount = event?.pointerCount
        if (!mScaleDetector.isInProgress) {
            res = mGestureDetector.onTouchEvent(event)
        }
        return res
    }

    /**
     * 初期化中および画面回転中のコールバックメソッド
     */
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        val orientation = context.resources.configuration.orientation
        if ((orientation == Configuration.ORIENTATION_PORTRAIT && w > h) ||
                orientation == Configuration.ORIENTATION_LANDSCAPE && w < h
        ) {
            return
        }
        initView()
    }

    /**
     * 1つのposition、1つのLocaleModelに変換
     *
     */
    private fun originalDataTransferToLocaleModel() {
        mDataList.clear()
        mOriginalData.forEach { bean ->
            bean.illustrations?.forEach { illustration ->
                if (illustration.illustration_id == mIllustrationId) {
                    illustration.positions.forEach {
                        //この図のillustrationId（座標点は一部）の位置座標をmDataListに保存します。1つのposition、1つのLocaleModelに変換
                        mDataList.add(
                                LocaleModel(
                                        bean.part_id!!,
                                        it,
                                        bean.reference_number1
                                )
                        )
                    }
                }
            }
        }
    }

    /**
     * データと画像を初期化する
     */
    private fun initView() {
        //APIデータをローカルデータに変換します。これは、後で使用するのにより便利です。
        originalDataTransferToLocaleModel()
        //画像を読み込む
        val customTarget = object : CustomTarget<Bitmap>() {

            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                //元の画像サイズを取得し、サイズに応じて最小ズーム値と最大ズーム値を決定します
                val recyclerViewHeight = 56
                val actualHeight = if (viewModel?.illustrationId != 0) {
                    measuredHeight - recyclerViewHeight
                } else {
                    measuredHeight
                }

                mImageWidth = resource.width
                mImageHeight = resource.height

                mImageScale = max((mImageWidth / measuredWidth), (mImageHeight / measuredHeight))

                mImageScale = if (mImageScale > 5) 2 else 1
                mImage =
                        Bitmap.createScaledBitmap(
                                resource,
                                mImageWidth / mImageScale,
                                mImageHeight / mImageScale,
                                true
                        )

                mImageWidth /= mImageScale
                mImageHeight /= mImageScale

                if (mImageScale > 1) {
                    if (mDataList.size > 0) {
                        mPointResetFlag = true
                    }
                    mDataList.forEach {
                        it.fixLocalModel()
                    }
                }

                val heightScale = actualHeight / mImageHeight.toFloat()
                val widthScale = measuredWidth / mImageWidth.toFloat()
                if (heightScale < widthScale) {
                    mMinScale = heightScale
                    mMaxScale = width / mImageWidth.toFloat() * 2
                } else {
                    mMinScale = widthScale
                    mMaxScale = height / mImageHeight.toFloat() * 2
                }
                if (mAllMinScale == 0F || mAllMinScale > mMinScale) {
                    if (mCurrentScale == mAllMinScale) {
                        mCurrentScale = mMinScale
                    }
                    mAllMinScale = mMinScale
                }
                if (mCurrentScale == 0f) {
                    mCurrentScale = mMinScale
                }
                mDataList.forEach {
                    it.translateRect()
                }
                mHighLightData?.forEach {
                    it.translateRect()
                }
                mSelectedData?.forEach {
                    it.translateRect()
                }
                invalidate()
                listener?.loadFinish()
            }

            override fun onLoadCleared(placeholder: Drawable?) {
                // do nothing
            }

            override fun onLoadFailed(errorDrawable: Drawable?) {
                super.onLoadFailed(errorDrawable)
                listener?.loadFinish()
            }
        }
        Glide.with(context).asBitmap().load(mImageUrl)
                .into(customTarget)

    }

    /**
     * Paint
     *
     */
    private fun initPaint() {
        mPaint.style = Paint.Style.FILL
        mPaint.strokeWidth = 2.toPx().toFloat()
    }

    private fun initGestureDetector() {

        mGestureDetector =
                GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {

                    override fun onDown(e: MotionEvent?): Boolean {
                        return true
                    }

                    /**
                     * クリック
                     */
                    override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
                        e?.let { event ->
                            val data = mDataList.find {
                                it.rectOnScreen.contains(event.x.toInt(), event.y.toInt())
                            }
                            mSelectedData = mDataList.filter { it.partId == data?.partId }
                            if (mSelectedData != null && mSelectedData!!.isNotEmpty()) {
                                onDeviceClicked(
                                        mSelectedData?.map { it.partId }?.toMutableList(),
                                        mSelectedData?.get(0)?.referenceNumber
                                )
                            } else {
                                onDeviceClicked(mutableListOf(), "")
                            }
                            invalidate()
                        }
                        return super.onSingleTapConfirmed(e)
                    }

                    /**
                     * ダブルタップ
                     *
                     * @param e MotionEvent
                     * @return
                     */
                    override fun onDoubleTap(e: MotionEvent?): Boolean {
                        e?.let {
                            mFocusX = it.x
                            mFocusY = it.y
                        }

                        mScaleAnimator.setFloatValues(
                                mCurrentScale,
                                if ((mCurrentScale - mMinScale) <= 0.001) mMaxScale else mAllMinScale
                        )
                        mScaleAnimator.start()
                        return super.onDoubleTap(e)
                    }

                    override fun onScroll(
                            e1: MotionEvent?,
                            e2: MotionEvent?,
                            distanceX: Float,
                            distanceY: Float
                    ): Boolean {
                        mOffsetX -= distanceX
                        mOffsetY -= distanceY
                        invalidate()
                        return false
                    }

                    override fun onFling(
                            e1: MotionEvent?,
                            e2: MotionEvent?,
                            velocityX: Float,
                            velocityY: Float
                    ): Boolean {
                        mScroller.fling(
                                mOffsetX.toInt(), mOffsetY.toInt(), velocityX.toInt(), velocityY.toInt(),
                                (-(mImageWidth * mCurrentScale - width) / 2).toInt(),
                                ((mImageWidth * mCurrentScale - width) / 2).toInt(),
                                (-(mImageHeight * mCurrentScale - height) / 2).toInt(),
                                ((mImageHeight * mCurrentScale - height) / 2).toInt()
                        )
                        postInvalidateOnAnimation()
                        return super.onFling(e1, e2, velocityX, velocityY)
                    }
                })

        mScaleDetector = ScaleGestureDetector(
                context,
                object : ScaleGestureDetector.SimpleOnScaleGestureListener() {

                    override fun onScaleBegin(detector: ScaleGestureDetector?): Boolean {
                        //ダブルクリックしてもonDoubleTapが実行されない、ズームがトリガーされるという状況を解決するために、判断するfingerCountを追加します
                        if (fingerCount!! > 1) {
                            mBeginScale = mCurrentScale
                        }
                        return super.onScaleBegin(detector)
                    }

                    override fun onScale(detector: ScaleGestureDetector?): Boolean {
                        if (fingerCount!! > 1) {
                            detector?.let {
                                mFocusX = it.focusX
                                mFocusY = it.focusY
                                mCurrentScale =
                                        MathUtils.clamp(mBeginScale * it.scaleFactor, mMinScale, mMaxScale)
                            }
                            invalidate()
                        }
                        return super.onScale(detector)
                    }
                })
    }

    private fun initAnimator() {
        mScaleAnimator = ObjectAnimator.ofFloat(this, "mCurrentScale", 0f)
        mScaleAnimator.interpolator = LinearInterpolator()
        mScaleAnimator.duration = 300
    }

    /**
     * ズーム値に従って、現在のオフセット値を計算します
     */
    private fun offsetWhenScale(x: Float, y: Float, currentScale: Float, targetScale: Float) {
        mOffsetX += (x - width / 2 - mOffsetX) - (x - width / 2 - mOffsetX) / currentScale * targetScale
        mOffsetY += (y - height / 2 - mOffsetY) - (y - height / 2 - mOffsetY) / currentScale * targetScale
        if (mOffsetX.isNaN()) {
            mOffsetX = Float.POSITIVE_INFINITY
        }
        if (mOffsetY.isNaN()) {
            mOffsetY = Float.POSITIVE_INFINITY
        }
    }

    /**
     * オフセットを範囲に制限する
     */
    private fun fixOffset() {
        val maxOffsetX = max((mImageWidth * mCurrentScale - width) / 2, 0f)
        val maxOffsetY = max((mImageHeight * mCurrentScale - height) / 2, 0f)
        mOffsetX = if (mOffsetX > 0) {
            min(maxOffsetX, mOffsetX)
        } else {
            max(-maxOffsetX, mOffsetX)
        }

        mOffsetY = if (mOffsetY > 0) {
            min(maxOffsetY, mOffsetY)
        } else {
            max(-maxOffsetY, mOffsetY)
        }
    }

    inner class LocaleModel(
            val partId: Int,
            positions: Position,
            val referenceNumber: String?
    ) {
        private val originalRect = Rect(
                (positions.start_x * if (mIsHighQuality || mIsFromLocal) 1.0 else 0.4).toInt(),
                (positions.start_y * if (mIsHighQuality || mIsFromLocal) 1.0 else 0.4).toInt(),
                (positions.end_x * if (mIsHighQuality || mIsFromLocal) 1.0 else 0.4).toInt(),
                (positions.end_y * if (mIsHighQuality || mIsFromLocal) 1.0 else 0.4).toInt()
        )

        /**
         * 画像が大きの場合、座標を修正する
         *
         */
        fun fixLocalModel() {
            originalRect.top = originalRect.top / mImageScale
            originalRect.bottom = originalRect.bottom / mImageScale
            originalRect.left = originalRect.left / mImageScale
            originalRect.right = originalRect.right / mImageScale
            translateRect()
        }

        var translateRect = Rect()

        /**
         * スケーリングと変位の後、キャンバスの初期座標を実際の座標に変換します
         */
        var rectOnScreen = Rect()
            get() = field.apply {
                left =
                        (measuredWidth / 2 - (measuredWidth / 2 - translateRect.left) * mCurrentScale + mOffsetX).toInt()
                right =
                        (measuredWidth / 2 - (measuredWidth / 2 - translateRect.right) * mCurrentScale + mOffsetX).toInt()
                top =
                        (measuredHeight / 2 - (measuredHeight / 2 - translateRect.top) * mCurrentScale + mOffsetY).toInt()
                bottom =
                        (measuredHeight / 2 - (measuredHeight / 2 - translateRect.bottom) * mCurrentScale + mOffsetY).toInt()
            }

        fun translateRect() {
            /**
             * 生データをキャンバス座標に変換します
             */
            translateRect.apply {
                left = originalRect.left + (measuredWidth - mImageWidth) / 2 - mRectPadding
                right = originalRect.right + (measuredWidth - mImageWidth) / 2 + mRectPadding
                top = originalRect.top + (measuredHeight - mImageHeight) / 2 - mRectPadding
                bottom = originalRect.bottom + (measuredHeight - mImageHeight) / 2 + mRectPadding
            }
        }
    }
}