package com.wulfmann.mnemocity.logic.machinelearning

import android.util.Log
import com.wulfmann.mnemocity.data.CacheSource
import com.wulfmann.mnemocity.data.FeedbackType
import com.wulfmann.mnemocity.data.MLCacheData.cacheTask
import com.wulfmann.mnemocity.data.TaskArchetypes
import com.wulfmann.mnemocity.logic.scheduler.RecurrenceType
import com.wulfmann.mnemocity.logic.taskflow.ClassifiedTask
import com.wulfmann.mnemocity.logic.taskflow.TaskFlow
import com.wulfmann.mnemocity.logic.taskflow.TaskType

object MLModule {                                                                                       // Singleton object to hold phased ML Logic
    fun seedBaseLineData(
        preferred: List<TaskArchetypes> = emptyList(),
        nonpreferred: List<TaskArchetypes> = emptyList()
    ) {
        TaskArchetypes.entries.forEach { archetype ->
            val confidence = when {
                preferred.contains(archetype) -> 0.95f
                nonpreferred.contains(archetype) -> 0.6f
                else -> 0.8f
            }

            val task = ClassifiedTaskML(
                taskText = archetype.displayName,
                classification = archetype.displayName,
                isRecurring = true,
                confidence = confidence
            )

            cacheTask(task, source = CacheSource.SEED) // Wire it here
        }
        val baselineTasks = listOf(
            ClassifiedTaskML("Drink water", classification = TaskArchetypes.HEALTH.toString(), isRecurring = true, confidence = 1.0f),
            ClassifiedTaskML("Reply to email", classification = TaskArchetypes.COMMUNICATION.toString(), isRecurring = false, confidence = 0.9f),
            ClassifiedTaskML("Stretch", classification = TaskArchetypes.HEALTH.toString(), isRecurring = true, confidence = 0.9f)
        )

        baselineTasks.forEach {
            cacheTask(it)
            Log.d("MLSeed", "Seeded ${it.taskText} -> ${it.classification} (${it.confidence}")
        }
    }

    fun classifyTask(text: String): ClassifiedTask {                                                    // Supervised logic: keyword-based classification
        // Supervised: keyword-based classification
        val lower = text.lowercase()
        val classification = when {                                                                     // Maps keywords to task types
            listOf("today", "now", "soon").any { lower.contains(it) } -> TaskType.SHORT_TERM
            listOf("next week", "monthly").any { lower.contains(it) } -> TaskType.LONG_TERM
            else -> TaskType.UNKNOWN
        }
        val isRecurring = listOf("every", "daily", "weekly").any { lower.contains(it) }         // Returns a modular ClassifiedTask object with metadata

        return ClassifiedTask(
            taskText = text,
            isRecurring = isRecurring,
            classification = classification,
            suggestedSteps = emptyList(),
            confidence = 0.8f
        )
    }
    fun suggestRecurringOptions(text: String): RecurrenceType {                                         // Unsupervsed logic: detects recurrence patterns from natural language.
        // Unsupervised: pattern detection (placeholder)
        return when {
            text.contains("every day", ignoreCase = true) -> RecurrenceType.DAILY
            text.contains("every week", ignoreCase = true) -> RecurrenceType.WEEKLY
            else -> RecurrenceType.NONE
        }
    }

    fun reinforceTaskCompletion(task: TaskFlow, feedback: FeedbackType) {                                    // Reinforcement logic: placeholder for feedback-based learning. Can evolve into a reward-weighted engine
        // Reinforcement: adjust weights based on user feedback
        Log.d("MLModule", "Reinforcing task ${task.taskText} with feedback: $feedback")
        MLCacheLogic.log(task)
        // TODO: Store feedback, adjust model weights
    }
}