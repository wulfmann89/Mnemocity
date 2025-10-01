package com.wulfmann.mnemocity.logic.taskflow

data class ClassifiedTask(
    val taskText: String,
    val isRecurring: Boolean,
    val classification: TaskType,
    val suggestedSteps: List<String> = emptyList(),
    val confidence: Float
)