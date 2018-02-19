package me.itchallenges.collageapp.video

import android.hardware.Camera
import android.media.MediaRecorder
import io.reactivex.Completable
import me.itchallenges.collageapp.framework.executor.ExecutionScheduler
import me.itchallenges.collageapp.framework.getRotation
import me.itchallenges.collageapp.framework.interactor.UseCase
import me.itchallenges.collageapp.settings.SettingsRepository
import java.io.File
import javax.inject.Inject


@Suppress("DEPRECATION")
class StartCapturingVideoInteractor
@Inject constructor(private val settingsRepository: SettingsRepository,
                    private val scheduler: ExecutionScheduler) : UseCase.RxCompletable<StartCapturingVideoInteractor.Params>() {

    override fun build(params: Params?): Completable =
            settingsRepository
                    .getCameraId()
                    .flatMap({ cameraId ->
                        settingsRepository
                                .getFileToSaveVideo().map { file -> Pair(cameraId, file) }
                    })
                    .compose(scheduler.highPrioritySingle())
                    .flatMapCompletable({ settings ->
                        startRecording(params!!.camera, params.recorder, params.windowRotation, settings.first, settings.second)
                    })
                    .compose(scheduler.highPriorityCompletable())

    private fun startRecording(camera: Camera, recorder: MediaRecorder, windowRotation: Int, cameraId: Int, file: File): Completable =
            Completable.fromCallable({

                recorder.setOrientationHint(camera.getRotation(windowRotation, cameraId))

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

    data class Params(val camera: Camera, val recorder: MediaRecorder, val windowRotation: Int)
}