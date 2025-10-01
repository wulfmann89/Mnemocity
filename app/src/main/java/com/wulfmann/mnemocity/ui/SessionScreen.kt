package com.wulfmann.mnemocity.ui

import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wulfmann.mnemocity.logic.SessionViewModel
import com.wulfmann.mnemocity.logic.scheduler.ReminderManager
import com.wulfmann.mnemocity.logic.scheduler.TaskScheduler
import com.wulfmann.mnemocity.ui.calendar.CalendarScreen
import org.threeten.bp.LocalDate

@Composable
fun SessionScreen(
    speechLauncher: ActivityResultLauncher<Intent>,
    onResult: (String?) -> Unit
    ) {
    val context = LocalContext.current
    val viewModel: SessionViewModel = viewModel()
    val reminderManager = remember { ReminderManager(context) }
    val taskMap = remember { TaskScheduler.getAllTasks() }
    var sessionStarted by remember { mutableStateOf(false) }
    var spokenText by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        if (reminderManager.canScheduleExactAlarms()) {
            taskMap.forEach { (date, tasks) ->
                tasks.forEach {task ->
                    reminderManager.scheduleReminder(task, date.atTime(9, 0))
                }
            }
        } else {
            Log.w("SessionScreen", "Exact alarms not permitted. Consider fallback.")
        }
    }

    Column {
        // Passive calendar preview
        CalendarScreen(
            date = LocalDate.now(),
            taskMap = taskMap,
            onDateSelected = { selectedDate ->
                // Optional: highlight or preview, not necessarily start session
                println("Tapped date: $selectedDate")
            }
        )
        // Primary session trigger
        Button(onClick = {
            viewModel.startSession(context, speechLauncher, onResult)
            sessionStarted = true
        }) {
            Text("Start Session")
        }

        if (sessionStarted) {
            Text("Session started. Listening for task request...")
        }

        spokenText?.let {
            Text("You said: $it")
        }
    }
}

@Composable
fun SessionScreenPreviewOnly() {
    Column {
        Text("Preview Mode")
        CalendarScreen(
            date = LocalDate.now(),
            taskMap = TaskScheduler.getAllTasks(),
            onDateSelected = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainScreen() {
    SessionScreenPreviewOnly()
}