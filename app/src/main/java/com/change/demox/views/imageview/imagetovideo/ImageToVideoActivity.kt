package com.change.demox.views.imageview.imagetovideo

import android.os.Bundle
import android.os.FileUtils
import com.change.base.BaseActivity
import com.change.demox.R
import com.change.demox.utils.FileUtils.getOutPutMovieDirectory
import com.change.demox.utils.FileUtils.getOutPutMusicDirectory
import com.change.demox.views.imageview.imagetovideo.rxffmpeg.MyRxFFmpegSubscriber
import io.microshow.rxffmpeg.RxFFmpegInvoke
import kotlinx.android.synthetic.main.activity_image_to_video.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class ImageToVideoActivity : BaseActivity() {

    var myRxFFmpegSubscriber = MyRxFFmpegSubscriber()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_to_video)
        initView()
    }

    private fun initView() {
        btn_pic_to_video.setOnClickListener {
            image2Video()
        }
    }

    private fun image2Video() {
        runFFmpegRxJava()
    }

    /**
     *
     * 执行FFmpeg命令:
     * 将所有的图片文件放到一个临时目录，并且给图片名制定一个命名规则（可正则的）,输入图片文件，图片文件保存为 image0001.jpg image0002.jpg ….
     *
     *  图片转视频 ： ffmpeg -f image2 -i /home/test/images/image%d.jpg  -vcodec libx264  test.h264
     *
     */
    private fun runFFmpegRxJava() {
        myRxFFmpegSubscriber = MyRxFFmpegSubscriber()
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                val targetVideoFilePath =
           getOutPutMovieDirectory() + "Video_$timeStamp.mp4"
        val videoDir = File(com.change.demox.utils.FileUtils.getOutPutMovieDirectory())
        if (!videoDir.exists())
            videoDir.mkdirs()
        if(!com.change.demox.utils.FileUtils.fileIsExist(targetVideoFilePath)){
            File(targetVideoFilePath).createNewFile()
        }
        val targetMusicFilePath =  getOutPutMusicDirectory()+ File.separator + "audio.mp3"
        val targetPicFilePath = com.change.demox.utils.FileUtils.getOutPutDirectory()  + "image%04d.jpg"
        val text = "ffmpeg -y -r 10 -i $targetPicFilePath -pix_fmt yuv420p -i $targetMusicFilePath -absf aac_adtstoasc $targetVideoFilePath"
        val commands = text.split(" ".toRegex()).toTypedArray()
        //开始执行FFmpeg命令
        RxFFmpegInvoke.getInstance()
            .runCommandRxJava(commands)
            .subscribe(myRxFFmpegSubscriber)
    }


    override fun onDestroy() {
        super.onDestroy()
        if (myRxFFmpegSubscriber != null) {
            myRxFFmpegSubscriber.dispose()
        }
    }

}