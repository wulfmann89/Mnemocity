package com.wulfmann.mnemocity.logic.machinelearning

import com.wulfmann.mnemocity.logic.taskflow.TaskFlow

object MLCacheLogic {
    val completionLog = mutableListOf<String>()

    fun log(task: TaskFlow) {
        completionLog.add(task.taskText)
    }
}