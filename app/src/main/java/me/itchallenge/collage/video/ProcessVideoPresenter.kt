package me.itchallenge.collage.video


class ProcessVideoPresenter(val view: ProcessVideoView) {

    fun onStart() {
        view.prepareCamera()
    }

    fun onCameraReady() {
        view.showCameraPreview()
    }

    fun onStop() {
        view.releaseCamera()
    }

    fun onCameraError(e: Exception) {

    }

    fun onStartRecordingClicked() {

    }

    fun onStopRecordingClicked() {

    }
}