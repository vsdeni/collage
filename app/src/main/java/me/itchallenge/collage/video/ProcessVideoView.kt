package me.itchallenge.collage.video

import android.hardware.Camera
import me.itchallenge.collage.BaseView


interface ProcessVideoView : BaseView {
    fun prepareCamera()

    fun releaseCamera()

    fun getCamera(): Camera?

    fun showCameraPreview()

    fun navigateToNextScreen()
}