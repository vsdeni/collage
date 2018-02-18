package me.itchallenges.collageapp.browse

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent


class BrowseScreenPresenter(private val view: BrowseScreenView,
                            private val getCollageInteractor: GetCollageInteractor) : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private fun loadCollage() {
        getCollageInteractor
                .execute({ image -> view.showCollage(image) }, {
                    it.printStackTrace()
                    view.showMessage(it.message!!)
                })
    }
}