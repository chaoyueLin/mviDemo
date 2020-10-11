package com.example.mvidemo.base

import io.reactivex.Observable


/**
 * 创建人：linchaoyue
 * 创建时间：20/9/19 上午10:33
 * 描述：
 */
interface IViewModel<I:IIntent,VS:IViewState>{
    fun processIntent(intents: Observable<I>)
    fun state():Observable<VS>
}
