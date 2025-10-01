package com.wulfmann.mnemocity.data

import android.util.Log
import com.wulfmann.mnemocity.core.intake.IntakeModule

class AnalyticsService {
    private var sessionStartTime: Long? = null
    @Suppress("unused")
    fun logModuleCompletion(module: IntakeModule) {
        // Store locally or send to remote
        Log.d("Analytics", "Module completed: $module.key")
    }
    @Suppress("unused")
    fun logResetTrigger(trigger: String) {
        // Track user friction points
        Log.d("Analytics", "Reset triggered gby $trigger")
    }
    @Suppress("unused")
    fun logSessionStart() {
        // Track session start
        sessionStartTime = System.currentTimeMillis()
        Log.d("Analytics", "Session started at $sessionStartTime")
        // Optional: store to local DB or send to remote analytics
    }
    @Suppress("unused")
    fun logSessionEnd() {
        // Track session end
        val endTime = System.currentTimeMillis()
        val duration = sessionStartTime?.let { endTime - it } ?: -1
        Log.d("Analytics", "Session ended at $endTime (duration: $duration ms)")
        // Optional: calculate duration, store summary, or trigger sync
    }
}