package me.itchallenges.collageapp.video

import android.media.MediaMetadataRetriever
import android.media.MediaRecorder
import android.view.WindowManager
import io.reactivex.android.schedulers.AndroidSchedulers
import me.itchallenges.collageapp.R
import me.itchallenges.collageapp.settings.SettingsRepository


class VideoPresenter(private val view: VideoView,
                     private val prepareCameraInteractor: PrepareCameraInteractor,
                     private val releaseCameraInteractor: ReleaseCameraInteractor,
                     private val startCapturingVideoInteractor: StartCapturingVideoInteractor,
                     private val stopCapturingVideoInteractor: StopCapturingVideoInteractor,
                     private val splitVideoToFramesInteractor: SplitVideoToFramesInteractor,
                     private val saveFramesInteractor: SaveFramesInteractor,
                     private val windowManager: WindowManager,
                     private val mediaRecorder: MediaRecorder,
                     private val settingsRepository: SettingsRepository) {

    fun onStart() {
        prepareCameraInteractor
                .build(PrepareCameraInteractor.Params(windowManager))
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe({ view.showCameraPreview(it) },
                        {
                            it.printStackTrace()
                            view.showMessage(view.context().getString(R.string.error_camera_init))
                        })
    }

    fun onStop() {
        releaseCameraInteractor
                .build(ReleaseCameraInteractor.Params(view.getPreviewCamera()))
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe({}, {
                    it.printStackTrace()
                })
    }

    fun onStartRecordingClicked() {
        settingsRepository
                .getFileToSaveVideo()
                .flatMapCompletable({
                    startCapturingVideoInteractor
                            .build(StartCapturingVideoInteractor.Params(view.getPreviewCamera()!!, mediaRecorder, it))
                }).subscribeOn(AndroidSchedulers.mainThread())
                .subscribe({ }, {
                    it.printStackTrace()
                    view.showMessage(view.context().getString(R.string.error_camera_init))
                })
    }

    fun onStopRecordingClicked() {
        stopCapturingVideoInteractor
                .build(StopCapturingVideoInteractor.Params(mediaRecorder))
                .andThen({
                    settingsRepository
                            .getFileToSaveVideo()
                            .flatMapCompletable({
                                splitVideoToFramesInteractor
                                        .build(SplitVideoToFramesInteractor.Params(it, MediaMetadataRetriever()))
                                        .toList()
                                        .flatMap({ list ->
                                            settingsRepository.getDirToSaveFrames()
                                                    .map { Pair(list, it) }
                                        })
                                        .flatMapCompletable({ saveFramesInteractor.build(SaveFramesInteractor.Params(it.first, it.second)) })
                            }).subscribe({}, { it.printStackTrace() })
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe({ }, {
                    it.printStackTrace()
                    view.showMessage(view.context().getString(R.string.error_camera_init))
                })
    }
}