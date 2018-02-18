package me.itchallenges.collageapp.filter

import com.urancompany.indoorapp.executor.ExecutionScheduler
import com.urancompany.indoorapp.interactor.UseCase
import io.reactivex.Single
import me.itchallenges.collageapp.collage.CollageRepository


class GetFilterCollageInteractor(private val collageRepository: CollageRepository,
                                 private val scheduler: ExecutionScheduler) : UseCase.RxSingle<CollageFilterViewModel, UseCase.None>() {

    override fun build(params: None?): Single<CollageFilterViewModel> {
        return collageRepository
                .getFrames()
                .toList()
                .flatMap({ frames ->
                    collageRepository
                            .getPattern()
                            .map { pattern ->
                                CollageFilterViewModel(frames,
                                        pattern)
                            }
                }).compose(scheduler.highPrioritySingle())
    }
}