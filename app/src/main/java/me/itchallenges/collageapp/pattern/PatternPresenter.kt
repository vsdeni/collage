package me.itchallenges.collageapp.pattern


class PatternPresenter(private val view: PatternView,
                       private val getPatternsInteractor: GetPatternsInteractor,
                       private val getFramesInteractor: GetFramesInteractor) {

    fun loadPatterns() {
        getPatternsInteractor
                .execute({ patterns -> view.showPatternsPicker(patterns, null) },
                        { it.printStackTrace() })
    }

    private fun loadFrames() {
        getFramesInteractor
                .execute({ list -> view.showPatternPreview(view.getSelectedPattern(), list) },
                        { it.printStackTrace() })
    }

    fun onPatternChanged() {
        loadFrames()
    }

}