package me.itchallenges.collageapp.video

import com.urancompany.indoorapp.executor.ExecutionScheduler
import com.urancompany.indoorapp.interactor.UseCase
import io.reactivex.Completable
import me.itchallenges.collageapp.settings.SettingsRepository
import java.io.FileNotFoundException


class ValidateFramesInteractor(
        private val settingsRepository: SettingsRepository,
        private val scheduler: ExecutionScheduler) : UseCase.RxCompletable<UseCase.None>() {

    override fun build(params: None?): Completable {
        return settingsRepository
                .getDirToSaveFrames()
                .flatMapCompletable({ dir ->
                    Completable.create({ emitter ->
                        if (dir.exists() && dir.list().isNotEmpty()) {
                            emitter.onComplete()
                        } else {
                            emitter.onError(FileNotFoundException())
                        }
                    })
                }).compose(scheduler.highPriorityCompletable())
    }
}