package com.change.demox.remote

import com.change.demox.views.recyclerview.paging.delete.bean.BookModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface RetrofitService {

    /**
     * Retrofit使用接口，方法和参数，使用注解表明了请求将如何处理，每一种方法都必须有一个HTTP标注提供请求的方法和相对URL,
     * 有五种内置注解：GET, POST, PUT, DELETE, 和 HEAD，在注解中指定URL。请选择以下方式中合适的请求方式来处理您的请求
     */

    /**
     *
     *  文件下载我们需要使用@Url和 @Streaming ：
     *  @Url动态Url正好非常适合我们的场景，而使用@Streaming注解可以让我们下载非常大的文件时，避免Retrofit将整个文件读进内存，否则可能造成OOM现象
     */
    @Streaming
    @GET
    fun downLoadPDFByLink(
            @Url fileUrl: String
    ): Call<ResponseBody>


    /**
     * 获取Books的数据列表
     *
     * @return
     */
    @GET(ApiConfigRetrofit.PATH_BOOKS)
    fun getBooks(
    ): Call<BookModel>


}