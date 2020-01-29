package com.zaher.base.network

import com.zaher.base.entities.News
import com.zaher.base.entities.NewsSource
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @author achmad.zaher
 * @date 21-May-19
 */
interface SampleService {
    @GET("top-headlines")
    fun getNewsAsync(
        @Query("apiKey") apiKey: String,
        @Query("country") country: String,
        @Query("pageSize") pageSize: Int,
        @Query("page") page: Int,
        @Query("sources") q: String?
    ): Deferred<Response<News>>


    @GET("top-headlines")
    fun getNewsAsync(
        @Query("apiKey") apiKey: String,
        @Query("pageSize") pageSize: Int,
        @Query("page") page: Int,
        @Query("sources") q: String?
    ): Deferred<Response<News>>


    @GET("sources")
    fun getDataSource(
        @Query("apiKey") apiKey: String
    ): Deferred<Response<NewsSource>>


}
