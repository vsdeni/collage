package me.itchallenges.collageapp.filter

import com.urancompany.indoorapp.executor.ExecutionScheduler
import com.urancompany.indoorapp.interactor.UseCase
import io.reactivex.Single
import me.itchallenges.collageapp.collage.CollageRepository
import me.itchallenges.collageapp.settings.SettingsRepository


class GetFilterCollageInteractor(private val settingsRepository: SettingsRepository,
                                 private val collageRepository: CollageRepository,
                                 private val scheduler: ExecutionScheduler) : UseCase.RxSingle<CollageFilterViewModel, UseCase.None>() {

    override fun build(params: None?): Single<CollageFilterViewModel> {
        return settingsRepository.getDirToSaveFrames()
                .flatMap({ dir ->
                    settingsRepository.getCollageImagesCount()
                            .map { count -> Pair(dir, count) }
                })
                .flatMap({ settings ->
                    collageRepository
                            .getFrames(settings.first, settings.second)
                            .toList()
                }).flatMap({ frames ->
            collageRepository
                    .getPattern()
                    .map { pattern ->
                        CollageFilterViewModel(frames,
                                pattern)
                    }
        }).compose(scheduler.highPrioritySingle())
    }
}