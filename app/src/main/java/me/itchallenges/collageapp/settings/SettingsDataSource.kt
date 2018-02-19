package me.itchallenges.collageapp.settings

import android.content.Context
import android.graphics.Paint
import android.os.Environment
import io.reactivex.Single
import me.itchallenges.collageapp.R
import me.itchallenges.collageapp.pattern.PreviewParams
import java.io.File


class SettingsDataSource(val context: Context) : SettingsRepository {

    override fun getCameraId(): Single<Int> {
        return Single.just(0)
    }

    override fun getFileToSaveVideo(): Single<File> =
            Single.just(File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_MOVIES), "video"))

    override fun getDirToSaveFrames(): Single<File> =
            Single.just(File(context.filesDir, "frames/"))

    override fun getCacheDir(): Single<File> =
            Single.just(context.cacheDir)

    override fun getCollageImagesCount(): Single<Int> =
            Single.just(5)

    override fun getFileToSaveCollage(): Single<File> =
            Single.just(File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), "collage.jpg"))

    override fun getPatternPreviewParams(): Single<PreviewParams> {
        return Single.fromCallable<PreviewParams>({

            val strokePaint = Paint()
            strokePaint.color = context.resources.getColor(R.color.darkerGray)
            strokePaint.style = Paint.Style.STROKE
            strokePaint.strokeWidth = 2.toFloat()

            val fillPaint = Paint()
            fillPaint.style = Paint.Style.FILL
            fillPaint.color = context.resources.getColor(R.color.lightGray)

            PreviewParams(150, 150, fillPaint, strokePaint)
        })
    }

    override fun getCollageImageParams(): Single<PreviewParams> {
        return Single.fromCallable<PreviewParams>({

            val strokePaint = Paint()
            strokePaint.color = context.resources.getColor(R.color.white)
            strokePaint.style = Paint.Style.STROKE
            strokePaint.strokeWidth = 2.toFloat()

            val fillPaint = Paint()
            fillPaint.style = Paint.Style.FILL
            fillPaint.color = context.resources.getColor(R.color.lightGray)

            PreviewParams(800, 800, fillPaint, strokePaint)
        })
    }
}