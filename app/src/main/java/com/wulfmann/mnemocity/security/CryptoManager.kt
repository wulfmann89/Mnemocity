package com.wulfmann.mnemocity.security

/**
 * Handles AES-GCM encryption/decryption and key retrieval from Android Keystore
 */

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import android.util.Base64

object CryptoManager {                                                                                  // Singleton object that encapsulates encryption logic in one reusable, testable module.  No need to instantiate - just call CryptoManager.encrypt() or .decrypt()
    private const val ANDROID_KEYSTORE = "AndroidKeyStore"                                              // Secure system-level storage
    private const val KEY_ALIAS = "MnemocitySecureKey"                                                  // Unique name for app's key
    private const val TRANSFORMATION = "AES/GCM/NoPadding"                                              // defines cipher mode (AES-GCM, no padding)
    private const val IV_SIZE = 12                                                                      // GCM standard.  Initialization vector size for GCM (12 bytes standard)

    private fun getOrCreateSecretKey(): SecretKey {
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE).apply { load(null) }       // Loads the secure Android Keystore.  load(null) initializes it for use.
        keyStore.getKey(KEY_ALIAS, null)?.let {
            return it as SecretKey
        }

        val keyGen = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE)    // Uses AES algorithm to generate key inside Android Keystore.
        val spec = KeyGenParameterSpec.Builder(                                                         // Key specification that defines how the key behaves.
            KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT                   // Can ecrypt and decrypt
        ).setBlockModes(KeyProperties.BLOCK_MODE_GCM)                                                   // Uses GCM block mode
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)                               // No padding (GCM doesn't need it)
            .setKeySize(256)                                                                            // 256-bit key for strong encryption
            .build()
        keyGen.init(spec)                                                                      // Initializes the generator with spec and creates the key securely.
        return keyGen.generateKey()
    }

    fun encrypt(plainText: String): String {                                                             // Encrypt function
        val cipher = Cipher.getInstance(TRANSFORMATION)                                   // initializes cipher in encryption mode
        cipher.init(Cipher.ENCRYPT_MODE, getOrCreateSecretKey())                         // uses secure key
        val iv = cipher.iv                                                                               // generates a random IV
        val encryptedBytes = cipher.doFinal(plainText.toByteArray())                              // Encrypts the plaintext string
        val combined = iv + encryptedBytes                                                               // Combines the IV and ciphertext
        return Base64.encodeToString(combined, Base64.DEFAULT)                            // Encodes the result in Base64 for safe storage/transmission
    }

    fun decrypt(encryptedData: String): String {                                                        // Decrypt function
        val combined = Base64.decode(encryptedData, Base64.DEFAULT)                        // Decodes the Base64 string
        val iv = combined.sliceArray(0 until IV_SIZE)                                    // Splits out IV and ciphertext for decryption
        val encryptedBytes = combined.sliceArray(IV_SIZE until combined.size)
        val cipher = Cipher.getInstance(TRANSFORMATION)                                  // Initializes cipher with IV and key
        val spec = GCMParameterSpec(128, iv)
        cipher.init(Cipher.DECRYPT_MODE, getOrCreateSecretKey(), spec)         // Decrypts the bytes

        return String(cipher.doFinal(encryptedBytes))                                    // Returns the original plaintext string
    }
}