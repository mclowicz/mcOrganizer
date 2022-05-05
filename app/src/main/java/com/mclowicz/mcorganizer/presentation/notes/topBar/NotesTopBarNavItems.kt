package com.mclowicz.mcorganizer.presentation.notes.topBar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.ui.graphics.vector.ImageVector

val NotesTopBarNavItems = listOf(
    TopBarNavItem(
        label = "Date Picker",
        icon = Icons.Filled.DateRange,
    )
)

data class TopBarNavItem(
    val label: String,
    val icon: ImageVector
)