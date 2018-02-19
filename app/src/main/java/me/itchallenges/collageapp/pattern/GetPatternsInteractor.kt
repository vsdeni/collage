package me.itchallenges.collageapp.pattern

import io.reactivex.Single
import me.itchallenges.collageapp.common.executor.ExecutionScheduler
import me.itchallenges.collageapp.common.interactor.UseCase
import me.itchallenges.collageapp.settings.SettingsRepository
import javax.inject.Inject


class GetPatternsInteractor
@Inject constructor(private val patternsRepository: PatternRepository,
                    private val settingsRepository: SettingsRepository,
                    private val scheduler: ExecutionScheduler) : UseCase.RxSingle<List<Pattern>, UseCase.None>() {

    override fun build(params: None?): Single<List<Pattern>> =
            settingsRepository.getCollageImagesCount()
                    .flatMap({ patternsRepository.getPatterns(it) })
                    .compose(scheduler.highPrioritySingle())

}