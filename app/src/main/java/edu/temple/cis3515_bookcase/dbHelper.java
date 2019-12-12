package edu.temple.cis3515_bookcase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

public class dbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "booksOnPause.db";

    public dbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase database) {
        super.onOpen(database);
        if (Build.VERSION.SDK_INT >= 28) {
            database.disableWriteAheadLogging();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(dbBook.SQL_CREATE_BOOKS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(dbBook.SQL_DELETE_BOOKS);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public boolean insertBook(SQLiteDatabase db, int id, int position) {

        ContentValues values = new ContentValues();
        values.put(dbBook.BookEntry.COLUMN_NAME_ID, id);
        values.put(dbBook.BookEntry.COLUMN_NAME_POSITION, position);
        long _id = db.insertWithOnConflict(dbBook.BookEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        long result;
        if (_id == -1) {
            result = db.update(dbBook.BookEntry.TABLE_NAME, values, "id=" + id, null);
            if (result != -1)
                return true;
        }
        return false;
    }

    public int getBookPosition(SQLiteDatabase db, int id) {
        int pos = -1;
        String query = dbBook.SQL_SELECT_BOOK_POSITION(id);
        Cursor cursor = db.rawQuery(query, null);
        cursor = db.query(dbBook.BookEntry.TABLE_NAME, new String[]{"position"}, "id=" + id, null, null, null, null);
        if (cursor.moveToFirst()) {
            pos = cursor.getInt(0);
        }
        return pos;
    }
}
