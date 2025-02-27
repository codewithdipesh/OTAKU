package com.codewithdipesh.mangareader

import android.app.Application
import coil3.ImageLoader
import coil3.util.CoilUtils
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MangaReaderApp : Application() {

    override fun onCreate() {
        super.onCreate()
    }

    //clearing the coil cache after closing the app
    override fun onTerminate() {
        super.onTerminate()
        ImageLoader(this).memoryCache?.clear()
        ImageLoader(this).diskCache?.clear()
    }
}