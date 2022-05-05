package com.mclowicz.mcorganizer.presentation.notes

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import com.mclowicz.mcorganizer.presentation.notes.topBar.NotesTopBarNavItems

@Composable
fun NotesTopBar(
    title: String = String(),
    openDatePicker: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = title
            )
        },
        actions = {
            NotesTopBarNavItems.forEach { navItem ->
                IconButton(onClick = {
                    // open date Picker
                    openDatePicker()
                }) {
                    Icon(navItem.icon, contentDescription = navItem.label)
                }
            }
        },
        backgroundColor = MaterialTheme.colors.primaryVariant
    )
}