package osoc.leiedal.android.aandacht.database.model.reports;

import android.content.ContentValues;
import android.os.Bundle;

import osoc.leiedal.android.aandacht.database.ReportsTable;

/**
 * The model representing a report in the database. This class is currently only used to
 * temporarily store generated test data.
 *
 */
public class Report {

    /**********************************************************************************************
     * MEMBERS
     **********************************************************************************************/

    public ContentValues properties;

    /**********************************************************************************************
     * CONSTRUCTORS
     **********************************************************************************************/

    public Report(
            final String description,
            final String address,
            final double latitude,
            final double longitude,
            final String status,
            final long timestamp_start,
            final long timestamp_end) {
       fillValues(description,address,latitude,longitude,status,timestamp_start,timestamp_end);
    }

    public Report(final Bundle jor){
        fillValues(jor.getString("description"),
                jor.getString("address"),
                Double.parseDouble(jor.getString("latitude")),
                Double.parseDouble(jor.getString("longitude")),
                jor.getString("status"),
                Long.parseLong(jor.getString("timestamp_start")),
                Long.parseLong(jor.getString("timestamp_end"))
        );
    }

    /**********************************************************************************************
     * METHODS
     **********************************************************************************************/

    public ContentValues getContentValues() {
        return properties;
    }

    // --------------------------------------------------------------------------------------------

    private void fillValues(final String description,
                            final String address,
                            final double latitude,
                            final double longitude,
                            final String status,
                            final long timestamp_start,
                            final long timestamp_end) {
        properties = new ContentValues();
        properties.put(ReportsTable.COLUMN_DESCRIPTION, description);
        properties.put(ReportsTable.COLUMN_ADDRESS, address);
        properties.put(ReportsTable.COLUMN_LOCATION_LAT, latitude);
        properties.put(ReportsTable.COLUMN_LOCATION_LNG, longitude);
        properties.put(ReportsTable.COLUMN_STATUS, status);
        properties.put(ReportsTable.COLUMN_TIMESTAMP_START, timestamp_start);
        properties.put(ReportsTable.COLUMN_TIMESTAMP_END, timestamp_end);
    }

}
