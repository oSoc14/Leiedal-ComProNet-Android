package osoc.leiedal.android.aandacht.database.model.reports;

import android.content.ContentValues;

import osoc.leiedal.android.aandacht.database.ReportsTable;

/**
 * Created by Maarten on 11/07/2014.
 */
public class Report {

    public ContentValues properties;

    public Report(
            String description,
            String address,
            double latitude,
            double longitude,
            String status,
            long timestamp_start,
            long timestamp_end) {
        properties = new ContentValues();
        properties.put(ReportsTable.COLUMN_DESCRIPTION, description);
        properties.put(ReportsTable.COLUMN_ADDRESS, address);
        properties.put(ReportsTable.COLUMN_LOCATION_LAT, latitude);
        properties.put(ReportsTable.COLUMN_LOCATION_LNG, longitude);
        properties.put(ReportsTable.COLUMN_STATUS, status);
        properties.put(ReportsTable.COLUMN_TIMESTAMP_START, timestamp_start);
        properties.put(ReportsTable.COLUMN_TIMESTAMP_END, timestamp_end);
    }

    public ContentValues getContentValues() {
        return properties;
    }

}
