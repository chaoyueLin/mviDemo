package com.example.mvidemo.login

import com.example.mvidemo.Errors
import com.example.mvidemo.base.flatMapErrorActionObservable
import com.example.mvidemo.login.entry.UserInfo
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * 创建人：linchaoyue
 * 创建时间：20/10/18 下午4:58
 * 描述：
 */
class LoginActionProcessorHolder {
    private val loginRepository: LoginRepository = LoginRepository()
    private val initialUiActionTransformer =
        ObservableTransformer<LoginIntent.InitialIntent, LoginResult.AutoLoginInfoResult> { actions ->
            actions.flatMap {
                Observable.just(it)
                    .map { event ->
                        LoginResult.AutoLoginInfoResult.NoUserData
                    }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
            }
        }
    private val loginClickActionTransformer =
        ObservableTransformer<LoginIntent.LoginClicksIntent, LoginResult.ClickLoginResult> { actions ->
            actions.flatMap { o ->
                val (username, password) = o
                when (username.isNullOrEmpty() || password.isNullOrEmpty()) {
                    true -> onLoginParamEmptyResult()
                    false -> loginRepository
                        .login(username, password)
                        .toObservable()
                        .flatMap(::onLoginSuccessResult)
                        .onErrorReturn(LoginResult.ClickLoginResult::Failure)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .startWith(LoginResult.ClickLoginResult.InFlight)
                }
            }
        }

    internal val actionProcessor =
        ObservableTransformer<LoginIntent, LoginResult> { actions ->
            actions.publish { shared ->
                Observable.merge(
                    shared.ofType(LoginIntent.InitialIntent::class.java)
                        .compose(initialUiActionTransformer),
                    shared.ofType(LoginIntent.LoginClicksIntent::class.java)
                        .compose(loginClickActionTransformer),
                    shared.filter { all ->
                        all !is LoginIntent.InitialIntent &&
                                all !is LoginIntent.LoginClicksIntent
                    }.flatMapErrorActionObservable()
                )
            }.retry()
        }

    private fun onLoginSuccessResult(loginUser: UserInfo): Observable<LoginResult.ClickLoginResult> =
        Observable.just(LoginResult.ClickLoginResult.Success(loginUser))


    private fun onLoginParamEmptyResult(): Observable<LoginResult.ClickLoginResult> =
        Observable.just(Errors.SimpleMessageError("username or password can't be null!"))
            .map(LoginResult.ClickLoginResult::Failure)
}