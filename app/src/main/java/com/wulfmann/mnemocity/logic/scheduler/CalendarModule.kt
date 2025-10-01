package com.wulfmann.mnemocity.logic.scheduler

import org.threeten.bp.LocalDate

object CalendarModule {
    fun parseNaturalLanguageDate(input: String): LocalDate? {
        println("parsing input: $input")// Use ML or regex to extract "next Friday", "tomorrow", etc.
        return null
    }

    @Suppress("UNUSED_PARAMETER")
    fun validateDateRange(start: LocalDate, end: LocalDate): Boolean {
        return start <= end
    }

    fun suggestRecurringOptions(input: String): RecurrenceType {
        if (input.contains("every", ignoreCase = true)) {                                        // Detect "every Monday", "monthly", etc.
            return RecurrenceType.WEEKLY
        }
        return RecurrenceType.NONE
    }
}