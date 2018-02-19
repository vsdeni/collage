package me.itchallenges.collageapp.framework

import android.graphics.Bitmap
import android.graphics.Matrix
import android.hardware.Camera
import android.media.MediaRecorder
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream


fun MediaRecorder.stopAndRelease() {
    this.stop()
    this.reset()
    this.release()
}

fun Bitmap.convertToFile(file: File) {
    val bos = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.JPEG, 100, bos)
    val fos = FileOutputStream(file)
    fos.write(bos.toByteArray())
    fos.flush()
    fos.close()
}

fun Bitmap.rotate(degree: Int): Bitmap {
    val w = this.width
    val h = this.height

    val mtx = Matrix()
    mtx.postRotate(degree.toFloat())

    return Bitmap.createBitmap(this, 0, 0, w, h, mtx, true)
}

fun Camera.getRotation(windowRotation: Int, cameraId: Int): Int {
    val info = Camera.CameraInfo()
    Camera.getCameraInfo(cameraId, info)


    val result: Int
    result = if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
        (360 - (info.orientation + windowRotation) % 360) % 360
    } else {
        (info.orientation - windowRotation + 360) % 360
    }

    return result
}