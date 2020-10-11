package com.example.mvidemo.login

import com.example.mvidemo.base.IViewState
import com.example.mvidemo.login.entry.UserInfo

/**
 * 创建人：linchaoyue
 * 创建时间：20/9/19 上午11:18
 * 描述：
 */
data class LoginViewState(
    val isLoading: Boolean,
    val errors: Throwable?,
    val uiEvents: LoginUiEvents?
) : IViewState {

    sealed class LoginUiEvents {

        data class JumpMain(val loginUser: UserInfo) : LoginUiEvents()

        data class TryAutoLogin(
            val username: String,
            val password: String,
            val autoLogin: Boolean
        ) : LoginUiEvents()
    }

    companion object {

        fun idle(): LoginViewState = LoginViewState(
            isLoading = false,
            errors = null,
            uiEvents = null
        )
    }
}