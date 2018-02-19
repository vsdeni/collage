package me.itchallenges.collageapp.filter

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import me.itchallenges.collageapp.R
import javax.inject.Inject


class FilterScreenPresenter
@Inject constructor(private val getFilterCollageInteractor: GetFilterCollageInteractor,
                    private val getFiltersInteractor: GetFiltersInteractor,
                    private val saveFiltersInteractor: SaveImageInteractor) : LifecycleObserver {

    lateinit var view: FilterScreenView

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private fun loadFilters() {
        view.showLoader()
        getFiltersInteractor.execute({ filters ->
            view.hideLoader()
            val checkedFilters = getFiltersAppliedToCheckedCells()
            when {
                checkedFilters.size > 1 -> {
                    //different filters in the checked cells, all should be discarded in order
                    //to avoid ambiguity
                    view.showFiltersPicker(filters, Filter.NONE)
                }
                checkedFilters.size == 1 -> {
                    //only one filter is in the checked cells, nothing should be changed
                    view.showFiltersPicker(filters, checkedFilters[0])
                }
                else -> view.showFiltersPicker(filters, Filter.NONE)
            }
        }, {
            view.showMessage(view.context().getString(R.string.error_filter_loading))
            view.hideLoader()
            it.printStackTrace()
        })
    }

    fun onFilterChanged() {
        loadCollage()
    }

    fun onCheckedChanged() {
        loadFilters()
    }

    fun onNavigateNextClicked() {
        view.showLoader()
        saveFiltersInteractor
                .execute({
                    view.hideLoader()
                    view.navigateNext()
                }, {
                    view.showMessage(view.context().getString(R.string.error_filter_saving))
                    view.hideLoader()
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

    private fun getFiltersForAllCells(checked: BooleanArray): Array<Filter> {
        val filters = view.getAppliedFilters()
        return (if (!filters.isEmpty()) {
            filters
        } else {
            //no filters applied, therefore all are `None` filter
            Array(checked.size, { Filter.NONE })
        }).mapIndexed({ index, filter ->
            if (checked[index]) {
                //if a cell is checked, the new filter should be applied to it
                view.getSelectedFilter()
            } else {
                //if a cell is not checked, nothing should be changed
                filter
            }
        }).toTypedArray()
    }

    private fun loadCollage() {
        view.showLoader()
        getFilterCollageInteractor
                .execute({ collage ->
                    view.hideLoader()
                    val checked = getChecked(collage.images.size)
                    val filters = getFiltersForAllCells(checked)
                    view.showCollagePreview(
                            collage,
                            filters,
                            checked)
                },
                        {
                            view.hideLoader()
                            it.printStackTrace()
                        })
    }

    private fun getChecked(size: Int): BooleanArray {
        val checked = view.getCheckedCells()
        return if (!checked.isEmpty()) {
            checked
        } else {
            BooleanArray(size, { true })//if the array is empty this is initial display, all should be selected
        }
    }
}