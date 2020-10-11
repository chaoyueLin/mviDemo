package com.example.mvidemo.login.entry

import com.example.mvidemo.BuildConfig
import com.google.gson.annotations.SerializedName

/**
 * 创建人：linchaoyue
 * 创建时间：20/9/19 下午12:15
 * 描述：
 */
data class LoginRequestModel(
    val scopes: List<String>,
    val note: String,
    @SerializedName("client_id")
    val clientId: String,
    @SerializedName("client_secret")
    val clientSecret: String
) {
    companion object {

        fun generate(): LoginRequestModel {
            return LoginRequestModel(
                scopes = listOf("user", "repo", "gist", "notifications"),
                note = BuildConfig.APPLICATION_ID,
                clientId = BuildConfig.CLIENT_ID,
                clientSecret = BuildConfig.CLIENT_SECRET
            )
        }
    }
}