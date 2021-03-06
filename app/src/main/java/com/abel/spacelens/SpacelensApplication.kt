package com.abel.spacelens

import android.app.Application

class SpacelensApplication : Application() {

    init {
        instance = this
    }

    companion object {
        private var instance: SpacelensApplication? = null

        fun applicationContext(): SpacelensApplication {
            return instance as SpacelensApplication
        }
    }
}