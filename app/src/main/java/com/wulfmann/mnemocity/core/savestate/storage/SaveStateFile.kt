package com.wulfmann.mnemocity.core.savestate.storage

import com.wulfmann.mnemocity.core.savestate.model.SaveState

object SaveStateFile {
    fun save(state: SaveState): Boolean {
        println("Saving state with ID: ${state.id}")                                                    // or any relevant field
        // TODO: File I/O logic
        return true
    }

    fun load(id: String): SaveState? {
        println("Loading state for ID: $id")
        // TODO: Load from file
        return null
    }
}