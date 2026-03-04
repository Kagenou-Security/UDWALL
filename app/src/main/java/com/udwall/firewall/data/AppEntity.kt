package com.udwall.firewall.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "apps")
data class AppEntity(
    @PrimaryKey val packageName: String,
    val appName: String,
    val isWhitelisted: Boolean = false,
    val isSystemApp: Boolean = false
)
