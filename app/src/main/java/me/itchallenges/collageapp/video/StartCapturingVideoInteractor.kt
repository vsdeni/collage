package me.itchallenges.collageapp.video

import android.hardware.Camera
import android.media.MediaRecorder
import com.urancompany.indoorapp.interactor.UseCase
import io.reactivex.Completable
import java.io.File


@Suppress("DEPRECATION")
class StartCapturingVideoInteractor : UseCase.RxCompletable<StartCapturingVideoInteractor.Params>() {

    override fun build(params: Params?): Completable {
        return Completable.fromCallable({
            val camera = params!!.camera
            val recorder = params.mediaRecorder
            val file = params.file

            if (file.exists()) {
                file.delete()
            }

            camera.lock()
            camera.unlock()
            recorder.setCamera(camera)
            recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA)
            recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            recorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264)
            recorder.setOutputFile(file.path)
            recorder.prepare()
            recorder.start()
        })
    }

    data class Params(val camera: Camera, val mediaRecorder: MediaRecorder, val file: File)
}