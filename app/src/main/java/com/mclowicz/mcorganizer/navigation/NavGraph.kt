package com.mclowicz.mcorganizer.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.mclowicz.mcorganizer.presentation.WelcomeScreen
import com.mclowicz.mcorganizer.presentation.detail.DetailScreen
import com.mclowicz.mcorganizer.presentation.detailSpot.DetailSpotScreen
import com.mclowicz.mcorganizer.presentation.home.HomeScreen
import com.mclowicz.mcorganizer.presentation.notes.NotesScreen
import com.mclowicz.mcorganizer.presentation.search.SearchScreen
import com.mclowicz.mcorganizer.presentation.spots.SpotsScreen
import com.mclowicz.mcorganizer.util.Constants.DETAILS_ARGUMENT_ID_KEY

@ExperimentalPermissionsApi
@ExperimentalAnimationApi
@ExperimentalPagerApi
@Composable
fun SetupNavGraph(
    navController: NavHostController,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(route = Screen.Welcome.route) {
            WelcomeScreen(navController = navController)
        }
        composable(route = Screen.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(route = Screen.Notes.route) {
            NotesScreen(navController = navController)
        }
        composable(route = Screen.Spots.route) {
            SpotsScreen(navController = navController)
        }
        composable(route = Screen.Search.route) {
            SearchScreen(navController = navController)
        }
        composable(
            route = Screen.Details.route,
            arguments = listOf(
                navArgument(DETAILS_ARGUMENT_ID_KEY) {
                    type = NavType.StringType
                },
            )
        ) { backStackEntry ->
            backStackEntry.arguments?.getString(DETAILS_ARGUMENT_ID_KEY)?.let { noteId ->
                DetailScreen(navController = navController, noteId = noteId)
            }
        }
        composable(
            route = Screen.DetailsSpot.route,
            arguments = listOf(
                navArgument(DETAILS_ARGUMENT_ID_KEY) {
                    type = NavType.StringType
                },
            )
        ) { backStackEntry ->
            backStackEntry.arguments?.getString(DETAILS_ARGUMENT_ID_KEY)?.let { spotId ->
                DetailSpotScreen(navController = navController, spotId = spotId)
            }
        }
    }
}