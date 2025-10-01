package com.wulfmann.mnemocity.core.savestate.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity(tableName = "save_state")
data class SaveStateEntity(
    @PrimaryKey val id: String,
    val timestamp: Long,
    val payloadJson: String,
    val version: Int = 1
)

fun SaveState.toEntity(): SaveStateEntity = SaveStateEntity(
    id = id,
    timestamp = timestamp,
    payloadJson = Gson().toJson(payload),
    version = version
)

fun SaveStateEntity.toModel(): SaveState = SaveState(
    id = id,
    timestamp = timestamp,
    payload = Gson().fromJson(payloadJson, object : TypeToken<Map<String, Any>>() {}.type),
    version = version
)