package edu.temple.cis3515_bookcase;

import android.database.Cursor;
import android.provider.BaseColumns;

public final class dbBook {
    private static final String COMMA_SEP = ",";
    public static final String SQL_CREATE_BOOKS =
            "CREATE TABLE " + BookEntry.TABLE_NAME + " (" +
                    BookEntry.COLUMN_NAME_ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
                    BookEntry.COLUMN_NAME_POSITION + " INTEGER" +
                    " )";

    public static final String SQL_DELETE_BOOKS =
            "DROP TABLE IF EXISTS " + BookEntry.TABLE_NAME;

    public static String SQL_SELECT_BOOK(int bookID) {
        String query =
                "SELECT * FROM " + BookEntry.TABLE_NAME +
                        " WHERE " + BookEntry.COLUMN_NAME_ID + "=" + bookID;
        return query;
    }

    public static abstract class BookEntry implements BaseColumns {
        public static final String TABLE_NAME = "books";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_POSITION = "position";
    }
}
