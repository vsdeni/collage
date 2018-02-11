package me.itchallenges.collageapp.video

import io.reactivex.Completable
import io.reactivex.Single
import java.io.File


interface VideoRepository {
    fun getVideo(): Single<File>

    fun saveVideo(file: File): Completable

    fun deleteVideo(): Completable
}