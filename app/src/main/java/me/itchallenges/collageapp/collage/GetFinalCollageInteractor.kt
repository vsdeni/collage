package me.itchallenges.collageapp.collage

import com.urancompany.indoorapp.executor.ExecutionScheduler
import com.urancompany.indoorapp.interactor.UseCase
import io.reactivex.Observable
import io.reactivex.Single
import me.itchallenges.collageapp.filter.Filter
import me.itchallenges.collageapp.settings.SettingsRepository
import java.io.File


class GetFinalCollageInteractor(private val settingsRepository: SettingsRepository,
                                private val collageRepository: CollageRepository,
                                private val scheduler: ExecutionScheduler) : UseCase.RxSingle<CollageFinalViewModel, UseCase.None>() {

    override fun build(params: None?): Single<CollageFinalViewModel> {
        return settingsRepository.getDirToSaveFrames()
                .flatMap({
                    collageRepository
                            .getFrames(it)
                            .toList()
                })
                .flatMap({ frames ->
                    collageRepository
                            .getGlobalFilter()
                            .switchIfEmpty(Single.just(Filter.NONE))
                            .map { filter -> Pair<List<File>, Filter>(frames, filter) }
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
                                CollageFinalViewModel(pair.first,
                                        pair.second.globalFilter,
                                        pair.second.frameFilters,
                                        pattern)
                            }
                })
                .compose(scheduler.highPrioritySingle())
    }

    data class FiltersWrapper(val globalFilter: Filter, val frameFilters: List<Filter>)
}