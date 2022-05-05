package com.mclowicz.mcorganizer.presentation.detailSpot

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.mclowicz.mcorganizer.navigation.Screen
import com.mclowicz.mcorganizer.presentation.common.NoteDeleteDialogUI
import com.mclowicz.mcorganizer.presentation.detail.DetailTopBar
import com.mclowicz.mcorganizer.util.openMap
import kotlinx.coroutines.flow.collectLatest

@Composable
fun DetailSpotScreen(
    navController: NavHostController,
    spotId: String,
    detailsSpotViewModel: DetailSpotViewModel = hiltViewModel()
) {
    val spot = detailsSpotViewModel.spot.collectAsState().value
    val openDeleteDialog = remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        detailsSpotViewModel.apply {
            getSpotById(spotId = spotId)
            viewEvents.collectLatest { viewEvent ->
                when (viewEvent) {
                    is DetailSpotViewEvent.OpenDeleteDialog -> {
                        openDeleteDialog.value = viewEvent.isOpen
                    }
                    is DetailSpotViewEvent.OpenMapIntent -> {
                        context.openMap(spot = viewEvent.spot)
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
                    title = Screen.DetailsSpot.title,
                    navController = navController
                )
            },
        ) {
            NoteDeleteDialogUI(
                isVisible = openDeleteDialog.value,
                onConfirmClicked = {
                    spot?.let {
                        detailsSpotViewModel.deleteSpot(spot = spot)
                        navController.popBackStack()
                        detailsSpotViewModel.triggerEvent(
                            event = DetailSpotViewEvent.OpenDeleteDialog(false)
                        )
                    }
                },
                onDismiss = {
                    detailsSpotViewModel.triggerEvent(
                        event = DetailSpotViewEvent.OpenDeleteDialog(false)
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
                spot?.let { spot ->
                    Row() {
                        Text(
                            text = spot.name,
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
                            Text(text = spot.date)
                        }
                        Column(modifier = Modifier.weight(0.5f)) {
                            Text(text = spot.time)
                        }
                    }
                    Row() {
                        Column(
                            modifier = Modifier
                                .weight(0.5f)
                                .fillMaxWidth()
                        ) {
                            OutlinedButton(
                                border = BorderStroke(1.dp, MaterialTheme.colors.primary),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                onClick = {
                                    detailsSpotViewModel.triggerEvent(
                                        event = DetailSpotViewEvent.OpenMapIntent(spot = spot)
                                    )
                                }
                            ) {
                                Text(text = "Navigate")
                            }
                        }
                        Column(
                            modifier = Modifier
                                .weight(0.5f)
                                .fillMaxWidth()
                        ) {
                            OutlinedButton(
                                border = BorderStroke(1.dp, MaterialTheme.colors.primary),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                onClick = {
                                    detailsSpotViewModel.triggerEvent(
                                        event = DetailSpotViewEvent.OpenDeleteDialog(true)
                                    )
                                }
                            ) {
                                Text(text = "Delete spot")
                            }
                        }
                    }
                }
            }
        }
    }
}