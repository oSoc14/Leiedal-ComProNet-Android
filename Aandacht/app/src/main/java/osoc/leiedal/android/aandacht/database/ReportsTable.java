package osoc.leiedal.android.aandacht.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Maarten on 7/07/2014.
 */
public class ReportsTable {

    // Database and column names
    public static final String TABLE_NAME = "reports";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_LOCATION = "location";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    // Database creation SQL statement
    private static final String DATABASE_CREATE =
            "create table " + TABLE_NAME
            + "("
            + COLUMN_ID + " integer primary key autoincrement,"
            + COLUMN_DESCRIPTION + " text not null,"
            + COLUMN_LOCATION + "string not null,"
            + COLUMN_TIMESTAMP + "string not null"
            + ")";

    public static void onCreate(final SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(final SQLiteDatabase database, final int oldVersion, final int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(database);
    }


}
