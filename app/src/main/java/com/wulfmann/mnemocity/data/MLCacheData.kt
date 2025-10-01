package com.wulfmann.mnemocity.data

import com.wulfmann.mnemocity.logic.machinelearning.ClassifiedTaskML

object MLCacheData {
    private val cachedTasks = mutableListOf<ClassifiedTaskML>()

    fun cacheTask(task: ClassifiedTaskML, source: CacheSource = CacheSource.SEED) {
        cachedTasks.add(task)
        android.util.Log.d("MLCache", "Cached: ${task.taskText} -> ${task.classification} (${task.confidence}")
    }

    fun getCachedTasks(): List<ClassifiedTaskML> = cachedTasks.toList()
}