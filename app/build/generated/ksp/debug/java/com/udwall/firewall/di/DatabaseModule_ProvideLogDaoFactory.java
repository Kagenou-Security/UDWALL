package com.udwall.firewall.di;

import com.udwall.firewall.data.LogDao;
import com.udwall.firewall.data.UdwallDatabase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class DatabaseModule_ProvideLogDaoFactory implements Factory<LogDao> {
  private final Provider<UdwallDatabase> databaseProvider;

  public DatabaseModule_ProvideLogDaoFactory(Provider<UdwallDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public LogDao get() {
    return provideLogDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideLogDaoFactory create(
      Provider<UdwallDatabase> databaseProvider) {
    return new DatabaseModule_ProvideLogDaoFactory(databaseProvider);
  }

  public static LogDao provideLogDao(UdwallDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideLogDao(database));
  }
}
