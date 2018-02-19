package me.itchallenges.collageapp.video

import android.hardware.Camera
import android.view.Surface
import android.view.WindowManager
import io.reactivex.Single
import me.itchallenges.collageapp.common.executor.ExecutionScheduler
import me.itchallenges.collageapp.common.interactor.UseCase

@Suppress("DEPRECATION")
class PreviewCameraInteractor(private val scheduler: ExecutionScheduler) : UseCase.RxSingle<Camera, PreviewCameraInteractor.Params>() {

    override fun build(params: Params?): Single<Camera> {
        return Single.fromCallable({
            val camera = Camera.open()
            val info = Camera.CameraInfo()
            Camera.getCameraInfo(0, info)
            camera!!.setDisplayOrientation(getCorrectCameraOrientation(params!!.windowManager, info))
            camera
        }).compose(scheduler.highPrioritySingle())
    }

    private fun getCorrectCameraOrientation(windowManager: WindowManager, info: Camera.CameraInfo): Int {
        val rotation: Int = windowManager.defaultDisplay.rotation
        var degrees = 0

        when (rotation) {
            Surface.ROTATION_0 -> degrees = 0
            Surface.ROTATION_90 -> degrees = 90
            Surface.ROTATION_180 -> degrees = 180
            Surface.ROTATION_270 -> degrees = 270
        }

        var result: Int
        result = if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            (360 - (info.orientation + degrees) % 360) % 360
        } else {
            (info.orientation - degrees + 360) % 360
        }

        return result
    }

    data class Params(val windowManager: WindowManager)
}