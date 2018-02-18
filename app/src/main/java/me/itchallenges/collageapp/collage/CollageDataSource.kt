package me.itchallenges.collageapp.collage

import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.Uri
import com.google.gson.Gson
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import me.itchallenges.collageapp.filter.Filter
import me.itchallenges.collageapp.pattern.Pattern
import me.itchallenges.collageapp.settings.SettingsRepository
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream


class CollageDataSource(private val settingsRepository: SettingsRepository,
                        private val sharedPreferences: SharedPreferences,
                        private val gson: Gson) : CollageRepository {

    override fun savePattern(pattern: Pattern): Completable =
            Completable.fromCallable({
                sharedPreferences.edit().putString(patternKey, gson.toJson(pattern)).apply()
            })

    override fun getPattern(): Single<Pattern> =
            Single.fromCallable<Pattern>({
                gson.fromJson(sharedPreferences.getString(patternKey, ""), Pattern::class.java)
            })

    override fun saveFrames(images: List<Bitmap>): Completable {
        return settingsRepository.getDirToSaveFrames()
                .flatMapCompletable({ dir ->
                    Completable.fromCallable({
                        dir.deleteRecursively()
                        dir.mkdirs()
                        for (frame in images) {
                            convertBitmapToFile(File(dir, System.currentTimeMillis().toString()), frame)
                        }
                    })
                })
    }

    override fun getFrames(): Observable<Uri> {
        return settingsRepository.getDirToSaveFrames()
                .flatMap({ dir ->
                    settingsRepository.getCollageImagesCount()
                            .map { Pair(dir, it) }
                })
                .flatMapObservable { settings ->
                    Observable.create<Uri>({ emitter ->
                        settings.first.list()
                                .filterIndexed({ i, _ -> i < settings.second })
                                .map { Uri.fromFile(File(settings.first, it)) }
                                .forEach { emitter.onNext(it) }

                        emitter.onComplete()
                    })
                }
    }

    override fun saveFilters(filters: Array<Filter>): Completable =
            Completable.fromCallable({
                filters
                        .map { it.ordinal }
                        .forEachIndexed { index, filterId ->
                            sharedPreferences.edit().putInt(filterKey + index, filterId).apply()
                        }
            })

    override fun getFilters(indexes: IntArray): Observable<Filter> =
            Observable.create({ emitter ->
                indexes
                        .filter { sharedPreferences.contains(filterKey + it) }
                        .map { Filter.values()[sharedPreferences.getInt(filterKey + it, 0)] }
                        .forEach { filter ->
                            emitter.onNext(filter)
                        }
                emitter.onComplete()
            })

    override fun saveCollageImage(bitmap: Bitmap): Single<Uri> {
        return settingsRepository
                .getFileToSaveCollage()
                .flatMap({ file ->
                    convertBitmapToFile(file, bitmap)
                            .andThen(Single.just(Uri.fromFile(file)))
                })

    }

    private fun convertBitmapToFile(file: File, bitmap: Bitmap): Completable {
        return Completable.fromCallable({

            file.delete()

            val bos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos)
            val fos = FileOutputStream(file)
            fos.write(bos.toByteArray())
            fos.flush()
            fos.close()
        })
    }

    override fun getCollageImage(): Single<Uri> {
        return settingsRepository
                .getFileToSaveCollage()
                .map { Uri.fromFile(it) }
    }
}

private const val patternKey = "pattern"
private const val filterKey = "filter"