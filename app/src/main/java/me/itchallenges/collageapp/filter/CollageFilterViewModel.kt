package me.itchallenges.collageapp.filter

import me.itchallenges.collageapp.pattern.Pattern
import java.io.File


data class CollageFilterViewModel(val frames: List<File>,
                                  val pattern: Pattern)