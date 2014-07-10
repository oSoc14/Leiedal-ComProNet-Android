package osoc.leiedal.android.aandacht.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Maarten on 7/07/2014.
 */
public class ReportsTable {

    // database and column names
    public static final String TABLE_NAME = "reports";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_LOCATION_LAT = "latitude";
    public static final String COLUMN_LOCATION_LNG = "longitude";
    public static final String COLUMN_TIMESTAMP_START = "timestamp_start";
    public static final String COLUMN_TIMESTAMP_END = "timestamp_end";
    public static final String COLUMN_STATUS = "status";

    // possible status values
    public static final String STATUS_ACTIVE = "active";
    public static final String STATUS_DENIED = "denied";
    public static final String STATUS_PENDING = "pending";
    public static final String STATUS_FINISHED = "finished";

    // projections
    public static final String[] PROJECTION_ALL = null;

    // Database creation SQL statement
    private static final String DATABASE_CREATE =
            "create table " + TABLE_NAME
            + "("
            + COLUMN_ID + " integer primary key autoincrement,"
            + COLUMN_DESCRIPTION + " text not null,"
            + COLUMN_ADDRESS + " text not null,"
            + COLUMN_LOCATION_LAT + " real not null,"
            + COLUMN_LOCATION_LNG + " real not null,"
            + COLUMN_STATUS + " string not null,"
            + COLUMN_TIMESTAMP_START + " integer not null,"
            + COLUMN_TIMESTAMP_END + " integer not null"
            + ")";

    public static void onCreate(final SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(final SQLiteDatabase database, final int oldVersion, final int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(database);
    }


}
