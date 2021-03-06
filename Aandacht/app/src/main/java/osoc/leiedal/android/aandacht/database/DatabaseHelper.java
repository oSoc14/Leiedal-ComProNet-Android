package osoc.leiedal.android.aandacht.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Class to abstract database creation.
 *
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    /* ============================================================================================
        STATIC MEMBERS
    ============================================================================================ */

    private static final int dbVersion = 3;
    private static final String dbName = "aandacht.db";

    /* ============================================================================================
        CONSTRUCTORS
    ============================================================================================ */

    public DatabaseHelper(Context context) {
        super(context, dbName, null, dbVersion);
    }

    /* ============================================================================================
        METHODS
    ============================================================================================ */

    @Override
    public void onCreate(SQLiteDatabase database) {
        ReportsTable.onCreate(database);
        MessagesTable.onCreate(database);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        ReportsTable.onUpgrade(database, oldVersion, newVersion);
        MessagesTable.onUpgrade(database, oldVersion, newVersion);
    }

}
