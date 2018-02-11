package me.itchallenges.collageapp.pattern

import com.urancompany.indoorapp.executor.ExecutionScheduler
import com.urancompany.indoorapp.interactor.UseCase
import io.reactivex.Single


class GetPatternsInteractor(private val patternsRepository: PatternRepository,
                            private val scheduler: ExecutionScheduler) : UseCase.RxSingle<Array<Pattern>, UseCase.None>() {
    override fun build(params: None?): Single<Array<Pattern>> {
        return patternsRepository.getPatterns()
                .compose(scheduler.highPrioritySingle())
    }
}