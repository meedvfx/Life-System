package com.meedz.lifeos.ui.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meedz.lifeos.domain.models.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

data class CalendarUiState(
    val events: List<Event> = emptyList(),
    val selectedDateMillis: Long = System.currentTimeMillis(),
    val isLoading: Boolean = false
)

@HiltViewModel
class CalendarViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(CalendarUiState())
    val uiState: StateFlow<CalendarUiState> = _uiState.asStateFlow()

    init {
        loadEventsForDate(_uiState.value.selectedDateMillis)
    }

    fun onDateSelected(dateMillis: Long) {
        _uiState.update { it.copy(selectedDateMillis = dateMillis) }
        loadEventsForDate(dateMillis)
    }

    private fun loadEventsForDate(dateMillis: Long) {
        // TODO: Combine EventRepository, TaskRepository deadlines, and scheduled habits
        // For now, load empty or mock
        _uiState.update {
            it.copy(
                isLoading = false,
                events = listOf() // Normally fetched from repository
            )
        }
    }
}
