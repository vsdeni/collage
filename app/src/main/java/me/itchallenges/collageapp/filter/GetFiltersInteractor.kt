package me.itchallenges.collageapp.filter

import com.urancompany.indoorapp.executor.ExecutionScheduler
import com.urancompany.indoorapp.interactor.UseCase
import io.reactivex.Single


class GetFiltersInteractor(private val filtersRepository: FilterRepository,
                           private val scheduler: ExecutionScheduler) : UseCase.RxSingle<List<Filter>, UseCase.None>() {

    override fun build(params: None?): Single<List<Filter>> {
        return filtersRepository.getFilters()
                .toList()
                .compose(scheduler.highPrioritySingle())
    }
}