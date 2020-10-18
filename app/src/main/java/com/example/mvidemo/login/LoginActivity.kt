package com.example.mvidemo.login

import android.os.Bundle
import android.view.View
import com.example.mvidemo.Errors
import com.example.mvidemo.MainActivity

import com.example.mvidemo.R
import com.example.mvidemo.base.BaseActivity
import com.example.mvidemo.toast
import com.jakewharton.rxbinding3.view.clicks
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_login.*

/**
 * 创建人：linchaoyue
 * 创建时间：20/9/19 上午11:08
 * 描述：
 */
class LoginActivity : BaseActivity<LoginIntent, LoginViewState>() {
    private val loginClicksIntentPublisher =
        PublishSubject.create<LoginIntent.LoginClicksIntent>()

    private var mViewModel: LoginViewModel = LoginViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        bind()
    }

    private fun bind() {
        btnLogin.clicks().map {
            LoginIntent.LoginClicksIntent(et_username.text.toString(), et_password.text.toString())
        }.subscribe(loginClicksIntentPublisher)

        mViewModel.state().subscribe(this::render)
//
        mViewModel.processIntent(intents())
    }

    override fun intents(): Observable<LoginIntent> = Observable.mergeArray<LoginIntent>(
        loginClicksIntentPublisher
    ).startWith(LoginIntent.InitialIntent)

    override fun render(state: LoginViewState) {
        when(state.uiEvents){
            is LoginViewState.LoginUiEvents.JumpMain -> {
                MainActivity.launch(this)
                finish()
                return
            }
        }
        progressBar.visibility=when(state.isLoading){
            true-> View.VISIBLE
            false->View.GONE
        }

        state.errors?.apply {
            when (this) {
                is Errors.SimpleMessageError -> {
                    toast { simpleMessage }
                }
                else -> {
                    toast { localizedMessage }
                }
            }
        }
    }

}