package me.itchallenges.collageapp.pattern

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.net.Uri


data class Pattern(val name: String, val positions: List<Position>, var preview: Uri?) {

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
}

data class Position(val x: Int, val y: Int, val width: Int, val height: Int)

data class PreviewParams(val width: Int, val height: Int, val fillPaint: Paint, val strokePaint: Paint)