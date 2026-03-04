package com.udwall.firewall.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    @Query("SELECT * FROM apps ORDER BY appName ASC")
    fun getAllApps(): Flow<List<AppEntity>>

    @Query("SELECT * FROM apps WHERE isWhitelisted = 1 ORDER BY appName ASC")
    fun getWhitelistedApps(): Flow<List<AppEntity>>

    @Query("SELECT * FROM apps WHERE isWhitelisted = 0 ORDER BY appName ASC")
    fun getBlacklistedApps(): Flow<List<AppEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertApps(apps: List<AppEntity>)

    @Update
    suspend fun updateApp(app: AppEntity)

    @Query("UPDATE apps SET isWhitelisted = :isWhitelisted WHERE packageName = :packageName")
    suspend fun updateWhitelistStatus(packageName: String, isWhitelisted: Boolean)
}
