package me.itchallenge.collage.video

import android.view.WindowManager
import io.reactivex.android.schedulers.AndroidSchedulers
import me.itchallenge.collage.R


class ProcessVideoPresenter(private val view: ProcessVideoView,
                            private val prepareCameraInteractor: PrepareCameraInteractor,
                            private val releaseCameraInteractor: ReleaseCameraInteractor,
                            private val windowManager: WindowManager) {

    fun onStart() {
        prepareCameraInteractor
                .build(PrepareCameraInteractor.Params(windowManager))
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe({ view.showCameraPreview(it) },
                        { error ->
                            error.printStackTrace()
                            view.showMessage(view.context().getString(R.string.error_camera_init))
                        })
    }

    fun onStop() {
        releaseCameraInteractor
                .build(ReleaseCameraInteractor.Params(view.getPreviewCamera()))
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe({}, { error ->
                    error.printStackTrace()
                })
    }

    fun onStartRecordingClicked() {

    }

    fun onStopRecordingClicked() {

    }
}