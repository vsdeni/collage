package me.itchallenges.collageapp.pattern

import me.itchallenges.collageapp.BaseView
import java.io.File


interface PatternView : BaseView {
    fun showPatternsPicker(patterns: Array<Pattern>, active: Pattern?)

    fun getSelectedPattern(): Pattern

    fun showCollagePreview(pattern: Pattern, frames: List<File>)
}