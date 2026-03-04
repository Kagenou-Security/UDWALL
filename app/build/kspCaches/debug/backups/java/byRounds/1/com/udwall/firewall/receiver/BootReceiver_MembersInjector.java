package com.udwall.firewall.receiver;

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
public final class BootReceiver_MembersInjector implements MembersInjector<BootReceiver> {
  private final Provider<SettingsRepository> settingsRepositoryProvider;

  public BootReceiver_MembersInjector(Provider<SettingsRepository> settingsRepositoryProvider) {
    this.settingsRepositoryProvider = settingsRepositoryProvider;
  }

  public static MembersInjector<BootReceiver> create(
      Provider<SettingsRepository> settingsRepositoryProvider) {
    return new BootReceiver_MembersInjector(settingsRepositoryProvider);
  }

  @Override
  public void injectMembers(BootReceiver instance) {
    injectSettingsRepository(instance, settingsRepositoryProvider.get());
  }

  @InjectedFieldSignature("com.udwall.firewall.receiver.BootReceiver.settingsRepository")
  public static void injectSettingsRepository(BootReceiver instance,
      SettingsRepository settingsRepository) {
    instance.settingsRepository = settingsRepository;
  }
}
