package me.itchallenges.collageapp.browse

import android.content.Intent
import android.net.Uri
import io.reactivex.Single
import me.itchallenges.collageapp.collage.CollageRepository
import me.itchallenges.collageapp.common.executor.ExecutionScheduler
import me.itchallenges.collageapp.common.interactor.UseCase


class ShareCollageImageInteractor(private val collageRepository: CollageRepository,
                                  private val executionScheduler: ExecutionScheduler) : UseCase.RxSingle<Intent, ShareCollageImageInteractor.Params>() {

    override fun build(params: Params?): Single<Intent> {
        return collageRepository
                .getCollageImageForSharing()
                .flatMap { uri ->
                    createShareIntent(uri, params!!.message)
                }
                .compose(executionScheduler.highPrioritySingle())
    }

    private fun createShareIntent(image: Uri, message: String): Single<Intent> {
        return Single.fromCallable<Intent>({
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.putExtra(Intent.EXTRA_STREAM, image)
            shareIntent.putExtra(Intent.EXTRA_TEXT, message)
            shareIntent.type = "image/jpeg"
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            shareIntent
        })
    }

    data class Params(val message: String)
}