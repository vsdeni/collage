package me.itchallenges.collageapp.framework

import android.media.MediaRecorder


fun MediaRecorder.stopAndRelease() {
    this.stop()
    this.reset()
    this.release()
}