package com.wulfmann.mnemocity

import androidx.compose.runtime.mutableStateListOf
import com.wulfmann.mnemocity.data.FeedbackType
import com.wulfmann.mnemocity.logic.machinelearning.MLModule
import com.wulfmann.mnemocity.logic.taskflow.TaskFlow
import org.threeten.bp.LocalDate

object TaskRepository {
    val activeTasks = mutableStateListOf<TaskFlow>()

    val taskMap: MutableMap<LocalDate, MutableList<TaskFlow>> = mutableMapOf()

    fun addTask(task: TaskFlow) {
        activeTasks.add(task)
        val date = task.startDate?.let { LocalDate.from(it) } ?: LocalDate.now()
        taskMap.getOrPut(date) { mutableListOf() }.add(task)
    }

    fun markTaskCompleted(task: TaskFlow, feedback: FeedbackType) {
        task.isCompleted = true
        MLModule.reinforceTaskCompletion(task, feedback)
    }

    fun clearTasks() {
        activeTasks.clear()
        taskMap.clear()
    }

    private fun matchTaskFromSpeech(spokenText: String, tasks: List<TaskFlow>): TaskFlow? {
        return tasks.find { spokenText.contains(it.taskText, ignoreCase = true) }
    }
}