package com.mclowicz.mcorganizer.presentation.welcome

sealed class WelcomeViewEvent {
    object NavigateToHomeScreen : WelcomeViewEvent()
}