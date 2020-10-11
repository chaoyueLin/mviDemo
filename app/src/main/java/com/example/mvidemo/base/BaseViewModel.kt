package com.example.mvidemo.base

/**
 * 创建人：linchaoyue
 * 创建时间：20/9/19 上午10:56
 * 描述：
 */
abstract class BaseViewModel<I : IIntent, S : IViewState> : IViewModel<I, S>