package com.example.navigation.navigation

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.navigation.ui.screen.FavoritesScreen
import com.example.navigation.ui.screen.HomeDetailsScreen
import com.example.navigation.ui.screen.HomeScreen
import com.example.navigation.ui.screen.ProfileScreen

@Composable
fun NavigationApp() {
    val bottomItems = listOf(Screen.Home, Screen.Favorites, Screen.Profile)

    // NavController для каждой вкладки
    val navControllers: Map<Screen, NavHostController> = bottomItems.associateWith { rememberNavController() }

    // Saver для сохранения текущей вкладки при пересоздании Activity
    val screenSaver = Saver<Screen, String>(
        save = { it.route },
        restore = { route ->
            when (route) {
                Screen.Home.route -> Screen.Home
                Screen.HomeDetails.route -> Screen.HomeDetails
                Screen.Favorites.route -> Screen.Favorites
                Screen.Profile.route -> Screen.Profile
                else -> Screen.Home
            }
        }
    )

    var currentTab: Screen by rememberSaveable(stateSaver = screenSaver) {
        mutableStateOf(Screen.Home)
    }

    val homeTab = Screen.Home
    val context = LocalContext.current

    // 🔹 Обработка системной кнопки Back
    BackHandler {
        val navController = navControllers[currentTab]!!

        // Если можно подняться по стеку текущей вкладки → делаем pop
        if (!navController.popBackStack()) {
            // На корне текущей вкладки
            if (currentTab != homeTab) {
                // Переходим на Home, но сохраняем его стек
                currentTab = homeTab
            } else {
                // Уже на корне Home → обычный выход
                (context as? Activity)?.finish()
            }
        }
    }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            bottomItems.forEach { screen ->
                val isSelected = currentTab == screen

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
                    selected = isSelected,
                    onClick = {
                        if (currentTab == screen) {
                            // Сброс стека текущей вкладки до корня
                            navControllers[screen]?.popBackStack(route = screen.route, inclusive = false)
                        } else {
                            // Переключение на другую вкладку
                            currentTab = screen
                        }
                    }
                )
            }
        }
    ) {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                bottomItems.forEach { screen ->
                    TabNavHost(
                        navController = navControllers[screen]!!,
                        startRoute = screen.route,
                        visible = currentTab == screen
                    ) {
                        when (screen) {
                            Screen.Home -> {
                                composable(Screen.Home.route) {
                                    HomeScreen(onGoToDetails = { navControllers[Screen.Home]?.navigate(Screen.HomeDetails.route) })
                                }
                                composable(Screen.HomeDetails.route) { HomeDetailsScreen() }
                            }
                            Screen.Favorites -> {
                                composable(Screen.Favorites.route) { FavoritesScreen() }
                            }
                            Screen.Profile -> {
                                composable(Screen.Profile.route) { ProfileScreen() }
                            }
                            else -> {}
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TabNavHost(
    navController: NavHostController,
    startRoute: String,
    visible: Boolean,
    content: NavGraphBuilder.() -> Unit
) {
    if (visible) {
        NavHost(
            navController = navController,
            startDestination = startRoute,
            builder = content
        )
    }
}