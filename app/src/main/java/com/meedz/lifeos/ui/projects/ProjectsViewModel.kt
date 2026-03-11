package com.meedz.lifeos.ui.projects

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meedz.lifeos.domain.models.Project
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class ProjectsUiState(
    val activeProjects: List<Project> = emptyList(),
    val isLoading: Boolean = false
)

@HiltViewModel
class ProjectsViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(ProjectsUiState())
    val uiState: StateFlow<ProjectsUiState> = _uiState.asStateFlow()

    init {
        loadProjects()
    }

    private fun loadProjects() {
        _uiState.update { it.copy(isLoading = true) }
        val mockActiveProjects = listOf(
            Project(id = 1, title = "Personal Life OS App", description = "Android native architecture and design", progress = 60, isCompleted = false),
            Project(id = 2, title = "Home Renovation", description = "Kitchen tiling and appliances", progress = 25, isCompleted = false)
        )
        _uiState.update { 
            it.copy(
                activeProjects = mockActiveProjects,
                isLoading = false
            ) 
        }
    }
}
