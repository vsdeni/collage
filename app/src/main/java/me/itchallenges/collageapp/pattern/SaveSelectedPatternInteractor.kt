package me.itchallenges.collageapp.pattern

import io.reactivex.Completable
import me.itchallenges.collageapp.collage.CollageRepository
import me.itchallenges.collageapp.common.executor.ExecutionScheduler
import me.itchallenges.collageapp.common.interactor.UseCase
import javax.inject.Inject


class SaveSelectedPatternInteractor
@Inject constructor(private val collageRepository: CollageRepository,
                    private val scheduler: ExecutionScheduler) :
        UseCase.RxCompletable<SaveSelectedPatternInteractor.Params>() {

    override fun build(params: Params?): Completable =
            collageRepository
                    .savePattern(params!!.pattern)
                    .compose(scheduler.highPriorityCompletable())

    data class Params(val pattern: Pattern)
}