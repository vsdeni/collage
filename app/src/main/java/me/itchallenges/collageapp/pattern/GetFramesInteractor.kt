package me.itchallenges.collageapp.pattern

import android.net.Uri
import io.reactivex.Single
import me.itchallenges.collageapp.collage.CollageRepository
import me.itchallenges.collageapp.common.executor.ExecutionScheduler
import me.itchallenges.collageapp.common.interactor.UseCase
import javax.inject.Inject


class GetFramesInteractor
@Inject constructor(private val collageRepository: CollageRepository,
                    private val scheduler: ExecutionScheduler) : UseCase.RxSingle<List<Uri>, UseCase.None>() {

    override fun build(params: None?): Single<List<Uri>> {
        return collageRepository.getFrames()
                .toList()
                .compose(scheduler.highPrioritySingle())
    }
}