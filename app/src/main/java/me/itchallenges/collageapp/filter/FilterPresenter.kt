package me.itchallenges.collageapp.filter


class FilterPresenter(private val view: FilterView,
                      private val getCollageInteractor: GetCollageInteractor,
                      private val getFiltersInteractor: GetFiltersInteractor) {

    fun loadFilters() {
        getFiltersInteractor.execute({ view.showFiltersPicker(it, null) },
                { it.printStackTrace() })
    }

    fun loadCollage() {
        getCollageInteractor
                .execute({ view.showCollagePreview(it) }, {
                    it.printStackTrace()
                })
    }

    fun onFilterChanged() {
        loadCollage()
    }
}