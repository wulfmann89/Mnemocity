package com.wulfmann.mnemocity.diagnostics

import android.util.Log
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.InputStreamReader

object LogcatReader {
    private var job: Job? = null

    fun start() {
        if (job != null) return // Already running

        job = CoroutineScope(Dispatchers.IO).launch {
            try {
                val process = ProcessBuilder("logcat").start()
                val reader = BufferedReader(InputStreamReader(process.inputStream))

                reader.forEachLine { line ->
                    if (line.contains("GC freed")) {
                        val freedKb = Regex("""GC freed (\d+)KB""").find(line)
                            ?.groupValues?.get(1)?.toIntOrNull()

                        if (freedKb != null) {
                            DebugOverlayManager.logMemoryPressure(freedKb)
                        }
                    }

                    if (line.contains("Skipped") && line.contains("frames")) {
                        val skipped = Regex("""Skipped (\d+) frames""").find(line)
                            ?.groupValues?.get(1)?.toIntOrNull()

                        if (skipped != null) {
                            DebugOverlayManager.logFrameSkip(skipped)
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("LogcatReader", "Failed to read logcat $e.message")
            }
        }
    }

    fun stop() {
        job?.cancel()
        job = null
    }
}