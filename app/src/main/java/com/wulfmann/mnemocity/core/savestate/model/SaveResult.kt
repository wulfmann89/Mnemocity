package com.wulfmann.mnemocity.core.savestate.model

sealed class SaveResult {
    data class Success(val message: String) : SaveResult()
    data class Failure(val error: String) : SaveResult()
}