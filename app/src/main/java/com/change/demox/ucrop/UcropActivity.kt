package com.change.demox.ucrop

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.change.demox.R
import com.change.demox.utils.FileUtils
import kotlinx.android.synthetic.main.activity_ucrop.*

class UcropActivity : AppCompatActivity() {

    //最后生成图片的filePath
    var filePath = ""
    /**
     * ContentType
     */
    private var contentType = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ucrop)
        initView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        FileUtils.activityResult(requestCode, resultCode, data, this@UcropActivity, {
            filePath = FileUtils.getIconCacheFile(this@UcropActivity).toString()
            //成功执行完流程以后，将contentType的类型设置成任意默认类型
            contentType = "image/png"
            displayGroupIcon()
        }, {
        })
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        if (requestCode == FileUtils.pickRequestCode) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                FileUtils.pickFromGallery(this@UcropActivity)
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun initView() {
        toolbar.setNavigationIcon(R.drawable.ic_menu_back)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        imageview_icon.setOnClickListener {
            FileUtils.pickFromGallery(this@UcropActivity)
        }
    }

    /**
     * 显示icon
     *
     */
    private fun displayGroupIcon() {
        val options = RequestOptions.bitmapTransform(CircleCrop())
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.drawable.ic_icon_placeholder)
                .fallback(R.drawable.ic_icon_placeholder)

        Glide.with(this@UcropActivity)
                .load(filePath)
                .apply(options)
                .into(imageview_icon)
    }
}