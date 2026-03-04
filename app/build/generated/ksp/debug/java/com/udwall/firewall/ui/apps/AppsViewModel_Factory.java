package com.udwall.firewall.ui.apps;

import android.content.Context;
import com.udwall.firewall.data.AppDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class AppsViewModel_Factory implements Factory<AppsViewModel> {
  private final Provider<AppDao> appDaoProvider;

  private final Provider<Context> contextProvider;

  public AppsViewModel_Factory(Provider<AppDao> appDaoProvider, Provider<Context> contextProvider) {
    this.appDaoProvider = appDaoProvider;
    this.contextProvider = contextProvider;
  }

  @Override
  public AppsViewModel get() {
    return newInstance(appDaoProvider.get(), contextProvider.get());
  }

  public static AppsViewModel_Factory create(Provider<AppDao> appDaoProvider,
      Provider<Context> contextProvider) {
    return new AppsViewModel_Factory(appDaoProvider, contextProvider);
  }

  public static AppsViewModel newInstance(AppDao appDao, Context context) {
    return new AppsViewModel(appDao, context);
  }
}
