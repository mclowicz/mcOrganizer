package com.mclowicz.mcorganizer.presentation.spots

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
import com.google.android.gms.maps.model.LatLng
import com.mclowicz.mcorganizer.domain.model.Spot
import com.mclowicz.mcorganizer.presentation.notes.getCurrentDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

@Composable
fun CreateSpotDialogUI(
    isVisible: Boolean,
    onConfirmClicked: (Spot) -> Unit,
    onDismiss: () -> Unit,
    latLng: LatLng?
) {
    val name = remember { mutableStateOf("") }
    val nameError = remember { mutableStateOf(false) }
    val nameErrorMsg = remember { mutableStateOf("") }

    if (isVisible) {
        Dialog(
            onDismissRequest = onDismiss,
        ) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colors.surface,
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Spot", style = MaterialTheme.typography.subtitle1)
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                            .weight(weight = 1f, fill = false)
                            .padding(vertical = 16.dp)
                    ) {
                        OutlinedTextField(
                            value = name.value,
                            onValueChange = {
                                nameError.value = it.length < 3
                                nameErrorMsg.value = if (it.length < 3) {
                                    "Name length needs at least 3 characters."
                                } else {
                                    ""
                                }
                                name.value = it
                            },
                            modifier = Modifier.padding(top = 8.dp),
                            label = { Text(text = "Name") },
                            isError = nameError.value
                        )
                        if (nameError.value) {
                            Text(text = nameErrorMsg.value, color = Color.Red)
                        }
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(onClick = onDismiss) {
                                Text(text = "Cancel")
                            }
                            TextButton(onClick = {
                                if (name.value.isEmpty()) {
                                    nameError.value = true
                                    nameErrorMsg.value = "Field cannot be empty."
                                }
                                if (!nameError.value) {
                                    latLng?.let {
                                        val spot = createSpot(
                                            name = name.value,
                                            latLng = latLng
                                        )
                                        onConfirmClicked.invoke(spot)
                                        name.value = String()
                                    }
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
}

fun createSpot(latLng: LatLng, name: String): Spot {
    val currentTime = LocalTime.now()
    val time = currentTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM))
    val date = getCurrentDate()
    val timeStamp = Calendar.getInstance().time
    return Spot(
        id = UUID.randomUUID().toString(),
        lat = latLng.latitude,
        lng = latLng.longitude,
        name = name,
        time = time,
        date = date,
        timeStamp = timeStamp.time
    )
}