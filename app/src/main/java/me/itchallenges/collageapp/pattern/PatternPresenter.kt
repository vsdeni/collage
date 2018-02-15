package me.itchallenges.collageapp.pattern


class PatternPresenter(private val view: PatternView,
                       private val getPatternsInteractor: GetPatternsInteractor,
                       private val getFramesInteractor: GetFramesInteractor,
                       private val saveSelectedPatternInteractor: SaveSelectedPatternInteractor) {

    fun loadPatterns() {
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