package me.itchallenges.collageapp.video

import android.hardware.Camera
import io.reactivex.Completable
import me.itchallenges.collageapp.common.executor.ExecutionScheduler
import me.itchallenges.collageapp.common.interactor.UseCase


class ReleaseCameraInteractor(private val scheduler: ExecutionScheduler) : UseCase.RxCompletable<ReleaseCameraInteractor.Params>() {

    override fun build(params: Params?): Completable {
        return Completable.fromCallable({
            val camera = params!!.camera
            params.camera?.let {
                camera!!.release()
            }
        }).compose(scheduler.highPriorityCompletable())
    }

    @Suppress("DEPRECATION")
    data class Params(val camera: Camera?)
}