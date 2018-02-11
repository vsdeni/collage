package me.itchallenges.collageapp.frame

import android.graphics.Bitmap
import io.reactivex.Observable
import java.io.File


interface FramesRepository {
    fun getImages(framesDir: File): Observable<Bitmap>
}