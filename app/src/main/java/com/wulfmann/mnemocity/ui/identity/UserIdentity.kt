package com.wulfmann.mnemocity.ui.identity

data class UserIdentity(
    val id: String,
    val preferredName: String,
    val pronouns: String,
    val currentFocus: String,
    val neuroType: String,
    val email: String? = null
)