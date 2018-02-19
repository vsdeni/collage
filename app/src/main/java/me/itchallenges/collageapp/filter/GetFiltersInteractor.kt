package me.itchallenges.collageapp.filter

import io.reactivex.Single
import me.itchallenges.collageapp.common.executor.ExecutionScheduler
import me.itchallenges.collageapp.common.interactor.UseCase


class GetFiltersInteractor(private val filtersRepository: FilterRepository,
                           private val scheduler: ExecutionScheduler) : UseCase.RxSingle<List<Filter>, UseCase.None>() {

    override fun build(params: None?): Single<List<Filter>> {
        return filtersRepository.getFilters()
                .toList()
                .compose(scheduler.highPrioritySingle())
    }
}