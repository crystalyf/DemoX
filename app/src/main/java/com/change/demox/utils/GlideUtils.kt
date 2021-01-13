/*
 * GlideUtils.kt
 *
 * Created by xingjunchao on 2019/12/11.
 * Copyright © 2019年 Eole. All rights reserved.
 */

package com.change.demox.utils

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.change.demox.R
import java.io.ByteArrayOutputStream


/**
 * Glide Util
 */
class GlideUtils {
    companion object {

        /**
         * 加载图片，数据绑定自动调用
         *
         * @param view imageview
         * @param url 画像的URL
         */
        @BindingAdapter("app:singleImage")
        @JvmStatic
        fun loadSingleImage(view: ImageView, url: String) {
            val options = RequestOptions.bitmapTransform(CircleCrop())
                    .placeholder(R.drawable.ic_topic_creator_placeholder)
                    .fallback(R.drawable.ic_topic_creator_placeholder)
            Glide.with(view.context)
                    .load(url)
                    .apply(options)
                    .into(view)
        }

        /**
         * Bitmap画像加载
         *
         * @param view imageview
         * @param bitmap Bitmap画像
         */
        fun loadBitmap(view: ImageView, bitmap: Bitmap) {
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            val bytes = outputStream.toByteArray()
            Glide.with(view.context)
                    .load(bytes)
                    .into(view)
        }
    }
}
