package me.itchallenges.collageapp.browse

import android.net.Uri
import io.reactivex.Single
import me.itchallenges.collageapp.collage.CollageRepository
import me.itchallenges.collageapp.common.executor.ExecutionScheduler
import me.itchallenges.collageapp.common.interactor.UseCase


class GetCollageImageInteractor(private val collageRepository: CollageRepository,
                                private val executionScheduler: ExecutionScheduler) : UseCase.RxSingle<Uri, UseCase.None>() {

    override fun build(params: None?): Single<Uri> {
        return collageRepository.getCollageImage()
                .compose(executionScheduler.highPrioritySingle())
    }
}