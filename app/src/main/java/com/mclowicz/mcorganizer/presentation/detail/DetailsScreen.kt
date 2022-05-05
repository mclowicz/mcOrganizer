package com.mclowicz.mcorganizer.presentation.detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.mclowicz.mcorganizer.navigation.Screen
import com.mclowicz.mcorganizer.presentation.common.NoteDeleteDialogUI
import kotlinx.coroutines.flow.collectLatest

@Composable
fun DetailScreen(
    navController: NavHostController,
    noteId: String,
    detailsViewModel: DetailsViewModel = hiltViewModel()
) {
    val note = detailsViewModel.note.collectAsState().value
    val openDeleteDialog = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        detailsViewModel.apply {
            getNoteById(noteId = noteId)
            viewEvents.collectLatest { viewEvent ->
                when (viewEvent) {
                    is DetailsViewEvent.OpenDeleteDialog -> {
                        openDeleteDialog.value = viewEvent.isOpen
                    }
                }
            }
        }
    }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Scaffold(
            topBar = {
                DetailTopBar(
                    title = Screen.Details.title,
                    navController = navController
                )
            },
        ) {

            NoteDeleteDialogUI(
                isVisible = openDeleteDialog.value,
                onConfirmClicked = {
                    note?.let {
                        detailsViewModel.deleteNote(note = note)
                        navController.popBackStack()
                        detailsViewModel.triggerEvent(
                            event = DetailsViewEvent.OpenDeleteDialog(false)
                        )
                    }
                },
                onDismiss = {
                    detailsViewModel.triggerEvent(
                        event = DetailsViewEvent.OpenDeleteDialog(false)
                    )
                },
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                note?.let { note ->
                    Row() {
                        Text(
                            text = note.title,
                            fontSize = MaterialTheme.typography.h4.fontSize
                        )
                    }
                    Spacer(
                        modifier = Modifier
                            .height(16.dp)
                            .fillMaxWidth()
                    )
                    Row() {
                        Column(modifier = Modifier.weight(0.5f)) {
                            Text(text = note.date)
                        }
                        Column(modifier = Modifier.weight(0.5f)) {
                            Text(text = note.time)
                        }
                    }
                    Spacer(
                        modifier = Modifier
                            .height(16.dp)
                            .fillMaxWidth()
                    )
                    Row() {
                        Text(text = note.content)
                    }
                    Spacer(
                        modifier = Modifier
                            .height(16.dp)
                            .fillMaxWidth()
                    )
                    Row() {
                        OutlinedButton(
                            border = BorderStroke(1.dp, MaterialTheme.colors.primary),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            onClick = {
                                detailsViewModel.triggerEvent(
                                    event = DetailsViewEvent.OpenDeleteDialog(true)
                                )
                            }
                        ) {
                            Text(text = "Delete note")
                        }
                    }
                }
            }
        }
    }
}