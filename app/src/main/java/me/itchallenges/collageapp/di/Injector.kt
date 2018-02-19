package me.itchallenges.collageapp.di

import android.content.Context

enum class Injector {
    INSTANCE;

    lateinit var appComponent: AppComponent

    companion object {

        fun initAppComponent(appContext: Context) {
            INSTANCE.appComponent = buildAppComponent(appContext)
        }

        private fun buildAppComponent(context: Context): AppComponent {
            return DaggerAppComponent.builder()
                    .appModule(AppModule(context))
                    .build()
        }
    }
}
