package me.itchallenges.collageapp.browse

import android.net.Uri
import com.urancompany.indoorapp.executor.ExecutionScheduler
import com.urancompany.indoorapp.interactor.UseCase
import io.reactivex.Single
import me.itchallenges.collageapp.collage.CollageRepository


class GetCollageImageInteractor(private val collageRepository: CollageRepository,
                                private val executionScheduler: ExecutionScheduler) : UseCase.RxSingle<Uri, UseCase.None>() {

    override fun build(params: None?): Single<Uri> {
        return collageRepository.getCollageImage()
                .compose(executionScheduler.highPrioritySingle())
    }
}