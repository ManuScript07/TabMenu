package com.example.navigation.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object HomeDetails : Screen("home/details")
    object Favorites : Screen("favorites")
    object Profile : Screen("profile")
}