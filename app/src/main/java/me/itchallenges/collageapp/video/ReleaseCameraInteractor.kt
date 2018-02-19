package me.itchallenges.collageapp.video

import android.hardware.Camera
import android.media.MediaRecorder
import io.reactivex.Completable
import me.itchallenges.collageapp.framework.executor.ExecutionScheduler
import me.itchallenges.collageapp.framework.interactor.UseCase
import me.itchallenges.collageapp.framework.stopAndRelease
import javax.inject.Inject


class ReleaseCameraInteractor
@Inject constructor(private val scheduler: ExecutionScheduler) : UseCase.RxCompletable<ReleaseCameraInteractor.Params>() {

    override fun build(params: Params?): Completable {
        return releaseRecorder(params!!.mediaRecorder)
                .andThen(releaseCamera(params.camera))
                .compose(scheduler.highPriorityCompletable())
    }

    private fun releaseRecorder(mediaRecorder: MediaRecorder?): Completable {
        return Completable.fromCallable({
            mediaRecorder?.let {
                mediaRecorder.stopAndRelease()
            }
        })
    }

    private fun releaseCamera(camera: Camera?): Completable {
        return Completable.fromCallable({
            camera?.let {
                camera.release()
            }
        })
    }

    @Suppress("DEPRECATION")
    data class Params(val camera: Camera?, val mediaRecorder: MediaRecorder?)
}