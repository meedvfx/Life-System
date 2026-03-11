package com.meedz.lifeos.ui.knowledge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meedz.lifeos.domain.models.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class KnowledgeUiState(
    val notes: List<Note> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = false
)

@HiltViewModel
class KnowledgeViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(KnowledgeUiState())
    val uiState: StateFlow<KnowledgeUiState> = _uiState.asStateFlow()

    init {
        loadNotes()
    }

    private fun loadNotes() {
        _uiState.update { it.copy(isLoading = true) }
        val mockNotes = listOf(
            Note(
                title = "Clean Architecture Concepts",
                content = "Clean architecture separates software into layers. Core logic in the center. Dependencies point inwards...",
                tags = listOf("android", "architecture")
            ),
            Note(
                title = "Books to Read 2026",
                content = "1. Think Fast and Slow\n2. Atomic Habits\n3. The Pragmatic Programmer",
                tags = listOf("reading", "goals")
            )
        )
        
        _uiState.update { 
            it.copy(
                notes = mockNotes,
                isLoading = false
            ) 
        }
    }

    fun updateSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        // TODO actual filtering logic
    }
}
