package com.udwall.firewall.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "logs")
data class LogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long,
    val packageName: String,
    val appName: String,
    val destinationIp: String,
    val destinationPort: Int,
    val protocol: String,
    val isBlocked: Boolean
)
