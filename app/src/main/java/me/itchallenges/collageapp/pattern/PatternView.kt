package me.itchallenges.collageapp.pattern


interface PatternView {
    fun showPatternsPicker(patterns: Array<Pattern>, active: Pattern?)
}