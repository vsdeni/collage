package me.itchallenges.collageapp.filter

import me.itchallenges.collageapp.BaseView


interface FilterView : BaseView {
    fun showFiltersPicker(filters: List<Filter>, active: Filter?)

    fun getCheckedCells(): BooleanArray

    fun getAppliedFilters(): Array<Filter>

    fun getSelectedFilter(): Filter

    fun showCollagePreview(collage: CollageFilterViewModel,
                           filters: Array<Filter>,
                           checked: BooleanArray)
}