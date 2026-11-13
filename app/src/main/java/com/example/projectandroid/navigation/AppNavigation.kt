package com.example.projectandroid.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.projectandroid.ui.screens.categories.CategoriesScreen
import com.example.projectandroid.ui.screens.details.DetailsScreen
import com.example.projectandroid.ui.screens.favorites.FavoritesScreen
import com.example.projectandroid.ui.screens.home.HomeScreen

@Composable
fun AppNavigation(
    navController: NavHostController
) {

    NavHost(
        navController = navController,
        startDestination = Routes.HOME
    ) {

        /*
         * HOME
         */
        composable(
            route = Routes.HOME
        ) {

            HomeScreen(
                onNavigateToCategories = {
                    navController.navigate(
                        Routes.categories("characters")
                    )
                },

                onNavigateToEpisodes = {
                    navController.navigate(
                        Routes.categories("episodes")
                    )
                },

                onNavigateToFavorites = {
                    navController.navigate(
                        Routes.FAVORITES
                    )
                }
            )
        }

        /*
         * CATEGORIES
         */
        composable(
            route = Routes.CATEGORIES
        ) { backStackEntry ->

            val section =
                backStackEntry.arguments
                    ?.getString("section")
                    ?: "characters"

            CategoriesScreen(
                initialSection = section,

                onNavigateToDetails = { type, id ->

                    navController.navigate(
                        Routes.details(
                            type = type,
                            id = id
                        )
                    )
                },

                onBackToHome = {
                    navController.popBackStack(
                        Routes.HOME,
                        false
                    )
                }
            )
        }

        /*
         * FAVORITES
         */
        composable(
            route = Routes.FAVORITES
        ) {

            FavoritesScreen(
                onBack = {
                    navController.popBackStack()
                },

                onCharacterClick = { characterId ->

                    navController.navigate(
                        Routes.details(
                            type = "character",
                            id = characterId
                        )
                    )
                }
            )
        }

        /*
         * DETAILS
         */
        composable(
            route = Routes.DETAILS
        ) { backStackEntry ->

            val type =
                backStackEntry.arguments
                    ?.getString("type")
                    ?: "character"

            val id =
                backStackEntry.arguments
                    ?.getString("id")
                    ?.toIntOrNull()
                    ?: 1

            DetailsScreen(
                type = type,
                id = id,

                onBack = {
                    navController.popBackStack()
                },

                onCharacterClick = { characterId ->

                    navController.navigate(
                        Routes.details(
                            type = "character",
                            id = characterId
                        )
                    )
                }
            )
        }
    }
}