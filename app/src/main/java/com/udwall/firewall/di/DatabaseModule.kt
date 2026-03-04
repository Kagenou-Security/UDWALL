package com.udwall.firewall.di

import android.content.Context
import androidx.room.Room
import com.udwall.firewall.data.AppDao
import com.udwall.firewall.data.LogDao
import com.udwall.firewall.data.UdwallDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): UdwallDatabase {
        SQLiteDatabase.loadLibs(context)
        val passphrase = SQLiteDatabase.getBytes("udwall-hardened-vault-2024".toCharArray())
        val factory = SupportFactory(passphrase)

        return Room.databaseBuilder(context, UdwallDatabase::class.java, "udwall.db")
            .openHelperFactory(factory)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideAppDao(database: UdwallDatabase): AppDao {
        return database.appDao()
    }

    @Provides
    fun provideLogDao(database: UdwallDatabase): LogDao {
        return database.logDao()
    }
}
