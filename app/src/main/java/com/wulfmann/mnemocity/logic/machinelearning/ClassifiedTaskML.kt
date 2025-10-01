package com.wulfmann.mnemocity.logic.machinelearning

import com.wulfmann.mnemocity.data.CacheSource

data class ClassifiedTaskML(
    val taskText: String,
    val classification: String,
    val isRecurring: Boolean,
    val confidence: Float,
    val source: CacheSource = CacheSource.SEED,
    val suggestedSteps: List<String> = emptyList()
)