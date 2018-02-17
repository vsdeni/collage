package me.itchallenges.collageapp.video

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.media.MediaRecorder
import android.view.WindowManager
import me.itchallenges.collageapp.R


class VideoPresenter(private val view: VideoView,
                     private val previewCameraInteractor: PreviewCameraInteractor,
                     private val releaseCameraInteractor: ReleaseCameraInteractor,
                     private val startCapturingVideoInteractor: StartCapturingVideoInteractor,
                     private val stopCapturingVideoInteractor: StopCapturingVideoInteractor,
                     private val validateVideoInteractor: ValidateVideoInteractor,
                     private val windowManager: WindowManager,
                     private var mediaRecorder: MediaRecorder? = null) : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun startCameraPreview() {
        previewCameraInteractor
                .execute({ view.startCameraPreview(it) }, {
                    it.printStackTrace()
                    view.showMessage(view.context().getString(R.string.error_camera_init))
                }, PreviewCameraInteractor.Params(windowManager))
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stopCameraPreview() {
        releaseCameraInteractor
                .execute({
                    view.stopCameraPreview()
                }, {
                    view.stopCameraPreview()
                    it.printStackTrace()
                }, ReleaseCameraInteractor.Params(view.getPreviewCamera()))
    }

    fun onStartRecordingClicked() {
        startCapturingVideoInteractor
                .execute({}, {
                    it.printStackTrace()
                    view.showMessage(view.context().getString(R.string.error_camera_init))
                }, StartCapturingVideoInteractor.Params(view.getPreviewCamera()!!,
                        getMediaRecorder()))
    }

    private fun getMediaRecorder(): MediaRecorder {
        if (mediaRecorder == null) {
            mediaRecorder = MediaRecorder()
        }
        return mediaRecorder!!
    }

    fun onStopRecordingClicked() {
        stopCapturingVideoInteractor
                .execute({
                    mediaRecorder = null
                }, {
                    mediaRecorder = null
                    it.printStackTrace()
                    view.showMessage(view.context().getString(R.string.error_camera_init))//TODO
                }, StopCapturingVideoInteractor.Params(mediaRecorder!!))
    }

    fun onNavigateNextClicked() {
        validateVideoInteractor
                .execute({
                    view.navigateNext()
                }, {
                    view.showMessage(it.message
                            ?: view.context().getString(R.string.error_no_video))
                })
    }
}