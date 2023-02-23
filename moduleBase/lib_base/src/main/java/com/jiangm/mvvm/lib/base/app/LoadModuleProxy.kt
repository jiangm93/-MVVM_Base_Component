package com.jiangm.mvvm.lib.base.app

import android.app.Application
import android.content.Context
import android.util.Log
import java.util.*

/**
 *@date：2023/2/23 11:03
 *@author: jiangm
 *@version: 1.0
 *@desc:  加载组件代理类
 * 组件初始化的工作将由该代理类代理实现
 */
class LoadModuleProxy : ApplicationLifecycle {

    private var mLoader: ServiceLoader<ApplicationLifecycle> =
        ServiceLoader.load(ApplicationLifecycle::class.java)

    /**
     * 同[Application.attachBaseContext]
     * @param context Context
     */
    override fun onAttachBaseContext(context: Context) {
        mLoader.forEach {
            Log.d("ApplicationInit", it.toString())
            it.onAttachBaseContext(context)
        }
    }

    /**
     * 同[Application.onCreate]
     * @param application Application
     */
    override fun onCreate(application: Application) {
        mLoader.forEach { it.onCreate(application) }
    }

    /**
     * 同[Application.onTerminate]
     * @param application Application
     */
    override fun onTerminate(application: Application) {
        mLoader.forEach { it.onTerminate(application) }
    }

    /**
     * 主线程前台初始化
     * @return MutableList<() -> String> 初始化方法集合
     */
    override fun initByFrontDesk(): MutableList<() -> String> {
        val list: MutableList<() -> String> = mutableListOf()
        mLoader.forEach { list.addAll(it.initByFrontDesk()) }
        return list
    }

    /**
     * 不需要立即初始化的放在这里进行后台初始化
     */
    override fun initByBackstage() {
        mLoader.forEach { it.initByBackstage() }
    }
}