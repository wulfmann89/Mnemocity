package com.wulfmann.mnemocity.logic.session

enum class IdentityPhase {
    UNIDENTIFIED,
    SELF_DECLARED,
    VERIFIED,
    NEW_USER,
    LOGGED_IN,
    ANONYMOUS,
    GUEST
}