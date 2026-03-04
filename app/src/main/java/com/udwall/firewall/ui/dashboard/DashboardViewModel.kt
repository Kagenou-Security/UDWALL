package com.udwall.firewall.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udwall.firewall.data.AppDao
import com.udwall.firewall.data.LogDao
import com.udwall.firewall.data.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import java.util.*
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val logDao: LogDao,
    private val appDao: AppDao,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    val blockedTodayCount = logDao.getRecentLogs().map { logs ->
        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
        logs.count { it.timestamp >= today && it.isBlocked }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val whitelistedCount = appDao.getWhitelistedApps().map { it.size }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val isStrictMode = settingsRepository.blockAllMode
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)
}
