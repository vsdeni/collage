package me.itchallenges.collageapp

import android.app.Application
import com.squareup.leakcanary.LeakCanary
import me.itchallenges.collageapp.di.Injector


class App : Application() {
    override fun onCreate() {
        super.onCreate()
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return
        }
        LeakCanary.install(this)

        Injector.initAppComponent(this.applicationContext)
    }
}