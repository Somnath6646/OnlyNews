package dev.somnath.onlynews

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class OnlyNewsApp: Application(){


    companion object {
        const val API_KEY = "ENTER_YOUR_API_KEY_HERE"
    }

}