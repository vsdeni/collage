package me.itchallenges.collageapp.pattern

import io.reactivex.Single


interface PatternRepository {
    fun getPatterns(): Single<Array<Pattern>>
}