package com.mclowicz.mcorganizer.presentation.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.mclowicz.mcorganizer.navigation.Screen
import com.mclowicz.mcorganizer.presentation.common.bottomNavBar.BottomNavBar
import com.mclowicz.mcorganizer.presentation.home.NotesUI
import com.mclowicz.mcorganizer.presentation.home.SpotsUI
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SearchScreen(
    navController: NavHostController,
    searchViewModel: SearchViewModel = hiltViewModel()
) {
    val notes = searchViewModel.notes.collectAsState().value
    val spots = searchViewModel.spots.collectAsState().value
    LaunchedEffect(Unit) {
        searchViewModel.viewEvents.collectLatest { viewEvent ->
            when (viewEvent) {
                is SearchViewEvent.NavigateToNoteDetailScreen -> {
                    navController.navigate(Screen.Details.passArguments(id = viewEvent.id))
                }
                is SearchViewEvent.NavigateToSpotDetailScreen -> {
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
            Column() {
                SearchBar(
                    hint = "Search...",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    onSearch = { searchFraze ->
                        if (searchFraze.length > 2 && searchFraze.isNotBlank()) {
                            searchViewModel.search(search = searchFraze)
                        } else {
                            searchViewModel.resetView()
                        }
                    }
                )
                if (notes.isNotEmpty()) {
                    NotesUI(
                        title = "Searched notes",
                        notes = notes.toMutableList(),
                        onNoteClicked = {
                            searchViewModel.triggerViewEvent(
                                event = SearchViewEvent.NavigateToNoteDetailScreen(id = it.id)
                            )
                        })
                }
                if (spots.isNotEmpty()) {
                    SpotsUI(
                        title = "Searched spots",
                        spots = spots.toMutableList(),
                        onSpotClicked = {
                            searchViewModel.triggerViewEvent(
                                event = SearchViewEvent.NavigateToSpotDetailScreen(id = it.id)
                            )
                        }
                    )
                }
                if (notes.isEmpty() && spots.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = "No results...")
                    }
                }
            }
        }
    }
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    hint: String = "",
    onSearch: (String) -> Unit = {}
) {
    var text by remember {
        mutableStateOf("")
    }
    var isHintDisplayed by remember {
        mutableStateOf(hint != "")
    }

    Box(modifier = modifier) {
        BasicTextField(
            value = text,
            onValueChange = {
                text = it
                onSearch(it)
            },
            maxLines = 1,
            singleLine = true,
            textStyle = TextStyle(color = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .shadow(5.dp, CircleShape)
                .background(Color.White, CircleShape)
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .onFocusChanged {
                    isHintDisplayed = !it.hasFocus
                }
        )
        if (isHintDisplayed) {
            Text(
                text = hint,
                color = Color.LightGray,
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            )
        }
    }
}