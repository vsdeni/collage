package me.itchallenges.collageapp.framework.view

import android.content.Context


interface BaseScreenView {
    fun showLoader()

    fun hideLoader()

    fun showMessage(message: String)

    fun navigateNext()

    fun context(): Context
}