package me.itchallenges.collageapp.video

import android.hardware.Camera
import me.itchallenges.collageapp.BaseScreenView
import me.itchallenges.collageapp.PermissionScreenView


interface VideoScreenView : BaseScreenView, PermissionScreenView {

    fun getPreviewCamera(): Camera?

    fun startCameraPreview(camera: Camera)

    fun stopCameraPreview()

    fun setStartCaptureButtonVisible(visible: Boolean)

    fun setStopCaptureButtonVisible(visible: Boolean)
}