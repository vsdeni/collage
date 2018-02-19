package me.itchallenges.collageapp.filter

import io.reactivex.Single
import me.itchallenges.collageapp.collage.CollageRepository
import me.itchallenges.collageapp.framework.executor.ExecutionScheduler
import me.itchallenges.collageapp.framework.interactor.UseCase
import javax.inject.Inject


class GetFilterCollageInteractor
@Inject constructor(private val collageRepository: CollageRepository,
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