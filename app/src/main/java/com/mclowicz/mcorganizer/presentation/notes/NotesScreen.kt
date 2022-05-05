package com.mclowicz.mcorganizer.presentation.notes

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.mclowicz.mcorganizer.domain.model.Note
import com.mclowicz.mcorganizer.navigation.Screen
import com.mclowicz.mcorganizer.presentation.common.NoteDeleteDialogUI
import com.mclowicz.mcorganizer.presentation.common.bottomNavBar.BottomNavBar
import kotlinx.coroutines.flow.collectLatest
import java.util.*

@Composable
fun NotesScreen(
    navController: NavHostController,
    noteViewModel: NoteViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val date = remember { mutableStateOf("") }
    val openDialog = remember { mutableStateOf(false) }
    val openDeleteDialog = remember { mutableStateOf(false) }
    val tempNote = remember {
        mutableStateOf<Note?>(null)
    }
    val viewState = noteViewModel.notesViewState.collectAsState().value
    val viewEvent = noteViewModel.viewEvents

    fun openDatePicker(
        date: String,
        confirmDate: (String) -> Unit
    ) {
        val dateArray = date.split("/")
        val day = dateArray[0].toInt()
        val month = dateArray[1].toInt()
        val year = dateArray[2].toInt()
        val datePicker = DatePickerDialog(
            context,
            { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                val chosenDate = "$dayOfMonth/$month/$year"
                confirmDate.invoke(chosenDate)
            }, year, month, day
        ).apply {
        }
        datePicker.show()
    }

    LaunchedEffect(Unit) {
        date.value = getCurrentDate()
        noteViewModel.setCurrentDate(currentDate = date.value)
        viewEvent.collectLatest { viewEvent ->
            when (viewEvent) {
                is NotesViewEvent.OpenDialog -> {
                    openDialog.value = viewEvent.isOpen
                }
                is NotesViewEvent.OpenDeleteDialog -> {
                    openDeleteDialog.value = viewEvent.isOpen
                    tempNote.value = viewEvent.note
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
                NotesTopBar(
                    title = date.value,
                    openDatePicker = {
                        openDatePicker(
                            date = date.value,
                            confirmDate = {
                                noteViewModel.setCurrentDate(it)
                            }
                        )
                    }
                )
            },
            bottomBar = {
                BottomNavBar(navController = navController)
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        noteViewModel.triggerViewEvent(
                            event = NotesViewEvent.OpenDialog(isOpen = true)
                        )
                    },
                    backgroundColor = MaterialTheme.colors.primary,
                    contentColor = Color.White,
                ) {
                    Icon(Icons.Filled.Add, "Calendar")
                }
            }
        ) {

            when (viewState) {
                is NotesViewState.Idle -> {
                }
                is NotesViewState.Content -> {
                    date.value = viewState.currentDate
                    if (viewState.notes.isEmpty()) {
                        // show empty notes ui
                    } else {
                        // show notes list ui
                        LazyColumn() {
                            items(items = viewState.notes) { note ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .wrapContentHeight()
                                        .padding(8.dp)
                                        .clickable(
                                            onClick = {
                                                navController.navigate(Screen.Details.passArguments(id = note.id))
                                            }
                                        ),
                                    shape = RoundedCornerShape(6.dp),
                                    border = BorderStroke(1.dp, MaterialTheme.colors.primary),
                                    elevation = 8.dp
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(16.dp),
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.Start
                                    ) {
                                        Row() {
                                            Column(
                                                Modifier.weight(0.8f)
                                            ) {
                                                Text(
                                                    text = note.title,
                                                    fontSize = MaterialTheme.typography.subtitle1.fontSize,
                                                    fontWeight = FontWeight.Bold,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                                Spacer(
                                                    modifier = Modifier
                                                        .height(6.dp)
                                                        .fillMaxWidth()
                                                )
                                                Text(
                                                    text = note.content,
                                                    fontSize = MaterialTheme.typography.subtitle2.fontSize,
                                                    fontWeight = FontWeight.Normal,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                                Text(text = note.time, fontStyle = FontStyle.Italic, fontWeight = FontWeight.Light)
                                            }
                                            Column(
                                                Modifier.weight(0.1f)
                                            ) {
                                                IconButton(onClick = {
                                                    noteViewModel.triggerViewEvent(
                                                        event = NotesViewEvent.OpenDeleteDialog(true, note = note)
                                                    )
                                                }) {
                                                    Icon(
                                                        Icons.Default.Delete,
                                                        "delete icon",
                                                        tint = MaterialTheme.colors.primary,
                                                        modifier = Modifier.alpha(0.5f),
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            NoteDeleteDialogUI(
                isVisible = openDeleteDialog.value,
                onConfirmClicked = {
                    tempNote.value?.let {
                        noteViewModel.deleteNote(note = it)
                        noteViewModel.triggerViewEvent(
                            event = NotesViewEvent.OpenDeleteDialog(false, null)
                        )
                    }
                },
                onDismiss = {
                    noteViewModel.triggerViewEvent(
                        event = NotesViewEvent.OpenDeleteDialog(false, null)
                    )
                },
            )

            NoteDialogUI(
                isVisible = openDialog.value,
                onConfirmClicked = {
                    noteViewModel.triggerViewEvent(
                        event = NotesViewEvent.OpenDialog(isOpen = false)
                    )
                    noteViewModel.saveNote(note = it)
                },
                onDismiss = {
                    noteViewModel.triggerViewEvent(
                        event = NotesViewEvent.OpenDialog(isOpen = false)
                    )
                },
                date = date.value
            )
        }
    }
}

fun getCurrentDate(): String {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    calendar.time = Date()
    return "$day/$month/$year"
}