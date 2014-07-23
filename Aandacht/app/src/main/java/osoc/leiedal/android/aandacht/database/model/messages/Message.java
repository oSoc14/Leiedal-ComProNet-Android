package osoc.leiedal.android.aandacht.database.model.messages;

import android.content.ContentValues;

import osoc.leiedal.android.aandacht.database.MessagesTable;

/**
 * The model representing a message in the database. This class is currently only used to
 * temporarily store generated test data.
 *
 * Created by Maarten on 11/07/2014.
 */
public class Message {

    private ContentValues properties;

    public Message(
            int report_id,
            String source,
            String content,
            long timestamp) {
        properties = new ContentValues();
        properties.put(MessagesTable.COLUMN_REPORT_ID, report_id);
        properties.put(MessagesTable.COLUMN_SOURCE, source);
        properties.put(MessagesTable.COLUMN_CONTENT, content);
        properties.put(MessagesTable.COLUMN_TIMESTAMP, timestamp);
    }

    public ContentValues getContentValues() {
        return properties;
    }

}
