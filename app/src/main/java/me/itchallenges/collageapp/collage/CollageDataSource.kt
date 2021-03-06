package me.itchallenges.collageapp.collage

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.Uri
import android.support.v4.content.FileProvider
import com.google.gson.Gson
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import me.itchallenges.collageapp.filter.Filter
import me.itchallenges.collageapp.framework.convertToFile
import me.itchallenges.collageapp.pattern.Pattern
import me.itchallenges.collageapp.settings.SettingsRepository
import java.io.File


class CollageDataSource(private val context: Context,
                        private val settingsRepository: SettingsRepository,
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
                .flatMapObservable({ dir ->
                    Completable.fromAction({
                        dir.deleteRecursively()
                        dir.mkdirs()
                    }).andThen(Observable.fromIterable(images)
                            .map { Pair(it, File(dir, System.currentTimeMillis().toString())) })
                })
                .flatMapCompletable({ frame ->
                    Completable.fromCallable({
                        frame.first.convertToFile(frame.second)
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
                    file.delete()
                    Completable.fromCallable({ bitmap.convertToFile(file) })
                            .andThen(Single.just(Uri.fromFile(file)))
                })

    }

    override fun saveCollageImage(bitmaps: Array<Bitmap>, pattern: Pattern): Single<Uri> {
        return settingsRepository.getCollageImageParams()
                .flatMap({ params ->
                    settingsRepository.getFileToSaveCollage()
                            .map { Pair(params, it) }
                }).flatMap({ settings ->
            Completable.fromCallable({
                val bitmap = pattern.drawCollage(settings.first, bitmaps)
                bitmap.convertToFile(settings.second)
            })
                    .andThen(Single.just(Uri.fromFile(settings.second)))
        })
    }

    override fun getCollageImage(): Single<Uri> {
        return settingsRepository
                .getFileToSaveCollage()
                .map { Uri.fromFile(it) }
    }

    override fun getCollageImageForSharing(): Single<Uri> {
        return getCollageImage()
                .map {
                    FileProvider.getUriForFile(context,
                            context.packageName + ".provider", File(it.path))
                }
    }

}

private const val patternKey = "pattern"
private const val filterKey = "filter"