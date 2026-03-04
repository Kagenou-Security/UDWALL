package com.udwall.firewall.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udwall.firewall.data.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: SettingsRepository
) : ViewModel() {

    val startOnBoot = repository.startOnBoot.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)
    val blockAllMode = repository.blockAllMode.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)
    val notificationsEnabled = repository.notificationsEnabled.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    fun setStartOnBoot(enabled: Boolean) = viewModelScope.launch { repository.setStartOnBoot(enabled) }
    fun setBlockAllMode(enabled: Boolean) = viewModelScope.launch { repository.setBlockAllMode(enabled) }
    fun setNotificationsEnabled(enabled: Boolean) = viewModelScope.launch { repository.setNotificationsEnabled(enabled) }
}
