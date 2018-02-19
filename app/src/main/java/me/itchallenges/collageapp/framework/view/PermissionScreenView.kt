package me.itchallenges.collageapp.framework.view


interface PermissionScreenView {

    fun isAccessGranted(permission: String): Boolean

    fun shouldShowPermissionRationale(permission: String): Boolean

    fun showPermissionRationale(permission: String, rationale: String)

    fun openAppPermissionSettings()

    fun requestPermission(permission: String)
}