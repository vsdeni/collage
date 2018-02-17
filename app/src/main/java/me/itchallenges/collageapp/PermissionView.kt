package me.itchallenges.collageapp


interface PermissionView {

    fun isAccessGranted(permission: String): Boolean

    fun shouldShowPermissionRationale(permission: String): Boolean

    fun showPermissionRationale(permission: String, rationale: String)

    fun openAppPermissionSettings()

    fun requestPermission(permission: String)
}