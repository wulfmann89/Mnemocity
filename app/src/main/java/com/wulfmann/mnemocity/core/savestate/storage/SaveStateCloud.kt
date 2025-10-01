package com.wulfmann.mnemocity.core.savestate.storage

import com.wulfmann.mnemocity.core.savestate.model.SaveState

object SaveStateCloud {
    fun save(state: SaveState): Boolean {
        println("Saving state with ID: ${state.id}")                                                    // or any field from SaveState
        // TODO: Cloud sync logic
        return true
    }

    fun load(id: String): SaveState? {
        println("Loading state with ID: $id")
        // TODO: Fetch from cloud
        return null
    }
}