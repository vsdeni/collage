package me.itchallenges.collageapp.browse

import android.net.Uri
import me.itchallenges.collageapp.BaseScreenView


interface BrowseScreenView : BaseScreenView {
    fun showCollage(image: Uri)
}