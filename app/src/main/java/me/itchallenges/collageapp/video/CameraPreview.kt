package me.itchallenges.collageapp.video

import android.content.ContentValues.TAG
import android.content.Context
import android.hardware.Camera
import android.util.Log
import android.view.Gravity
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.FrameLayout
import java.io.IOException


class CameraPreview(context: Context, private val camera: Camera) : SurfaceView(context), SurfaceHolder.Callback {
    private var cameraHolder: SurfaceHolder = holder
    private var supportedPreviewSizes: List<Camera.Size> = camera.parameters.supportedPreviewSizes
    private var optimalSize: Camera.Size? = null

    init {
        cameraHolder.addCallback(this)
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (cameraHolder.surface == null) {
            // preview surface does not exist
            return
        }

        // stop preview before making changes
        try {
            camera.stopPreview()
        } catch (e: Exception) {
            // ignore: tried to stop a non-existent preview
        }

        val parameters = camera.parameters
        optimalSize?.let {
            parameters.setPreviewSize(it.width, it.height)
            camera.parameters = parameters
        }

        cameraSetup(width, height)

        // start preview with new settings
        try {
            camera.setPreviewDisplay(cameraHolder)
            camera.startPreview()

        } catch (e: Exception) {
            Log.d(TAG, "Error starting camera preview: " + e.message)
        }
    }

    private fun getOptimalPreviewSize(sizes: List<Camera.Size>?, w: Int, h: Int): Camera.Size? {
        val ASPECT_TOLERANCE = 0.1
        val targetRatio = h.toDouble() / w

        if (sizes == null) return null

        var optimalSize: Camera.Size? = null
        var minDiff = java.lang.Double.MAX_VALUE

        for (size in sizes) {
            val ratio = size.width.toDouble() / size.height
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue
            if (Math.abs(size.height - h) < minDiff) {
                optimalSize = size
                minDiff = Math.abs(size.height - h).toDouble()
            }
        }

        if (optimalSize == null) {
            minDiff = java.lang.Double.MAX_VALUE
            for (size in sizes) {
                if (Math.abs(size.height - h) < minDiff) {
                    optimalSize = size
                    minDiff = Math.abs(size.height - h).toDouble()
                }
            }
        }
        return optimalSize
    }

    private fun cameraSetup(w: Int, h: Int) {
        // set the camera parameters, including the preview size

        val lp = layoutParams as FrameLayout.LayoutParams
        val cameraAspectRatio: Double = ((optimalSize?.width
                ?: 1).toDouble()) / ((optimalSize?.height
                ?: 1).toDouble())

        if ((h.toDouble() / w) > cameraAspectRatio) {
            lp.width = (h / cameraAspectRatio + 0.5).toInt()
            lp.height = h
        } else {
            lp.height = (w * cameraAspectRatio + 0.5).toInt()
            lp.width = w
            lp.topMargin = (h - lp.height) / 2
        }
        lp.gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP

        layoutParams = lp
        requestLayout()
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        // empty. Take care of releasing the Camera preview in your activity.
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        try {
            camera.setPreviewDisplay(holder)
            camera.startPreview()
        } catch (e: IOException) {
            Log.d(TAG, "Error setting camera preview: " + e.message)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = View.resolveSize(suggestedMinimumWidth, widthMeasureSpec)
        val height = View.resolveSize(suggestedMinimumHeight, heightMeasureSpec)
        setMeasuredDimension(width, height)

        optimalSize = getOptimalPreviewSize(supportedPreviewSizes, width, height)
    }
}