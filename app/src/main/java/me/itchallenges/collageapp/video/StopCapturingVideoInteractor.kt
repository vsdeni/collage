package me.itchallenges.collageapp.video

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.media.MediaRecorder
import io.reactivex.Completable
import io.reactivex.Observable
import me.itchallenges.collageapp.collage.CollageRepository
import me.itchallenges.collageapp.common.executor.ExecutionScheduler
import me.itchallenges.collageapp.common.interactor.UseCase
import me.itchallenges.collageapp.extentions.stopAndRelease
import me.itchallenges.collageapp.settings.SettingsRepository
import java.io.File
import javax.inject.Inject

class StopCapturingVideoInteractor
@Inject constructor(private val settingsRepository: SettingsRepository,
                    private val collageRepository: CollageRepository,
                    private val scheduler: ExecutionScheduler) : UseCase.RxCompletable<StopCapturingVideoInteractor.Params>() {

    override fun build(params: Params?): Completable =
            releaseRecorder(params!!.mediaRecorder)
                    .andThen(
                            settingsRepository
                                    .getFileToSaveVideo()
                    ).flatMap({ fileToSave ->
                settingsRepository.getCollageImagesCount().map { Pair(fileToSave, it) }
            })
                    .flatMap({ settings ->
                        splitVideoToFrames(settings.first, MediaMetadataRetriever(), settings.second)
                                .toList()
                    })
                    .flatMapCompletable({ saveFrames(it) })
                    .compose(scheduler.highPriorityCompletable())


    private fun releaseRecorder(recorder: MediaRecorder): Completable {
        return Completable.fromCallable({
            recorder.stopAndRelease()
        })
    }

    private fun splitVideoToFrames(videoFile: File, retriever: MediaMetadataRetriever, framesCount: Int): Observable<Bitmap> {
        return Observable.create({ emitter ->
            try {
                retriever.setDataSource(videoFile.path)
                val duration = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION))
                val framesInterval = duration / framesCount
                (0 until framesCount)
                        .map { retriever.getFrameAtTime((it * framesInterval * 1000).toLong(), MediaMetadataRetriever.OPTION_CLOSEST) }
                        .filter { it != null }
                        .forEach { emitter.onNext(it) }
                emitter.onComplete()
            } finally {
                retriever.release()
            }
        })
    }

    private fun saveFrames(frames: List<Bitmap>): Completable =
            collageRepository
                    .saveFrames(frames)

    data class Params(val mediaRecorder: MediaRecorder)
}