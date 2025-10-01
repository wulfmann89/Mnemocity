package com.wulfmann.mnemocity.security

import android.content.Context
import android.util.Log
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.wulfmann.mnemocity.ui.identity.UserIdentity
import kotlinx.serialization.json.Json

/**
 * Concrete implementation using CryptoManager and local file I/O
 */

class SecureImplementRepository(private val appContext: Context) {

    private val crypto =
        CryptoManager                                                                  // using the singleton directly.
    private val json = Json { encodeDefaults = true }

    fun saveUserIdentity(identity: UserIdentity) {                                                  // Securely stores the intake data as encrypted bytes.
        val jsonString = json.encodeToString(identity)
        val encrypted = crypto.encrypt(jsonString)
        appContext.openFileOutput("user_identity.enc", Context.MODE_PRIVATE).use {
            it.write(encrypted.toByteArray())
        }
    }

    fun loadUserIdentity(): UserIdentity? {                                                         // A clean, testable loop: encrypt -> store -> retrieve -> decrypt
        return try {
            val encrypted = appContext.openFileInput("user_identity.enc").readBytes()
            val decrypted = CryptoManager.decrypt(String(encrypted))
            val identity = Json.Default.decodeFromString<UserIdentity>(decrypted)
            Log.d("SecureRepo", "Loaded identity: $identity.preferredName")
            identity
        } catch (e: Exception) {
            Log.e("SecureRepo", "Failed to load identity", e)
            null                                                                                            // Fallback if file missing or corrupted
        }
    }

    fun clearSecure(key: String) {
        securePrefs.edit { remove(key) }
    }

    @Suppress("DEPRECATION")
    val masterKey = MasterKey.Builder(appContext)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    @Suppress("DEPRECATION")
    val securePrefs = EncryptedSharedPreferences.create(
        appContext,
        "secure_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    inline fun <reified T : Any> saveSecure(key: String, data: T) {
        val json = Json.Default.encodeToString(data)
        val encrypted = CryptoManager.encrypt(json)
        securePrefs.edit { putString(key, encrypted) }
    }

@Suppress("unused")
    inline fun <reified T : Any> loadSecure(key: String): T? {
        val encrypted = securePrefs.getString(key, null) ?: return null
        val json = CryptoManager.decrypt(encrypted)
        return Json.Default.decodeFromString(json)
    }
}