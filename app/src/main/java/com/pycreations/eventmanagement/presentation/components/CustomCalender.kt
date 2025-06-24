package com.pycreations.eventmanagement.presentation.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.kizitonwose.calendar.compose.VerticalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.*
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import com.pycreations.eventmanagement.R

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OneMonthCustomCalendar(eventDates: Set<LocalDate>) {
    val currentMonth = YearMonth.now()
    val calendarState = rememberCalendarState(
        startMonth = currentMonth,
        endMonth = currentMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = DayOfWeek.MONDAY
    )

    VerticalCalendar(
        state = calendarState,
        dayContent = { day ->
            val isEvent = eventDates.contains(day.date)
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .size(35.dp)
                    .background(
                        color = if (isEvent) Color(0xFF4CAF50) else Color.Transparent,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = day.date.dayOfMonth.toString(),
                    color = if (isEvent) Color.White else colorResource(R.color.black).copy(0.5f)
                )
            }
        }
    )
}
