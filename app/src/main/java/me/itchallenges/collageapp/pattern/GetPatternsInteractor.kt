package me.itchallenges.collageapp.pattern

import com.urancompany.indoorapp.executor.ExecutionScheduler
import com.urancompany.indoorapp.interactor.UseCase
import io.reactivex.Single
import me.itchallenges.collageapp.settings.SettingsRepository


class GetPatternsInteractor(private val patternsRepository: PatternRepository,
                            private val settingsRepository: SettingsRepository,
                            private val scheduler: ExecutionScheduler) : UseCase.RxSingle<List<Pattern>, UseCase.None>() {

    override fun build(params: None?): Single<List<Pattern>> =
            settingsRepository.getCollageImagesCount()
                    .flatMap({ patternsRepository.getPatterns(it) })
                    .compose(scheduler.highPrioritySingle())

}