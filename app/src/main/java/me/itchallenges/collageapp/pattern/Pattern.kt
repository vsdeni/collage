package me.itchallenges.collageapp.pattern

import android.graphics.Bitmap


data class Pattern(val name: String, val positions: List<Position>, var preview: Bitmap?)

data class Position(val x: Int, val y: Int, val width: Int, val height: Int)