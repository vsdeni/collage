package me.itchallenges.collageapp.video

import android.content.Context
import android.content.Intent
import android.hardware.Camera
import android.media.MediaRecorder
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.Toast
import com.google.gson.Gson
import com.urancompany.indoorapp.executor.ThreadScheduler
import kotlinx.android.synthetic.main.activity_video.*
import me.itchallenges.collageapp.R
import me.itchallenges.collageapp.collage.CollageDataSource
import me.itchallenges.collageapp.pattern.PatternActivity
import me.itchallenges.collageapp.settings.SettingsDataSource


@Suppress("DEPRECATION")
class VideoActivity : AppCompatActivity(), VideoView {
    private var camera: Camera? = null
    private lateinit var presenter: VideoPresenter
    private lateinit var previewView: ViewGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)

        previewView = findViewById(R.id.camera_preview)

        presenter = VideoPresenter(this,
                PreviewCameraInteractor(ThreadScheduler()),
                ReleaseCameraInteractor(ThreadScheduler()),
                StartCapturingVideoInteractor(SettingsDataSource(this), ThreadScheduler()),
                StopCapturingVideoInteractor(SettingsDataSource(this), CollageDataSource(getSharedPreferences(
                        getString(R.string.preference_file_key), Context.MODE_PRIVATE), Gson()), ThreadScheduler()),
                windowManager,
                MediaRecorder())

        lifecycle.addObserver(presenter)

        start_recording.setOnClickListener { presenter.onStartRecordingClicked() }
        stop_recording.setOnClickListener { presenter.onStopRecordingClicked() }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.video_menu, menu)
        return true
    }

    override fun getPreviewCamera(): Camera? =
            camera

    override fun startCameraPreview(camera: Camera) {
        this.camera = camera
        if (previewView.childCount == 0) {
            val cameraPreview = CameraPreview(this, camera)
            previewView.addView(cameraPreview)
        }
    }

    override fun stopCameraPreview() {
        camera = null
        previewView.removeAllViews()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_next -> presenter.onNavigateNextClicked()
        }
        return super.onOptionsItemSelected(item)
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

    override fun navigateNext() {
        startActivity(Intent(this, PatternActivity::class.java))
    }

    override fun context(): Context =
            this

}
