package me.itchallenge.collage.settings

import io.reactivex.Single
import java.io.File


interface SettingsRepository {
    fun getFileToSaveVideo(): Single<File>

    fun getDirToSaveFrames(): Single<File>
}