package com.example.mvidemo.base

import io.reactivex.Observable

/**
 * 创建人：linchaoyue
 * 创建时间：20/9/19 上午10:57
 * 描述：
 */
interface IView<I:IIntent,VS:IViewState>{
    fun intents():Observable<I>
    fun render(state: VS)
}