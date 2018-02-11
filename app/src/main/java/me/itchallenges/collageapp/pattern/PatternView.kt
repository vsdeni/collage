package me.itchallenges.collageapp.pattern

import android.graphics.Bitmap


interface PatternView {
    fun showPatternsPicker(patterns: Array<Pattern>, active: Pattern?)

    fun getSelectedPattern(): Pattern?

    fun showPatternPreview(pattern: Pattern?, frames: List<Bitmap>)
}