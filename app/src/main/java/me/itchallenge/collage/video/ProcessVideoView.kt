package me.itchallenge.collage.video

import android.content.Context
import android.hardware.Camera
import me.itchallenge.collage.BaseView


interface ProcessVideoView : BaseView {

    fun getPreviewCamera(): Camera?

    fun showCameraPreview(camera: Camera)

    fun navigateToNextScreen()

    fun context(): Context
}