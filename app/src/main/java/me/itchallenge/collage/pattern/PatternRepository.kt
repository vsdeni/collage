package me.itchallenge.collage.pattern

import io.reactivex.Single


interface PatternRepository {
    fun getPatterns(): Single<Array<Pattern>>
}