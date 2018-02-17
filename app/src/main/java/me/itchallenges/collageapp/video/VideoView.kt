package me.itchallenges.collageapp.video

import android.hardware.Camera
import me.itchallenges.collageapp.BaseView
import me.itchallenges.collageapp.PermissionView


interface VideoView : BaseView, PermissionView {

    fun getPreviewCamera(): Camera?

    fun startCameraPreview(camera: Camera)

    fun stopCameraPreview()
}