package com.mclowicz.mcorganizer.presentation.detailSpot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mclowicz.mcorganizer.domain.model.Spot
import com.mclowicz.mcorganizer.domain.model.fromSpot
import com.mclowicz.mcorganizer.domain.model.fromSpotEntity
import com.mclowicz.mcorganizer.domain.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailSpotViewModel @Inject constructor(
    private val repository: NoteRepository
) : ViewModel() {

    private val _spot: MutableStateFlow<Spot?> = MutableStateFlow(null)
    val spot = _spot.asStateFlow()

    private val _viewEvents = Channel<DetailSpotViewEvent>(Channel.BUFFERED)
    val viewEvents = _viewEvents.receiveAsFlow()

    fun getSpotById(spotId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getSpotByGivenId(spotId)
                .collect { spotEntity ->
                    _spot.update {
                        fromSpotEntity(spotEntity = spotEntity)
                    }
                }
        }
    }

    fun deleteSpot(spot: Spot) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteSpot(
                spotEntity = fromSpot(spot = spot)
            )
        }
    }

    fun triggerEvent(event: DetailSpotViewEvent) {
        viewModelScope.launch(Dispatchers.IO) {
            _viewEvents.send(element = event)
        }
    }
}