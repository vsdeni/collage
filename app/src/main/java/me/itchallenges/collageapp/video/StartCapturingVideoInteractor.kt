package me.itchallenges.collageapp.video

import android.hardware.Camera
import android.media.MediaRecorder
import com.urancompany.indoorapp.executor.ExecutionScheduler
import com.urancompany.indoorapp.interactor.UseCase
import io.reactivex.Completable
import me.itchallenges.collageapp.settings.SettingsRepository
import java.io.File


@Suppress("DEPRECATION")
class StartCapturingVideoInteractor(
        private val settingsRepository: SettingsRepository,
        private val scheduler: ExecutionScheduler) :
        UseCase.RxCompletable<StartCapturingVideoInteractor.Params>() {

    override fun build(params: Params?): Completable =
            settingsRepository
                    .getFileToSaveVideo()
                    .compose(scheduler.highPrioritySingle())
                    .flatMapCompletable({ startRecording(params!!.camera, params.recorder, it) })
                    .compose(scheduler.highPriorityCompletable())

    private fun startRecording(camera: Camera, recorder: MediaRecorder, file: File): Completable =
            Completable.fromCallable({
                if (file.exists()) {
                    file.delete()
                }

                camera.unlock()
                recorder.setCamera(camera)
                recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA)
                recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                recorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264)
                recorder.setOutputFile(file.path)
                recorder.prepare()
                recorder.start()
            })

    data class Params(val camera: Camera, val recorder: MediaRecorder)
}