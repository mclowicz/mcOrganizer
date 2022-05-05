package com.mclowicz.mcorganizer.navigation

import com.mclowicz.mcorganizer.util.Constants.DETAILS_ARGUMENT_ID_KEY

sealed class Screen(val title: String, val route: String) {
    object Welcome : Screen(title = "Welcome", route = "welcome_screen")
    object Home : Screen(title = "Home", route = "home_screen")
    object Notes : Screen(title = "Notes", route = "notes_screen")
    object Spots : Screen(title = "Spots", route = "spots_screen")
    object Search : Screen(title = "Search", route = "search_screen")
    object Details : Screen(title = "Details", route = "detail_screen/{$DETAILS_ARGUMENT_ID_KEY}") {
        fun passArguments(id: String): String {
            return "detail_screen/$id"
        }
    }
    object DetailsSpot : Screen(title = "Details Spot", route = "detail_spot_screen/{$DETAILS_ARGUMENT_ID_KEY}") {
        fun passArguments(id: String): String {
            return "detail_spot_screen/$id"
        }
    }
}