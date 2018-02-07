package me.itchallenge.collage


interface BaseView {
    fun showLoader()

    fun hideLoader()

    fun showMessage(message: String)
}