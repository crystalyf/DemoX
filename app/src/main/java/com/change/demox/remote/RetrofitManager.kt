package com.change.demox.remote

import com.change.demox.BuildConfig
import com.change.demox.application.MyApplication
import com.change.demox.remote.bean.ApiErrorModel
import com.change.demox.utils.SharedPreferences
import com.change.demox.views.recyclerview.paging.delete.bean.BookModel
import com.google.common.collect.Sets
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.io.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

object RetrofitManager : BaseRetrofitManager() {

    /**
     *  Retrofit是在Version 2.6.0开始支持协程的
     */

    override val apiPool: MutableSet<Request> = Sets.newConcurrentHashSet()
    private val mCookieStore: ConcurrentHashMap<String, MutableList<Cookie>> = ConcurrentHashMap()
    var sharedPreferences: SharedPreferences? = null
    const val BUFFER_SIZE = 4096

    /**
     *  创建Retrofit实例
     */
    private var client: OkHttpClient = OkHttpClient.Builder()
            .cookieJar(object : CookieJar {

                override fun loadForRequest(url: HttpUrl): MutableList<Cookie> {
                    val list = mCookieStore[url.host]
                    return list ?: mutableListOf()
                }

                override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
                    mCookieStore[url.host] = cookies as MutableList<Cookie>
                }
            })
            .readTimeout(60L, TimeUnit.SECONDS)
            .connectTimeout(60L, TimeUnit.SECONDS)
            .build()


    /**
     *  [retrofitService] static instance of [RetrofitService]
     */
    private val retrofitService = Retrofit.Builder()
            .baseUrl(BuildConfig.API_HOST)
            .addConverterFactory(GsonConverterFactory.create())   //Gson解析器（转换工厂）
            .client(client)
            .build()
            .create(RetrofitService::class.java)


    /**
     *  [retrofitService] banner画面发API所用
     */
    val retrofitService2= Retrofit.Builder()
            .baseUrl("http://baobab.kaiyanapp.com/api/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) //Gson解析器（Rxjava2转换工厂）
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(RetrofitService::class.java)


    /**
     *
     * @param offset  获取数据的偏移量，用APISTUB其实没用上，API是一个无参数的get请求。
     */
    suspend fun getBooks(
            offset: Int = 0
    ) = suspendCancellableCoroutine<BookModel?> {
        exclusiveRun(
                retrofitService.getBooks(), ApiCallback(it)
        )
    }


    /**
     * 下载PDF文件
     *
     * @param url  pdf的URl
     * @param fileNameTemp 文件名
     */
    suspend fun downloadPDF(url: String, fileNameTemp: String) =
            suspendCancellableCoroutine<String> { continuation ->
                var fileName = fileNameTemp
                //Android的最大文件长度为255个字节，categoryName以外的其他长度为99个字节
                while (fileName.toByteArray().size > (255 - 4)) {
                    fileName = fileName.dropLast(1)
                }
                // 如果缓存有文件，它将返回文件的路径。如：/data/user/0/com.kubota.kpad/cache/1部品表(1).pdf
                val file =
                        File(MyApplication.instance?.cacheDir!!.path, fileName)
                if (file.exists() && file.isFile) {
                    file.delete()
                }
                val call = retrofitService.downLoadPDFByLink(url)

                //enqueue进行异步请求，并且获取了我们期待的数据(实体对象)
                call.enqueue(object : Callback<ResponseBody> {
                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        continuation.resumeWithException(
                                RetrofitApiException(ApiErrorModel.getNetWorkErrorModel(t))
                        )
                    }

                    override fun onResponse(
                            call: Call<ResponseBody>,
                            response: Response<ResponseBody>
                    ) {
                        //获取ResponseBody,内部是PDF文件的内容（contentType：application/pdf）
                        if (response.isSuccessful) {
                            if (response.body()?.contentLength() ?: 0L == 0L) {
                                continuation.resumeWithException(
                                        RetrofitApiException(ApiErrorModel("", ApiConfigRetrofit.NETWORK_ERROR, "", "", "", arrayListOf()))
                                )
                                return
                            }
                            GlobalScope.launch(Dispatchers.IO) {
                                var filePath = ""
                                //写入到缓存
                                response.body()?.byteStream()?.let { inputStream ->
                                    // create file dir
                                    if (file.parentFile != null)
                                        if (!file.parentFile!!.exists())
                                            file.parentFile!!.mkdir()
                                    try {
                                        file.createNewFile()
                                    } catch (e: IOException) {
                                        e.printStackTrace()
                                        continuation.resumeWithException(
                                                RetrofitApiException(ApiErrorModel("", "", "", "", "", arrayListOf(), e))
                                        )
                                        return@launch
                                    }
                                    var os: OutputStream? = null
                                    var currentLength: Long = 0
                                    try {
                                        os = BufferedOutputStream(FileOutputStream(file))
                                        val data = ByteArray(BUFFER_SIZE)
                                        var len: Int
                                        while (inputStream.read(data, 0, BUFFER_SIZE).also { len = it } != -1
                                        ) {
                                            os.write(data, 0, len)
                                            currentLength += len.toLong()
                                        }

                                        Timber.d("pdf download finish${file.absolutePath}")
                                        filePath = file.absolutePath
                                    } catch (e: IOException) {
                                        e.printStackTrace()
                                        continuation.resumeWithException(
                                                RetrofitApiException(ApiErrorModel("", "", "", "", "", arrayListOf(), e))
                                        )
                                        Timber.d("pdf download onFail$e")
                                        return@launch
                                    } finally {
                                        try {
                                            inputStream.close()
                                        } catch (e: IOException) {
                                            e.printStackTrace()
                                            continuation.resumeWithException(
                                                    RetrofitApiException(ApiErrorModel("", "", "", "", "", arrayListOf(), e))
                                            )
                                            return@launch
                                        }
                                        try {
                                            os?.close()
                                        } catch (e: IOException) {
                                            e.printStackTrace()
                                            continuation.resumeWithException(
                                                    RetrofitApiException(ApiErrorModel("", "", "", "", "", arrayListOf(), e))
                                            )
                                            return@launch
                                        }
                                    }
                                }
                                continuation.resume(filePath)
                            }
                        } else {
                            val jsonString = response.errorBody()?.string()
                            val model = try {
                                Gson().fromJson(jsonString, ApiErrorModel::class.java)
                            } catch (e: Exception) {
                                ApiErrorModel("", "", "", "", "", arrayListOf(), e)
                            }
                            continuation.resumeWithException(RetrofitApiException(model))
                        }
                    }
                })
            }
}