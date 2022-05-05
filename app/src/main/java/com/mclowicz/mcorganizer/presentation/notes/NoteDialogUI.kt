package com.mclowicz.mcorganizer.presentation.notes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.mclowicz.mcorganizer.domain.model.Note
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

@Composable
fun NoteDialogUI(
    isVisible: Boolean,
    onConfirmClicked: (Note) -> Unit,
    onDismiss: () -> Unit,
    date: String
) {
    val title = remember { mutableStateOf("") }
    val titleError = remember { mutableStateOf(false) }
    val titleErrorMsg = remember { mutableStateOf("") }

    val content = remember { mutableStateOf("") }
    val contentError = remember { mutableStateOf(false) }
    val contentErrorMsg = remember { mutableStateOf("") }

    if (isVisible) {
        Dialog(
            onDismissRequest = onDismiss,
        ) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colors.surface,
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Note", style = MaterialTheme.typography.subtitle1)
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                            .weight(weight = 1f, fill = false)
                            .padding(vertical = 16.dp)
                    ) {
                        OutlinedTextField(
                            value = title.value,
                            onValueChange = {
                                titleError.value = it.length < 3
                                titleErrorMsg.value = if (it.length < 3) {
                                    "Title length needs at least 3 characters."
                                } else {
                                    ""
                                }
                                title.value = it
                            },
                            modifier = Modifier.padding(top = 8.dp),
                            label = { Text(text = "Title") },
                            isError = titleError.value
                        )
                        if (titleError.value) {
                            Text(text = titleErrorMsg.value, color = Color.Red)
                        }
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                        )
                        OutlinedTextField(
                            value = content.value,
                            onValueChange = {
                                contentError.value = it.length < 3
                                contentErrorMsg.value = if (it.length < 3) {
                                    "Content length needs at least 3 characters."
                                } else {
                                    ""
                                }
                                content.value = it
                            },
                            modifier = Modifier.padding(top = 8.dp),
                            label = { Text(text = "Content") },
                            isError = contentError.value
                        )
                        if (contentError.value) {
                            Text(text = contentErrorMsg.value, color = Color.Red)
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = onDismiss) {
                            Text(text = "Cancel")
                        }
                        TextButton(onClick = {
                            if (title.value.isEmpty()) {
                                titleError.value = true
                                titleErrorMsg.value = "Field cannot be empty."
                            }
                            if (content.value.isEmpty()) {
                                contentError.value = true
                                contentErrorMsg.value = "Field cannot be empty."
                            }
                            if (!titleError.value && !contentError.value) {
                                val note = createNote(date = date, title = title.value, content = content.value)
                                onConfirmClicked.invoke(note)
                                title.value = String()
                                content.value = String()
                            }
                        }) {
                            Text(text = "OK")
                        }
                    }
                }
            }
        }
    }
}

fun createNote(
    date: String,
    title: String,
    content: String
) : Note {
    val newUUID = UUID.randomUUID().toString()
    val currentTime = LocalTime.now()
    val time = currentTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM))
    val timeStamp = Calendar.getInstance().time
    return Note(
        id = newUUID,
        title = title,
        content = content,
        date = date,
        time = time,
        timeStamp = timeStamp.time
    )
}