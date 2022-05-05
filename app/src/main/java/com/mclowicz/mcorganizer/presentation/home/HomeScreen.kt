package com.mclowicz.mcorganizer.presentation.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.mclowicz.mcorganizer.navigation.Screen
import com.mclowicz.mcorganizer.presentation.common.bottomNavBar.BottomNavBar
import kotlinx.coroutines.flow.collectLatest

@ExperimentalPagerApi
@ExperimentalAnimationApi
@Composable
fun HomeScreen(
    navController: NavHostController,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val viewState = homeViewModel.homeViewState.collectAsState().value
    val viewEvent = homeViewModel.viewEvents

    LaunchedEffect(Unit) {
        homeViewModel.getData()
        viewEvent.collectLatest { viewEvent ->
            when (viewEvent) {
                is HomeViewEvent.OpenDetailScreen -> {
                    navController.navigate(Screen.Details.passArguments(id = viewEvent.id))
                }
                is HomeViewEvent.OpenDetailSpotScreen -> {
                    navController.navigate(Screen.DetailsSpot.passArguments(id = viewEvent.id))
                }
            }
        }
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
            }
        ) {

            when (viewState) {
                is HomeViewState.Idle -> {}
                is HomeViewState.Content -> {
                    LazyColumn(modifier = Modifier.padding(bottom = 64.dp)) {
                        items(items = viewState.components) { component ->
                            when (component) {
                                is HomeComponent.HeaderComponent -> {
                                    HomeHeaderUI()
                                }
                                is HomeComponent.NotesComponent -> {
                                    NotesUI(
                                        title = "Latest notes",
                                        notes = component.notes,
                                        onNoteClicked = {
                                            homeViewModel.triggerEvent(
                                                event = HomeViewEvent.OpenDetailScreen(id = it.id)
                                            )
                                        }
                                    )
                                }
                                is HomeComponent.SpotsComponent -> {
                                    SpotsUI(
                                        title = "Latest spots",
                                        spots = component.spots,
                                        onSpotClicked = {
                                            homeViewModel.triggerEvent(
                                                event = HomeViewEvent.OpenDetailSpotScreen(id = it.id)
                                            )
                                        }
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