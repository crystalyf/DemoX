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

}