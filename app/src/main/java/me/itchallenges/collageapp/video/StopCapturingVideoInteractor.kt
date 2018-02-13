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
                    .andThen({
                        settingsRepository
                                .getFileToSaveVideo()
                                .flatMapCompletable({
                                    splitVideoToFrames(it, MediaMetadataRetriever())
                                            .toList()
                                            .flatMap({ list ->
                                                settingsRepository.getDirToSaveFrames()
                                                        .map { Pair(list, it) }
                                            })
                                            .flatMapCompletable({ saveFrames(it.first, it.second) })
                                }).subscribe({}, { it.printStackTrace() })
                    }).compose(scheduler.highPriorityCompletable())


    private fun releaseCamera(recorder: MediaRecorder): Completable {
        return Completable.fromCallable({
            recorder.stop()
            recorder.reset()
            recorder.release()
        })
    }

    private fun splitVideoToFrames(videoFile: File, retriever: MediaMetadataRetriever): Observable<Bitmap> {
        return Observable.create({ emitter ->
            try {
                retriever.setDataSource(videoFile.path)
                val dur = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                val duration: Int = Integer.parseInt(dur) / 1000
                for (second: Int in 1 until 6) {//TODO
                    emitter.onNext(retriever.getFrameAtTime(second.toLong(), MediaMetadataRetriever.OPTION_CLOSEST))
                }
                emitter.onComplete()
            } finally {
                retriever.release()
            }
        })
    }

    private fun saveFrames(frames: List<Bitmap>, dir: File): Completable =
            collageRepository
                    .saveFrames(frames, dir)

    data class Params(val mediaRecorder: MediaRecorder)
}