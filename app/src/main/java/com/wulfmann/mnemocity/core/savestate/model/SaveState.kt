package com.wulfmann.mnemocity.core.savestate.model

data class SaveState(
    val id: String,
    val timestamp: Long,
    val payload: Map<String, Any>,          // or a sealed class per module
    val version: Int = 1
)