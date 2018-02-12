package me.itchallenges.collageapp.pattern


class PatternPresenter(private val view: PatternView,
                       private val getPatternsInteractor: GetPatternsInteractor,
                       private val getImagesInteractor: GetImagesInteractor,
                       private val saveSelectedPatternInteractor: SaveSelectedPatternInteractor) {

    fun loadPatterns() {
        getPatternsInteractor
                .execute({ patterns -> view.showPatternsPicker(patterns, null) },
                        { it.printStackTrace() })
    }

    private fun loadFrames() {
        getImagesInteractor
                .execute({ list -> view.showCollagePreview(view.getSelectedPattern(), list) },
                        { it.printStackTrace() })
    }

    fun onPatternChanged() {
        loadFrames()
    }

    fun onNavigateNextClicked() {
        saveSelectedPatternInteractor
                .execute({
                    view.navigateNext()
                }, {
                    it.printStackTrace()
                }, SaveSelectedPatternInteractor.Params(view.getSelectedPattern()))
    }

}