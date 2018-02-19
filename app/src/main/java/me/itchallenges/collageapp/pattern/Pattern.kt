package me.itchallenges.collageapp.pattern

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.net.Uri


data class Pattern(val name: String, val positions: List<Position>, @Transient var preview: Uri? = null) {

    fun drawPreview(previewParams: PreviewParams): Bitmap {
        val bitmap = Bitmap.createBitmap(previewParams.width, previewParams.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        for (position in positions) {
            val rect = Rect()
            rect.top = position.y * (previewParams.height / 10)
            rect.left = position.x * (previewParams.width / 10)
            rect.right = rect.left + position.width * (previewParams.width / 10)
            rect.bottom = rect.top + position.height * (previewParams.height / 10)

            canvas.drawRect(rect, previewParams.fillPaint)
            canvas.drawRect(rect, previewParams.strokePaint)
        }

        return bitmap
    }

    fun drawCollage(collageParams: PreviewParams, bitmaps: Array<Bitmap>): Bitmap {
        val bitmap = Bitmap.createBitmap(collageParams.width, collageParams.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        positions
                .forEachIndexed({ index, position ->
                    val rect = Rect()
                    rect.top = position.y * (collageParams.height / 10)
                    rect.left = position.x * (collageParams.width / 10)
                    rect.right = rect.left + position.width * (collageParams.width / 10)
                    rect.bottom = rect.top + position.height * (collageParams.height / 10)

                    val frameBitmap = Bitmap.createBitmap(bitmaps[index])

                    canvas.drawBitmap(frameBitmap, null, rect, collageParams.fillPaint)
                    canvas.drawRect(rect, collageParams.strokePaint)
                })

        return bitmap
    }
}

data class Position(val x: Int, val y: Int, val width: Int, val height: Int)

data class PreviewParams(val width: Int, val height: Int, val fillPaint: Paint, val strokePaint: Paint)