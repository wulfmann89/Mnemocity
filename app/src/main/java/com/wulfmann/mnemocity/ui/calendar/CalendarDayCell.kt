package com.wulfmann.mnemocity.ui.calendar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wulfmann.mnemocity.logic.taskflow.TaskFlow
import org.threeten.bp.LocalDate
import kotlin.collections.isNotEmpty

@Composable
fun CalendarDayCell(
    date: LocalDate,
    tasks: List<TaskFlow>,
    onClick: () -> Unit
) {
    val hasTasks = tasks.isNotEmpty()

    Surface(
        modifier = Modifier
            .padding(4.dp)
            .size(48.dp)
            .clickable { onClick() },
        color = if (hasTasks) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
        shape = MaterialTheme.shapes.small,
        tonalElevation = if (hasTasks) 2.dp else 0.dp
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = date.dayOfMonth.toString(),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}