package me.itchallenges.collageapp.video

import android.media.MediaRecorder
import android.view.WindowManager
import me.itchallenges.collageapp.R


class VideoPresenter(private val view: VideoView,
                     private val previewCameraInteractor: PreviewCameraInteractor,
                     private val releaseCameraInteractor: ReleaseCameraInteractor,
                     private val startCapturingVideoInteractor: StartCapturingVideoInteractor,
                     private val stopCapturingVideoInteractor: StopCapturingVideoInteractor,
                     private val windowManager: WindowManager,
                     private val mediaRecorder: MediaRecorder) {

    fun onStart() {
        previewCameraInteractor
                .execute({ view.showCameraPreview(it) }, {
                    it.printStackTrace()
                    view.showMessage(view.context().getString(R.string.error_camera_init))
                }, PreviewCameraInteractor.Params(windowManager))
    }

    fun onStop() {
        releaseCameraInteractor
                .execute({}, {
                    it.printStackTrace()
                }, ReleaseCameraInteractor.Params(view.getPreviewCamera()))
    }

    fun onStartRecordingClicked() {
        startCapturingVideoInteractor
                .execute({}, {
                    it.printStackTrace()
                    view.showMessage(view.context().getString(R.string.error_camera_init))
                }, StartCapturingVideoInteractor.Params(view.getPreviewCamera()!!, mediaRecorder))
    }

    fun onStopRecordingClicked() {
        stopCapturingVideoInteractor
                .execute({
                    view.showMessage("Success!")//TODO
                }, {
                    it.printStackTrace()
                    view.showMessage(view.context().getString(R.string.error_camera_init))//TODO
                }, StopCapturingVideoInteractor.Params(mediaRecorder))
    }

    fun onNavigateNextClicked() {
        view.navigateNext()
    }
}