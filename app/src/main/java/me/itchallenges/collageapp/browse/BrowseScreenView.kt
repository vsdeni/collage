package me.itchallenges.collageapp.browse

import android.content.Intent
import android.net.Uri
import me.itchallenges.collageapp.BaseScreenView


interface BrowseScreenView : BaseScreenView {
    fun showCollageImage(image: Uri)

    fun showShareDialog(intent: Intent)

    fun getCaption(): String
}