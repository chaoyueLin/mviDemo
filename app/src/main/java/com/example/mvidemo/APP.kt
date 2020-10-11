package com.example.mvidemo

import android.app.Application
import android.content.Context

/**
 * 创建人：linchaoyue
 * 创建时间：20/9/19 下午6:58
 * 描述：
 */
class APP :Application() {
    companion object{
        lateinit var context:Application
    }


    override fun onCreate() {
        super.onCreate()
        context=this


    }
}