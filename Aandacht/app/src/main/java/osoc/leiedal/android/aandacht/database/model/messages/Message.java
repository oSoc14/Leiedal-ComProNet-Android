package osoc.leiedal.android.aandacht.database.model.messages;

import android.content.ContentValues;

import osoc.leiedal.android.aandacht.database.MessagesTable;

/**
 * The model representing a message in the database. This class is currently only used to
 * temporarily store generated test data.
 *
 */
public class Message {

    /* ============================================================================================
        MEMBERS
    ============================================================================================ */

    private ContentValues properties;

    /* ============================================================================================
        CONSTRUCTORS
    ============================================================================================ */

    public Message(final int report_id,
                   final String source,
                   final String content,
                   final long timestamp) {
        properties = new ContentValues();
        properties.put(MessagesTable.COLUMN_REPORT_ID, report_id);
        properties.put(MessagesTable.COLUMN_SOURCE, source);
        properties.put(MessagesTable.COLUMN_CONTENT, content);
        properties.put(MessagesTable.COLUMN_TIMESTAMP, timestamp);
    }

    /* ============================================================================================
        METHODS
    ============================================================================================ */

    public ContentValues getContentValues() {
        return properties;
    }

}
