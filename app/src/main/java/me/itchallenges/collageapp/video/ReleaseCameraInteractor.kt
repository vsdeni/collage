package me.itchallenges.collageapp.video

import android.hardware.Camera
import android.media.MediaRecorder
import io.reactivex.Completable
import me.itchallenges.collageapp.common.executor.ExecutionScheduler
import me.itchallenges.collageapp.common.interactor.UseCase
import me.itchallenges.collageapp.extentions.stopAndRelease


class ReleaseCameraInteractor(private val scheduler: ExecutionScheduler) : UseCase.RxCompletable<ReleaseCameraInteractor.Params>() {

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