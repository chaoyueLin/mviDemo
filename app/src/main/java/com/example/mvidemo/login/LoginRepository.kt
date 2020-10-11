package com.example.mvidemo.login

import android.content.SharedPreferences
import com.example.mvidemo.WebService
import com.example.mvidemo.login.entry.LoginRequestModel
import com.example.mvidemo.login.entry.UserInfo
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.WebSocket
import retrofit2.Retrofit

/**
 * 创建人：linchaoyue
 * 创建时间：20/9/19 上午11:28
 * 描述：
 */
class LoginRepository {

    val localDataSource: LoginLocalDataSource = LoginLocalDataSource(UserInfoRepository)

    fun login(username: String, password: String): Flowable<UserInfo> {
        // 保存用户登录信息
        return localDataSource.savePrefsUser(username, password)
            .andThen(login())
            // 如果登录失败，清除登录信息
            .doOnError { localDataSource.clearPrefsUser() }
    }

    fun login(): Flowable<UserInfo> {
        val authObservable = WebService.service().create(LoginService::class.java)
            .authorizations(LoginRequestModel.generate())
        val ownerInfoObservable = WebService.service().create(LoginService::class.java)
            .fetchUserOwner()

        return authObservable                       // 1.用户登录认证
            .flatMap { ownerInfoObservable }        // 2.获取用户详细信息
            .subscribeOn(Schedulers.io())
    }
}

class LoginLocalDataSource(
    private val userRepository: UserInfoRepository
) {

    fun savePrefsUser(username: String, password: String): Completable {
        return Completable.fromAction {
            userRepository.username = username
            userRepository.password = password
        }
    }

    fun clearPrefsUser(): Completable {
        return Completable.fromAction {
            userRepository.username = ""
            userRepository.password = ""
        }
    }
}

object UserInfoRepository {

    var accessToken: String = ""

    var username: String = ""

    var password: String = ""


}