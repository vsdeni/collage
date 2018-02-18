package me.itchallenges.collageapp.pattern

import android.net.Uri
import com.urancompany.indoorapp.executor.ExecutionScheduler
import com.urancompany.indoorapp.interactor.UseCase
import io.reactivex.Single
import me.itchallenges.collageapp.collage.CollageRepository


class GetFramesInteractor(private val collageRepository: CollageRepository,
                          private val scheduler: ExecutionScheduler) : UseCase.RxSingle<List<Uri>, UseCase.None>() {

    override fun build(params: None?): Single<List<Uri>> {
        return collageRepository.getFrames()
                .toList()
                .compose(scheduler.highPrioritySingle())
    }
}