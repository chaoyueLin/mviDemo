package com.example.mvidemo.login

import com.example.mvidemo.base.IIntent

/**
 * 创建人：linchaoyue
 * 创建时间：20/9/19 上午11:18
 * 描述：
 */
sealed class LoginIntent : IIntent {

    object InitialIntent : LoginIntent()

    data class LoginClicksIntent(
        val username: String?,
        val password: String?
    ) : LoginIntent()
}