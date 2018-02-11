package me.itchallenges.collageapp


interface BaseView {
    fun showLoader()

    fun hideLoader()

    fun showMessage(message: String)
}