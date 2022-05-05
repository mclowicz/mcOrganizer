package com.mclowicz.mcorganizer.presentation.common.bottomNavBar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import com.mclowicz.mcorganizer.domain.model.NavItem
import com.mclowicz.mcorganizer.navigation.Screen

val BottomNavItems = listOf(
    NavItem(
        label = Screen.Home.title,
        icon = Icons.Filled.Home,
        route = Screen.Home.route
    ),
    NavItem(
        label = Screen.Notes.title,
        icon = Icons.Filled.List,
        route = Screen.Notes.route
    ),
    NavItem(
        label = Screen.Spots.title,
        icon = Icons.Filled.Place,
        route = Screen.Spots.route
    ),
    NavItem(
        label = Screen.Search.title,
        icon = Icons.Filled.Search,
        route = Screen.Search.route
    )
)