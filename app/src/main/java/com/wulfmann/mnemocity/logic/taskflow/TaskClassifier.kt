package com.wulfmann.mnemocity.logic.taskflow

import com.wulfmann.mnemocity.logic.taskflow.ClassifiedTask
fun classifyTask(text: String): ClassifiedTask {
    val lower = text.lowercase()

    val classification = when {
        listOf("today", "now", "soon", "before bed", "in an hour").any { lower.contains(it) } ->
            TaskType.SHORT_TERM
        listOf("next week", "monthly", "every friday", "long term", "next month").any { lower.contains(it) } ->
            TaskType.LONG_TERM
        else -> TaskType.UNKNOWN
    }

    val isRecurring = listOf("every", "daily", "weekly", "monthly").any { lower.contains(it) }

    return ClassifiedTask(
        taskText = text,
        isRecurring = isRecurring,
        classification = classification,
        suggestedSteps = emptyList(),                                                                                            // can populate this later via AI
        confidence = 0.85f
    )
}