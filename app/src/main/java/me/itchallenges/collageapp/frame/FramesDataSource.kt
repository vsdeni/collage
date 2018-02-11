package me.itchallenges.collageapp.frame

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import io.reactivex.Observable
import java.io.File


class FramesDataSource : FramesRepository {

    override fun getImages(framesDir: File): Observable<Bitmap> {
        return Observable.create<Bitmap>({ emitter ->
            framesDir.list()
                    .map { bitmapFromFile(File(framesDir, it)) }
                    .forEach { emitter.onNext(it) }

            emitter.onComplete()
        })
    }

    private fun bitmapFromFile(file: File): Bitmap {
        return BitmapFactory.decodeFile(file.absolutePath)
    }
}