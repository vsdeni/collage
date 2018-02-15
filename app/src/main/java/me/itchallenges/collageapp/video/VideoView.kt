package me.itchallenges.collageapp.video

import android.hardware.Camera
import me.itchallenges.collageapp.BaseView


interface VideoView : BaseView {

    fun getPreviewCamera(): Camera?

    fun startCameraPreview(camera: Camera)

    fun stopCameraPreview()
}