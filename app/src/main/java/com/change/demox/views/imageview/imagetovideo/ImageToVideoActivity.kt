package com.change.demox.views.imageview.imagetovideo

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.change.base.BaseActivity
import com.change.demox.R
import com.change.demox.utils.EventObserver
import com.change.demox.utils.FileUtils.getOutPutMovieDirectory
import com.change.demox.utils.FileUtils.getOutPutMusicDirectory
import com.change.demox.views.dialog.bottomsheetdialog.BottomSheetDialogViewModel
import com.change.demox.views.imageview.imagetovideo.rxffmpeg.ImageToVideoViewModel
import com.change.demox.views.imageview.imagetovideo.rxffmpeg.MyRxFFmpegSubscriber
import io.microshow.rxffmpeg.RxFFmpegInvoke
import kotlinx.android.synthetic.main.activity_image_to_video.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class ImageToVideoActivity : BaseActivity() {

    private lateinit var viewModel: ImageToVideoViewModel
    lateinit var myRxFFmpegSubscriber : MyRxFFmpegSubscriber
    //是否成功生成临时的合成视频（无声音）
    var hasTempVideo = false

    var tempVideoFilePath :String? = null
    var targetVideoFilePath:String? = null
    //合成视频的mp3路径
    var targetMusicFilePath:String? = null
    lateinit var ffmpegInVoke :RxFFmpegInvoke

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_to_video)
        viewModel = ViewModelProviders.of(this).get(ImageToVideoViewModel::class.java)
        initView()
    }

    private fun initView() {
        myRxFFmpegSubscriber = MyRxFFmpegSubscriber(viewModel)
        viewModel.afterTempVideo.observe(this, EventObserver {
            afterFirstTransfer()
        })
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
     *  ffmpeg -f image2 -r 1 -i 9405054-bfr%1d.jpg -r 1 aa.avi
     *
     */
    private fun runFFmpegRxJava() {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        //无声音版本的图片转视频
        tempVideoFilePath = getOutPutMovieDirectory() + "temp.mp4"
        targetVideoFilePath = getOutPutMovieDirectory() + "Video_$timeStamp.mp4"
        val videoDir = File(com.change.demox.utils.FileUtils.getOutPutMovieDirectory())
        if (!videoDir.exists())
            videoDir.mkdirs()
        if(!com.change.demox.utils.FileUtils.fileIsExist(tempVideoFilePath)){
            File(tempVideoFilePath).createNewFile()
        }
        if(!com.change.demox.utils.FileUtils.fileIsExist(targetVideoFilePath)){
            File(targetVideoFilePath).createNewFile()
        }
        targetMusicFilePath =  getOutPutMusicDirectory() + "audio.mp3"
        val targetPicFilePath = com.change.demox.utils.FileUtils.getOutPutDirectory()  + "image%d.jpg"
        //循环取所有图片转成视频（无声音），好使
        val text = "ffmpeg -y -loop 1 -r 25 -i $targetPicFilePath -vf zoompan=z=1.1:x='if(eq(x,0),100,x-1)':s='960*540' -t 10 -pix_fmt yuv420p $tempVideoFilePath"
        val commands = text.split(" ".toRegex()).toTypedArray()
        //开始执行FFmpeg命令
        ffmpegInVoke = RxFFmpegInvoke.getInstance()
        ffmpegInVoke.runCommandRxJava(commands)
            .subscribe(myRxFFmpegSubscriber)
    }


    override fun onDestroy() {
        super.onDestroy()
        if (myRxFFmpegSubscriber != null) {
            myRxFFmpegSubscriber.dispose()
        }
    }

    /**
     * 执行完毕第一个ffmpeg命令，得到没有声音的合成视频
     */
    fun afterFirstTransfer(){
        if(!hasTempVideo){
            hasTempVideo = true
            //视频 + 音频 去合成新的mp4，好使
            val text = "ffmpeg -i $targetMusicFilePath -i $tempVideoFilePath $targetVideoFilePath"
            val commands = text.split(" ".toRegex()).toTypedArray()
            //开始执行FFmpeg命令
            RxFFmpegInvoke.getInstance().runCommand(commands, null);
        }
    }

}