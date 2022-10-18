package com.change.demox.camera.onlycamera


import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.res.Configuration
import android.hardware.display.DisplayManager
import android.os.*
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.change.demox.R
import com.change.demox.camera.CameraBaseFragment
import com.change.demox.camera.part.LuminosityAnalyzer
import com.change.demox.camera.part.ThreadExecutor
import com.change.demox.databinding.FragmentTakePhotoBinding
import com.change.demox.extension.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.properties.Delegates


class TakePhotoFragment : CameraBaseFragment<FragmentTakePhotoBinding>() {

    private lateinit var viewModel: TakePhotoViewModel

    companion object {
        private const val TAG = "takePhotoFragment"

        private const val RATIO_4_3_VALUE = 4.0 / 3.0 // 宽高比 4x3
        private const val RATIO_16_9_VALUE = 16.0 / 9.0 // 宽高比 16x9
    }

    // 屏幕管理类, 用于显示管理器获取显示更改回调
    private val displayManager by lazy { requireContext().getSystemService(Context.DISPLAY_SERVICE) as DisplayManager }

    private var preview: Preview? = null
    private var cameraProvider: ProcessCameraProvider? = null
    private var imageCapture: ImageCapture? = null
    private var imageAnalyzer: ImageAnalysis? = null

    override val binding: FragmentTakePhotoBinding by lazy { FragmentTakePhotoBinding.inflate(layoutInflater) }

    private var displayId = -1

    //选择了哪个摄像头（正面或背面）
    private var lensFacing = CameraSelector.DEFAULT_BACK_CAMERA

    // 选择了哪种闪光灯 (on, off or auto)
    private var flashMode by Delegates.observable(ImageCapture.FLASH_MODE_AUTO) { _, _, new ->
        binding.btnFlash.setImageResource(
                when (new) {
                    ImageCapture.FLASH_MODE_ON -> R.drawable.ic_flash_on
                    ImageCapture.FLASH_MODE_AUTO -> R.drawable.ic_flash_auto
                    else -> R.drawable.ic_flash_off
                }
        )
    }

    /**
     * A display listener for orientation changes that do not trigger a configuration
     * change, for example if we choose to override config change in manifest or for 180-degree
     * orientation changes.
     */
    private val displayListener = object : DisplayManager.DisplayListener {
        override fun onDisplayAdded(displayId: Int) = Unit
        override fun onDisplayRemoved(displayId: Int) = Unit

        @SuppressLint("UnsafeExperimentalUsageError")
        override fun onDisplayChanged(displayId: Int) = view?.let { view ->
            if (displayId == this@TakePhotoFragment.displayId) {
                preview?.targetRotation = view.display.rotation
                imageCapture?.targetRotation = view.display.rotation
                imageAnalyzer?.targetRotation = view.display.rotation
            }
        } ?: Unit
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        viewModel = activity?.viewModels<TakePhotoViewModel> { getViewModelFactory() }?.value!!
        binding.viewModel = TakePhotoViewModel()
        binding.lifecycleOwner = this.viewLifecycleOwner
        initView()
        return binding.root
    }

    private fun initView() {
        flashMode = ImageCapture.FLASH_MODE_AUTO
        initViewsCamera()
        displayManager.registerDisplayListener(displayListener, null)
        binding.run {

            //当PreviewView准备好，attach 或者 detach到window的时候都会回调这个接口
            viewFinder.addOnAttachStateChangeListener(object :
                    View.OnAttachStateChangeListener {
                override fun onViewDetachedFromWindow(v: View) =
                        displayManager.registerDisplayListener(displayListener, null)

                override fun onViewAttachedToWindow(v: View) =
                        displayManager.unregisterDisplayListener(displayListener)
            })

            // click take photo
            btnTakePicture.setOnClickListener {
                takePicture()
            }

            // click album
            btnGallery.setOnClickListener {
                openPreview()
            }

            // switch camera
            btnSwitchCamera.setOnClickListener {
                toggleCamera()
            }

            // open/close flash
            btnFlash.setOnClickListener {
                selectFlash()
            }

            // この写真を使用 click
            btnUseImage.setOnClickListener {
                useImage()
            }

            // 再撮影 click
            btnReTake.setOnClickListener {
                reLoad()
            }

            // click camera
            btnCamera.setOnClickListener {
                showCameraFinderView()
            }

            // click delete button
            btnDelete.setOnClickListener {

            }
        }
    }

    private fun useImage() {

    }


    private fun showCameraFinderView() {
        binding.galleryImage.visibility = View.GONE
        binding.viewFinder.visibility = View.VISIBLE
        binding.btnFlash.visibility = View.VISIBLE
        binding.btnSwitchCamera.visibility = View.VISIBLE
        binding.rlTakePicture.visibility = View.VISIBLE
        binding.rlBtnDelete.visibility = View.GONE
        startCamera()
    }

