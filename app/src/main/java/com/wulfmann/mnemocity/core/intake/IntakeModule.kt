package com.wulfmann.mnemocity.core.intake

import kotlinx.serialization.Serializable

@Serializable
sealed class IntakeModule(val key: String) {
    object EffortSignals : IntakeModule("effort_signals")
    object CognitiveStyle : IntakeModule("cognitive_style")
    object GoalBaseline : IntakeModule("goal_baseline")
    object MLSeedTraits : IntakeModule("ml_seed_traits")
    object UserIdentity : IntakeModule("user_identity")
}
