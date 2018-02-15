package me.itchallenges.collageapp.pattern

import com.urancompany.indoorapp.executor.ExecutionScheduler
import com.urancompany.indoorapp.interactor.UseCase
import io.reactivex.Single
import me.itchallenges.collageapp.collage.CollageRepository
import me.itchallenges.collageapp.settings.SettingsRepository
import java.io.File


class GetFramesInteractor(private val collageRepository: CollageRepository,
                          private val settingsRepository: SettingsRepository,
                          private val scheduler: ExecutionScheduler) : UseCase.RxSingle<List<File>, UseCase.None>() {

    override fun build(params: None?): Single<List<File>> {
        return settingsRepository.getDirToSaveFrames()
                .flatMap({ dir ->
                    settingsRepository.getCollageImagesCount()
                            .map { count -> Pair(dir, count) }
                })
                .flatMapObservable({ settings -> collageRepository.getFrames(settings.first,settings.second) })
                .toList()
                .compose(scheduler.highPrioritySingle())
    }
}