package com.example.navigation.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.navigation.ui.screen.FavoritesScreen
import com.example.navigation.ui.screen.HomeDetailsScreen
import com.example.navigation.ui.screen.HomeScreen
import com.example.navigation.ui.screen.ProfileScreen

@Preview
@Composable
fun NavigationApp() {

    val navController = rememberNavController()

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination

    val bottomItems = listOf(
        Screen.Home,
        Screen.Favorites,
        Screen.Profile
    )

    NavigationSuiteScaffold(
        navigationSuiteItems = {

            bottomItems.forEach { screen ->

                val selected = currentDestination
                    ?.hierarchy
                    ?.any { it.route?.startsWith(screen.route) == true } == true

                item(
                    icon = {
                        Icon(
                            when (screen) {
                                Screen.Home -> Icons.Default.Home
                                Screen.Favorites -> Icons.Default.Favorite
                                Screen.Profile -> Icons.Default.AccountBox
                                else -> Icons.Default.Home
                            },
                            contentDescription = screen.route
                        )
                    },
                    label = { Text(screen.route.substringBefore("/").replaceFirstChar { it.uppercase() }) },
                    selected = selected,
                    onClick = {
                        val currentDestination = navController.currentDestination

                        val isOnSameTab = currentDestination
                            ?.hierarchy
                            ?.any { it.route?.startsWith(screen.route) == true } == true

                        if (isOnSameTab) {
                            // Если уже в этом табе — возвращаемся к корню
                            navController.popBackStack(
                                route = screen.route,
                                inclusive = false
                            )
                        } else {
                            // Если другой таб — сохраняем состояние
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                )
            }
        }
    ) {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

            NavHost(
                navController = navController,
                startDestination = Screen.Home.route,
                modifier = Modifier.padding(innerPadding)
            ) {

                composable(Screen.Home.route) {
                    HomeScreen(
                        onGoToDetails = {
                            navController.navigate(Screen.HomeDetails.route)
                        }
                    )
                }

                composable(Screen.HomeDetails.route) {
                    HomeDetailsScreen()
                }

                composable(Screen.Favorites.route) {
                    FavoritesScreen()
                }

                composable(Screen.Profile.route) {
                    ProfileScreen()
                }
            }
        }
    }
}