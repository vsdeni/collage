package me.itchallenges.collageapp.pattern

import android.graphics.Bitmap
import com.urancompany.indoorapp.executor.ExecutionScheduler
import com.urancompany.indoorapp.interactor.UseCase
import io.reactivex.Single
import me.itchallenges.collageapp.frame.FramesRepository
import me.itchallenges.collageapp.settings.SettingsRepository


class GetFramesInteractor(private val framesRepository: FramesRepository,
                          private val settingsRepository: SettingsRepository,
                          private val scheduler: ExecutionScheduler) : UseCase.RxSingle<List<Bitmap>, UseCase.None>() {

    override fun build(params: None?): Single<List<Bitmap>> {
        return settingsRepository.getDirToSaveFrames()
                .flatMapObservable({ framesRepository.getImages(it) })
                .toList()
                .compose(scheduler.highPrioritySingle())
    }
}