package com.wulfmann.mnemocity.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wulfmann.mnemocity.TaskRepository
import com.wulfmann.mnemocity.logic.taskflow.TaskFlow
import com.wulfmann.mnemocity.ui.calendar.CalendarDayCell
import com.wulfmann.mnemocity.ui.calendar.generateMonthDays
import org.threeten.bp.LocalDate

@Composable
fun MainScreen(
    taskMap: Map<LocalDate, List<TaskFlow>>,                                                            // All tasks mapped by date
    activeTasks: List<TaskFlow>,                                                                        // Currently active tasks
    onCalendarTap: (LocalDate) -> Unit,                                                                          // Triggered when calendar is tapped
    onStartSession: () -> Unit,                                                                         // Triggered when start button is tapped
    onTaskCheck: (TaskFlow) -> Unit                                                                     // Triggered when a task is checked off
) {
    val today = remember { LocalDate.now() }                                                            // Get today's date
    val daysInMonth = remember { generateMonthDays(today) }                                  // Generate all days for current month

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())                                               // Wraps a Vertical Scroll to give scroll support
            .padding(16.dp)) {                                                                     // Vertical layout container with padding for breathing room.
        Text(                                                                                           // Displays current month using branded colors and fonts
            text = today.month.name.lowercase().replaceFirstChar { it.uppercase() },
            style = MaterialTheme.typography.displayLarge
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)                 // Adjust as needed
            ) {                                         // Renders a 7-column calendar grid. Highlights dates with tasks.  Tapping any date opens the full calendar
            items(daysInMonth) { date ->
                val tasksForDate = taskMap[date].orEmpty()
                CalendarDayCell(
                    date = date,
                    tasks = tasksForDate,
                    onClick = { onCalendarTap(date) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))                                               // Adds vertical spacing between calendar and button

        SessionStartButton(onStart = onStartSession)

        Spacer(modifier = Modifier.height(16.dp))

        Text("Active Tasks", style = MaterialTheme.typography.headlineMedium)                     // Section header for active tasks

        if (activeTasks.isEmpty()) {
            Text(
                text = "You've got no active tasks.  Take a breath or start a new one.",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        } else {
            activeTasks.forEach { task ->                                                                   // Lists each active task with a checkbox. Tapping checks it off manually or vocally.
                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Checkbox(
                        checked = task.isCompleted,
                        onCheckedChange = { onTaskCheck(task) }
                    )
                    Text(task.taskText, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}

