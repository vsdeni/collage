package me.itchallenges.collageapp.pattern

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import javax.inject.Inject


class PatternScreenPresenter
@Inject constructor(private val getPatternsInteractor: GetPatternsInteractor,
                    private val getFramesInteractor: GetFramesInteractor,
                    private val saveSelectedPatternInteractor: SaveSelectedPatternInteractor) : LifecycleObserver {

    lateinit var view: PatternScreenView

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private fun loadPatterns() {
        getPatternsInteractor
                .execute({ patterns ->
                    view.showPatternsPicker(patterns, null)
                },
                        { it.printStackTrace() })
    }

    private fun loadFrames() {
        view.getSelectedPattern()?.let {
            getFramesInteractor
                    .execute({ list -> view.showCollagePreview(it, list) },
                            { it.printStackTrace() })
        }
    }

    fun onPatternChanged() {
        loadFrames()
    }

    fun onNavigateNextClicked() {
        view.getSelectedPattern()?.let {
            saveSelectedPatternInteractor
                    .execute({
                        view.navigateNext()
                    }, {
                        it.printStackTrace()
                    }, SaveSelectedPatternInteractor.Params(it))
        }
    }

}