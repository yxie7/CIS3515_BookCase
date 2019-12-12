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
        if(Build.VERSION.SDK_INT >= 28)
        {
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

        long result = db.insert(dbBook.BookEntry.TABLE_NAME, null, values);
        if (result == -1)
            return false;
        return true;
    }

    public Cursor getBook(SQLiteDatabase db, int id) {
        String query = dbBook.SQL_SELECT_BOOK(id);
        Cursor cursor = db.rawQuery(query, null);
        cursor = db.query(dbBook.BookEntry.TABLE_NAME, new String[]{"id, position"}, "" + id, null, null, null, null);
        return cursor;
    }
}
