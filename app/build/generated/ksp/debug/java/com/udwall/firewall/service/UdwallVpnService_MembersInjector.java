package com.udwall.firewall.service;

import com.udwall.firewall.data.AppDao;
import com.udwall.firewall.data.LogDao;
import com.udwall.firewall.data.SettingsRepository;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast"
})
public final class UdwallVpnService_MembersInjector implements MembersInjector<UdwallVpnService> {
  private final Provider<AppDao> appDaoProvider;

  private final Provider<LogDao> logDaoProvider;

  private final Provider<SettingsRepository> settingsRepositoryProvider;

  public UdwallVpnService_MembersInjector(Provider<AppDao> appDaoProvider,
      Provider<LogDao> logDaoProvider, Provider<SettingsRepository> settingsRepositoryProvider) {
    this.appDaoProvider = appDaoProvider;
    this.logDaoProvider = logDaoProvider;
    this.settingsRepositoryProvider = settingsRepositoryProvider;
  }

  public static MembersInjector<UdwallVpnService> create(Provider<AppDao> appDaoProvider,
      Provider<LogDao> logDaoProvider, Provider<SettingsRepository> settingsRepositoryProvider) {
    return new UdwallVpnService_MembersInjector(appDaoProvider, logDaoProvider, settingsRepositoryProvider);
  }

  @Override
  public void injectMembers(UdwallVpnService instance) {
    injectAppDao(instance, appDaoProvider.get());
    injectLogDao(instance, logDaoProvider.get());
    injectSettingsRepository(instance, settingsRepositoryProvider.get());
  }

  @InjectedFieldSignature("com.udwall.firewall.service.UdwallVpnService.appDao")
  public static void injectAppDao(UdwallVpnService instance, AppDao appDao) {
    instance.appDao = appDao;
  }

  @InjectedFieldSignature("com.udwall.firewall.service.UdwallVpnService.logDao")
  public static void injectLogDao(UdwallVpnService instance, LogDao logDao) {
    instance.logDao = logDao;
  }

  @InjectedFieldSignature("com.udwall.firewall.service.UdwallVpnService.settingsRepository")
  public static void injectSettingsRepository(UdwallVpnService instance,
      SettingsRepository settingsRepository) {
    instance.settingsRepository = settingsRepository;
  }
}
