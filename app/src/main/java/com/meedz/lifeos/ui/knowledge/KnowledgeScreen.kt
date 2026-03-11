package com.meedz.lifeos.ui.knowledge

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.meedz.lifeos.domain.models.Note
import com.meedz.lifeos.ui.components.GlassCard
import com.meedz.lifeos.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun KnowledgeRoute(viewModel: KnowledgeViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    KnowledgeScreen(
        uiState = uiState,
        onSearchQueryChanged = viewModel::updateSearchQuery
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KnowledgeScreen(
    uiState: KnowledgeUiState,
    onSearchQueryChanged: (String) -> Unit
) {
    Scaffold(
        containerColor = DarkBackground,
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Knowledge Base", 
                        style = AppTypography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimary
                    ) 
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* TODO create new note */ },
                containerColor = AccentBlue,
                contentColor = Color.White
            ) {
                Icon(Icons.Filled.Add, contentDescription = "New Note")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Search Bar
            SearchBar(
                query = uiState.searchQuery,
                onQueryChanged = onSearchQueryChanged,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = AccentBlue)
                }
            } else {
                if (uiState.notes.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp)) {
                        Text(
                            "Your personal wiki is empty.\nStart writing notes and linking ideas!",
                            style = AppTypography.bodyMedium,
                            color = TextTertiary,
                            modifier = Modifier.padding(top = 20.dp)
                        )
                    }
                } else {
                    // Staggered Masonry Grid for Notes
                    LazyVerticalStaggeredGrid(
                        columns = StaggeredGridCells.Fixed(2),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalItemSpacing = 16.dp
                    ) {
                        items(uiState.notes, key = { it.id }) { note ->
                            NoteCard(note = note)
                        }
                        item { Spacer(modifier = Modifier.height(80.dp)) }
                    }
                }
            }
        }
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .clip(RoundedCornerShape(25.dp))
            .background(DarkSurfaceElevated)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Search",
                tint = TextTertiary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            BasicTextField(
                value = query,
                onValueChange = onQueryChanged,
                textStyle = AppTypography.bodyLarge.copy(color = TextPrimary),
                cursorBrush = SolidColor(AccentBlue),
                modifier = Modifier.fillMaxWidth(),
                decorationBox = { innerTextField ->
                    if (query.isEmpty()) {
                        Text("Search notes...", style = AppTypography.bodyLarge, color = TextTertiary)
                    }
                    innerTextField()
                }
            )
        }
    }
}

@Composable
fun NoteCard(note: Note) {
    val dateFormat = SimpleDateFormat("MMM dd", Locale.getDefault())
    val dateString = dateFormat.format(Date(note.dateModified))

    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        cornerRadius = 16.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = note.title,
                style = AppTypography.titleLarge.copy(fontSize = 18.sp, fontWeight = FontWeight.SemiBold),
                color = TextPrimary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = note.content,
                style = AppTypography.bodyMedium.copy(lineHeight = 20.sp),
                color = TextSecondary,
                maxLines = 5,
                overflow = TextOverflow.Ellipsis
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = dateString,
                    style = AppTypography.labelMedium,
                    color = TextTertiary
                )
                
                if (note.tags.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(AccentBlue.copy(alpha = 0.2f))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "#${note.tags.first()}",
                            style = AppTypography.labelMedium.copy(fontSize = 10.sp),
                            color = AccentBlue
                        )
                    }
                }
            }
        }
    }
}
