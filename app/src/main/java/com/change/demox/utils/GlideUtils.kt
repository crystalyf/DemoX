/*
 * GlideUtils.kt
 *
 * Created by xingjunchao on 2019/12/11.
 * Copyright © 2019年 Eole. All rights reserved.
 */

package com.change.demox.utils

import android.app.Application
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.change.demox.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File


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

        /**
         * 带转圈gif加载
         */
        fun loadWithAutoIndicator(
                view: ImageView,
                url: String?,
                scaleType: ImageView.ScaleType = ImageView.ScaleType.CENTER_CROP
        ) {
            view.scaleType =ImageView.ScaleType.CENTER_INSIDE
            Glide.with(view.context)
                    .load(url)
                    .placeholder(R.drawable.ic_progress_glide_rotate_24)
                    .error(R.drawable.ic_progress_glide_rotate_24)
                    .fallback(R.drawable.ic_progress_glide_rotate_24)
                    .listener(
                            object : RequestListener<Drawable> {
                                override fun onLoadFailed(
                                        e: GlideException?,
                                        model: Any?,
                                        target: Target<Drawable>?,
                                        isFirstResource: Boolean
                                ): Boolean {
                                    return false
                                }

                                override fun onResourceReady(
                                        resource: Drawable?,
                                        model: Any?,
                                        target: Target<Drawable>?,
                                        dataSource: DataSource?,
                                        isFirstResource: Boolean
                                ): Boolean {
                                    view.scaleType = scaleType
                                    return false
                                }

                            }
                    )
                    .into(view)
        }

        /**
         * 居中裁剪模式 + Progress 显示图片
         */
        fun loadWithCenterCropTransformAutoIndicator(
                view: ImageView,
                url: String?,
        ) {
            val options: RequestOptions = RequestOptions.centerCropTransform()
                    .placeholder(R.drawable.ic_progress_glide_rotate_24) //before image load
                    .fallback(R.drawable.ic_menu_back) //url is empty
            Glide.with(view.context)
                    .load(url)
                    .apply(options)
                    .into(view)
        }

        /**
         * 从HttpUrl获取图片文件
         */
        suspend fun getFileFromHttpUrl(url: String?, context: Application): File {
            var result : File
            withContext(Dispatchers.IO) {
                result = Glide.with(context).asFile().load(url).submit().get()
            }
            return result
        }
    }
}
