package me.itchallenges.collageapp.filter

import android.graphics.*
import io.reactivex.Completable
import io.reactivex.Single
import me.itchallenges.collageapp.collage.CollageRepository
import me.itchallenges.collageapp.common.executor.ExecutionScheduler
import me.itchallenges.collageapp.common.interactor.UseCase
import me.itchallenges.collageapp.pattern.Pattern
import me.itchallenges.collageapp.settings.SettingsRepository
import java.util.*
import javax.inject.Inject


class SaveImageInteractor
@Inject constructor(private val collageRepository: CollageRepository,
                    private val settingsRepository: SettingsRepository,
                    private val executionScheduler: ExecutionScheduler) : UseCase.RxCompletable<SaveImageInteractor.Params>() {

    override fun build(params: Params?): Completable {
        return collageRepository
                .getPattern()
                .flatMap({ pattern ->
                    settingsRepository.getFinalCollageImageSize()
                            .map { size -> Pair(pattern, size) }
                })
                .flatMap({ settings ->
                    buildCollage(settings.second, settings.second, params!!.bitmaps, settings.first)
                })
                .flatMap({ collageRepository.saveCollageImage(it) })
                .flatMapCompletable({
                    collageRepository
                            .saveFilters(params!!.filters)
                })
                .compose(executionScheduler.highPriorityCompletable())
    }

    private fun buildCollage(height: Int, width: Int, bitmaps: Array<Bitmap>, pattern: Pattern): Single<Bitmap> {//TODO
        return Single.fromCallable({
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            val fillPaint = Paint()
            fillPaint.style = Paint.Style.FILL
            fillPaint.color = Color.parseColor("#c6c6c6")

            val strokePaint = Paint()
            strokePaint.style = Paint.Style.STROKE
            strokePaint.strokeWidth = 2.toFloat()
            strokePaint.color = Color.parseColor("#747474")

            pattern.positions
                    .forEachIndexed({ index, position ->
                        val rect = Rect()
                        rect.top = position.y * (height / 10)
                        rect.left = position.x * (width / 10)
                        rect.right = rect.left + position.width * (width / 10)
                        rect.bottom = rect.top + position.height * (height / 10)

                        val frameBitmap = Bitmap.createBitmap(bitmaps[index])

                        canvas.drawBitmap(frameBitmap, null, rect, fillPaint)
                        canvas.drawRect(rect, strokePaint)
                    })

            bitmap
        })
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