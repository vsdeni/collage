package me.itchallenge.collage.video

import android.content.Context
import android.hardware.Camera
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
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

        presenter = ProcessVideoPresenter(this,
                PrepareCameraInteractor(),
                ReleaseCameraInteractor(),
                windowManager)

        presenter.onStart()
    }

    override fun getPreviewCamera(): Camera? =
            camera

    override fun onPause() {
        super.onPause()
        presenter.onStop()
    }

    override fun showCameraPreview(camera: Camera) {
        val preview = findViewById<FrameLayout>(R.id.camera_preview)
        if (preview.childCount == 0) {
            val cameraPreview = CameraPreview(this, camera)
            preview.addView(cameraPreview)
        }
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

    override fun navigateToNextScreen() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun context(): Context =
            this

}
