package com.wulfmann.mnemocity.logic.session

enum class SessionOrigin {
    BUTTON_PRESS,
    VOICE_TRIGGER,
    REMINDER_NOTIFICATION,
    SYSTEM_RESTART,
    TASK_FLOW,
    UNKNOWN,
    LAUNCH,
    SIGNUP
}