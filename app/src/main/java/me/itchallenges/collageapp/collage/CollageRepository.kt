package me.itchallenges.collageapp.collage

import android.graphics.Bitmap
import android.net.Uri
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import me.itchallenges.collageapp.filter.Filter
import me.itchallenges.collageapp.pattern.Pattern


interface CollageRepository {
    fun savePattern(pattern: Pattern): Completable

    fun getPattern(): Single<Pattern>

    fun saveFrames(images: List<Bitmap>): Completable

    fun getFrames(): Observable<Uri>

    fun saveFilters(filters: Array<Filter>): Completable

    fun getFilters(indexes: IntArray): Observable<Filter>

    fun getCollageImage(): Single<Uri>

    fun saveCollageImage(bitmap: Bitmap): Single<Uri>
}