package osoc.leiedal.android.aandacht.View;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Criteria;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import osoc.leiedal.android.aandacht.R;
import osoc.leiedal.android.aandacht.contentproviders.AandachtContentProvider;
import osoc.leiedal.android.aandacht.database.ReportsTable;

public class MapsActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_REQUEST = 3;

    // ------------------------------

    private BitmapDescriptor MARKER_PENDING;
    private BitmapDescriptor MARKER_ACTIVE;
    private BitmapDescriptor MARKER_DENIED;
    private BitmapDescriptor MARKER_FINISHED;

    private GoogleMap map;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        //back / up button
        if (getActionBar() != null)
            getActionBar().setDisplayHomeAsUpEnabled(true);
        setUpMapIfNeeded();
        getSupportLoaderManager().initLoader(LOADER_REQUEST, null, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    // ------------------------------

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (map == null) {
            // Try to obtain the map from the SupportMapFragment.
            map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (map != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        map.setMyLocationEnabled(true);
        MARKER_PENDING = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE);
        MARKER_ACTIVE = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
        MARKER_DENIED = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
        MARKER_FINISHED = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE);
    }

    // ------------------------------

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Uri uri = AandachtContentProvider.CONTENT_URI_REPORTS;
        String selection = ReportsTable.COLUMN_STATUS + " IN ('" + ReportsTable.STATUS_ACTIVE + "','" + ReportsTable.STATUS_PENDING + "')";
        CursorLoader cursorLoader = new CursorLoader(this, uri, null, selection, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // make sure map is not null
        setUpMapIfNeeded();
        if (cursor != null && cursor.moveToFirst()) {
            // create a new array to hold all MarkerOptions
            MarkerOptions[] markerOptionses = new MarkerOptions[cursor.getCount()];
            do {
                // compare the current time to the expiry timestamp of the report and only add it if it's not expired
                long timestamp_end = cursor.getLong(cursor.getColumnIndex(ReportsTable.COLUMN_TIMESTAMP_END));
                long current_time = System.currentTimeMillis() / 1000;
                if (current_time < timestamp_end) {
                    // extract the oprions from the cursor and save it locally in the array
                    markerOptionses[cursor.getPosition()] = extractMarkerOptions(cursor);
                }
            } while (cursor.moveToNext());
            setUpMarkers(markerOptionses);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    // ------------------------------

    private MarkerOptions extractMarkerOptions(Cursor cursor) {
        // extract the data from the cursor
        String description = cursor.getString(cursor.getColumnIndex(ReportsTable.COLUMN_DESCRIPTION));
        String address = cursor.getString(cursor.getColumnIndex(ReportsTable.COLUMN_ADDRESS));
        String status = cursor.getString(cursor.getColumnIndex(ReportsTable.COLUMN_STATUS));
        // determine the color of the marker
        BitmapDescriptor markerColor;
        if (status.equals(ReportsTable.STATUS_ACTIVE)) {
            markerColor = MARKER_ACTIVE;
        } else if (status.equals(ReportsTable.STATUS_PENDING)) {
            markerColor = MARKER_PENDING;
        } else if (status.equals(ReportsTable.STATUS_DENIED)) {
            markerColor = MARKER_DENIED;
        } else if (status.equals(ReportsTable.STATUS_FINISHED)) {
            markerColor = MARKER_FINISHED;
        } else {
            throw new RuntimeException("[MapsActivity] Unexpected status: " + status);
        }
        // get the longitude and latitude
        double lat = cursor.getDouble(cursor.getColumnIndex(ReportsTable.COLUMN_LOCATION_LAT));
        double lng = cursor.getDouble(cursor.getColumnIndex(ReportsTable.COLUMN_LOCATION_LNG));
        LatLng latLng = new LatLng(lat, lng);
        // create the new MarkerOptions object
        return new MarkerOptions()
                .title(address)
                .snippet(description)
                .position(latLng)
                .icon(markerColor);
    }

    private void setUpMarkers(MarkerOptions[] markerOptionses) {
        for (MarkerOptions markerOptions : markerOptionses) {
            map.addMarker(markerOptions);
        }
    }

}
