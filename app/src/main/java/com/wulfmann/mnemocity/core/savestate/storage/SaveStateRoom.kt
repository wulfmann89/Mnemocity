package com.wulfmann.mnemocity.core.savestate.storage

import com.wulfmann.mnemocity.core.savestate.model.SaveState

object SaveStateRoom {
    fun save(state: SaveState): Boolean {
        println("Saving state with ID: ${state.id}")                                                    // or any relevant field
        // TODO: Save state room logic
        return true
    }

    fun load(id: String): SaveState? {
        println("Loading state for ID: $id")
        // TODO: Load state room logic
        return null
    }
}