package com.wulfmann.mnemocity.data

enum class CacheSource {
    SEED,           // From initial onboarding or baseline data
    VOICE,          // Inferred from speech input
    COMPLETION,     // Reinforced after task completion
    FEEDBACK,       // Adjusted based on user feedback
    RETURNED        // Updated after user revisits intake or preferences
}