package com.example.mvidemo.login.entry

/**
 * 创建人：linchaoyue
 * 创建时间：20/9/19 下午12:13
 * 描述：
 */
import com.google.gson.annotations.SerializedName

data class UserAccessToken(
    @SerializedName("id")
    val id: Int,
    @SerializedName("token")
    val token: String,
    @SerializedName("url")
    val url: String
)