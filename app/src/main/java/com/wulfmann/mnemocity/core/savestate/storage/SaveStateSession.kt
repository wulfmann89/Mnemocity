package com.wulfmann.mnemocity.core.savestate.storage

import com.wulfmann.mnemocity.core.savestate.model.SaveState

object SaveStateSession {
    private val sessionCache = mutableMapOf<String, SaveState>()

    fun save(state: SaveState): Boolean {
        sessionCache[state.id] = state
        return true
    }

    fun load(id: String): SaveState? {
        return sessionCache[id]
    }
}