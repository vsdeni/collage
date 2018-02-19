package me.itchallenges.collageapp.pattern

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.net.Uri


data class Pattern(val name: String, val height: Int, val width: Int, val positions: List<Position>, @Transient var preview: Uri? = null) {

    fun drawPreview(imageParams: ImageParams): Bitmap {
        val bitmap = Bitmap.createBitmap(imageParams.width, imageParams.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        positions
                .forEach({ position ->
                    val rect = Rect()
                    rect.top = position.y * (imageParams.height / height)
                    rect.left = position.x * (imageParams.width / width)
                    rect.right = rect.left + position.width * (imageParams.width / width)
                    rect.bottom = rect.top + position.height * (imageParams.height / height)

                    canvas.drawRect(rect, imageParams.fillPaint)
                    canvas.drawRect(rect, imageParams.strokePaint)
                })

        return bitmap
    }

    fun drawCollage(imageParams: ImageParams, bitmaps: Array<Bitmap>): Bitmap {
        val bitmap = Bitmap.createBitmap(imageParams.width, imageParams.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        positions
                .forEachIndexed({ index, position ->
                    val rect = Rect()
                    rect.top = position.y * (imageParams.height / height)
                    rect.left = position.x * (imageParams.width / width)
                    rect.right = rect.left + position.width * (imageParams.width / height)
                    rect.bottom = rect.top + position.height * (imageParams.height / width)

                    val frameBitmap = Bitmap.createBitmap(bitmaps[index])

                    canvas.drawBitmap(frameBitmap, null, rect, imageParams.fillPaint)
                    canvas.drawRect(rect, imageParams.strokePaint)
                })

        return bitmap
    }
}

data class Position(val x: Int, val y: Int, val width: Int, val height: Int)

data class ImageParams(val width: Int, val height: Int, val fillPaint: Paint, val strokePaint: Paint)