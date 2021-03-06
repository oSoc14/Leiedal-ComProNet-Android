package osoc.leiedal.android.aandacht.View;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBarActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import osoc.leiedal.android.aandacht.R;
import osoc.leiedal.android.aandacht.contentproviders.AandachtContentProvider;
import osoc.leiedal.android.aandacht.database.ReportsTable;

public class MapsActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    /* ============================================================================================
        STATIC MEMBERS
    ============================================================================================ */

    private static final int LOADER_REQUEST = 3;

    /* ============================================================================================
        MEMBERS
    ============================================================================================ */

    private BitmapDescriptor MARKER_PENDING;
    private BitmapDescriptor MARKER_ACTIVE;
    private BitmapDescriptor MARKER_DENIED;
    private BitmapDescriptor MARKER_FINISHED;

    private GoogleMap map;

    /* ============================================================================================
        METHODS
    ============================================================================================ */

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Uri uri = AandachtContentProvider.CONTENT_URI_REPORTS;
        String selection = ReportsTable.COLUMN_STATUS + " IN ('" + ReportsTable.STATUS_ACTIVE + "','" + ReportsTable.STATUS_PENDING + "')";
        return new CursorLoader(this, uri, null, selection, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // make sure map is not null
        setUpMapIfNeeded();
        if (cursor != null && cursor.moveToFirst()) {
            // create a new array to hold all MarkerOptions
            MarkerOptions[] markerOptionses = new MarkerOptions[cursor.getCount()];
            do {
                // extract the options from the cursor and save it locally in the array
                markerOptionses[cursor.getPosition()] = extractMarkerOptions(cursor);
            } while (cursor.moveToNext());
            setUpMarkers(markerOptionses);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    // --------------------------------------------------------------------------------------------

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        //back / up button
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setUpMapIfNeeded();
        getSupportLoaderManager().initLoader(LOADER_REQUEST, null, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    // --------------------------------------------------------------------------------------------

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

        double loc[] = getGPS();
        LatLng coordinate = new LatLng(loc[0], loc[1]);
        CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 13);
        map.animateCamera(yourLocation);
    }

    private double[] getGPS() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = lm.getProviders(true);

        /* Loop over the array backwards, and if you get an accurate location, then break out the loop*/
        Location l = null;

        for (int i = providers.size() - 1; i >= 0; i--) {
            l = lm.getLastKnownLocation(providers.get(i));
            if (l != null) break;
        }

        double[] gps = new double[2];
        if (l != null) {
            gps[0] = l.getLatitude();
            gps[1] = l.getLongitude();
        }
        return gps;
    }

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
        //try {
        for (MarkerOptions markerOptions : markerOptionses) {
            map.addMarker(markerOptions);
        }
        //} catch(NullPointerException ignored) {

        // }
    }

}
