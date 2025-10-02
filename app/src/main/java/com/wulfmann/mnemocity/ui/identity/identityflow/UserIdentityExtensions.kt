package com.wulfmann.mnemocity.ui.identity.identityflow

import com.wulfmann.mnemocity.core.savestate.model.SaveState
import com.wulfmann.mnemocity.ui.identity.UserIdentity

fun UserIdentity.toState(): SaveState {
    return SaveState(
        id = this.id,
        timestamp = System.currentTimeMillis(),
        payload = mapOf(
            "preferredName" to this.preferredName,
            "pronouns" to this.pronouns,
            "currentFocus" to this.currentFocus,
            "neuroType" to this.neuroType
        )
    )
}