package osoc.leiedal.android.aandacht.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Maarten on 7/07/2014.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int dbVersion = 1;
    private static final String dbName = "aandacht.db";

    public DatabaseHelper(Context context) {
        super(context, dbName, null, dbVersion);
    }

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