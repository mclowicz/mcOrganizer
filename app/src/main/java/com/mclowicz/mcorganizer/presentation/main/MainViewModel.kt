package com.mclowicz.mcorganizer.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mclowicz.mcorganizer.data.repository.DataStoreRepositoryImpl
import com.mclowicz.mcorganizer.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val repositoryImpl: DataStoreRepositoryImpl
) : ViewModel() {

    private val _startDestination: MutableStateFlow<String> = MutableStateFlow(Screen.Welcome.route)
    val startDestination: StateFlow<String> = _startDestination

    init {
        viewModelScope.launch {
            repositoryImpl.readOnBoardingState().collect { completed ->
                if (completed) {
                    _startDestination.value = Screen.Home.route
                } else {
                    _startDestination.value = Screen.Welcome.route
                }
            }
        }
    }
}