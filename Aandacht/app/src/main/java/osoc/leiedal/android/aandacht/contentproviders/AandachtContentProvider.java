package osoc.leiedal.android.aandacht.contentproviders;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import osoc.leiedal.android.aandacht.database.DatabaseHelper;
import osoc.leiedal.android.aandacht.database.MessagesTable;
import osoc.leiedal.android.aandacht.database.ReportsTable;

/**
 * The ContentProvider which provides access to reports and messages through URIs. Below is a list
 * of possible URIs and their supported actions.
 *
 * content://osoc.leiedal.android.aandacht.contentproviders.AandachtContentProvider
 *
 * content://osoc.leiedal.android.aandacht.contentproviders.AandachtContentProvider/reports
 * -query: the complete reports table
 * -insert: insert into the reports table
 * -update: update all rows in the reports table
 * -delete: delete all rows in the reports table
 *
 * content://osoc.leiedal.android.aandacht.contentproviders.AandachtContentProvider/reports/[id]
 * -query: the report with the given id
 * -update: update the row with the given id
 * -delete: delete the row with the given id
 *
 * content://osoc.leiedal.android.aandacht.contentproviders.AandachtContentProvider/reports/status/[status]
 * -query: all reports with the given status
 * -update: update all rows with the given status
 * -delete: delete all rows with the given status
 *
 * content://osoc.leiedal.android.aandacht.contentproviders.AandachtContentProvider/messages
 * -query: the complete messages table
 * -insert: insert into the messages table
 * -update: update all rows in the reports table
 * -delete: delete all rows in the reports table
 *
 * content://osoc.leiedal.android.aandacht.contentproviders.AandachtContentProvider/messages/[id]
 * -query: the message with the given id
 * -update: update the message with the given id
 * -delete: delete the message with the given id
 *
 * content://osoc.leiedal.android.aandacht.contentproviders.AandachtContentProvider/messages/report/[id]
 * -query: all messages belonging to the report with the given id
 * -update: update all messages belonging to the report with the given id
 * -delete: delete all messages belonging to the report with the given id
 *
 * Created by Maarten on 7/07/2014.
 */
public class AandachtContentProvider extends ContentProvider {

    public static final String PROVIDER_NAME = "osoc.leiedal.android.aandacht.contentproviders.AandachtContentProvider";

    public static final Uri CONTENT_URI_ALL = Uri.parse("content://" + PROVIDER_NAME);
    // uris for the tables
    public static final Uri CONTENT_URI_REPORTS = Uri.parse("content://" + PROVIDER_NAME + "/reports");
    public static final Uri CONTENT_URI_MESSAGES = Uri.parse("content://" + PROVIDER_NAME + "/messages");
    // uris for the reports table
    public static final Uri CONTENT_URI_REPORTS_ID = Uri.parse("content://" + PROVIDER_NAME + "/reports");
    public static final Uri CONTENT_URI_REPORTS_STATUS = Uri.parse("content://" + PROVIDER_NAME + "/reports/status");
    // uris for the messages table
    public static final Uri CONTENT_URI_MESSAGES_ID = Uri.parse("content://" + PROVIDER_NAME + "/messages");
    public static final Uri CONTENT_URI_MESSAGES_REPORT = Uri.parse("content://" + PROVIDER_NAME + "/messages/report");

    private static final int TYPE_ALL = 1;                  // uri for the ContentProvider
    private static final int TYPE_REPORTS = 10;             // uri for the reports table
    private static final int TYPE_REPORTS_ID = 11;          // uri for the reports by id
    private static final int TYPE_REPORTS_STATUS = 12;      // uri for the reports by status
    private static final int TYPE_MESSAGES = 20;            // uri for the messages table
    private static final int TYPE_MESSAGES_ID = 21;         // uri for the messages by id
    private static final int TYPE_MESSAGES_REPORT = 22;     // uri for the messages by report(-id)

