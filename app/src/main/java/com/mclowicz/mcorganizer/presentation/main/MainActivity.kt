package com.mclowicz.mcorganizer.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.mclowicz.mcorganizer.navigation.SetupNavGraph
import com.mclowicz.mcorganizer.ui.theme.McOrganizerTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@ExperimentalPermissionsApi
@ExperimentalPagerApi
@ExperimentalAnimationApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            McOrganizerTheme {
                val screen = mainViewModel.startDestination.collectAsState().value
                val navController = rememberNavController()
                SetupNavGraph(navController = navController, startDestination = screen)
            }
        }
    }
}