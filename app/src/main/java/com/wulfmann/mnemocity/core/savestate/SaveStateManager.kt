package com.wulfmann.mnemocity.core.savestate

import com.wulfmann.mnemocity.core.savestate.model.SaveLocation
import com.wulfmann.mnemocity.core.savestate.model.SaveResult
import com.wulfmann.mnemocity.core.savestate.model.SaveState
import com.wulfmann.mnemocity.core.savestate.storage.SaveStateCloud
import com.wulfmann.mnemocity.core.savestate.storage.SaveStateFile
import com.wulfmann.mnemocity.core.savestate.storage.SaveStateRoom
import com.wulfmann.mnemocity.core.savestate.storage.SaveStateSession

fun saveState(state: SaveState, location: SaveLocation): Boolean {
    return when (location) {
        SaveLocation.LOCAL_DB -> SaveStateRoom.save(state)
        SaveLocation.LOCAL_FILE -> SaveStateFile.save(state)
        SaveLocation.CLOUD_SYNC -> SaveStateCloud.save(state)
        SaveLocation.SESSION_ONLY -> SaveStateSession.save(state)
    }
}

fun loadState(id: String, location: SaveLocation): SaveState? {
    return when (location) {
        SaveLocation.LOCAL_DB -> SaveStateRoom.load(id)
        SaveLocation.LOCAL_FILE -> SaveStateFile.load(id)
        SaveLocation.CLOUD_SYNC -> SaveStateCloud.load(id)
        SaveLocation.SESSION_ONLY -> SaveStateSession.load(id)
    }
}

fun generateSaveFeedback(state: SaveState, success: Boolean): SaveResult {
    return if (success) {
        SaveResult.Success("Saved with clarity at ${state.timestamp}")
    } else {
        SaveResult.Failure("Save failed")
    }
}

fun saveStateWithFeedback(state: SaveState, location: SaveLocation): SaveResult {
    val result = saveState(state, location)
    return generateSaveFeedback(state, result)
}