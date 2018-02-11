package me.itchallenges.collageapp.video

import android.content.Context
import android.hardware.Camera
import me.itchallenges.collageapp.BaseView


interface VideoView : BaseView {

    fun getPreviewCamera(): Camera?

    fun showCameraPreview(camera: Camera)

    fun navigateToNextScreen()

    fun context(): Context
}