package com.wulfmann.mnemocity.ui.calendar

import android.Manifest
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.wulfmann.mnemocity.logic.scheduler.ReminderManager
import com.wulfmann.mnemocity.logic.taskflow.TaskFlow
import org.threeten.bp.LocalDate

@RequiresPermission(Manifest.permission.SCHEDULE_EXACT_ALARM)
@Composable
fun CalendarScreen(
    date: LocalDate,
    taskMap: Map<LocalDate, List<TaskFlow>>,                                // Task previews per date
    onDateSelected: (LocalDate) -> Unit
) {
    val context = LocalContext.current
    val reminderManager = remember { ReminderManager(context) }

    LaunchedEffect(Unit) {
        if (reminderManager.canScheduleExactAlarms()) {
            taskMap.forEach { (date, tasks) ->
                tasks.forEach { task ->
                    reminderManager.scheduleReminder(task, date.atTime(9, 0))
            }
                }
        } else {
            Log.w("CalendarScreen", "Exact alarms not permitted. Consider fallback.")
        }
    }

    val today = remember { LocalDate.now() }
    val daysInMonth = remember { generateMonthDays(today) }

    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(daysInMonth) { date ->
            CalendarDayCell(
                date = date,
                tasks = taskMap[date].orEmpty(),
                onClick = { onDateSelected(date) }
            )
        }
    }
}

fun generateMonthDays(reference: LocalDate): List<LocalDate> {
    val firstDay = reference.withDayOfMonth(1)
    val lastDay = reference.withDayOfMonth(reference.lengthOfMonth())
    return (0 until lastDay.dayOfMonth).map { firstDay.plusDays(it.toLong())}
}