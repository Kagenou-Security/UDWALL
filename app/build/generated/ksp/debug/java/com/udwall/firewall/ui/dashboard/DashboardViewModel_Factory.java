package com.udwall.firewall.ui.dashboard;

import com.udwall.firewall.data.AppDao;
import com.udwall.firewall.data.LogDao;
import com.udwall.firewall.data.SettingsRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class DashboardViewModel_Factory implements Factory<DashboardViewModel> {
  private final Provider<LogDao> logDaoProvider;

  private final Provider<AppDao> appDaoProvider;

  private final Provider<SettingsRepository> settingsRepositoryProvider;

  public DashboardViewModel_Factory(Provider<LogDao> logDaoProvider,
      Provider<AppDao> appDaoProvider, Provider<SettingsRepository> settingsRepositoryProvider) {
    this.logDaoProvider = logDaoProvider;
    this.appDaoProvider = appDaoProvider;
    this.settingsRepositoryProvider = settingsRepositoryProvider;
  }

  @Override
  public DashboardViewModel get() {
    return newInstance(logDaoProvider.get(), appDaoProvider.get(), settingsRepositoryProvider.get());
  }

  public static DashboardViewModel_Factory create(Provider<LogDao> logDaoProvider,
      Provider<AppDao> appDaoProvider, Provider<SettingsRepository> settingsRepositoryProvider) {
    return new DashboardViewModel_Factory(logDaoProvider, appDaoProvider, settingsRepositoryProvider);
  }

  public static DashboardViewModel newInstance(LogDao logDao, AppDao appDao,
      SettingsRepository settingsRepository) {
    return new DashboardViewModel(logDao, appDao, settingsRepository);
  }
}
