package me.itchallenges.collageapp.video

import android.hardware.Camera
import android.media.MediaRecorder
import io.reactivex.Completable
import me.itchallenges.collageapp.common.executor.ExecutionScheduler
import me.itchallenges.collageapp.common.interactor.UseCase
import me.itchallenges.collageapp.settings.SettingsRepository
import java.io.File
import javax.inject.Inject


@Suppress("DEPRECATION")
class StartCapturingVideoInteractor
@Inject constructor(private val settingsRepository: SettingsRepository,
                    private val scheduler: ExecutionScheduler) : UseCase.RxCompletable<StartCapturingVideoInteractor.Params>() {

    override fun build(params: Params?): Completable =
            settingsRepository
                    .getFileToSaveVideo()
                    .compose(scheduler.highPrioritySingle())
                    .flatMapCompletable({ startRecording(params!!.camera, params.recorder, it) })
                    .compose(scheduler.highPriorityCompletable())

    private fun startRecording(camera: Camera, recorder: MediaRecorder, file: File): Completable =
            Completable.fromCallable({

                file.mkdirs()

                if (!file.canWrite()) {
                    throw SecurityException()
                }

                if (file.exists()) {
                    file.deleteRecursively()
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