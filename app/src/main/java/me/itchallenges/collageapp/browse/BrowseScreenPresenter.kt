package me.itchallenges.collageapp.browse

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import javax.inject.Inject


class BrowseScreenPresenter
@Inject constructor(private val getCollageImageInteractor: GetCollageImageInteractor,
                    private val shareCollageImageInteractor: ShareCollageImageInteractor) : LifecycleObserver {

    lateinit var view: BrowseScreenView

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private fun loadCollage() {
        getCollageImageInteractor
                .execute({ image -> view.showCollageImage(image) }, {
                    it.printStackTrace()
                    view.showMessage(it.message!!)
                })
    }

    fun onStartOverClicked() {
        view.navigateNext()
    }

    fun onShareClicked() {
        shareCollageImageInteractor
                .execute({
                    view.showShareDialog(it)
                }, {
                    it.printStackTrace()
                    view.showMessage(it.message!!)
                }, ShareCollageImageInteractor.Params(view.getCaption()))
    }
}