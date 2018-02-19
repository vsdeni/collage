package me.itchallenges.collageapp.video

import io.reactivex.Completable
import me.itchallenges.collageapp.common.executor.ExecutionScheduler
import me.itchallenges.collageapp.common.interactor.UseCase
import me.itchallenges.collageapp.settings.SettingsRepository
import java.io.File
import java.io.FileNotFoundException
import javax.inject.Inject


class ValidateFramesInteractor
@Inject constructor(private val settingsRepository: SettingsRepository,
                    private val scheduler: ExecutionScheduler) : UseCase.RxCompletable<UseCase.None>() {

    override fun build(params: None?): Completable {
        return settingsRepository
                .getDirToSaveFrames()
                .flatMap({ dir ->
                    settingsRepository.getCollageImagesCount()
                            .map { count -> Pair(dir, count) }
                })
                .flatMapCompletable({ settings ->
                    Completable.create({ emitter ->
                        val dir = settings.first
                        if (dir.exists() && isExistingFramesEnough(dir, settings.second)) {
                            emitter.onComplete()
                        } else {
                            emitter.onError(FileNotFoundException())
                        }
                    })
                }).compose(scheduler.highPriorityCompletable())
    }

    private fun isExistingFramesEnough(dir: File, requiredCount: Int): Boolean {
        return dir.list().size == requiredCount
    }
}