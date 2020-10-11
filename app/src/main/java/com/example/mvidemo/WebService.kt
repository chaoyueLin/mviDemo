package com.example.mvidemo

import android.content.Context
import com.example.mvidemo.login.UserInfoRepository
import com.github.qingmei2.sample.http.interceptor.BasicAuthInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * 创建人：linchaoyue
 * 创建时间：20/9/19 上午11:56
 * 描述：
 */
object WebService {
    const val TIME_OUT_SECONDS = 10
    const val BASE_URL = "https://api.github.com/"


    fun service(): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(provideOkHttpClient(UserInfoRepository))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())//解析方法
        .build()

    fun provideOkHttpClient(userInfoRepository: UserInfoRepository): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(
                TIME_OUT_SECONDS.toLong(),
                TimeUnit.SECONDS
            )
            .readTimeout(
                TIME_OUT_SECONDS.toLong(),
                TimeUnit.SECONDS
            )
            .addInterceptor(BasicAuthInterceptor(userInfoRepository))
            .build()
    }
}