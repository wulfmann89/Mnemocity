package com.wulfmann.mnemocity.diagnostics

import android.util.Log

object DebugOverlayManager {
    var isEnabled: Boolean = false

    fun logFrameSkip(skippedFrames: Int) {
        if (isEnabled && skippedFrames > 300) {
            Log.d("DebugOverlay", "Skipped $skippedFrames frames")
        }
    }

    fun logDavey(durationMs: Long) {
        if (isEnabled && durationMs > 1000) {
            Log.d("DebugOverlay", "Davey! Frame took $durationMs ms")
        }
    }

    fun logMemoryPressure(gcFreedKb: Int) {
        if (isEnabled && gcFreedKb > 400) {
            Log.d("DebugOverlay", "GC freed $gcFreedKb KB")
        }
    }
}