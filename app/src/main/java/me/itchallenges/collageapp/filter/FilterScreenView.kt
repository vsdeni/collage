package me.itchallenges.collageapp.filter

import android.graphics.Bitmap
import me.itchallenges.collageapp.framework.view.BaseScreenView


interface FilterScreenView : BaseScreenView {
    fun showFiltersPicker(filters: List<Filter>, active: Filter?)

    fun getCheckedCells(): BooleanArray

    fun getAppliedFilters(): Array<Filter>

    fun getSelectedFilter(): Filter

    fun showCollagePreview(collage: CollageFilterViewModel,
                           filters: Array<Filter>,
                           checked: BooleanArray)

    fun getFilteredImages(): Array<Bitmap>
}