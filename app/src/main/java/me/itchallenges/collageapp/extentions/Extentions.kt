package me.itchallenges.collageapp.extentions

import android.media.MediaRecorder


fun MediaRecorder.stopAndRelease() {
    this.stop()
    this.reset()
    this.release()
}