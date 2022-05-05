package com.mclowicz.mcorganizer.presentation.detail

sealed class DetailsViewEvent {
    class OpenDeleteDialog(val isOpen: Boolean) : DetailsViewEvent()
}
