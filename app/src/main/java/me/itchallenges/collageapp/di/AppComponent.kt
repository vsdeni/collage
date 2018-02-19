package me.itchallenges.collageapp.di

import dagger.Component
import me.itchallenges.collageapp.browse.BrowseActivity
import me.itchallenges.collageapp.filter.FilterActivity
import me.itchallenges.collageapp.pattern.PatternActivity
import me.itchallenges.collageapp.video.VideoActivity
import javax.inject.Singleton

@Component(modules = [(AppModule::class)])
@Singleton
interface AppComponent {

    fun inject(videoActivity: VideoActivity)
    fun inject(patternActivity: PatternActivity)
    fun inject(filterActivity: FilterActivity)
    fun inject(browseActivity: BrowseActivity)
}