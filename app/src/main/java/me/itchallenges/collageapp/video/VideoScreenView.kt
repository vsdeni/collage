package me.itchallenges.collageapp.video

import android.hardware.Camera
import me.itchallenges.collageapp.framework.view.BaseScreenView
import me.itchallenges.collageapp.framework.view.PermissionScreenView


interface VideoScreenView : BaseScreenView, PermissionScreenView {

    fun getPreviewCamera(): Camera?

    fun getWindowRotation(): Int

    fun startCameraPreview(camera: Camera)

    fun stopCameraPreview()

    fun setStartRecordingButtonVisible(visible: Boolean)

    fun setStopRecordingButtonVisible(visible: Boolean)
}