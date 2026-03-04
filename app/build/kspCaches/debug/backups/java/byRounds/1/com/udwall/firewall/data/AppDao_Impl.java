package com.udwall.firewall.data;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDao_Impl implements AppDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<AppEntity> __insertionAdapterOfAppEntity;

  private final EntityDeletionOrUpdateAdapter<AppEntity> __updateAdapterOfAppEntity;

  private final SharedSQLiteStatement __preparedStmtOfUpdateWhitelistStatus;

  public AppDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfAppEntity = new EntityInsertionAdapter<AppEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR IGNORE INTO `apps` (`packageName`,`appName`,`isWhitelisted`,`isSystemApp`) VALUES (?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final AppEntity entity) {
        statement.bindString(1, entity.getPackageName());
        statement.bindString(2, entity.getAppName());
        final int _tmp = entity.isWhitelisted() ? 1 : 0;
        statement.bindLong(3, _tmp);
        final int _tmp_1 = entity.isSystemApp() ? 1 : 0;
        statement.bindLong(4, _tmp_1);
      }
    };
    this.__updateAdapterOfAppEntity = new EntityDeletionOrUpdateAdapter<AppEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `apps` SET `packageName` = ?,`appName` = ?,`isWhitelisted` = ?,`isSystemApp` = ? WHERE `packageName` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final AppEntity entity) {
        statement.bindString(1, entity.getPackageName());
        statement.bindString(2, entity.getAppName());
        final int _tmp = entity.isWhitelisted() ? 1 : 0;
        statement.bindLong(3, _tmp);
        final int _tmp_1 = entity.isSystemApp() ? 1 : 0;
        statement.bindLong(4, _tmp_1);
        statement.bindString(5, entity.getPackageName());
      }
    };
    this.__preparedStmtOfUpdateWhitelistStatus = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE apps SET isWhitelisted = ? WHERE packageName = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertApps(final List<AppEntity> apps,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfAppEntity.insert(apps);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateApp(final AppEntity app, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfAppEntity.handle(app);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateWhitelistStatus(final String packageName, final boolean isWhitelisted,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateWhitelistStatus.acquire();
        int _argIndex = 1;
        final int _tmp = isWhitelisted ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 2;
        _stmt.bindString(_argIndex, packageName);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfUpdateWhitelistStatus.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<AppEntity>> getAllApps() {
    final String _sql = "SELECT * FROM apps ORDER BY appName ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"apps"}, new Callable<List<AppEntity>>() {
      @Override
      @NonNull
      public List<AppEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfPackageName = CursorUtil.getColumnIndexOrThrow(_cursor, "packageName");
          final int _cursorIndexOfAppName = CursorUtil.getColumnIndexOrThrow(_cursor, "appName");
          final int _cursorIndexOfIsWhitelisted = CursorUtil.getColumnIndexOrThrow(_cursor, "isWhitelisted");
          final int _cursorIndexOfIsSystemApp = CursorUtil.getColumnIndexOrThrow(_cursor, "isSystemApp");
          final List<AppEntity> _result = new ArrayList<AppEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AppEntity _item;
            final String _tmpPackageName;
            _tmpPackageName = _cursor.getString(_cursorIndexOfPackageName);
            final String _tmpAppName;
            _tmpAppName = _cursor.getString(_cursorIndexOfAppName);
            final boolean _tmpIsWhitelisted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsWhitelisted);
            _tmpIsWhitelisted = _tmp != 0;
            final boolean _tmpIsSystemApp;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsSystemApp);
            _tmpIsSystemApp = _tmp_1 != 0;
            _item = new AppEntity(_tmpPackageName,_tmpAppName,_tmpIsWhitelisted,_tmpIsSystemApp);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<AppEntity>> getWhitelistedApps() {
    final String _sql = "SELECT * FROM apps WHERE isWhitelisted = 1 ORDER BY appName ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"apps"}, new Callable<List<AppEntity>>() {
      @Override
      @NonNull
      public List<AppEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfPackageName = CursorUtil.getColumnIndexOrThrow(_cursor, "packageName");
          final int _cursorIndexOfAppName = CursorUtil.getColumnIndexOrThrow(_cursor, "appName");
          final int _cursorIndexOfIsWhitelisted = CursorUtil.getColumnIndexOrThrow(_cursor, "isWhitelisted");
          final int _cursorIndexOfIsSystemApp = CursorUtil.getColumnIndexOrThrow(_cursor, "isSystemApp");
          final List<AppEntity> _result = new ArrayList<AppEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AppEntity _item;
            final String _tmpPackageName;
            _tmpPackageName = _cursor.getString(_cursorIndexOfPackageName);
            final String _tmpAppName;
            _tmpAppName = _cursor.getString(_cursorIndexOfAppName);
            final boolean _tmpIsWhitelisted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsWhitelisted);
            _tmpIsWhitelisted = _tmp != 0;
            final boolean _tmpIsSystemApp;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsSystemApp);
            _tmpIsSystemApp = _tmp_1 != 0;
            _item = new AppEntity(_tmpPackageName,_tmpAppName,_tmpIsWhitelisted,_tmpIsSystemApp);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<AppEntity>> getBlacklistedApps() {
    final String _sql = "SELECT * FROM apps WHERE isWhitelisted = 0 ORDER BY appName ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"apps"}, new Callable<List<AppEntity>>() {
      @Override
      @NonNull
      public List<AppEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfPackageName = CursorUtil.getColumnIndexOrThrow(_cursor, "packageName");
          final int _cursorIndexOfAppName = CursorUtil.getColumnIndexOrThrow(_cursor, "appName");
          final int _cursorIndexOfIsWhitelisted = CursorUtil.getColumnIndexOrThrow(_cursor, "isWhitelisted");
          final int _cursorIndexOfIsSystemApp = CursorUtil.getColumnIndexOrThrow(_cursor, "isSystemApp");
          final List<AppEntity> _result = new ArrayList<AppEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AppEntity _item;
            final String _tmpPackageName;
            _tmpPackageName = _cursor.getString(_cursorIndexOfPackageName);
            final String _tmpAppName;
            _tmpAppName = _cursor.getString(_cursorIndexOfAppName);
            final boolean _tmpIsWhitelisted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsWhitelisted);
            _tmpIsWhitelisted = _tmp != 0;
            final boolean _tmpIsSystemApp;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsSystemApp);
            _tmpIsSystemApp = _tmp_1 != 0;
            _item = new AppEntity(_tmpPackageName,_tmpAppName,_tmpIsWhitelisted,_tmpIsSystemApp);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
