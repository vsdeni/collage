package me.itchallenges.collageapp.collage

import android.graphics.Bitmap
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import me.itchallenges.collageapp.filter.Filter
import me.itchallenges.collageapp.pattern.Pattern
import java.io.File


interface CollageRepository {
    fun savePattern(pattern: Pattern): Completable

    fun getPattern(): Single<Pattern>

    fun getImages(dir: File): Observable<Bitmap>

    fun saveImages(images: List<Bitmap>, dir: File): Completable

    fun saveFilter(index: Int, filter: Filter): Maybe<Filter>

    fun getFilter(index: Int): Filter
}