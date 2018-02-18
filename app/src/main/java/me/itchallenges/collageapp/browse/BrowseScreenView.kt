package me.itchallenges.collageapp.browse

import android.net.Uri
import me.itchallenges.collageapp.BaseScreenView


interface BrowseScreenView : BaseScreenView {
    fun showCollageImage(image: Uri)

    fun shareCollageImage(image: Uri, message: String)

    fun getCaption(): String
}