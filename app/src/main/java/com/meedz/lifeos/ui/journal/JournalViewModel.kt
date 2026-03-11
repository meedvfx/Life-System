package com.meedz.lifeos.ui.journal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meedz.lifeos.domain.models.JournalEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class JournalUiState(
    val entries: List<JournalEntry> = emptyList(),
    val isLoading: Boolean = false
)

@HiltViewModel
class JournalViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(JournalUiState())
    val uiState: StateFlow<JournalUiState> = _uiState.asStateFlow()

    init {
        loadEntries()
    }

    private fun loadEntries() {
        _uiState.update { it.copy(isLoading = true) }
        val mockEntries = listOf(
            JournalEntry(
                id = 1,
                date = System.currentTimeMillis() - 86400000L,
                content = "Today was extremely productive. I finally figured out the Jetpack Compose navigation bug that had been bothering me. Had a great workout too.",
                moodScore = 5,
                energyScore = 4,
                tags = listOf("productivity", "fitness")
            ),
            JournalEntry(
                id = 2,
                date = System.currentTimeMillis() - (86400000L * 2),
                content = "Felt a bit sluggish today. Need to make sure I get 8 hours of sleep tonight. Spent most of the day reading.",
                moodScore = 3,
                energyScore = 2,
                tags = listOf("health", "reading")
            )
        )
        
        _uiState.update { 
            it.copy(
                entries = mockEntries,
                isLoading = false
            ) 
        }
    }
}
