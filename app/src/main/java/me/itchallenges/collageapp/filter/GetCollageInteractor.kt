package me.itchallenges.collageapp.filter

import android.graphics.Bitmap
import com.urancompany.indoorapp.executor.ExecutionScheduler
import com.urancompany.indoorapp.interactor.UseCase
import io.reactivex.Observable
import io.reactivex.Single
import me.itchallenges.collageapp.collage.CollageRepository
import me.itchallenges.collageapp.settings.SettingsRepository


class GetCollageInteractor(private val settingsRepository: SettingsRepository,
                           private val collageRepository: CollageRepository,
                           private val scheduler: ExecutionScheduler) : UseCase.RxSingle<CollageFilterViewModel, UseCase.None>() {

    override fun build(params: None?): Single<CollageFilterViewModel> {
        return settingsRepository.getDirToSaveFrames()
                .flatMap({
                    collageRepository
                            .getImages(it)
                            .toList()
                })
                .flatMap({ frames ->
                    collageRepository
                            .getGlobalFilter()
                            .switchIfEmpty(Single.just(Filter("No Filter", null)))//TODO
                            .map { filter -> Pair<List<Bitmap>, Filter>(frames, filter) }
                })
                .flatMap({ pair ->
                    Observable
                            .range(0, pair.first.size)//all filters are required
                            .toList()
                            .flatMap({
                                collageRepository
                                        .getFrameFilter(it.toIntArray())
                                        .toList()
                            }).map { filters -> Pair(pair.first, FiltersWrapper(pair.second, filters)) }
                })
                .flatMap({ pair ->
                    collageRepository
                            .getPattern()
                            .map { pattern ->
                                CollageFilterViewModel(pair.first,
                                        pair.second.globalFilter,
                                        pair.second.frameFilters,
                                        pattern)
                            }
                })
                .compose(scheduler.highPrioritySingle())
    }

    data class FiltersWrapper(val globalFilter: Filter, val frameFilters: List<Filter>)
}