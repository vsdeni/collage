package me.itchallenges.collageapp.video

import android.Manifest
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.view.WindowManager
import me.itchallenges.collageapp.R


class VideoScreenPresenter(private val view: VideoScreenView,
                           private val previewCameraInteractor: PreviewCameraInteractor,
                           private val releaseCameraInteractor: ReleaseCameraInteractor,
                           private val startCapturingVideoInteractor: StartCapturingVideoInteractor,
                           private val stopCapturingVideoInteractor: StopCapturingVideoInteractor,
                           private val validateFramesInteractor: ValidateFramesInteractor,
                           private val windowManager: WindowManager,
                           private var mediaRecorder: MediaRecorder? = null) : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private fun startCameraPreview() {
        if (view.isAccessGranted(Manifest.permission.CAMERA)) {
            previewCameraInteractor
                    .execute({
                        changeRecordingButtonsMode(false)
                        view.startCameraPreview(it)
                    }, {
                        it.printStackTrace()
                        view.showMessage(view.context().getString(R.string.error_camera_init))
                    }, PreviewCameraInteractor.Params(windowManager))
        } else {
            requestCameraPermission()
        }
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
        changeRecordingButtonsMode(true)
        startCapturingVideoInteractor
                .execute({}, {
                    it.printStackTrace()
                    if (it is SecurityException) {
                        requestStoragePermission()
                    } else {
                        view.showMessage(view.context().getString(R.string.error_camera_init))
                    }
                }, StartCapturingVideoInteractor.Params(view.getPreviewCamera()!!,
                        getMediaRecorder()))
    }

    fun onStopRecordingClicked() {
        changeRecordingButtonsMode(false)
        stopCapturingVideoInteractor
                .execute({
                    mediaRecorder = null
                }, {
                    mediaRecorder = null
                    it.printStackTrace()
                    view.showMessage(view.context().getString(R.string.error_camera_init))//TODO
                }, StopCapturingVideoInteractor.Params(mediaRecorder!!))
    }

    private fun requestCameraPermission() {
        view.showPermissionRationale(Manifest.permission.CAMERA,
                view.context().getString(R.string.permission_rationale_camera))
        view.requestPermission(Manifest.permission.CAMERA)
    }

    private fun requestStoragePermission() {
        if (view.shouldShowPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            view.showPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    view.context().getString(R.string.permission_rationale_storage))
        } else {
            view.requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }

    private fun getMediaRecorder(): MediaRecorder {
        if (mediaRecorder == null) {
            mediaRecorder = MediaRecorder()
        }
        return mediaRecorder!!
    }

    fun onNavigateNextClicked() {
        validateFramesInteractor
                .execute({
                    view.navigateNext()
                }, {
                    view.showMessage(it.message
                            ?: view.context().getString(R.string.error_no_frames))
                })
    }

    fun onPermissionRequestResult(permissions: Array<out String>, grantResults: IntArray) {
        if (grantResults.contains(PackageManager.PERMISSION_GRANTED)) {
            if (permissions.contains(Manifest.permission.CAMERA)) {
                startCameraPreview()
            }
        }
    }

    fun onRationaleGrantPermissionClicked(permission: String) {
        if (view.shouldShowPermissionRationale(permission)) {
            view.requestPermission(permission)
        } else {
            view.openAppPermissionSettings()
        }
    }

    private fun changeRecordingButtonsMode(isRecording: Boolean) {
        view.setStartCaptureButtonVisible(!isRecording)
        view.setStopCaptureButtonVisible(isRecording)
    }
}