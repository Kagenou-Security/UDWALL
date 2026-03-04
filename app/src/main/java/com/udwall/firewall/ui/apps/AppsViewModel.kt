package com.udwall.firewall.ui.apps

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udwall.firewall.data.AppDao
import com.udwall.firewall.data.AppEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppsViewModel @Inject constructor(
    private val appDao: AppDao,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    val allApps = appDao.getAllApps().combine(searchQuery) { apps, query ->
        apps.filter { it.appName.contains(query, ignoreCase = true) || it.packageName.contains(query, ignoreCase = true) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val whitelistedApps = appDao.getWhitelistedApps().combine(searchQuery) { apps, query ->
        apps.filter { it.appName.contains(query, ignoreCase = true) || it.packageName.contains(query, ignoreCase = true) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val blacklistedApps = appDao.getBlacklistedApps().combine(searchQuery) { apps, query ->
        apps.filter { it.appName.contains(query, ignoreCase = true) || it.packageName.contains(query, ignoreCase = true) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        refreshInstalledApps()
    }

    private fun refreshInstalledApps() {
        viewModelScope.launch(Dispatchers.IO) {
            val pm = context.packageManager
            val installedApps = pm.getInstalledApplications(PackageManager.GET_META_DATA)
                .map { info ->
                    // Get the proper application label (Original Name)
                    val name = pm.getApplicationLabel(info).toString()
                    AppEntity(
                        packageName = info.packageName,
                        appName = if (name.isBlank()) info.packageName else name,
                        isSystemApp = (info.flags and ApplicationInfo.FLAG_SYSTEM) != 0
                    )
                }
                .sortedBy { it.appName.lowercase() }

            appDao.insertApps(installedApps)
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun toggleWhitelist(packageName: String, isWhitelisted: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            appDao.updateWhitelistStatus(packageName, isWhitelisted)
            // Notify VPN service to rebuild rules
            val intent = android.content.Intent("com.udwall.firewall.UPDATE_RULES")
            intent.`package` = context.packageName
            context.sendBroadcast(intent)
        }
    }
}
