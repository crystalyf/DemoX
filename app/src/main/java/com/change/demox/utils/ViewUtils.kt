package com.change.demox.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.R.color
import android.graphics.Paint
import com.change.demox.R
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.HandlerThread
import android.view.PixelCopy
import android.view.SurfaceView
import android.view.ViewGroup
import io.reactivex.Single


object ViewUtils {

    /**
     * 大点密码风格的TransformationMethod
     */
    object BiggerDotPasswordTransformationMethod : PasswordTransformationMethod() {

        override fun getTransformation(source: CharSequence, view: View): CharSequence {
            return PasswordCharSequence(super.getTransformation(source, view))
        }

        private class PasswordCharSequence(
                val transformation: CharSequence
        ) : CharSequence by transformation {
            override fun get(index: Int): Char = if (transformation[index] == DOT) {
                BIGGER_DOT
            } else {
                transformation[index]
            }
        }

        private const val DOT = '\u2022'
        private const val BIGGER_DOT = '●'
    }

    /**
     * dip to px
     *
     * @param context
     * @param dpValue
     * @return
     */
    fun dip2px(context: Context?, dpValue: Float): Float {
        if (context == null) return 0f
        val scale = context.resources.displayMetrics.density
        return dpValue * scale + 0.5f
    }

    /**
     * inflate动态加载出来的布局，转成bitmap
     */
    fun saveBitmapFromLayout2(v: View, width: Int, height: Int): Bitmap? {
        //测量使得view指定大小
        val measuredWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY)
        val measuredHeight = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)
        v.measure(measuredWidth, measuredHeight)
        //调用layout方法布局后，可以得到view的尺寸大小
        v.layout(0, 0, v.measuredWidth, v.measuredHeight)
        val bmp = Bitmap.createBitmap(v.width, v.height, Bitmap.Config.RGB_565)
        val c = Canvas(bmp)
        // Draw background
        val bgDrawable = v.background
        if (bgDrawable != null) bgDrawable.draw(c) else c.drawColor(Color.WHITE)
        v.draw(c)
        return bmp
    }

    /**
     *  遍历画面所有子view，找出其中的所有SurfaceView
     */
    var allSurfaceViewChildren = mutableListOf<View>()
     fun getAllChildSurfaceView(view: View?) {
        if (view is ViewGroup) {
            val vp = view
            for (i in 0 until vp.childCount) {
                val viewChild = vp.getChildAt(i)
                if (viewChild is SurfaceView) {
                    allSurfaceViewChildren.add(viewChild)
                }
                //再次调用本身（递归）
                getAllChildSurfaceView(viewChild)
            }
        }
    }

    /**
     * SurfaceView转Bitmap
     */
    private fun getBitmapFromSurfaceView(surfaceView: SurfaceView) = Single.create<Bitmap> {
        val bitmap = Bitmap.createBitmap(surfaceView.width, surfaceView.height, Bitmap.Config.ARGB_8888)
        val listener = PixelCopy.OnPixelCopyFinishedListener { copyResult ->
            when (copyResult) {
                PixelCopy.SUCCESS -> {
                    // onSuccessCallback(bitmap)
                }
                else -> {
                    // onErrorCallback()
                }
            }
        }
        PixelCopy.request(surfaceView, bitmap, listener, surfaceView.handler)
    }

    /**
     * 用Pixel类 copy SurfaceView to Bitmap
     */
    fun usePixelCopy(videoView: SurfaceView, callback: (Bitmap?) -> Unit) {
        val bitmap: Bitmap = Bitmap.createBitmap(
            videoView.width,
            videoView.height,
            Bitmap.Config.ARGB_8888
        );
        try {
            // Create a handler thread to offload the processing of the image.
            val handlerThread = HandlerThread("PixelCopier");
            handlerThread.start();
            PixelCopy.request(
                videoView, bitmap,
                PixelCopy.OnPixelCopyFinishedListener { copyResult ->
                    if (copyResult == PixelCopy.SUCCESS) {
                        callback(bitmap)
                    }
                    handlerThread.quitSafely();
                },
                Handler(handlerThread.looper)
            )
        } catch (e: IllegalArgumentException) {
            callback(null)
            // PixelCopy may throw IllegalArgumentException, make sure to handle it
            e.printStackTrace()
        }
    }

    /**
     * view转bitmap
     */
    fun getBitmapFromView(view: View, size: Int): Bitmap {
        val measuredWidth = View.MeasureSpec.makeMeasureSpec(size, View.MeasureSpec.EXACTLY)
        val measuredHeight = View.MeasureSpec.makeMeasureSpec(size, View.MeasureSpec.EXACTLY)
        view.measure(measuredWidth, measuredHeight)
        val w = view.measuredWidth
        val h = view.measuredHeight
        val bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bmp)
        canvas.drawColor(Color.TRANSPARENT) //如果不设置canvas画布为白色，则生成透明
        view.layout(0, 0, w, h)
        view.draw(canvas)
        return bmp
    }

}