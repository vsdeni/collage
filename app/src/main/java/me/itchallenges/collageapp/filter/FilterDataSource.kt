package me.itchallenges.collageapp.filter

import io.reactivex.Observable


class FilterDataSource : FilterRepository {

    override fun getFilters(): Observable<Filter> {
        val filters = Filter.values()
        return Observable.create({ emitter ->
            filters
                    .forEach { emitter.onNext(it) }

            emitter.onComplete()
        })
    }
}