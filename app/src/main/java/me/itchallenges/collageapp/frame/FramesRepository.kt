package me.itchallenges.collageapp.frame

import io.reactivex.Observable
import java.io.File


interface FramesRepository {
    fun getImages(): Observable<File>
}