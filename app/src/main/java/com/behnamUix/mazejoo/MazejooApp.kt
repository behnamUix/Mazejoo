package com.behnamUix.mazejoo

import android.app.Application
import com.behnamUix.mazejoo.koin.appModule
import com.orhanobut.hawk.Hawk
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MazejooApp:Application(){
    override fun onCreate() {
        super.onCreate()

        startKoin{
            androidContext(this@MazejooApp)
            modules(appModule)
        }
    }
}