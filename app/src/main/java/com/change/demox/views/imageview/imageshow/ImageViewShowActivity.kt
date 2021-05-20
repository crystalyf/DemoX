package com.change.demox.views.imageview.imageshow

import android.os.Bundle
import com.change.base.BaseActivity
import com.change.demox.R
import com.change.demox.utils.GlideUtils
import kotlinx.android.synthetic.main.activity_imageview_show.*


class ImageViewShowActivity : BaseActivity() {

    var imageUrl = "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fyouimg1.c-ctrip.com%2Ftarget%2Ftg%2F035%2F063%2F726%2F3ea4031f045945e1843ae5156749d64c.jpg&refer=http%3A%2F%2Fyouimg1.c-ctrip.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1624081121&t=b6e4e6dac6e01dd33e577ce2d5a1aba6"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_imageview_show)
        initView()
    }

    private fun initView() {
        //加转圈等待的图片加载：
        GlideUtils.loadWithAutoIndicator(img_show_with_progress, imageUrl)
        //居中裁剪模式 + Progress 显示图片：
        GlideUtils.loadWithCenterCropTransformAutoIndicator(img_center_crop_show_with_progress, imageUrl)
    }
}