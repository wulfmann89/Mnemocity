package com.wulfmann.mnemocity.core.intake

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.wulfmann.mnemocity.security.SecureImplementRepository
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Test

class SecureImplementRepositoryTest {

    private val context = ApplicationProvider.getApplicationContext<Context>()
    private val repo = SecureImplementRepository(context)

    @Test
    fun testSaveAndLoadUserIdentity() = runBlocking {
        // Sample intake data
        val sample = IntakeModule.UserIdentity(
            preferredName = "Junior",
            pronouns = "He/him",
            currentFocus = "launching a project",
            neuroType = "ADHD"
        )

        // Save encrypted
        repo.saveUserIdentity(sample)

        // Load and decrypt
        val result = repo.loadUserIdentity()

        // Assert emotional and structural integrity
        assertEquals(sample, result)
    }
}