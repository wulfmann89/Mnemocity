package com.wulfmann.mnemocity.ui.calendar

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wulfmann.mnemocity.TaskRepository
import com.wulfmann.mnemocity.logic.taskflow.TaskFlow
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime

@Composable
fun ScheduleDayScreen(date: LocalDate) {
    val tasksForDay = TaskRepository.taskMap[date] ?: emptyList()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Schedule for $date",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyColumn {
            items((0..23).toList()) { hour ->
                val timeSlot = LocalTime.of(hour, 0)
                var tasksAtHour = tasksForDay.filter { it.scheduledTime?.hour == hour }

                HourBlock(timeSlot, tasksAtHour)
            }
        }
    }
}

@Composable
fun HourBlock(time: LocalTime, tasks: List<TaskFlow>) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Text(
            text = "${time}:00",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )

        if (tasks.isEmpty()) {
            Text(text = "- No tasks -", style = MaterialTheme.typography.bodySmall)
        } else {
            tasks.forEach { task ->
                Text(text = "• ${task.taskText}", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}