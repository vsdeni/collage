package me.itchallenges.collageapp.browse

import android.graphics.*
import android.net.Uri
import com.urancompany.indoorapp.executor.ExecutionScheduler
import com.urancompany.indoorapp.interactor.UseCase
import io.reactivex.Single
import me.itchallenges.collageapp.collage.CollageRepository
import me.itchallenges.collageapp.filter.Filter
import me.itchallenges.collageapp.pattern.Pattern
import java.util.*


class GetCollageInteractor(private val collageRepository: CollageRepository,
                           private val executionScheduler: ExecutionScheduler) : UseCase.RxSingle<Uri, UseCase.None>() {

    override fun build(params: None?): Single<Uri> {
        return collageRepository.getCollageImage()
                .compose(executionScheduler.highPrioritySingle())
//        return getCollageParams()
//                .flatMap { collageParams -> createCollageImage(collageParams) }
//                .flatMap { bitmap ->
//                    collageRepository.saveCollageImage(bitmap)
//                }.compose(executionScheduler.highPrioritySingle())
    }

    private fun getCollageParams(): Single<CollageParamsWrapper> {
        return collageRepository.getFrames()
                .toList()
                .flatMap({ frameImages ->
                    collageRepository
                            .getFilters(IntArray(frameImages.size, { it }))
                            .toList()
                            .map { filters ->
                                Array(frameImages.size,
                                        { FrameParamsWrapper(frameImages[it], filters[it]) })
                            }
                }).flatMap({ frames ->
            collageRepository.getPattern()
                    .map { CollageParamsWrapper(it, frames) }
        })
    }

    private fun createCollageImage(params: CollageParamsWrapper): Single<Bitmap> {
        return Single.fromCallable({
            val width = 600
            val height = 600
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            val fillPaint = Paint()
            fillPaint.style = Paint.Style.FILL
            fillPaint.color = Color.parseColor("#c6c6c6")

            val strokePaint = Paint()
            strokePaint.style = Paint.Style.STROKE
            strokePaint.strokeWidth = 2.toFloat()
            strokePaint.color = Color.parseColor("#747474")

            params.pattern.positions
                    .forEachIndexed({ index, position ->
                        val rect = Rect()
                        rect.top = position.y * (height / 10)
                        rect.left = position.x * (width / 10)
                        rect.right = rect.left + position.width * (width / 10)
                        rect.bottom = rect.top + position.height * (height / 10)

                        val frameBitmap = BitmapFactory.decodeFile(params.frames[index].image.path)

                        canvas.drawBitmap(frameBitmap, null, rect, fillPaint)
                        canvas.drawRect(rect, strokePaint)
                    })

            bitmap
        })
    }

    data class CollageParamsWrapper(val pattern: Pattern, val frames: Array<FrameParamsWrapper>) {

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as CollageParamsWrapper

            if (pattern != other.pattern) return false
            if (!Arrays.equals(frames, other.frames)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = pattern.hashCode()
            result = 31 * result + Arrays.hashCode(frames)
            return result
        }
    }

    data class FrameParamsWrapper(val image: Uri, val filter: Filter)
}