package osoc.leiedal.android.aandacht.database.model.reports;

import android.content.ContentValues;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

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
       fillValues(description,address,latitude,longitude,status,timestamp_start,timestamp_end);
    }

    public Report (String json){
        try {
            JSONObject jor = new JSONObject(json);
            fillValues(jor.getString("description"),
                    jor.getString("address"),
                    jor.getDouble("latitude"),
                    jor.getDouble("longitude"),
                    jor.getString("status"),
                    jor.getLong("timestamp_start"),
                    jor.getLong("timestamp_end")
            );
        } catch (JSONException e) {
            Log.i("REPORT", "invalid json: " + json);
            e.printStackTrace();
        }

    }

    public Report(Bundle jor){
        fillValues(jor.getString("description"),
                jor.getString("address"),
                Double.parseDouble(jor.getString("latitude")),
                Double.parseDouble(jor.getString("longitude")),
                jor.getString("status"),
                Long.parseLong(jor.getString("timestamp_start")),
                Long.parseLong(jor.getString("timestamp_end"))
        );
    }

    private void fillValues(String description, String address, double latitude, double longitude,
                            String status, long timestamp_start, long timestamp_end) {
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
