package me.itchallenges.collageapp.filter

import me.itchallenges.collageapp.BaseView


interface FilterView : BaseView {
    fun showFiltersPicker(filters: List<Filter>, active: Filter?)

    fun getSelectedFilter(): Filter

    fun getSelectedImages(): List<Int>

    fun showCollagePreview(collageFilterViewModel: CollageFilterViewModel)
}