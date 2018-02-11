package me.itchallenges.collageapp.collage

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import me.itchallenges.collageapp.filter.Filter
import me.itchallenges.collageapp.pattern.Pattern
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream


class CollageDataSource : CollageRepository {

    override fun savePattern(pattern: Pattern): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getPattern(): Single<Pattern> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun saveImages(images: List<Bitmap>, dir: File): Completable {
        return Completable.fromCallable({
            dir.deleteRecursively()
            dir.mkdirs()
            for ((i, frame) in images.withIndex()) {
                val bos = ByteArrayOutputStream()
                frame.compress(Bitmap.CompressFormat.JPEG, 100, bos)
                val fos = FileOutputStream(File(dir, i.toString() + ".jpg"))//TODO
                fos.write(bos.toByteArray())
                fos.flush()
                fos.close()
            }
        })
    }

    override fun saveFilter(index: Int, filter: Filter): Maybe<Filter> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getFilter(index: Int): Filter {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getImages(dir: File): Observable<Bitmap> {
        return Observable.create<Bitmap>({ emitter ->
            dir.list()
                    .map { bitmapFromFile(File(dir, it)) }
                    .forEach { emitter.onNext(it) }

            emitter.onComplete()
        })
    }

    private fun bitmapFromFile(file: File): Bitmap {
        return BitmapFactory.decodeFile(file.absolutePath)
    }
}