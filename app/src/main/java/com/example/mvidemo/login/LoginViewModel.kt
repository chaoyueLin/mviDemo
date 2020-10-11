package com.example.mvidemo.login

import com.example.mvidemo.Errors
import com.example.mvidemo.base.BaseViewModel
import com.example.mvidemo.base.flatMapErrorActionObservable
import com.example.mvidemo.login.entry.UserInfo
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

/**
 * 创建人：linchaoyue
 * 创建时间：20/9/19 上午11:19
 * 描述：
 */

class LoginViewModel() : BaseViewModel<LoginIntent, LoginViewState>() {

    private val intentsSubject: PublishSubject<LoginIntent> = PublishSubject.create()
    private val statesObservable: Observable<LoginViewState> = compose()
    private val loginRepository:LoginRepository= LoginRepository()
    override fun processIntent(intents: Observable<LoginIntent>) {
        intents.subscribe(intentsSubject)
    }

    override fun state(): Observable<LoginViewState> = statesObservable

    private fun compose(): Observable<LoginViewState> {
        return intentsSubject
//            .compose(intentFilter)
//            .map(this::actionFromIntent)
            .compose(actionProcessor)
            .scan(LoginViewState.idle(), reducer)
            .switchMap(specialEventProcessor)
            .distinctUntilChanged()
            .replay(1)
            .autoConnect(0)
    }

    private val specialEventProcessor: io.reactivex.functions.Function<LoginViewState, ObservableSource<LoginViewState>>
        get() = io.reactivex.functions.Function { state ->
            when (state.uiEvents != null || state.errors != null) {
                true -> Observable.just(state, state.copy(uiEvents = null, errors = null))
                false -> Observable.just(state)
            }
        }

    private val loginClickActionTransformer =
        ObservableTransformer<LoginIntent.LoginClicksIntent, LoginResult> { actions ->
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

    private fun onLoginSuccessResult(loginUser: UserInfo): Observable<LoginResult.ClickLoginResult> =
        Observable.just(LoginResult.ClickLoginResult.Success(loginUser))


    private fun onLoginParamEmptyResult(): Observable<LoginResult.ClickLoginResult> =
        Observable.just(Errors.SimpleMessageError("username or password can't be null!"))
            .map(LoginResult.ClickLoginResult::Failure)


    internal val actionProcessor =
        ObservableTransformer<LoginIntent, LoginResult> { actions ->
            actions.publish { shared ->
                Observable.merge(
                    shared.ofType(LoginIntent.LoginClicksIntent::class.java).compose(loginClickActionTransformer),
                    shared.filter { all ->
                        all !is LoginIntent &&
                                 all !is LoginIntent.LoginClicksIntent
                    }.flatMapErrorActionObservable()
                )
            }.retry()
        }

    companion object {

        private val reducer = BiFunction { previousState: LoginViewState, result: LoginResult ->
            when (result) {
                is LoginResult.ClickLoginResult -> when (result) {
                    is LoginResult.ClickLoginResult.Success -> {
                        previousState.copy(
                            isLoading = false,
                            errors = null,
                            uiEvents = LoginViewState.LoginUiEvents.JumpMain(result.user)
                        )
                    }
                    is LoginResult.ClickLoginResult.Failure -> previousState.copy(
                        isLoading = false,
                        errors = result.error,
                        uiEvents = null
                    )
                    is LoginResult.ClickLoginResult.InFlight -> previousState.copy(
                        isLoading = true,
                        errors = null,
                        uiEvents = null
                    )
                }
                is LoginResult.AutoLoginInfoResult -> when (result) {
                    is LoginResult.AutoLoginInfoResult.Success -> {
                        previousState.copy(
                            isLoading = true,
                            uiEvents = LoginViewState.LoginUiEvents.TryAutoLogin(
                                username = result.username,
                                password = result.password,
                                autoLogin = result.autoLogin
                            )
                        )
                    }
                    is LoginResult.AutoLoginInfoResult.NoUserData -> {
                        previousState.copy(
                            isLoading = false,
                            uiEvents = null
                        )
                    }
                    is LoginResult.AutoLoginInfoResult.Failure -> previousState.copy(
                        isLoading = false,
                        errors = result.error,
                        uiEvents = null
                    )
                    is LoginResult.AutoLoginInfoResult.InFlight -> previousState.copy(
                        isLoading = true,
                        uiEvents = null
                    )
                }
            }
        }
    }
}