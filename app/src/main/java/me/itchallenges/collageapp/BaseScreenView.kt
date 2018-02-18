package me.itchallenges.collageapp

import android.content.Context


interface BaseScreenView {
    fun showLoader()

    fun hideLoader()

    fun showMessage(message: String)

    fun navigateNext()

    fun context(): Context
}