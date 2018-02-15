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

    fun saveFrames(images: List<Bitmap>, dir: File): Completable

    fun getFrames(dir: File, count: Int): Observable<File>

    fun saveFrameFilter(indexes: IntArray, filter: Filter): Completable

    fun getFrameFilter(indexes: IntArray): Observable<Filter>

    fun saveGlobalFilter(filter: Filter): Completable

    fun getGlobalFilter(): Maybe<Filter>
}