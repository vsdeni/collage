package me.itchallenges.collageapp.framework

import android.graphics.Bitmap
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