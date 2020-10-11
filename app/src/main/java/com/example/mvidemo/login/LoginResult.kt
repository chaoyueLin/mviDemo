package com.example.mvidemo.login

import com.example.mvidemo.login.entry.UserInfo

/**
 * 创建人：linchaoyue
 * 创建时间：20/9/19 下午12:35
 * 描述：
 */


sealed class LoginResult {

    sealed class AutoLoginInfoResult : LoginResult() {
        data class Success(
            val username: String,
            val password: String,
            val autoLogin: Boolean
        ) : AutoLoginInfoResult()

        data class Failure(val error: Throwable) : AutoLoginInfoResult()
        object NoUserData : AutoLoginInfoResult()
        object InFlight : AutoLoginInfoResult()
    }

    sealed class ClickLoginResult : LoginResult() {
        data class Success(val user: UserInfo) : ClickLoginResult()
        data class Failure(val error: Throwable) : ClickLoginResult()
        object InFlight : ClickLoginResult()
    }
}