    fun selectFlash() {
        when (flashMode) {
            ImageCapture.FLASH_MODE_ON -> {
                flashMode = ImageCapture.FLASH_MODE_OFF
                binding.btnFlash.setImageResource(R.drawable.ic_flash_off)
            }
            ImageCapture.FLASH_MODE_OFF -> {
                flashMode = ImageCapture.FLASH_MODE_AUTO
                binding.btnFlash.setImageResource(R.drawable.ic_flash_auto)
            }
            ImageCapture.FLASH_MODE_AUTO -> {
                flashMode = ImageCapture.FLASH_MODE_ON
                binding.btnFlash.setImageResource(R.drawable.ic_flash_on)
            }
        }
        imageCapture?.flashMode = flashMode
    }

    /**
     * Create some initial states
     * */
    private fun initViewsCamera() {
        adjustInsets()
    }

    /**
     * This methods adds all necessary margins to some views based on window insets and screen orientation
     * */
    private fun adjustInsets() {
        binding.btnTakePicture.onWindowInsets { view, windowInsets ->
            if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
                view.bottomMargin =
                        windowInsets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom
            else view.endMargin = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars()).right
        }
    }

    /**
     * Change the facing of camera
     *  toggleButton() function is an Extension function made to animate button rotation
     * */
    @SuppressLint("RestrictedApi")
    fun toggleCamera() = binding.btnSwitchCamera.toggleButton(
            flag = lensFacing == CameraSelector.DEFAULT_BACK_CAMERA,
    ) {
        lensFacing = if (it) {
            CameraSelector.DEFAULT_BACK_CAMERA
        } else {
            CameraSelector.DEFAULT_FRONT_CAMERA
        }

        startCamera()
    }

    /**
     * 点击之后，看照片的具体预览效果
     * */
    fun openPreview() {

    }

    override fun onPermissionGranted() {
        // Each time apps is coming to foreground the need permission check is being processed
        binding.viewFinder.let { vf ->
            vf.post {
                displayId = vf.display.displayId
                startCamera()
            }
        }
    }


    /**
     * Unbinds all the lifecycles from CameraX, then creates new with new parameters
     * */
    private fun startCamera() {
        // PreviewView预览组件
        val viewFinder = binding.viewFinder

        /**
         * ProcessCameraProvider 是一个单例类，可以把相机的生命周期绑定到任何LifecycleOwner类中。AppCompatActivity和Fragment都是LifecycleOwner
         */
        //Future表示一个异步的任务，ListenableFuture可以监听这个任务，当任务完成的时候执行回调
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener(Runnable {
            cameraProvider = cameraProviderFuture.get()
            //DisplayMetrics类取得画面宽高
            val metrics = DisplayMetrics().also { viewFinder.display.getRealMetrics(it) }
            // 输出图像和预览的宽高比
            val aspectRatio = aspectRatio(metrics.widthPixels, metrics.heightPixels)
            // 显示的旋转角度
            val rotation = viewFinder.display.rotation
            val localCameraProvider = cameraProvider
                    ?: throw IllegalStateException("Camera initialization failed.")

            //预览配置：Preview用于相机预览的时候显示预览画面
            preview = Preview.Builder()
                    .setTargetAspectRatio(aspectRatio) //设置宽高比
                    .setTargetRotation(rotation) //设置当前屏幕的旋转
                    .build()
            //照相配置：ImageCapture 用于拍照，并将图片保存
            imageCapture = ImageCapture.Builder()
                    //通过setCaptureMode方法来切换当前的模式是拍照还是录像。ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY：优化捕获速度，可能降低图片质量
                    .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                    .setFlashMode(flashMode)
                    .setTargetAspectRatio(aspectRatio) //设置宽高比
                    .setTargetRotation(rotation) //设置初始的旋转角度
                    .build()

            /**
             * CameraX还提供了图像分析功能，它提供了可供 CPU 访问以执行图像处理、计算机视觉或机器学习推断的图像，可以无缝的访问缓冲区，一般用不到但功能很强大。
             * 创建一个图片分析器然后绑定声明周期即可。
             */

            //image analyzer 的配置
            imageAnalyzer = ImageAnalysis.Builder()
                    .setTargetAspectRatio(aspectRatio) // 设置analyzer aspect 宽高比
                    .setTargetRotation(rotation) // 设置 analyzer 旋转角度
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST) //在我们的analyzer分析中，我们关心的是最新的图像
                    .build()
                    .apply {
                        //使用HandlerThread进行图像分析以防止出现故障
                        val analyzerThread = HandlerThread("LuminosityAnalysis").apply { start() }
                        setAnalyzer(
                                ThreadExecutor(Handler(analyzerThread.looper)),
                                LuminosityAnalyzer()
                        )
                    }
            //重新绑定之前必须先取消绑定
            localCameraProvider.unbindAll()

            try {
                //将 preview 和其他需要的用例绑定到 lifecycle 中
                localCameraProvider.bindToLifecycle(
                        viewLifecycleOwner,
                        lensFacing,
                        preview,
                        imageCapture,
                        imageAnalyzer,
                )
                // 将 previewView的surfaceProvider 设置到 preview 用例中来开始进行相机画面预览
                preview?.setSurfaceProvider(viewFinder.surfaceProvider)
                /**
                 * 至此，预览，照相的配置和绑定到生命周期的工作就完成了，这样当我们进入该页面的时候就可以看到相机的预览效果
                 */

            } catch (e: Exception) {
                Log.e(TAG, "Failed to bind use cases", e)
            }

        }, ContextCompat.getMainExecutor(requireContext()))
    }

    /**
     * 检测当前尺寸最合适的宽高比
     *
     *  @param width - preview width
     *  @param height - preview height
     *  @return suitable aspect ratio
     */
    private fun aspectRatio(width: Int, height: Int): Int {
        val previewRatio = max(width, height).toDouble() / min(width, height)
        if (abs(previewRatio - RATIO_4_3_VALUE) <= abs(previewRatio - RATIO_16_9_VALUE)) {
            return AspectRatio.RATIO_4_3
        }
        return AspectRatio.RATIO_16_9
    }

    @Suppress("NON_EXHAUSTIVE_WHEN")
    fun takePicture() = lifecycleScope.launch(Dispatchers.Main) {
        captureImage()
    }

    private fun captureImage() {
        val localImageCapture = imageCapture
                ?: throw IllegalStateException("Camera 初始化失败.")
        val metadata = ImageCapture.Metadata().apply {
            //使用前置摄像头拍照
            isReversedHorizontal = lensFacing == CameraSelector.DEFAULT_FRONT_CAMERA
        }
        //设置保存参数到ContentValues中, 兼容Android Q和以下版本
        val outputOptions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, System.currentTimeMillis())  //设置文件名
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")  //     //设置文件类型
                put(MediaStore.MediaColumns.RELATIVE_PATH, outputDirectory) //android Q中不再使用DATA字段，而用RELATIVE_PATH代替
            }
            val contentResolver = requireContext().contentResolver
            val contentUri = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            ImageCapture.OutputFileOptions.Builder(contentResolver, contentUri, contentValues)
        } else {
            File(outputDirectory).mkdirs()
            val file = File(outputDirectory, "${System.currentTimeMillis()}.jpg")
            ImageCapture.OutputFileOptions.Builder(file)
        }.setMetadata(metadata).build()

        /**
         * 调用ImageCapture的takePicture方法来拍照
        传入一个文件地址用来保存拍好的照片
        onImageSaved方法是照片已经拍好并存好之后的回调
        onFileSaved方法中将前面保存的文件添加到媒体中，最后跳转到预览界面。*/

        //拍照：
        localImageCapture.takePicture(
                outputOptions,
                requireContext().mainExecutor(), // 线程池, 拍照任务将在其上运行
                object : ImageCapture.OnImageSavedCallback { // 回调，关于捕获拍照过程的结果
                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                        //拍照成功调用
                        outputFileResults.savedUri
                                ?.let { uri ->
                                    //uri总是null,故下一行的代码才是经常走的，通过去媒体库查询找到对应的uri
                                    setLastPictureThumbnail()
                                } ?: setLastPictureThumbnail()
                    }

                    override fun onError(exception: ImageCaptureException) {
                        val msg = "Photo capture failed: ${exception.message}"
                        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
                        Log.e(TAG, msg)
                        exception.printStackTrace()
                    }
                }
        )
    }

    private fun showPictureUrl(uri: String?) {
        Toast.makeText(activity, "拍照成功保存路径：" + uri, Toast.LENGTH_SHORT).show()
        Log.v("ImageUrlStr:", uri!!)
    }

    private fun setLastPictureThumbnail() = binding.btnGallery.post {
        getMedia().firstOrNull() // check if there are any photos or videos in the app directory
                ?.let {
                    showPictureUrl(it.uri)
                }

    }

    private fun reLoad() {
        binding.imagePhoto.visibility = View.GONE
        binding.viewFinder.visibility = View.VISIBLE
        binding.btnFlash.visibility = View.VISIBLE
        binding.btnSwitchCamera.visibility = View.VISIBLE
        binding.type2Layout.visibility = View.GONE
        binding.type1Layout.visibility = View.VISIBLE
        binding.recyclerview.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        startCamera()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        displayManager.unregisterDisplayListener(displayListener)
    }

    override fun onBackPressed() {
        activity?.finish()
    }

}