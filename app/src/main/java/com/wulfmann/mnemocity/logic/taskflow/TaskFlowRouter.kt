package com.wulfmann.mnemocity.logic.taskflow

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.wulfmann.mnemocity.logic.machinelearning.MLModule
import com.wulfmann.mnemocity.logic.scheduler.CalendarModule
import com.wulfmann.mnemocity.logic.scheduler.RecurrenceType


fun routeTaskFlow(input: String): TaskFlow {
    val startDate = CalendarModule.parseNaturalLanguageDate(input)
    val recurrence = CalendarModule.suggestRecurringOptions(input)
    val classified = classifyTask(input)

    return TaskFlow(
        taskText = input,
        startDate = startDate,
        isRecurring = recurrence != RecurrenceType.NONE,
        recurrenceType = recurrence,
        classification = classified.classification
    )
}

fun handleTaskFlow(taskFlow: TaskFlow, context: Context) {
    Log.d("TaskFlowRouter", "Task: ${taskFlow.taskText}")
    Log.d("TaskFlowRouter", "Start Date: ${taskFlow.startDate}")
    Log.d("TaskFlowRouter", "Recurring: ${taskFlow.isRecurring}")
    Log.d("TaskFlowRouter", "Recurrence Type: ${taskFlow.recurrenceType}")
    Log.d("TaskFlowRouter", "Classification: ${taskFlow.classification}")
    Log.d("TaskFlowRouter", "Task: ${taskFlow.taskText}")
    // Log to MLModule for reinforcement learning
    val classificationResult = MLModule.classifyTask(taskFlow.taskText)
    val recurrence = MLModule.suggestRecurringOptions(taskFlow.taskText)

    taskFlow.classification = classificationResult.classification
    taskFlow.recurrenceType = recurrence
    Log.d("TaskClassifier", "Classified as: ${classificationResult.classification}, Recurring: ${classificationResult.isRecurring}")
    // Mark as completed if it's a match
    taskFlow.isCompleted = true
    showCompletionFeedback(taskFlow.taskText, context)

    // TODO: Pass to ViewModel or navigate to detail screen
}

fun showCompletionFeedback(taskName: String, context: Context) {
    Toast.makeText(context, "\"$taskName\" marked complete. You're doing great!", Toast.LENGTH_SHORT).show()
    // Optionally trigger celebratory animation or badge with first task completion and at certain intervals
}