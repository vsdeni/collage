package me.itchallenges.collageapp.filter

import android.media.effect.EffectFactory
import io.reactivex.Observable


class FilterDataSource : FilterRepository {

    override fun getFilters(): Observable<Filter> {
        val filters = arrayListOf(//TODO
                Filter("No Filter", null),
                Filter("Sepia", EffectFactory.EFFECT_SEPIA),
                Filter("Documentary", EffectFactory.EFFECT_DOCUMENTARY),
                Filter("Lomo", EffectFactory.EFFECT_LOMOISH)
        )
        return Observable.fromIterable(filters)
    }

}