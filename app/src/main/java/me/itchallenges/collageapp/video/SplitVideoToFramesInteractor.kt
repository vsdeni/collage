package me.itchallenges.collageapp.video

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import com.urancompany.indoorapp.interactor.UseCase
import io.reactivex.Observable
import java.io.File


class SplitVideoToFramesInteractor : UseCase.RxObservable<Bitmap, SplitVideoToFramesInteractor.Params>() {

    override fun build(params: Params?): Observable<Bitmap> {
        return Observable.create({ emitter ->
            val retriever = params!!.retriever
            try {
                retriever.setDataSource(params.videoFile.path)
                val dur = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                val duration: Int = Integer.parseInt(dur) / 1000
                for (second: Int in 1 until duration) {
                    emitter.onNext(retriever.getFrameAtTime(second.toLong(), MediaMetadataRetriever.OPTION_CLOSEST))
                }
                emitter.onComplete()
            } finally {
                retriever.release()
            }
        })
    }

    data class Params(val videoFile: File, val retriever: MediaMetadataRetriever)
}