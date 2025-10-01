package com.wulfmann.mnemocity.profile

import android.content.Context

class UserProfileManager(private val context: Context) {
    fun hasArchetypeProfile(): Boolean {
        val sharedPrefs = context.getSharedPreferences("user_profile", Context.MODE_PRIVATE)
        return sharedPrefs.contains("archetype_profile")
        // Replace with logic
    }
}