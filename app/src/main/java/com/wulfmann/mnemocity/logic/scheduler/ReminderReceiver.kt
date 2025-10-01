package com.wulfmann.mnemocity.logic.scheduler

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val taskText = intent.getStringExtra("taskText") ?: "TaskReminder"
        Toast.makeText(context, "Reminder: $taskText", Toast.LENGTH_LONG).show()
    }
}