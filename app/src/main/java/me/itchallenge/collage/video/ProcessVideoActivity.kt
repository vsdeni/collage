package me.itchallenge.collage.video

import android.hardware.Camera
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Surface
import android.widget.FrameLayout
import android.widget.Toast
import me.itchallenge.collage.R


@Suppress("DEPRECATION")
class ProcessVideoActivity : AppCompatActivity(), ProcessVideoView {
    private var camera: Camera? = null
    private lateinit var presenter: ProcessVideoPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_process)

        presenter = ProcessVideoPresenter(this)
        presenter.onStart()
    }

    override fun prepareCamera() {
        try {
            camera = Camera.open()
            val info = Camera.CameraInfo()
            Camera.getCameraInfo(0, info)
            camera!!.setDisplayOrientation(getCorrectCameraOrientation(info))

            presenter.onCameraReady()
        } catch (e: Exception) {
            presenter.onCameraError(e)
        }
    }

    override fun getCamera(): Camera? =
            camera

    override fun onPause() {
        super.onPause()
        presenter.onStop()
    }

    override fun releaseCamera() {
        camera?.let {
            camera!!.release()
            camera = null
        }
    }

    private fun getCorrectCameraOrientation(info: Camera.CameraInfo): Int {
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

    override fun showLoader() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideLoader() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun showCameraPreview() {
        val preview = findViewById<FrameLayout>(R.id.camera_preview)
        if (preview.childCount == 0) {
            val cameraPreview = CameraPreview(this, camera!!)
            preview.addView(cameraPreview)
        }
    }

    override fun navigateToNextScreen() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
