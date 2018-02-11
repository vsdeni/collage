package me.itchallenges.collageapp.video

import android.graphics.Bitmap
import com.urancompany.indoorapp.interactor.UseCase
import io.reactivex.Completable
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream


class SaveFramesInteractor : UseCase.RxCompletable<SaveFramesInteractor.Params>() {

    override fun build(params: SaveFramesInteractor.Params?): Completable {
        return Completable.fromCallable({
            val frames = params!!.frames
            val dir = params.dirToSave
            dir.deleteRecursively()
            dir.mkdirs()
            for ((i, frame) in frames.withIndex()) {
                val bos = ByteArrayOutputStream()
                frame.compress(Bitmap.CompressFormat.JPEG, 100, bos)
                val fos = FileOutputStream(File(params.dirToSave, i.toString() + ".jpg"))
                fos.write(bos.toByteArray())
                fos.flush()
                fos.close()
            }
        })
    }

    data class Params(val frames: List<Bitmap>, val dirToSave: File)
}