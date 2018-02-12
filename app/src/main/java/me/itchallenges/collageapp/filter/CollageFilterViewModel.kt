package me.itchallenges.collageapp.filter

import android.graphics.Bitmap
import me.itchallenges.collageapp.pattern.Pattern


data class CollageFilterViewModel(val frames: List<Bitmap>,
                                  val globalFilter: Filter,
                                  val frameFilters: List<Filter>,
                                  val pattern: Pattern)