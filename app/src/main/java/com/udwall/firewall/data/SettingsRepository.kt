package com.udwall.firewall.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Singleton
class SettingsRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val START_ON_BOOT = booleanPreferencesKey("start_on_boot")
    private val BLOCK_ALL_MODE = booleanPreferencesKey("block_all_mode")
    private val NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")

    val startOnBoot: Flow<Boolean> = context.dataStore.data.map { it[START_ON_BOOT] ?: true }
    val blockAllMode: Flow<Boolean> = context.dataStore.data.map { it[BLOCK_ALL_MODE] ?: false }
    val notificationsEnabled: Flow<Boolean> = context.dataStore.data.map { it[NOTIFICATIONS_ENABLED] ?: true }

    suspend fun setStartOnBoot(enabled: Boolean) {
        context.dataStore.edit { it[START_ON_BOOT] = enabled }
    }

    suspend fun setBlockAllMode(enabled: Boolean) {
        context.dataStore.edit { it[BLOCK_ALL_MODE] = enabled }
    }

    suspend fun setNotificationsEnabled(enabled: Boolean) {
        context.dataStore.edit { it[NOTIFICATIONS_ENABLED] = enabled }
    }
}
