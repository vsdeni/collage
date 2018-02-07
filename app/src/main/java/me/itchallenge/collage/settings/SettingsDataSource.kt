package me.itchallenge.collage.settings

import android.content.Context
import io.reactivex.Single
import java.io.File


class SettingsDataSource(val context: Context) : SettingsRepository {

    override fun getFileToSaveVideo(): Single<File> =
            Single.just(File(context.filesDir, "video"))

    override fun getDirToSaveFrames(): Single<File> =
            Single.just(File(context.filesDir, "frames/"))
}