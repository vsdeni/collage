package me.itchallenges.collageapp.pattern

import android.content.Context
import android.graphics.*
import com.google.gson.Gson
import io.reactivex.Single
import java.io.BufferedReader
import java.io.InputStreamReader


class PatternDataSource(private val context: Context,
                        private val gson: Gson) : PatternRepository {

    override fun getPatterns(): Single<Array<Pattern>> {
        return Single.fromCallable({
            val reader = BufferedReader(InputStreamReader(context.assets.open("patterns")))
            val patterns: Array<Pattern> = gson.fromJson(reader, Array<Pattern>::class.java)
            patterns.forEach { it.preview = getPreview(it) }
            patterns
        })
    }

    private fun getPreview(pattern: Pattern): Bitmap {
        val width = 150
        val height = 150
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val fillPaint = Paint()
        fillPaint.style = Paint.Style.FILL
        fillPaint.color = Color.parseColor("#c6c6c6")

        val strokePaint = Paint()
        strokePaint.style = Paint.Style.STROKE
        strokePaint.strokeWidth = 2.toFloat()
        strokePaint.color = Color.parseColor("#747474")

        for (position in pattern.positions) {
            val rect = Rect()
            rect.top = position.y * (height / 10)
            rect.left = position.x * (width / 10)
            rect.right = rect.left + position.width * (width / 10)
            rect.bottom = rect.top + position.height * (height / 10)

            canvas.drawRect(rect, fillPaint)
            canvas.drawRect(rect, strokePaint)
        }

        return bitmap
    }
}