package me.itchallenges.collageapp.browse

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent


class BrowseScreenPresenter(private val view: BrowseScreenView,
                            private val getCollageImageInteractor: GetCollageImageInteractor,
                            private val getCollageImageForSharingInteractor: GetCollageImageForSharingInteractor) : LifecycleObserver {

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
        getCollageImageForSharingInteractor
                .execute({ image ->
                    view.shareCollageImage(image, view.getCaption())
                }, {
                    it.printStackTrace()
                    view.showMessage(it.message!!)
                })
    }
}