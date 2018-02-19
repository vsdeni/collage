package me.itchallenges.collageapp.filter

import android.graphics.Bitmap
import io.reactivex.Completable
import me.itchallenges.collageapp.collage.CollageRepository
import me.itchallenges.collageapp.framework.executor.ExecutionScheduler
import me.itchallenges.collageapp.framework.interactor.UseCase
import java.util.*
import javax.inject.Inject


class SaveImageInteractor
@Inject constructor(private val collageRepository: CollageRepository,
                    private val executionScheduler: ExecutionScheduler) : UseCase.RxCompletable<SaveImageInteractor.Params>() {

    override fun build(params: Params?): Completable {
        return collageRepository
                .getPattern()
                .flatMap({ pattern ->
                    collageRepository.saveCollageImage(params!!.bitmaps, pattern)
                })
                .flatMapCompletable({
                    collageRepository
                            .saveFilters(params!!.filters)
                })
                .compose(executionScheduler.highPriorityCompletable())
    }

    data class Params(val bitmaps: Array<Bitmap>, val filters: Array<Filter>) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Params

            if (!Arrays.equals(bitmaps, other.bitmaps)) return false
            if (!Arrays.equals(filters, other.filters)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = Arrays.hashCode(bitmaps)
            result = 31 * result + Arrays.hashCode(filters)
            return result
        }
    }
}