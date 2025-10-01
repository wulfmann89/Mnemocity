package com.wulfmann.mnemocity.logic.input

import android.content.Context
import android.content.Intent
import android.speech.RecognizerIntent
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import com.wulfmann.mnemocity.TaskRepository
import com.wulfmann.mnemocity.data.FeedbackType
import com.wulfmann.mnemocity.logic.taskflow.handleTaskFlow
import com.wulfmann.mnemocity.logic.taskflow.routeTaskFlow
import com.wulfmann.mnemocity.logic.taskflow.showCompletionFeedback

object SpeechInputManager {

    fun listenForTaskRequest(
        context: Context,
        launcher: ActivityResultLauncher<Intent>,
        onResult: (String?) -> Unit
    ) {
        println("Listening for task reminder request...")
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_PROMPT, "What task would you like to remember?")
        }
        launcher.launch(intent)
        // Result will be handled in the ActivityResult callback
        // Hook into Android speech recognition or Compose voice layer
        // Once result is captured pass to TaskClassifier
    }

    fun handleSpokenText(spokenText: String, context: Context) {
        Log.d("SpeechInput", "User said: $spokenText")

        val match = TaskRepository.activeTasks.find {
            spokenText.contains(it.taskText, ignoreCase = true)
        }

        match?.let {
            TaskRepository.markTaskCompleted(it, FeedbackType.POSITIVE)
            showCompletionFeedback(it.taskText, context)
        }

        val taskFlow = routeTaskFlow(spokenText)
        handleTaskFlow(taskFlow, context)

        Log.d("TaskClassifier", "Classified as: ${taskFlow.classification}")
    }
}