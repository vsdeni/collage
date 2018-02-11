package me.itchallenges.collageapp.pattern

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class PatternPresenter(private val view: PatternView, private val patternRepository: PatternRepository) {

    fun loadPatterns() {
        patternRepository.getPatterns()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ patterns -> view.showPatternsPicker(patterns, null) })
    }

}