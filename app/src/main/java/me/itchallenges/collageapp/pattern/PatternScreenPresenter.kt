package me.itchallenges.collageapp.pattern

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import me.itchallenges.collageapp.R
import javax.inject.Inject


class PatternScreenPresenter
@Inject constructor(private val getPatternsInteractor: GetPatternsInteractor,
                    private val getFramesInteractor: GetFramesInteractor,
                    private val saveSelectedPatternInteractor: SaveSelectedPatternInteractor) : LifecycleObserver {

    lateinit var view: PatternScreenView

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private fun loadPatterns() {
        view.showLoader()
        getPatternsInteractor
                .execute({ patterns ->
                    view.hideLoader()
                    view.showPatternsPicker(patterns, null)
                },
                        {
                            view.hideLoader()
                            it.printStackTrace()
                        })
    }

    private fun loadFrames() {
        view.showLoader()
        view.getSelectedPattern()?.let {
            getFramesInteractor
                    .execute({ list ->
                        view.showCollagePreview(it, list)
                        view.hideLoader()
                    },
                            {
                                view.showMessage(view.context().getString(R.string.error_frames_loading))
                                it.printStackTrace()
                                view.hideLoader()
                            })
        }
    }

    fun onPatternChanged() {
        loadFrames()
    }

    fun onNavigateNextClicked() {
        view.showLoader()
        view.getSelectedPattern()?.let {
            saveSelectedPatternInteractor
                    .execute({
                        view.hideLoader()
                        view.navigateNext()
                    }, {
                        view.showMessage(view.context().getString(R.string.error_frames_loading))
                        view.hideLoader()
                        it.printStackTrace()
                    }, SaveSelectedPatternInteractor.Params(it))
        }
    }

}