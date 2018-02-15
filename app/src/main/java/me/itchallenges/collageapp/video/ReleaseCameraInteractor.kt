package me.itchallenges.collageapp.video

import android.hardware.Camera
import com.urancompany.indoorapp.executor.ExecutionScheduler
import com.urancompany.indoorapp.interactor.UseCase
import io.reactivex.Completable


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