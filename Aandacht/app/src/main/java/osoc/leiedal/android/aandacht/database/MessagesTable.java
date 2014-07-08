package osoc.leiedal.android.aandacht.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Maarten on 7/07/2014.
 */
public class MessagesTable {

    // Database and column names
    public static final String TABLE_NAME = "messages";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_REPORT_ID = "report_id";
    public static final String COLUMN_SOURCE = "source";
    public static final String COLUMN_CONTENT = "content";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    // Database creation SQL statement
    private static final String DATABASE_CREATE =
            "create table " + TABLE_NAME
            + "("
            + COLUMN_ID + " integer primary key autoincrement,"
            + COLUMN_REPORT_ID + " integer not null,"
            + COLUMN_SOURCE + " text,"
            + COLUMN_CONTENT + "text,"
            + COLUMN_TIMESTAMP + "integer not null"
            + ")";

    public static void onCreate(final SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(final SQLiteDatabase database, final int oldVersion, final int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(database);
    }


}
