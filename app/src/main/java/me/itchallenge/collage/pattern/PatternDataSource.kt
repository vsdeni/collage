package me.itchallenge.collage.pattern

import android.content.Context
import com.google.gson.Gson
import io.reactivex.Single
import java.io.BufferedReader
import java.io.InputStreamReader


class PatternDataSource(private val context: Context,
                        private val gson: Gson) : PatternRepository {

    override fun getPatterns(): Single<Array<Pattern>> {
        return Single.fromCallable({
            val reader = BufferedReader(InputStreamReader(context.assets.open("patterns")))
            gson.fromJson(reader, Array<Pattern>::class.java)
        })
    }
}