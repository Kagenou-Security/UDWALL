package com.udwall.firewall.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [AppEntity::class, LogEntity::class], version = 2, exportSchema = false)
abstract class UdwallDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao
    abstract fun logDao(): LogDao
}
