package com.change.demox.views.imageview.viewTag.widget

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.OverScroller
import androidx.core.math.MathUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.change.demox.R
import kotlin.math.max
import kotlin.math.min

/**
 * 首先，在Android系统中，每一次手势交互都会依照以下顺序执行。

1. 接触接触屏一刹那，触发一个MotionEvent事件。

2. 该事件被OnTouchListener监听，在其onTouchEvent()方法里获得该MotionEvent对象。

3. 通过GestureDetector（手势识别器）转发次MotionEvent对象至OnGestureListener。

4. OnGestureListener获得该对象，听根据该对象封装的的信息，做出合适的反馈。

MotionEvent: 这个类用于封装手势、触摸笔、轨迹球等等的动作事件。其内部封装了两个重要的属性X和Y，这两个属性分别用于记录横轴和纵轴的坐标。

GestureDetector: 识别各种手势。

OnGestureListener: 这是一个手势交互的监听接口，其中提供了多个抽象方法，并根据GestureDetector的手势识别结果调用相对应的方法。
 *
 */

class ScalableImageView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    var onDeviceClicked: (id: Int, rect: Rect) -> Unit = { _, _ -> }

    private var mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private lateinit var mImage: Bitmap
    private var mImageWidth = 0
    private var mImageHeight = 0
    private var mMinScale = 0f
    private var mMaxScale = 0f

    private var mBeginScale = 1f
    private var mCurrentScale = mMinScale
        get() = field
        set(value) {
            mPreScale = field
            field = value
            offsetWhenScale(mFocusX, mFocusY, mPreScale, mCurrentScale)
            invalidate()
        }
    private var mPreScale = mCurrentScale
    private var mOffsetX = 0f
    private var mOffsetY = 0f
    private var mFocusX = 0f
    private var mFocusY = 0f

    private var mRectPadding = 3.toPx()

    /**
     *    当View或者ViewGroup中的内容超过一个屏幕时，我们必须要通过滑动的方式使得用户可以查看那些超过屏幕的内容，如果
    直接调用ScrollTo（）或者ScrollBy（）的方式来移动的话会让用户觉得太突然而且效果不好看，因为它们都是瞬间移动的，这时候就可以使用Scroller来实现平滑移动。
    Scroller的本质及作用：
    Scroller本质就是一个Helper类，里面保存了目标对象要移动的距离，时间等属性！

    Scroller实现View的平滑移动

     */
    private var mScroller = OverScroller(context)
    private lateinit var mScaleAnimator: ObjectAnimator
    private lateinit var mGestureDetector: GestureDetector
    private lateinit var mScaleDetector: ScaleGestureDetector

    //data
    private var mIsHighQuilty = false
    private var mImageUrl: String = ""
    private var mOriginalData: List<DeviceModel> = listOf()
    private var mDataList: ArrayList<LocaleModel> = arrayListOf()

    private var mSelectedData: LocaleModel? = null
    private var mHighLightData: List<LocaleModel> = listOf()

    init {
        //初始化工具类
        initPaint()
        //初始化缩放感知器
        initScaleAnimator()
        //初始化手势感知器
        initGestureDetector()
    }

    private fun Int.toPx(): Int {
        return (Resources.getSystem().displayMetrics.scaledDensity * this).toInt()
    }

    /**
     * 设置初始数据
     * @param url 图片url
     * @param data 数据源
     * @param isHighQuality true：高质量  false：低质量
     */
    fun setDataSource(url: String, data: List<DeviceModel>, isHighQuality: Boolean) {
        mIsHighQuilty = isHighQuality
        mImageUrl = url
        mOriginalData = data
        if (measuredWidth == 0) return
        initView()
    }

    /**
     * @param ids 需要高亮显示的零件id
     */
    fun setHighLightDataIds(ids: ArrayList<Int>) {
        mHighLightData = mDataList.filter {
            ids.contains(it.partId)
        }
        invalidate()
    }

    /**
     * 在postInvalidateOnAnimation()后被回调，处理移动过程（fling）的动作
     *
     * 主要功能是计算移动的位移量
     */
    override fun computeScroll() {
        //这方法用于判断移动过程（fling）是否完成
        if (mScroller.computeScrollOffset()) {
            mOffsetX = mScroller.currX.toFloat()
            mOffsetY = mScroller.currY.toFloat()
            //重绘，必须调用该方法，否则不一定能看到移动效果
            postInvalidateOnAnimation()
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        //当前是否有初始数据；如果没有数据，onDraw什么都不需要做
        if (mMinScale == 0f) return
        canvas?.apply {
            //修正canvas的offset值，限制其在一定范围内
            fixOffset()
            //canvas做适当的偏移，以做正常的显示
            translate(mOffsetX, mOffsetY)
            //放缩
            scale(mCurrentScale, mCurrentScale, width / 2f, height / 2f)
            //画图，保证图片初始在画面中心显示
            drawBitmap(
                    mImage,
                    (measuredWidth - mImageWidth) / 2f,
                    (measuredHeight - mImageHeight) / 2f,
                    mPaint
            )
            //draw highlight rects
            mPaint.color = Color.YELLOW
            mHighLightData.forEach {
                drawRect(it.translateRect, mPaint)
            }
            //draw selected rect
            mPaint.color = Color.RED
            mSelectedData?.let {
                drawRect(it.translateRect, mPaint)
            }
        }
    }

    /**
     * 在其onTouchEvent()方法里获得该MotionEvent对象，交给GestureDetector
     *
     * @param event
     * @return
     */
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        //事件先交给mScaleDetector，如果返回false、说明当前不是双指操作，然后把事件交给mGestureDetector处理
        var res = mScaleDetector.onTouchEvent(event)
        if (!mScaleDetector.isInProgress) {
            res = mGestureDetector.onTouchEvent(event)
        }
        return res
    }

    /**
     * 初始化和屏幕旋转时的回调方法
     */
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        initView()
        super.onSizeChanged(w, h, oldw, oldh)
    }

    /**
     * 初始化数据和图片
     */
    private fun initView() {
        //把API数据转换成本地数据，后续使用更方便
        mDataList.clear()
        mOriginalData.forEach { bean ->
            bean.illustrations?.forEach { model ->
                model.positions?.forEach {
                    mDataList.add(LocaleModel(bean.part_id, it))
                }
            }
        }
        //加载图片。 此处是加载的本地图片，后续可以改成url
        Glide.with(context).asBitmap().load(R.mipmap.image_l)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onLoadCleared(placeholder: Drawable?) {
                    }

                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        //获取原始图片分辨率的size，根据Bitmap分辨率宽高确定最小和最大缩放值
                        mImage = resource
                        mImageWidth = mImage.width
                        mImageHeight = mImage.height
                        val heightScale = measuredHeight / mImageHeight.toFloat()
                        val widthScale = measuredWidth / mImageWidth.toFloat()
                        if (heightScale < widthScale) {
                            mMinScale = heightScale  //最小倍数自定义设置成0.75倍
                            mMaxScale = width / mImageWidth.toFloat() * 2
                        } else {
                            mMinScale = widthScale
                            mMaxScale = height / mImageHeight.toFloat() * 2
                        }
                        mCurrentScale = mMinScale
                        mDataList.forEach {
                            it.translateRect()
                        }
                        //imageView高亮显示的标记：
                        mHighLightData.forEach {
                            it.translateRect()
                        }
                        mSelectedData?.translateRect()
                        invalidate()
                    }
                })
    }

    private fun initPaint() {
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = 2.toPx().toFloat()
    }

    /**
     *   手势感知初始化，识别各种手势
     */
    private fun initGestureDetector() {
        mGestureDetector =
                GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {

                    override fun onDown(e: MotionEvent?): Boolean {
                        return true
                    }

                    /**
                     * 单击
                     */
                    override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
                        e?.let { event ->
                            //如果点击的x,y坐标在数据源集中，那么查出来
                            mSelectedData = mDataList.find {
                                it.rectOnScreen.contains(event.x.toInt(), event.y.toInt())
                            }
                            mSelectedData?.let {
                                onDeviceClicked(it.partId, it.originalRect)
                            }
                            //重绘画面，为了在界面画出mSelectedData，画出点击的标记
                            invalidate()
                        }

                        return super.onSingleTapConfirmed(e)
                    }

                    /**
                     * 双击
                     */
                    override fun onDoubleTap(e: MotionEvent?): Boolean {
                        e?.let {
                            mFocusX = it.x
                            mFocusY = it.y
                        }

                        //设置缩放比
                        mScaleAnimator.setFloatValues(
                                mCurrentScale,
                                if ((mCurrentScale - mMinScale) < (mMaxScale - mMinScale) / 2) mMaxScale else mMinScale
                        )
                        mScaleAnimator.start()
                        return super.onDoubleTap(e)
                    }

                    /**
                     * onScroll:  手指在触摸屏上滑动（滑动的时候手指会一直在屏幕上）
                     *
                     */
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

                    /**
                     * onFling: 手指在触摸屏上迅速移动，并迅速松开的动作，ps:抹一下
                     *
                     */
                    override fun onFling(
                            e1: MotionEvent?,
                            e2: MotionEvent?,
                            velocityX: Float,
                            velocityY: Float
                    ): Boolean {
                        //抹一下操作的时候，保持平滑移动
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

        //缩放
        mScaleDetector = ScaleGestureDetector(
                context,
                object : ScaleGestureDetector.SimpleOnScaleGestureListener() {

                    override fun onScaleBegin(detector: ScaleGestureDetector?): Boolean {
                        mBeginScale = mCurrentScale
                        return super.onScaleBegin(detector)
                    }

                    override fun onScale(detector: ScaleGestureDetector?): Boolean {
                        detector?.let {
                            mFocusX = it.focusX
                            mFocusY = it.focusY
                            //确定当前的缩放比例
                            mCurrentScale =
                                    MathUtils.clamp(mBeginScale * it.scaleFactor, mMinScale, mMaxScale)
                        }

                        invalidate()
                        return super.onScale(detector)
                    }

                    override fun onScaleEnd(detector: ScaleGestureDetector?) {
                        super.onScaleEnd(detector)
                    }
                })
    }

    /**
     * 初始化缩放感知器
     *
     */
    private fun initScaleAnimator() {
        mScaleAnimator = ObjectAnimator.ofFloat(this, "mCurrentScale", 0f)
        mScaleAnimator.interpolator = LinearInterpolator()
        mScaleAnimator.duration = 300
    }

    /**
     * 根据缩放的值，计算当前canvas需要的偏移值
     */
    private fun offsetWhenScale(x: Float, y: Float, currentScale: Float, targetScale: Float) {
        mOffsetX += (x - width / 2 - mOffsetX) - (x - width / 2 - mOffsetX) / currentScale * targetScale
        mOffsetY += (y - height / 2 - mOffsetY) - (y - height / 2 - mOffsetY) / currentScale * targetScale
    }

    /**
     * 把canvas的offset限定在范围之内
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
            val positions: DeviceModel.IllustrationsBean.PositionsBean
    ) {
        val originalRect = Rect(
                (positions.start_x * if (mIsHighQuilty) 1.0 else 0.4).toInt(),
                (positions.start_y * if (mIsHighQuilty) 1.0 else 0.4).toInt(),
                (positions.end_x * if (mIsHighQuilty) 1.0 else 0.4).toInt(),
                (positions.end_y * if (mIsHighQuilty) 1.0 else 0.4).toInt()
        )
        var translateRect = Rect()

        /**
         * 把canvas的初始坐标，转换成缩放和位移后的真实坐标
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

        /**
         * 把原始数据，根据位图大小转换成canvas上的坐标
         */
        fun translateRect() {
            translateRect.apply {
                left = originalRect.left + (measuredWidth - mImageWidth) / 2 - mRectPadding
                right = originalRect.right + (measuredWidth - mImageWidth) / 2 + mRectPadding
                top = originalRect.top + (measuredHeight - mImageHeight) / 2 - mRectPadding
                bottom = originalRect.bottom + (measuredHeight - mImageHeight) / 2 + mRectPadding
            }
        }
    }
}