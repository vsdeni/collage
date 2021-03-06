package me.itchallenges.collageapp.video

import android.Manifest
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.view.WindowManager
import me.itchallenges.collageapp.R
import javax.inject.Inject


class VideoScreenPresenter
@Inject constructor(private val prepareCameraInteractor: PrepareCameraInteractor,
                    private val releaseCameraInteractor: ReleaseCameraInteractor,
                    private val startCapturingVideoInteractor: StartCapturingVideoInteractor,
                    private val stopCapturingVideoInteractor: StopCapturingVideoInteractor,
                    private val validateFramesInteractor: ValidateFramesInteractor) : LifecycleObserver {

    lateinit var view: VideoScreenView
    lateinit var windowManager: WindowManager
    private var mediaRecorder: MediaRecorder? = null

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private fun startCameraPreview() {
        view.setStopRecordingButtonVisible(false)
        view.setStartRecordingButtonVisible(false)
        if (view.isAccessGranted(Manifest.permission.CAMERA)) {
            prepareCameraInteractor
                    .execute({
                        view.setStopRecordingButtonVisible(false)
                        view.setStartRecordingButtonVisible(true)
                        view.startCameraPreview(it)
                    }, {
                        view.setStopRecordingButtonVisible(false)
                        view.setStartRecordingButtonVisible(false)
                        it.printStackTrace()
                        view.showMessage(view.context().getString(R.string.error_camera_init))
                    }, PrepareCameraInteractor.Params(view.getWindowRotation()))
        } else {
            requestCameraPermission()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stopCameraPreview() {
        releaseCameraInteractor
                .execute({
                    mediaRecorder = null
                    view.stopCameraPreview()
                }, {
                    view.stopCameraPreview()
                    it.printStackTrace()
                }, ReleaseCameraInteractor.Params(view.getPreviewCamera(), mediaRecorder))
    }

    fun onStartRecordingClicked() {
        view.setStopRecordingButtonVisible(false)
        view.setStartRecordingButtonVisible(false)
        startCapturingVideoInteractor
                .execute({
                    view.setStopRecordingButtonVisible(true)
                    view.setStartRecordingButtonVisible(false)
                }, {
                    it.printStackTrace()
                    if (it is SecurityException) {
                        view.setStopRecordingButtonVisible(false)
                        view.setStartRecordingButtonVisible(true)
                        requestStoragePermission()
                    } else {
                        view.setStopRecordingButtonVisible(false)
                        view.setStartRecordingButtonVisible(false)
                        view.showMessage(view.context().getString(R.string.error_camera_init))
                    }
                }, StartCapturingVideoInteractor.Params(view.getPreviewCamera()!!,
                        getMediaRecorder(), view.getWindowRotation()))
    }

    fun onStopRecordingClicked() {
        view.showLoader()
        view.setStopRecordingButtonVisible(false)
        view.setStartRecordingButtonVisible(false)
        stopCapturingVideoInteractor
                .execute({
                    view.setStopRecordingButtonVisible(false)
                    view.setStartRecordingButtonVisible(true)
                    mediaRecorder = null
                    view.hideLoader()
                }, {
                    view.hideLoader()
                    view.setStopRecordingButtonVisible(false)
                    view.setStartRecordingButtonVisible(true)
                    mediaRecorder = null
                    it.printStackTrace()
                    view.showMessage(view.context().getString(R.string.error_video_saving))
                }, StopCapturingVideoInteractor.Params(mediaRecorder!!, view.getPreviewCamera()))
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
        view.showLoader()
        validateFramesInteractor
                .execute({
                    view.hideLoader()
                    view.navigateNext()
                }, {
                    view.hideLoader()
                    view.showMessage(view.context().getString(R.string.error_no_frames))
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
}