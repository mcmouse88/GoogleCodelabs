package com.mcmouse88.android_privacy.presentation

sealed class Screens(val route: String) {
    data object Permissions : Screens("permissions")
    data object Home : Screens("home")
    data object AddLog : Screens("add_log")
    data object Camera : Screens("camera")
}
