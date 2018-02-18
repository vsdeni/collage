package me.itchallenges.collageapp.filter

import android.net.Uri
import me.itchallenges.collageapp.pattern.Pattern


data class CollageFilterViewModel(val images: List<Uri>,
                                  val pattern: Pattern)