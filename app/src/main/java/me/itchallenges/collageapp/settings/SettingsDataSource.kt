package me.itchallenges.collageapp.settings

import android.content.Context
import android.os.Environment
import io.reactivex.Single
import java.io.File


class SettingsDataSource(val context: Context) : SettingsRepository {

    override fun getFileToSaveVideo(): Single<File> =
            Single.just(File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_MOVIES), "video"))

    override fun getDirToSaveFrames(): Single<File> =
            Single.just(File(context.filesDir, "frames/"))

    override fun getCollageImagesCount(): Single<Int> =
            Single.just(5)

    override fun getFileToSaveCollage(): Single<File> =
            Single.just(File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), "collage.jpg"))

    override fun getFinalCollageImageSize(): Single<Int> {
        return Single.just(800)
    }
}