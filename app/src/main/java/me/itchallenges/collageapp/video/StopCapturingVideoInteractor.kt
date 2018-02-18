package me.itchallenges.collageapp.video

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.media.MediaRecorder
import com.urancompany.indoorapp.executor.ExecutionScheduler
import com.urancompany.indoorapp.interactor.UseCase
import io.reactivex.Completable
import io.reactivex.Observable
import me.itchallenges.collageapp.collage.CollageRepository
import me.itchallenges.collageapp.settings.SettingsRepository
import java.io.File

class StopCapturingVideoInteractor(private val settingsRepository: SettingsRepository,
                                   private val collageRepository: CollageRepository,
                                   private val scheduler: ExecutionScheduler) : UseCase.RxCompletable<StopCapturingVideoInteractor.Params>() {

    override fun build(params: Params?): Completable =
            releaseCamera(params!!.mediaRecorder)
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


    private fun releaseCamera(recorder: MediaRecorder): Completable {
        return Completable.fromCallable({
            recorder.stop()
            recorder.reset()
            recorder.release()
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