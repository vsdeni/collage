package me.itchallenges.collageapp.browse

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import me.itchallenges.collageapp.R
import javax.inject.Inject


class BrowseScreenPresenter
@Inject constructor(private val getCollageImageInteractor: GetCollageImageInteractor,
                    private val shareCollageImageInteractor: ShareCollageImageInteractor) : LifecycleObserver {

    lateinit var view: BrowseScreenView

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private fun loadCollage() {
        view.showLoader()
        getCollageImageInteractor
                .execute({ image ->
                    view.hideLoader()
                    view.showCollageImage(image)
                }, {
                    view.showMessage(view.context().getString(R.string.error_collage_loading))
                    view.hideLoader()
                    it.printStackTrace()
                    view.showMessage(it.message!!)
                })
    }

    fun onStartOverClicked() {
        view.navigateNext()
    }

    fun onShareClicked() {
        view.showLoader()
        shareCollageImageInteractor
                .execute({
                    view.hideLoader()
                    view.showShareDialog(it)
                }, {
                    view.hideLoader()
                    it.printStackTrace()
                    view.showMessage(view.context().getString(R.string.error_collage_sharing))
                }, ShareCollageImageInteractor.Params(view.getCaption()))
    }
}