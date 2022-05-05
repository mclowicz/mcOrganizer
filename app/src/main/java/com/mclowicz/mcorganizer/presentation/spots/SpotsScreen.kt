package com.mclowicz.mcorganizer.presentation.spots

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionRequired
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.mclowicz.mcorganizer.presentation.common.bottomNavBar.BottomNavBar
import com.mclowicz.mcorganizer.presentation.spots.permission.DoNotShowMeRationaleUI
import com.mclowicz.mcorganizer.presentation.spots.permission.PermissionNotAvailableContent
import com.mclowicz.mcorganizer.presentation.spots.permission.PermissionNotGrantedUI
import com.mclowicz.mcorganizer.util.openMap
import com.mclowicz.mcorganizer.util.openSettings
import kotlinx.coroutines.flow.collectLatest

@ExperimentalPermissionsApi
@SuppressLint("RememberReturnType")
@Composable
fun SpotsScreen(
    navController: NavHostController,
    spotsViewModel: SpotsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val permissionState =
        rememberPermissionState(permission = Manifest.permission.ACCESS_COARSE_LOCATION)

    var doNotShowMeRationale by rememberSaveable {
        mutableStateOf(false)
    }
    val scaffoldState = rememberScaffoldState()
    LaunchedEffect(Unit) {
        spotsViewModel.getSpots()
    }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Scaffold(
            bottomBar = {
                AnimatedVisibility(
                    visible = true,
                    enter = expandVertically(),
                    exit = shrinkVertically()
                ) {
                    BottomNavBar(navController = navController)
                }
            },
            scaffoldState = scaffoldState
        ) {

            PermissionRequired(
                permissionState = permissionState,
                permissionNotGrantedContent = {
                    if (doNotShowMeRationale) {
                        DoNotShowMeRationaleUI(context = context)
                    } else {
                        PermissionNotGrantedUI(
                            onYesClick = { permissionState.launchPermissionRequest() },
                            onCancelClick = { doNotShowMeRationale = true })
                    }
                },
                permissionNotAvailableContent = {
                    PermissionNotAvailableContent(
                        onOpenSettingsClick = { context.openSettings() }
                    )
                },
                content = {
                    Map(
                        spotsViewModel = spotsViewModel,
                        context = context
                    )
                })
        }
    }
}

@SuppressLint("MissingPermission")
@Composable
fun Map(
    spotsViewModel: SpotsViewModel,
    context: Context
) {
    val location = spotsViewModel.location.collectAsState().value
    val viewState = spotsViewModel.spotsViewState.collectAsState().value
    val viewEvents = spotsViewModel.viewEvents
    val openCreateSpotDialog = remember { mutableStateOf(false) }
    val tempLatLng = remember { mutableStateOf<LatLng?>(null) }
    LocationServices.getFusedLocationProviderClient(context).apply {
        lastLocation.addOnSuccessListener { lastKnown ->
            if (location == null) {
                lastKnown?.let {
                    spotsViewModel.setLocation(it)
                }
            }
        }
    }

    val uiSettings = remember {
        MapUiSettings(
            zoomControlsEnabled = false,
            myLocationButtonEnabled = true
        )
    }
    val mapProperties by remember { mutableStateOf(MapProperties(isMyLocationEnabled = true)) }

    LaunchedEffect(Unit) {
        viewEvents.collectLatest { viewEvent ->
            when (viewEvent) {
                is SpotsViewEvent.OpenDialog -> {
                    openCreateSpotDialog.value = viewEvent.isOpen
                }
                is SpotsViewEvent.OnSpotLongClick -> {
                    viewEvent.isOpen?.let {
                        openCreateSpotDialog.value = viewEvent.isOpen
                    }
                    viewEvent.latLng?.let {
                        tempLatLng.value = it
                    }
                }
            }
        }
    }

    CreateSpotDialogUI(
        isVisible = openCreateSpotDialog.value,
        onConfirmClicked = { spot ->
            spotsViewModel.saveSpot(spot)
            spotsViewModel.triggerViewEvent(
                event = SpotsViewEvent.OpenDialog(isOpen = false)
            )
        },
        onDismiss = {
            spotsViewModel.triggerViewEvent(
                event = SpotsViewEvent.OpenDialog(isOpen = false)
            )
        },
        latLng = tempLatLng.value
    )

    location?.let {
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(LatLng(it.latitude, it.longitude), 15f)
        }
        GoogleMap(
            cameraPositionState = cameraPositionState,
            modifier = Modifier.fillMaxSize(),
            properties = mapProperties,
            uiSettings = uiSettings,
            onMapLongClick = {
                spotsViewModel.triggerViewEvent(
                    event = SpotsViewEvent.OnSpotLongClick(latLng = it, isOpen = true)
                )
            }
        ) {
            when (viewState) {
                is SpotsViewState.Idle -> {
                    // do nothing
                }
                is SpotsViewState.Content -> {
                    viewState.spots.forEach { spot ->
                        Marker(
                            position = LatLng(spot.lat, spot.lng),
                            title = spot.name,
                            snippet = "Long click to delete",
                            onInfoWindowClick = {
                                context.openMap(spot = spot)
                            },
                            onInfoWindowLongClick = {
                                spotsViewModel.deleteSpot(spot = spot)
                            },
                            onClick = {
                                it.showInfoWindow()
                                true
                            },
                            icon = BitmapDescriptorFactory.defaultMarker(
                                BitmapDescriptorFactory.HUE_VIOLET
                            )
                        )
                    }
                }
            }
        }
    }
}