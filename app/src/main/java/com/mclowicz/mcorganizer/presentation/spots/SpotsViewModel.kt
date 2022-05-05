package com.mclowicz.mcorganizer.presentation.spots

import android.location.Location
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
class SpotsViewModel @Inject constructor(
    private val repository: NoteRepository
) : ViewModel() {

    private var _location: MutableStateFlow<Location?> = MutableStateFlow(null)
    val location = _location.asStateFlow()

    private val _spotsViewState: MutableStateFlow<SpotsViewState> =
        MutableStateFlow(SpotsViewState.Idle)
    val spotsViewState = _spotsViewState.asStateFlow()

    private val _viewEvents = Channel<SpotsViewEvent>(Channel.BUFFERED)
    val viewEvents = _viewEvents.receiveAsFlow()

    fun setLocation(location: Location) {
        this._location.value = location
    }

    fun getSpots() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getSpots()
                .collect { list ->
                    if (list.isEmpty()) {

                    } else {
                        _spotsViewState.update {
                            SpotsViewState.Content(
                                spots = list.map {
                                    fromSpotEntity(spotEntity = it)
                                }.toMutableList()
                            )
                        }
                    }
                }
        }
    }

    fun triggerViewEvent(event: SpotsViewEvent) {
        viewModelScope.launch(Dispatchers.IO) {
            _viewEvents.send(element = event)
        }
    }

    fun saveSpot(spot: Spot) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertSpot(fromSpot(spot))
            (_spotsViewState.value as? SpotsViewState.Content)?.let { content ->
                _spotsViewState.update {
                    SpotsViewState.Content(
                        spots = content.spots.apply { add(spot) }
                    )
                }
            }
        }
    }

    fun deleteSpot(spot: Spot) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteSpot(
                spotEntity = fromSpot(spot = spot)
            )
            (_spotsViewState.value as? SpotsViewState.Content)?.let { content ->
                _spotsViewState.update {
                    SpotsViewState.Content(
                        spots = content.spots.apply { remove(spot) }
                    )
                }
            }
        }
    }
}