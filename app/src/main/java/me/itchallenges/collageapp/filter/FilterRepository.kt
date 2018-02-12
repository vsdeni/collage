package me.itchallenges.collageapp.filter

import io.reactivex.Observable


interface FilterRepository {
    fun getFilters(): Observable<Filter>
}