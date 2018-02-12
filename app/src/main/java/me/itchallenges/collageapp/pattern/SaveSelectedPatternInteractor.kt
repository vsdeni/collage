package me.itchallenges.collageapp.pattern

import com.urancompany.indoorapp.executor.ExecutionScheduler
import com.urancompany.indoorapp.interactor.UseCase
import io.reactivex.Completable
import me.itchallenges.collageapp.collage.CollageRepository


class SaveSelectedPatternInteractor(
        private val collageRepository: CollageRepository,
        private val scheduler: ExecutionScheduler) :
        UseCase.RxCompletable<SaveSelectedPatternInteractor.Params>() {

    override fun build(params: Params?): Completable =
            collageRepository
                    .savePattern(params!!.pattern)
                    .compose(scheduler.highPriorityCompletable())

    data class Params(val pattern: Pattern)
}