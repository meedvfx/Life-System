package com.meedz.lifeos.ui.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.meedz.lifeos.domain.models.Event
import com.meedz.lifeos.ui.components.GlassCard
import com.meedz.lifeos.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun CalendarRoute(viewModel: CalendarViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    CalendarScreen(
        uiState = uiState,
        onDateSelected = viewModel::onDateSelected
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    uiState: CalendarUiState,
    onDateSelected: (Long) -> Unit
) {
    Scaffold(
        containerColor = DarkBackground,
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Calendar", 
                        style = AppTypography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimary
                    ) 
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Week Strip
            WeekDateStrip(
                selectedDateMillis = uiState.selectedDateMillis,
                onDateSelected = onDateSelected
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Events Agenda
            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = AccentPurple)
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (uiState.events.isEmpty()) {
                        item { 
                            Text(
                                "No events scheduled for this day.",
                                style = AppTypography.bodyMedium,
                                color = TextTertiary,
                                modifier = Modifier.padding(top = 20.dp)
                            ) 
                        }
                    } else {
                        items(uiState.events, key = { it.id }) { event ->
                            EventItem(event)
                        }
                    }
                    item { Spacer(modifier = Modifier.height(32.dp)) }
                }
            }
        }
    }
}

@Composable
fun WeekDateStrip(selectedDateMillis: Long, onDateSelected: (Long) -> Unit) {
    val calendar = Calendar.getInstance().apply { 
        timeInMillis = selectedDateMillis
        set(Calendar.DAY_OF_WEEK, firstDayOfWeek)
    }
    
    val currentWeekDays = (0..6).map { 
        val time = calendar.timeInMillis
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        time
    }

    LazyRow(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        items(currentWeekDays) { dateMillis ->
            val isSelected = isSameDay(dateMillis, selectedDateMillis)
            DateBox(dateMillis = dateMillis, isSelected = isSelected) {
                onDateSelected(dateMillis)
            }
        }
    }
}

@Composable
fun DateBox(dateMillis: Long, isSelected: Boolean, onClick: () -> Unit) {
    val dateCalendar = Calendar.getInstance().apply { timeInMillis = dateMillis }
    val dayOfWeek = SimpleDateFormat("EEE", Locale.getDefault()).format(dateCalendar.time).uppercase()
    val dayOfMonth = dateCalendar.get(Calendar.DAY_OF_MONTH)

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) AccentPurple else Color.Transparent)
            .clickable { onClick() }
            .padding(vertical = 12.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = dayOfWeek,
            style = AppTypography.labelMedium,
            color = if (isSelected) Color.White else TextTertiary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(if (isSelected) Color.White.copy(alpha = 0.2f) else DarkSurfaceElevated),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = dayOfMonth.toString(),
                style = AppTypography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                color = if (isSelected) Color.White else TextPrimary
            )
        }
    }
}

@Composable
fun EventItem(event: Event) {
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val formattedStart = timeFormat.format(Date(event.startTime))
    val formattedEnd = timeFormat.format(Date(event.endTime))

    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(16.dp)) {
            // Timeline
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = formattedStart,
                    style = AppTypography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = AccentPurple
                )
                Box(
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .width(2.dp)
                        .height(30.dp)
                        .background(AccentPurple.copy(alpha = 0.3f))
                )
                Text(
                    text = formattedEnd,
                    style = AppTypography.labelMedium,
                    color = TextTertiary
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Content
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = event.title,
                    style = AppTypography.titleLarge.copy(fontSize = 18.sp),
                    color = TextPrimary
                )
                if (event.description.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = event.description,
                        style = AppTypography.bodyMedium,
                        color = TextSecondary,
                        maxLines = 2
                    )
                }
            }
        }
    }
}

private fun isSameDay(time1: Long, time2: Long): Boolean {
    val cal1 = Calendar.getInstance().apply { timeInMillis = time1 }
    val cal2 = Calendar.getInstance().apply { timeInMillis = time2 }
    return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
           cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
}
