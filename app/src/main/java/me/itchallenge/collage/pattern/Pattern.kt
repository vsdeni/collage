package me.itchallenge.collage.pattern


data class Pattern(val name: String, val positions: List<Position>)

data class Position(val x: Int, val y: Int, val width: Int, val height: Int)