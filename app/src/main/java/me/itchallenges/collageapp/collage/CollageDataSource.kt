package me.itchallenges.collageapp.collage

import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.google.gson.Gson
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import me.itchallenges.collageapp.filter.Filter
import me.itchallenges.collageapp.pattern.Pattern
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream


class CollageDataSource(private val sharedPreferences: SharedPreferences,
                        private val gson: Gson) : CollageRepository {

    override fun savePattern(pattern: Pattern): Completable =
            Completable.fromCallable({
                sharedPreferences.edit().putString(patternKey, gson.toJson(pattern)).apply()
            })

    override fun getPattern(): Single<Pattern> =
            Single.fromCallable<Pattern>({
                gson.fromJson(sharedPreferences.getString(patternKey, ""), Pattern::class.java)
            })

    override fun saveFrames(images: List<Bitmap>, dir: File): Completable {
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

    override fun getFrames(dir: File): Observable<File> {
        return Observable.create<File>({ emitter ->
            dir.list()
                    .forEach { emitter.onNext(File(dir, it)) }

            emitter.onComplete()
        })
    }

    override fun saveFrameFilter(indexes: IntArray, filter: Filter): Completable =
            Completable.fromCallable({
                indexes
                        .forEach { sharedPreferences.edit().putString(filterKey + it, gson.toJson(filter)).apply() }
            })

    override fun getFrameFilter(indexes: IntArray): Observable<Filter> =
            Observable.create({ emitter ->
                indexes
                        .filter { sharedPreferences.contains(filterKey + it) }
                        .forEach { emitter.onNext(gson.fromJson(sharedPreferences.getString(filterKey + it, ""), Filter::class.java)) }
                emitter.onComplete()
            })

    override fun saveGlobalFilter(filter: Filter): Completable =
            Completable.fromCallable({
                sharedPreferences.edit().putString(filterKey, gson.toJson(filter)).apply()
            })

    override fun getGlobalFilter(): Maybe<Filter> =
            Maybe.create({ emitter ->
                if (sharedPreferences.contains(filterKey)) {
                    emitter.onSuccess(gson.fromJson(sharedPreferences.getString(filterKey, ""), Filter::class.java))
                }
                emitter.onComplete()
            })

    private fun bitmapFromFile(file: File): Bitmap {
        return BitmapFactory.decodeFile(file.absolutePath)
    }
}

private const val patternKey = "pattern"
private const val filterKey = "filter"