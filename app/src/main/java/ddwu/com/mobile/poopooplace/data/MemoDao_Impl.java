package ddwu.com.mobile.poopooplace.data;

import android.database.Cursor;
import android.os.CancellationSignal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@SuppressWarnings({"unchecked", "deprecation"})
public final class MemoDao_Impl implements MemoDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<MemoDto> __insertionAdapterOfMemoDto;

  private final EntityDeletionOrUpdateAdapter<MemoDto> __deletionAdapterOfMemoDto;

  private final EntityDeletionOrUpdateAdapter<MemoDto> __updateAdapterOfMemoDto;

  public MemoDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMemoDto = new EntityInsertionAdapter<MemoDto>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `memo_table` (`id`,`photoName`,`memo`) VALUES (nullif(?, 0),?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, MemoDto value) {
        stmt.bindLong(1, value.getId());
        if (value.getPhotoName() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getPhotoName());
        }
        if (value.getMemo() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getMemo());
        }
      }
    };
    this.__deletionAdapterOfMemoDto = new EntityDeletionOrUpdateAdapter<MemoDto>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `memo_table` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, MemoDto value) {
        stmt.bindLong(1, value.getId());
      }
    };
    this.__updateAdapterOfMemoDto = new EntityDeletionOrUpdateAdapter<MemoDto>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `memo_table` SET `id` = ?,`photoName` = ?,`memo` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, MemoDto value) {
        stmt.bindLong(1, value.getId());
        if (value.getPhotoName() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getPhotoName());
        }
        if (value.getMemo() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getMemo());
        }
        stmt.bindLong(4, value.getId());
      }
    };
  }

  @Override
  public Object insertMemo(final MemoDto memo, final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfMemoDto.insert(memo);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object deleteMemo(final MemoDto memo, final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfMemoDto.handle(memo);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object updateMemo(final MemoDto memo, final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfMemoDto.handle(memo);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Flow<List<MemoDto>> getAllMemos() {
    final String _sql = "SELECT * FROM memo_table";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[]{"memo_table"}, new Callable<List<MemoDto>>() {
      @Override
      public List<MemoDto> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPhotoName = CursorUtil.getColumnIndexOrThrow(_cursor, "photoName");
          final int _cursorIndexOfMemo = CursorUtil.getColumnIndexOrThrow(_cursor, "memo");
          final List<MemoDto> _result = new ArrayList<MemoDto>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final MemoDto _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpPhotoName;
            if (_cursor.isNull(_cursorIndexOfPhotoName)) {
              _tmpPhotoName = null;
            } else {
              _tmpPhotoName = _cursor.getString(_cursorIndexOfPhotoName);
            }
            final String _tmpMemo;
            if (_cursor.isNull(_cursorIndexOfMemo)) {
              _tmpMemo = null;
            } else {
              _tmpMemo = _cursor.getString(_cursorIndexOfMemo);
            }
            _item = new MemoDto(_tmpId,_tmpPhotoName,_tmpMemo);
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
  public Object getMemoById(final long id, final Continuation<? super List<MemoDto>> continuation) {
    final String _sql = "SELECT * FROM memo_table WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<MemoDto>>() {
      @Override
      public List<MemoDto> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPhotoName = CursorUtil.getColumnIndexOrThrow(_cursor, "photoName");
          final int _cursorIndexOfMemo = CursorUtil.getColumnIndexOrThrow(_cursor, "memo");
          final List<MemoDto> _result = new ArrayList<MemoDto>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final MemoDto _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpPhotoName;
            if (_cursor.isNull(_cursorIndexOfPhotoName)) {
              _tmpPhotoName = null;
            } else {
              _tmpPhotoName = _cursor.getString(_cursorIndexOfPhotoName);
            }
            final String _tmpMemo;
            if (_cursor.isNull(_cursorIndexOfMemo)) {
              _tmpMemo = null;
            } else {
              _tmpMemo = _cursor.getString(_cursorIndexOfMemo);
            }
            _item = new MemoDto(_tmpId,_tmpPhotoName,_tmpMemo);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, continuation);
  }

  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}