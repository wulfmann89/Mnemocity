package com.wulfmann.mnemocity.logic.taskflow

import com.wulfmann.mnemocity.logic.scheduler.RecurrenceType
import org.threeten.bp.LocalDateTime
import java.time.LocalDate
import java.util.UUID

data class TaskFlow(
    val taskText: String,
    val isRecurring: Boolean,
    var classification: TaskType = TaskType.UNKNOWN,
    val startDate: org.threeten.bp.LocalDate?,
    var scheduledTime: org.threeten.bp.LocalDateTime? = null,
    var recurrenceType: RecurrenceType = RecurrenceType.NONE,
    val suggestedSteps: List<String> = emptyList(),
    val recurrence: RecurrenceType? = null,
    val isRecurringInstance: Boolean = false,
    val taskId: String = UUID.randomUUID().toString(),
    var isCompleted: Boolean = false,
    val completedAt: LocalDateTime? = null,
    var completionState: CompletionState = CompletionState.NOT_STARTED,
)

enum class TaskType {
    SHORT_TERM,
    LONG_TERM,
    UNKNOWN
}

enum class CompletionState {
    NOT_STARTED,
    IN_PROGRESS,
    COMPLETED,
    DEFERRED
}

// Function to determine if it's recurring
fun determineTaskType(text: String): TaskType {
    val lower = text.lowercase()
    return when {
        listOf("today", "now", "soon", "before bed", "in an hour").any { lower.contains(it) } ->
            TaskType.SHORT_TERM
        listOf("next week", "monthly", "every friday", "long term", "next month").any { lower.contains(it) } ->
            TaskType.LONG_TERM
        else -> TaskType.UNKNOWN
    }
}

// Function to determine if it's recurring
fun checkIsRecurring(text: String): Boolean {
    val lower = text.lowercase()
    return listOf("every, daily", "weekly", "monthly").any { lower.contains(it) }
}

// Function to create the TaskFlow object
fun createTaskFlowObject(
    text: String,
    startDate: LocalDate? = null, // Optional startDate
    scheduledTime: org.threeten.bp.LocalDateTime? = null // Optional scheduledTime
    // Add other parameters as needed like recurrenceType, suggestedSteps
): TaskFlow {
    val determinedType = determineTaskType(text)
    val isRecurring = checkIsRecurring(text)

    return TaskFlow(
        taskText = text,
        isRecurring = isRecurring,
        recurrenceType = RecurrenceType.NONE,
        classification = determinedType,
        startDate = startDate as org.threeten.bp.LocalDate?,
        scheduledTime = scheduledTime
    )
}