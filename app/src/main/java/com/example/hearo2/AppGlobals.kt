package com.example.hearo2

import android.app.Application
import android.content.Context

class AppGlobals : Application() {

    init {
        instance = this
    }

    companion object {
        private var instance: AppGlobals? = null

        // 전역에서 사용 가능한 ApplicationContext
        val context: Context
            get() = instance!!.applicationContext
    }
}
