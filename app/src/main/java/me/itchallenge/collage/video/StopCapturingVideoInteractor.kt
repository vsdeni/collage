package me.itchallenge.collage.video

import android.media.MediaRecorder
import com.urancompany.indoorapp.interactor.UseCase
import io.reactivex.Completable

class StopCapturingVideoInteractor : UseCase.RxCompletable<StopCapturingVideoInteractor.Params>() {

    override fun build(params: Params?): Completable {
        return Completable.fromCallable({
            val recorder = params!!.mediaRecorder
            recorder.stop()
            recorder.reset()
            recorder.release()
        })
    }

    data class Params(val mediaRecorder: MediaRecorder)
}