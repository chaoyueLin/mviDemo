package com.example.mvidemo.base

import androidx.appcompat.app.AppCompatActivity

/**
 * 创建人：linchaoyue
 * 创建时间：20/9/19 上午11:00
 * 描述：
 */
abstract class BaseActivity<I : IIntent,  VS : IViewState> : AppCompatActivity(),IView<I,VS>{

}