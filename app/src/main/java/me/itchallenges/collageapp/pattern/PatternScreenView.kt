package me.itchallenges.collageapp.pattern

import android.net.Uri
import me.itchallenges.collageapp.framework.view.BaseScreenView


interface PatternScreenView : BaseScreenView {
    fun showPatternsPicker(patterns: List<Pattern>, active: Pattern?)

    fun getSelectedPattern(): Pattern?

    fun showCollagePreview(pattern: Pattern, frames: List<Uri>)
}