    private static final UriMatcher uriMatcher;
    static {
        // # = any number; * = any string of text
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, null, TYPE_ALL);
        uriMatcher.addURI(PROVIDER_NAME, "reports", TYPE_REPORTS);
        uriMatcher.addURI(PROVIDER_NAME, "reports/#", TYPE_REPORTS_ID);
        uriMatcher.addURI(PROVIDER_NAME, "reports/status/*", TYPE_REPORTS_STATUS);
        uriMatcher.addURI(PROVIDER_NAME, "messages", TYPE_MESSAGES);
        uriMatcher.addURI(PROVIDER_NAME, "messages/#", TYPE_MESSAGES_ID);
        uriMatcher.addURI(PROVIDER_NAME, "messages/report/#", TYPE_MESSAGES_REPORT);
    }

    // ------------------------------------------------------------------------------------------

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;

    @Override
    public boolean onCreate() {
        this.databaseHelper = new DatabaseHelper(this.getContext());
        this.database = this.databaseHelper.getWritableDatabase();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        // use a querybuilder and set the table to 'reports'
        SQLiteQueryBuilder qBuilder = new SQLiteQueryBuilder();

        // get the number of one of our uris which match the given uri
        int match = uriMatcher.match(uri);

        switch (match) {
            case TYPE_ALL:
                // not supported yet, don't really know what to return on this uri
                throw new UnsupportedOperationException("Unsupported uri: " + uri);
                // break;
            case TYPE_REPORTS:
                // all reports ordered by timestamp descending, unless specified otherwise
                qBuilder.setTables(ReportsTable.TABLE_NAME);
                if (sortOrder == null || sortOrder == "") {
                    sortOrder = ReportsTable.COLUMN_TIMESTAMP + " DESC";
                }
                break;
            case TYPE_REPORTS_ID:
                // the report with a given id (always one, since the id is unique)
                qBuilder.setTables(ReportsTable.TABLE_NAME);
                qBuilder.appendWhere(ReportsTable.COLUMN_ID + "=" + uri.getLastPathSegment());
                break;
            case TYPE_REPORTS_STATUS:
                // all reports with a given status ordered by timestamp descending, unless specified otherwise
                qBuilder.setTables(ReportsTable.TABLE_NAME);
                qBuilder.appendWhere(ReportsTable.COLUMN_STATUS + "=" + uri.getLastPathSegment());
                if (sortOrder == null || sortOrder == "") {
                    sortOrder = ReportsTable.COLUMN_TIMESTAMP + " DESC";
                }
                break;
            case TYPE_MESSAGES:
                // all messages ordered by timestamp descending, unless specified otherwise
                qBuilder.setTables(MessagesTable.TABLE_NAME);
                if (sortOrder == null || sortOrder == "") {
                    sortOrder = MessagesTable.COLUMN_TIMESTAMP + " DESC";
                }
                break;
            case TYPE_MESSAGES_ID:
                // the message with a given id (always one, since the id is unique)
                qBuilder.setTables(MessagesTable.TABLE_NAME);
                qBuilder.appendWhere(MessagesTable.COLUMN_ID + "=" + uri.getLastPathSegment());
                break;
            case TYPE_MESSAGES_REPORT:
                // all messages belonging to a report with a given id ordered by timestamp descending, unless specified otherwise
                qBuilder.setTables(MessagesTable.TABLE_NAME);
                qBuilder.appendWhere(MessagesTable.COLUMN_REPORT_ID + "=" + uri.getLastPathSegment());
                if (sortOrder == null || sortOrder == "") {
                    sortOrder = MessagesTable.COLUMN_TIMESTAMP + " DESC";
                }
                break;
            default:
                // uri did not match one of our specified uris, throw an exception
                throw new IllegalArgumentException("Invalid uri: " + uri);
        }

        // execute the query on the given database, then notify all observers for the uri
        Cursor cursor = qBuilder.query(this.database, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(this.getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        // first, we'll define which table to insert the values into
        String tableName;
        int match = uriMatcher.match(uri);
        switch (match) {
            case TYPE_REPORTS:
                // the uri for the reports table
                tableName = ReportsTable.TABLE_NAME;
                break;
            case TYPE_MESSAGES:
                // the uri for the messages table
                tableName = MessagesTable.TABLE_NAME;
                break;
            default:
                // any other uri is not supported to insert values into one of the database tables
                throw new UnsupportedOperationException("Unsupported uri: " + uri);
        }
        // now insert those values in the table, we'll do this in one transaction
        int numInserted = 0;
        this.database.beginTransaction();
        try {
            // for each value, insert it and check if it has been inserted, if not throw an exception
            for (ContentValues cv : values) {
                long newId = this.database.insertOrThrow(tableName, null, cv);
                if (newId < 0) {
                    throw new SQLException("Failed to insert row into " + uri);
                }
            }
            // set the transaction successful and notify observers of the change
            this.database.setTransactionSuccessful();
            this.getContext().getContentResolver().notifyChange(uri, null);
            numInserted = values.length;
        } finally {
            // end the transaction, whether it was successful or not
            this.database.endTransaction();
        }
        return numInserted;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        String tableName;
        int match = uriMatcher.match(uri);
        // first, define the table in which to insert the data
        switch (match) {
            case TYPE_REPORTS:
                tableName = ReportsTable.TABLE_NAME;
                break;
            case TYPE_MESSAGES:
                tableName = MessagesTable.TABLE_NAME;
                break;
            default:
                throw new UnsupportedOperationException("Unsupported uri: " + uri);
        }
        // insert the data
        long newId = this.database.insert(tableName, null, values);
        if (newId < 0) {
            throw new SQLException("Failed to insert row into " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        // return the uri of the inserted data
        return Uri.parse(uri + "/" + newId);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {String tableName;
        int numUpdated;
        int match = uriMatcher.match(uri);
        switch (match) {
            case TYPE_ALL:
                throw new UnsupportedOperationException("Unsupported uri: " + uri);
                // break;
            case TYPE_REPORTS:
                tableName = ReportsTable.TABLE_NAME;
                break;
            case TYPE_REPORTS_ID:
                tableName = ReportsTable.TABLE_NAME;
                selection += (TextUtils.isEmpty(selection)?"":" AND ") + ReportsTable.COLUMN_ID + "=" + uri.getLastPathSegment();
                break;
            case TYPE_REPORTS_STATUS:
                tableName = ReportsTable.TABLE_NAME;
                selection += (TextUtils.isEmpty(selection)?"":" AND ") + ReportsTable.COLUMN_STATUS + "=" + uri.getLastPathSegment();
                break;
            case TYPE_MESSAGES:
                tableName = MessagesTable.TABLE_NAME;
                break;
            case TYPE_MESSAGES_ID:
                tableName = MessagesTable.TABLE_NAME;
                selection += (TextUtils.isEmpty(selection)?"":" AND ") + MessagesTable.COLUMN_ID + "=" + uri.getLastPathSegment();
                break;
            case TYPE_MESSAGES_REPORT:
                tableName = MessagesTable.TABLE_NAME;
                selection += (TextUtils.isEmpty(selection)?"":" AND ") + MessagesTable.COLUMN_REPORT_ID + "=" + uri.getLastPathSegment();
                break;
            default:
                throw new IllegalArgumentException("Invalid uri: " + uri);
        }
        numUpdated = this.database.update(tableName, values, selection, selectionArgs);
        this.getContext().getContentResolver().notifyChange(uri, null);
        return numUpdated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        String tableName;
        int numDeleted;
        int match = uriMatcher.match(uri);
        switch (match) {
            case TYPE_ALL:
                throw new UnsupportedOperationException("Unsupported uri: " + uri);
                // break;
            case TYPE_REPORTS:
                tableName = ReportsTable.TABLE_NAME;
                break;
            case TYPE_REPORTS_ID:
                tableName = ReportsTable.TABLE_NAME;
                selection += (TextUtils.isEmpty(selection)?"":" AND ") + ReportsTable.COLUMN_ID + "=" + uri.getLastPathSegment();
                break;
            case TYPE_REPORTS_STATUS:
                tableName = ReportsTable.TABLE_NAME;
                selection += (TextUtils.isEmpty(selection)?"":" AND ") + ReportsTable.COLUMN_STATUS + "=" + uri.getLastPathSegment();
                break;
            case TYPE_MESSAGES:
                tableName = MessagesTable.TABLE_NAME;
                break;
            case TYPE_MESSAGES_ID:
                tableName = MessagesTable.TABLE_NAME;
                selection += (TextUtils.isEmpty(selection)?"":" AND ") + MessagesTable.COLUMN_ID + "=" + uri.getLastPathSegment();
                break;
            case TYPE_MESSAGES_REPORT:
                tableName = MessagesTable.TABLE_NAME;
                selection += (TextUtils.isEmpty(selection)?"":" AND ") + MessagesTable.COLUMN_REPORT_ID + "=" + uri.getLastPathSegment();
                break;
            default:
                throw new IllegalArgumentException("Invalid uri: " + uri);
        }
        numDeleted = this.database.delete(tableName, selection, selectionArgs);
        this.getContext().getContentResolver().notifyChange(uri, null);
        return numDeleted;
    }

    @Override
    public String getType(Uri uri) {
        String type;
        int match = uriMatcher.match(uri);
        switch(match) {
            case TYPE_ALL:
            case TYPE_REPORTS:
            case TYPE_REPORTS_ID:
            case TYPE_REPORTS_STATUS:
            case TYPE_MESSAGES:
            case TYPE_MESSAGES_ID:
            case TYPE_MESSAGES_REPORT:
                type = "vnd.android.cursor.dir/" + PROVIDER_NAME;
                break;
            default:
                throw new IllegalArgumentException("Invalid uri: " + uri);
        }
        return type;
    }

}
