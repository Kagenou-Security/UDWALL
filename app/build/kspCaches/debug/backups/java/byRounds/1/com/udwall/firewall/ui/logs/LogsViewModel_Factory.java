package com.udwall.firewall.ui.logs;

import com.udwall.firewall.data.LogDao;
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
public final class LogsViewModel_Factory implements Factory<LogsViewModel> {
  private final Provider<LogDao> logDaoProvider;

  public LogsViewModel_Factory(Provider<LogDao> logDaoProvider) {
    this.logDaoProvider = logDaoProvider;
  }

  @Override
  public LogsViewModel get() {
    return newInstance(logDaoProvider.get());
  }

  public static LogsViewModel_Factory create(Provider<LogDao> logDaoProvider) {
    return new LogsViewModel_Factory(logDaoProvider);
  }

  public static LogsViewModel newInstance(LogDao logDao) {
    return new LogsViewModel(logDao);
  }
}
