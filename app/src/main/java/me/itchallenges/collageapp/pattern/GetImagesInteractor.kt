package me.itchallenges.collageapp.pattern

import android.graphics.Bitmap
import com.urancompany.indoorapp.executor.ExecutionScheduler
import com.urancompany.indoorapp.interactor.UseCase
import io.reactivex.Single
import me.itchallenges.collageapp.collage.CollageRepository
import me.itchallenges.collageapp.settings.SettingsRepository


class GetImagesInteractor(private val collageRepository: CollageRepository,
                          private val settingsRepository: SettingsRepository,
                          private val scheduler: ExecutionScheduler) : UseCase.RxSingle<List<Bitmap>, UseCase.None>() {

    override fun build(params: None?): Single<List<Bitmap>> {
        return settingsRepository.getDirToSaveFrames()
                .flatMapObservable({ collageRepository.getImages(it) })
                .toList()
                .compose(scheduler.highPrioritySingle())
    }
}