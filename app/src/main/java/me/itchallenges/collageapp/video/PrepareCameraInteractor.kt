package me.itchallenges.collageapp.video

import android.hardware.Camera
import io.reactivex.Single
import me.itchallenges.collageapp.framework.executor.ExecutionScheduler
import me.itchallenges.collageapp.framework.getRotation
import me.itchallenges.collageapp.framework.interactor.UseCase
import me.itchallenges.collageapp.settings.SettingsRepository
import javax.inject.Inject

@Suppress("DEPRECATION")
class PrepareCameraInteractor
@Inject constructor(private val settingsRepository: SettingsRepository,
                    private val scheduler: ExecutionScheduler) : UseCase.RxSingle<Camera, PrepareCameraInteractor.Params>() {

    override fun build(params: Params?): Single<Camera> {
        return settingsRepository.getCameraId()
                .flatMap({ cameraId ->
                    Single.fromCallable({
                        val camera = Camera.open()
                        val rotateDegrees = camera.getRotation(params!!.windowRotation, cameraId)
                        camera!!.setDisplayOrientation(rotateDegrees)
                        camera.parameters.setRotation(rotateDegrees)
                        camera
                    })
                })
                .compose(scheduler.highPrioritySingle())
    }


    data class Params(val windowRotation: Int)
}