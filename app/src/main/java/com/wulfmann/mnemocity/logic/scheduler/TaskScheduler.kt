package com.wulfmann.mnemocity.logic.scheduler

import com.wulfmann.mnemocity.logic.taskflow.TaskFlow
import com.wulfmann.mnemocity.logic.scheduler.RecurrenceType
import org.threeten.bp.LocalDate

object TaskScheduler {
    private val taskStore: MutableMap<LocalDate, MutableList<TaskFlow>> = mutableMapOf()

    // Add a task to a specific date
    fun addTask(date: LocalDate, task: TaskFlow) {
        val tasks = taskStore.getOrPut(date) { mutableListOf() }
        tasks.add(task)

        // Handle recurrence if applicable
        task.recurrence?.let { recurrence ->
            val recurrenceDates = applyRecurrence(date, recurrence)
            recurrenceDates.forEach { recurDate ->
                val recurTasks = taskStore.getOrPut(recurDate) { mutableListOf() }
                recurTasks.add(task.copy(isRecurringInstance = true))
            }
        }
    }

    // Retrieve tasks for a specific date
    fun getTasks(date: LocalDate): List<TaskFlow> {
        return taskStore[date].orEmpty()
    }

    //Retrieve all tasks (for calendar preview)
    fun getAllTasks(): Map<LocalDate, List<TaskFlow>> {
        return taskStore.toMap()
    }

    //Recurrence logic (basic scaffold)
    private fun applyRecurrence(startDate: LocalDate, recurrence: RecurrenceType): List<LocalDate> {
        return when (recurrence) {
            RecurrenceType.DAILY -> (1..6).map { startDate.plusDays(it.toLong()) }
            RecurrenceType.WEEKLY -> (1..3).map { startDate.plusWeeks(it.toLong()) }
            RecurrenceType.MONTHLY -> (1..2).map { startDate.plusMonths(it.toLong()) }
            RecurrenceType.NONE -> emptyList()
            RecurrenceType.CUSTOM -> emptyList()
        }
    }
}