package me.itchallenges.collageapp.collage

import me.itchallenges.collageapp.filter.Filter
import me.itchallenges.collageapp.pattern.Pattern
import java.io.File

data class CollageFinalViewModel(val frames: List<File>,
                                 val globalFilter: Filter,
                                 val frameFilters: List<Filter>,
                                 val pattern: Pattern)