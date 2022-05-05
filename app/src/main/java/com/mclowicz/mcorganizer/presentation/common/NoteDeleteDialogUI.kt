package com.mclowicz.mcorganizer.presentation.common

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun NoteDeleteDialogUI(
    isVisible: Boolean,
    onConfirmClicked: () -> Unit,
    onDismiss: () -> Unit,
) {

    if (isVisible) {
        Dialog(
            onDismissRequest = onDismiss,
        ) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colors.surface,
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Delete", style = MaterialTheme.typography.h5)
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                            .weight(weight = 1f, fill = false)
                            .padding(vertical = 16.dp)
                    ) {
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                        )
                        Text(text = "Are you sure you want to delete a note?")
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = onDismiss) {
                            Text(text = "Cancel")
                        }
                        TextButton(onClick = {
                            onConfirmClicked()
                        }) {
                            Text(text = "OK")
                        }
                    }
                }
            }
        }
    }
}