package me.itchallenges.collageapp.pattern

import android.content.Context
import android.net.Uri
import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.Single
import me.itchallenges.collageapp.framework.convertToFile
import me.itchallenges.collageapp.settings.SettingsRepository
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.util.*


class PatternDataSource(private val context: Context,
                        private val settingsRepository: SettingsRepository,
                        private val gson: Gson) : PatternRepository {

    override fun getPatterns(framesCount: Int): Single<List<Pattern>> {
        return settingsRepository.getPatternPreviewParams()
                .flatMap({ params ->
                    settingsRepository.getCacheDir()
                            .map { Pair(params, it) }
                })
                .flatMapObservable({ settings ->
                    Observable.create<Pattern>({ emitter ->
                        val reader = BufferedReader(InputStreamReader(context.assets.open("patterns")))
                        gson.fromJson(reader, Array<Pattern>::class.java)
                                .filter { it.positions.size == framesCount }
                                .forEach({
                                    val bitmap = it.drawPreview(settings.first)
                                    val file = File(settings.second, UUID.randomUUID().toString())
                                    bitmap.convertToFile(file)
                                    it.preview = Uri.fromFile(file)
                                    emitter.onNext(it)
                                })

                        emitter.onComplete()
                    })
                })
                .toList()
    }
}