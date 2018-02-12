package me.itchallenges.collageapp.pattern

import android.graphics.Bitmap
import me.itchallenges.collageapp.BaseView


interface PatternView : BaseView {
    fun showPatternsPicker(patterns: Array<Pattern>, active: Pattern?)

    fun getSelectedPattern(): Pattern

    fun showCollagePreview(pattern: Pattern, frames: List<Bitmap>)
}