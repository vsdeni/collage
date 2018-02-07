package me.itchallenge.collage.video

import android.hardware.Camera
import com.urancompany.indoorapp.interactor.UseCase
import io.reactivex.Completable


class ReleaseCameraInteractor : UseCase.RxCompletable<ReleaseCameraInteractor.Params>() {

    override fun build(params: Params?): Completable {
        return Completable.fromCallable({
            val camera = params!!.camera
            params.camera?.let {
                camera!!.release()
            }
        })
    }

    @Suppress("DEPRECATION")
    data class Params(val camera: Camera?)
}