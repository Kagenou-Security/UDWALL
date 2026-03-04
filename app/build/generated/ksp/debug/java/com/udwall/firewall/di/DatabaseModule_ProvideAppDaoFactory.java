package com.udwall.firewall.di;

import com.udwall.firewall.data.AppDao;
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
public final class DatabaseModule_ProvideAppDaoFactory implements Factory<AppDao> {
  private final Provider<UdwallDatabase> databaseProvider;

  public DatabaseModule_ProvideAppDaoFactory(Provider<UdwallDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public AppDao get() {
    return provideAppDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideAppDaoFactory create(
      Provider<UdwallDatabase> databaseProvider) {
    return new DatabaseModule_ProvideAppDaoFactory(databaseProvider);
  }

  public static AppDao provideAppDao(UdwallDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideAppDao(database));
  }
}
