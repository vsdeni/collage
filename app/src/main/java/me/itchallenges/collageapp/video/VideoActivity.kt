package me.itchallenges.collageapp.video

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Camera
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.Surface
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_video.*
import me.itchallenges.collageapp.R
import me.itchallenges.collageapp.di.Injector
import me.itchallenges.collageapp.pattern.PatternActivity
import javax.inject.Inject


@Suppress("DEPRECATION")
class VideoActivity : AppCompatActivity(), VideoScreenView {
    private var camera: Camera? = null

    @Inject
    lateinit var presenter: VideoScreenPresenter

    private var menuNext: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)

        Injector.INSTANCE.appComponent.inject(this)

        initPresenter()
        initViews()
    }

    private fun initPresenter() {
        presenter.view = this
        presenter.windowManager = windowManager
        lifecycle.addObserver(presenter)
    }

    private fun initViews() {
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setTitle(R.string.screen_video_title)
        supportActionBar?.setSubtitle(R.string.screen_video_subtitle)
        supportActionBar?.setLogo(R.mipmap.ic_launcher)
        placeholder_no_access.visibility = View.GONE
        start_recording.setOnClickListener { presenter.onStartRecordingClicked() }
        stop_recording.setOnClickListener { presenter.onStopRecordingClicked() }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.video_menu, menu)
        menuNext = menu.findItem(R.id.menu_next)
        return true
    }

    override fun getPreviewCamera(): Camera? =
            camera

    override fun getWindowRotation(): Int {
        val rotation: Int = windowManager.defaultDisplay.rotation
        var degrees = 0

        when (rotation) {
            Surface.ROTATION_0 -> degrees = 0
            Surface.ROTATION_90 -> degrees = 90
            Surface.ROTATION_180 -> degrees = 180
            Surface.ROTATION_270 -> degrees = 270
        }
        return degrees
    }

    override fun startCameraPreview(camera: Camera) {
        placeholder_no_access.visibility = View.GONE
        this.camera = camera
        if (camera_preview.childCount == 0) {
            val cameraPreview = CameraPreview(this, camera)
            camera_preview.addView(cameraPreview)
        }
    }

    override fun stopCameraPreview() {
        camera = null
        camera_preview.removeAllViews()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_next -> presenter.onNavigateNextClicked()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showLoader() {
        menuNext?.let {
            it.setActionView(R.layout.progressbar)
            it.expandActionView()
        }
    }

    override fun hideLoader() {
        menuNext?.let {
            it.collapseActionView()
            it.actionView = null
        }
    }

    override fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun navigateNext() {
        startActivity(Intent(this, PatternActivity::class.java))
    }

    override fun context(): Context =
            this

    override fun isAccessGranted(permission: String): Boolean {
        return ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
    }

    override fun requestPermission(permission: String) {
        ActivityCompat.requestPermissions(this,
                arrayOf(permission), PERMISSION_REQUEST_CAMERA)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_REQUEST_CAMERA) {
            presenter.onPermissionRequestResult(permissions, grantResults)
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    override fun shouldShowPermissionRationale(permission: String): Boolean {
        return ActivityCompat.shouldShowRequestPermissionRationale(this, permission)
    }

    override fun showPermissionRationale(permission: String, rationale: String) {
        when (permission) {
            Manifest.permission.CAMERA -> {
                placeholder_no_access.visibility = View.VISIBLE
                no_access_message.text = rationale
                grant_camera_access.setOnClickListener({
                    presenter.onRationaleGrantPermissionClicked(permission)
                })
            }
            Manifest.permission.WRITE_EXTERNAL_STORAGE -> {
                Snackbar.make(root_layout, rationale, Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.grant_access, {
                            presenter.onRationaleGrantPermissionClicked(permission)
                        }).show()
            }
        }
    }

    override fun openAppPermissionSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent)
    }

    override fun setStartRecordingButtonVisible(visible: Boolean) {
        start_recording.visibility = if (visible) {
            View.VISIBLE
        } else {
            View.INVISIBLE
        }
    }

    override fun setStopRecordingButtonVisible(visible: Boolean) {
        stop_recording.visibility = if (visible) {
            View.VISIBLE
        } else {
            View.INVISIBLE
        }
    }

    companion object {
        /**
         * Id to identify a camera permission request.
         */
        const val PERMISSION_REQUEST_CAMERA = 0
    }
}
