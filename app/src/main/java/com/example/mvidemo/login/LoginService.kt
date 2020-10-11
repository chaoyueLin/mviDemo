package com.example.mvidemo.login

import com.example.mvidemo.login.entry.LoginRequestModel
import com.example.mvidemo.login.entry.UserAccessToken
import com.example.mvidemo.login.entry.UserInfo
import io.reactivex.Flowable
import retrofit2.http.*

/**
 * 创建人：linchaoyue
 * 创建时间：20/9/19 上午11:58
 * 描述：
 */
interface LoginService {

    @POST("authorizations")
    @Headers("Accept: application/json")
    fun authorizations(
        @Body authRequestModel: LoginRequestModel
    ): Flowable<UserAccessToken>


    @GET("user")
    fun fetchUserOwner(): Flowable<UserInfo>

}