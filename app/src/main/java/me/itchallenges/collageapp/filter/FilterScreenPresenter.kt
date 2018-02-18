package me.itchallenges.collageapp.filter

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent


class FilterScreenPresenter(private val view: FilterScreenView,
                            private val getFilterCollageInteractor: GetFilterCollageInteractor,
                            private val getFiltersInteractor: GetFiltersInteractor,
                            private val saveFiltersInteractor: SaveImageInteractor) : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private fun loadFilters() {
        getFiltersInteractor.execute({ filters ->
            val checkedFilters = getFiltersAppliedToCheckedCells()
            when {
                checkedFilters.size > 1 -> view.showFiltersPicker(filters, Filter.NONE)
                checkedFilters.size == 1 -> view.showFiltersPicker(filters, checkedFilters[0])
                else -> view.showFiltersPicker(filters, Filter.NONE)
            }
        }, { it.printStackTrace() })
    }

    fun onFilterChanged() {
        loadCollage()
    }

    fun onCheckedChanged() {
        loadFilters()
    }

    fun onNavigateNextClicked() {
        saveFiltersInteractor
                .execute({
                    view.navigateNext()
                }, {
                    it.printStackTrace()
                }, SaveImageInteractor.Params(view.getFilteredImages(), view.getAppliedFilters()))
    }

    private fun getFiltersAppliedToCheckedCells(): List<Filter> {
        val checked = view.getCheckedCells()
        val filters = view.getAppliedFilters()

        val checkedFilters: MutableList<Filter> = mutableListOf()
        filters.forEachIndexed({ index, filter ->
            if (checked[index]) {
                if (!checkedFilters.contains(filter)) {
                    checkedFilters.add(filter)
                }
            }
        })
        return checkedFilters
    }

    private fun loadCollage() {
        getFilterCollageInteractor
                .execute({ collage ->
                    val checked = getChecked(collage.images.size)
                    val filters = getFilters(checked)
                    view.showCollagePreview(
                            collage,
                            filters,
                            checked)
                },
                        { it.printStackTrace() })
    }

    private fun getChecked(size: Int): BooleanArray {
        val checked = view.getCheckedCells()
        return if (!checked.isEmpty()) {
            checked
        } else {
            BooleanArray(size, { true })//if the array is empty this is initial display, // all should be selected
        }
    }

    private fun getFilters(checked: BooleanArray): Array<Filter> {//TODO
        val filters = view.getAppliedFilters()
        return (if (!filters.isEmpty()) {
            filters
        } else {
            Array(checked.size, { Filter.NONE })
        }).mapIndexed({ index, filter ->
            if (checked[index]) {
                view.getSelectedFilter()
            } else {
                filter
            }
        }).toTypedArray()
    }
}