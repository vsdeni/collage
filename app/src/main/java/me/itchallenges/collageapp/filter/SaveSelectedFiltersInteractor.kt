package me.itchallenges.collageapp.filter

import com.urancompany.indoorapp.executor.ExecutionScheduler
import com.urancompany.indoorapp.interactor.UseCase
import io.reactivex.Completable
import me.itchallenges.collageapp.collage.CollageRepository
import java.util.*


class SaveSelectedFiltersInteractor(
        private val collageRepository: CollageRepository,
        private val scheduler: ExecutionScheduler) :
        UseCase.RxCompletable<SaveSelectedFiltersInteractor.Params>() {

    override fun build(params: Params?): Completable =
            collageRepository
                    .saveFilters(params!!.filters)
                    .compose(scheduler.highPriorityCompletable())

    data class Params(val filters: Array<Filter>) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Params

            if (!Arrays.equals(filters, other.filters)) return false

            return true
        }

        override fun hashCode(): Int {
            return Arrays.hashCode(filters)
        }
    }
}