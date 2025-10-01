package com.wulfmann.mnemocity.logic.machinelearning

enum class ConfidenceBand { LOW, MEDIUM, HIGH }

fun Float.toBand(): ConfidenceBand = when {
    this < 0.5f -> ConfidenceBand.LOW
    this < 0.85f -> ConfidenceBand.MEDIUM
    else -> ConfidenceBand.HIGH
}