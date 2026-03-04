package com.udwall.firewall.data;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class UdwallDatabase_Impl extends UdwallDatabase {
  private volatile AppDao _appDao;

  private volatile LogDao _logDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(2) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `apps` (`packageName` TEXT NOT NULL, `appName` TEXT NOT NULL, `isWhitelisted` INTEGER NOT NULL, `isSystemApp` INTEGER NOT NULL, PRIMARY KEY(`packageName`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `logs` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `timestamp` INTEGER NOT NULL, `packageName` TEXT NOT NULL, `appName` TEXT NOT NULL, `destinationIp` TEXT NOT NULL, `destinationPort` INTEGER NOT NULL, `protocol` TEXT NOT NULL, `isBlocked` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'a77ce28c012982ea5421ac431ce915af')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `apps`");
        db.execSQL("DROP TABLE IF EXISTS `logs`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsApps = new HashMap<String, TableInfo.Column>(4);
        _columnsApps.put("packageName", new TableInfo.Column("packageName", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsApps.put("appName", new TableInfo.Column("appName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsApps.put("isWhitelisted", new TableInfo.Column("isWhitelisted", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsApps.put("isSystemApp", new TableInfo.Column("isSystemApp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysApps = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesApps = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoApps = new TableInfo("apps", _columnsApps, _foreignKeysApps, _indicesApps);
        final TableInfo _existingApps = TableInfo.read(db, "apps");
        if (!_infoApps.equals(_existingApps)) {
          return new RoomOpenHelper.ValidationResult(false, "apps(com.udwall.firewall.data.AppEntity).\n"
                  + " Expected:\n" + _infoApps + "\n"
                  + " Found:\n" + _existingApps);
        }
        final HashMap<String, TableInfo.Column> _columnsLogs = new HashMap<String, TableInfo.Column>(8);
        _columnsLogs.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLogs.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLogs.put("packageName", new TableInfo.Column("packageName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLogs.put("appName", new TableInfo.Column("appName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLogs.put("destinationIp", new TableInfo.Column("destinationIp", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLogs.put("destinationPort", new TableInfo.Column("destinationPort", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLogs.put("protocol", new TableInfo.Column("protocol", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLogs.put("isBlocked", new TableInfo.Column("isBlocked", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysLogs = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesLogs = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoLogs = new TableInfo("logs", _columnsLogs, _foreignKeysLogs, _indicesLogs);
        final TableInfo _existingLogs = TableInfo.read(db, "logs");
        if (!_infoLogs.equals(_existingLogs)) {
          return new RoomOpenHelper.ValidationResult(false, "logs(com.udwall.firewall.data.LogEntity).\n"
                  + " Expected:\n" + _infoLogs + "\n"
                  + " Found:\n" + _existingLogs);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "a77ce28c012982ea5421ac431ce915af", "3a03dfde87e2f9a235b81ce3edfba898");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "apps","logs");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `apps`");
      _db.execSQL("DELETE FROM `logs`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(AppDao.class, AppDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(LogDao.class, LogDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public AppDao appDao() {
    if (_appDao != null) {
      return _appDao;
    } else {
      synchronized(this) {
        if(_appDao == null) {
          _appDao = new AppDao_Impl(this);
        }
        return _appDao;
      }
    }
  }

  @Override
  public LogDao logDao() {
    if (_logDao != null) {
      return _logDao;
    } else {
      synchronized(this) {
        if(_logDao == null) {
          _logDao = new LogDao_Impl(this);
        }
        return _logDao;
      }
    }
  }
}
