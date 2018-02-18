package me.itchallenges.collageapp.settings

import io.reactivex.Single
import java.io.File


interface SettingsRepository {
    fun getFileToSaveVideo(): Single<File>

    fun getDirToSaveFrames(): Single<File>

    fun getFileToSaveCollage(): Single<File>

    fun getCollageImagesCount(): Single<Int>

    //only 1 value since collage is square
    fun getFinalCollageImageSize(): Single<Int>
}