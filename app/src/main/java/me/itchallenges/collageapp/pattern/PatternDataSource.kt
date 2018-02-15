package me.itchallenges.collageapp.pattern

import android.content.Context
import com.google.gson.Gson
import io.reactivex.Single
import java.io.BufferedReader
import java.io.InputStreamReader


class PatternDataSource(private val context: Context,
                        private val gson: Gson) : PatternRepository {

    override fun getPatterns(framesCount: Int): Single<List<Pattern>> {
        return Single.fromCallable({
            val reader = BufferedReader(InputStreamReader(context.assets.open("patterns")))
            val patterns: List<Pattern> = gson.fromJson(reader, Array<Pattern>::class.java)
                    .filter({ it.positions.size == framesCount })
                    .toList()
            patterns
        })
    }
}