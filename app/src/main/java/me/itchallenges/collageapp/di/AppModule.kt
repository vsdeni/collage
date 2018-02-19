package me.itchallenges.collageapp.di

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import me.itchallenges.collageapp.R
import me.itchallenges.collageapp.collage.CollageDataSource
import me.itchallenges.collageapp.collage.CollageRepository
import me.itchallenges.collageapp.framework.executor.ExecutionScheduler
import me.itchallenges.collageapp.framework.executor.ThreadScheduler
import me.itchallenges.collageapp.filter.FilterDataSource
import me.itchallenges.collageapp.filter.FilterRepository
import me.itchallenges.collageapp.pattern.PatternDataSource
import me.itchallenges.collageapp.pattern.PatternRepository
import me.itchallenges.collageapp.settings.SettingsDataSource
import me.itchallenges.collageapp.settings.SettingsRepository
import javax.inject.Singleton

@Module
class AppModule(private val appContext: Context) {

    @Provides
    @Singleton
    fun provideExecutionScheduler(threadScheduler: ThreadScheduler): ExecutionScheduler = threadScheduler

    @Provides
    @Singleton
    fun provideContext(): Context = appContext

    @Provides
    @Singleton
    fun provideSharedPreferences(context: Context): SharedPreferences =
            context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideSettingsRepository(context: Context): SettingsRepository =
            SettingsDataSource(context)

    @Provides
    @Singleton
    fun provideGson(): Gson =
            GsonBuilder().create()

    @Provides
    @Singleton
    fun provideCollageRepository(context: Context, settingsRepository: SettingsRepository,
                                 sharedPreferences: SharedPreferences, gson: Gson): CollageRepository =
            CollageDataSource(context, settingsRepository, sharedPreferences, gson)

    @Provides
    @Singleton
    fun providePatternRepository(context: Context, gson: Gson): PatternRepository =
            PatternDataSource(context, gson)

    @Provides
    @Singleton
    fun provideFiltersRepository(): FilterRepository =
            FilterDataSource()
}
