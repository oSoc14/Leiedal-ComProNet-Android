package osoc.leiedal.android.aandacht.View;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

import osoc.leiedal.android.aandacht.R;
import osoc.leiedal.android.aandacht.View.model.apiAccess.DummyAPIAccess;
import osoc.leiedal.android.aandacht.View.model.apiAccess.iAPIAccess;
import osoc.leiedal.android.aandacht.database.DummyData;


public class LoginActivity extends Activity {

    /* ============================================================================================
        STATIC MEMBERS
    ============================================================================================ */

    public static final String PROPERTY_REG_ID = "registration_id";

    public static String SENDER_ID;

    // --------------------------------------------------------------------------------------------

    private static final String TAG = LoginActivity.class.getSimpleName();
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private static String regid;

    /* ============================================================================================
        STATIC METHODS
    ============================================================================================ */

    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    /* ============================================================================================
        MEMBERS
    ============================================================================================ */

    private GoogleCloudMessaging gcm = null;

    /* ============================================================================================
        METHODS
    ============================================================================================ */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(osoc.leiedal.android.aandacht.R.menu.login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    /**
     * Handles the login button
     * checks login details
     * checks if play services are running
     * if both are true, the ViewReportsActivity will be launched
     */
    public void login(View view) {
        String login = ((EditText) findViewById(R.id.login_txtLogin)).getText().toString();
        String pass = ((EditText) findViewById(R.id.login_txtPass)).getText().toString();

        login(login, pass);
    }

    public void generate(View view) {
        DummyData.InjectDummyData(this.getContentResolver());
    }

    public void login(String login, String pass) {
        iAPIAccess api = DummyAPIAccess.getInstance();
        if (api.login(login, pass)) {

            //gotoPref.putExtra("user",login);
            startActivity(new Intent(this, ViewReportsActivity.class));

            if (checkPlayServices()) {
                Intent gotoPref = new Intent(this, ViewReportsActivity.class);
                startActivity(gotoPref);

                getSharedPreferences(getResources().getString(R.string.app_pref), 0).edit().putString("user", login).apply();
                getSharedPreferences(getResources().getString(R.string.app_pref), 0).edit().putBoolean("authenticated", true).apply();
            } else {
                finish();
            }
            //else => finish in checkPlayServices()
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.login_tstFail), Toast.LENGTH_LONG);
            toast.show();
        }
    }

    // --------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // NOTE this is not secure at all; a user can edit SharedPreferences and set the authenticated boolean to true
        // For a more secure login, a working backend is needed; used for proof of concept! handle with care!

        //if authenticated & action is main => proceed
        if (getSharedPreferences(getResources().getString(R.string.app_pref), 0).getBoolean("authenticated", false)) {
            //ALREADY SIGNED IN
            setContentView(osoc.leiedal.android.aandacht.R.layout.activity_login);
            // Check device for Play Services APK. If check succeeds, proceed with
            //  GCM registration.
            ((EditText) findViewById(R.id.login_txtPass)).setText("");
            ((EditText) findViewById(R.id.login_txtLogin)).setText("");

            Intent gotoPref = new Intent(this, ViewReportsActivity.class);
            startActivity(gotoPref);
        } else {
            setContentView(R.layout.activity_login);
            final EditText edittext = (EditText) findViewById(R.id.login_txtPass);
            edittext.setOnKeyListener(new View.OnKeyListener() {
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                        String login = ((EditText) findViewById(R.id.login_txtLogin)).getText().toString();
                        String pass = ((EditText) findViewById(R.id.login_txtPass)).getText().toString();

                        login(login, pass);
                        return true;
                    }
                    return false;
                }
            });
            ((EditText) findViewById(R.id.login_txtLogin)).setText(getSharedPreferences(getResources().getString(R.string.app_pref), 0).getString("user", ""));
        }

        if (checkPlayServices()) {
            SENDER_ID = getResources().getString(R.string.app_senderId);
            gcm = GoogleCloudMessaging.getInstance(this);
            regid = getRegistrationId(getApplicationContext());

            if (TextUtils.isEmpty(regid)) {
                Log.i(TAG, "regid empty, registering");
                registerInBackground();
            } else {
                Log.i(TAG, "devide already registered id: " + regid);
            }
        } else {
            Log.i(TAG, "No valid Google Play Services APK found.");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
        ((EditText) findViewById(R.id.login_txtPass)).setText("");
    }

    // --------------------------------------------------------------------------------------------

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    /**
     * Gets the current registration ID for application on GCM service.
     * <p/>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     * registration ID.
     */
    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences();
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }

        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    /**
     * @return Application's {@code SharedPreferences}.
     */
    private SharedPreferences getGCMPreferences() {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the regID in your app is up to you.
        return getSharedPreferences(LoginActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }

    /**
     * Registers the application with GCM servers asynchronously.
     * <p/>
     * Stores the registration ID and app versionCode in the application's
     * shared preferences.
     */
    private void registerInBackground() {
        (new AsyncTask() {
            @Override
            protected String doInBackground(Object[] params) {
                String msg;
                try {
                    Context context = getApplicationContext();

                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regid = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;
                    Log.i(TAG, "registering in background: " + regid);

                    // You should send the registration ID to your server over HTTP,
                    // so it can use GCM/HTTP or CCS to send messages to your app.
                    // The request to your server should be authenticated if your app
                    // is using accounts.
                    sendRegistrationIdToBackend();

                    // For this demo: we don't need to send it because the device
                    // will send upstream messages to a server that echo back the
                    // message using the 'from' address in the message.

                    // Persist the regID - no need to register again.
                    storeRegistrationId(context, regid);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                Log.i(TAG, msg);
                return msg;
            }
        }).execute(null, null, null);
    }

    /**
     * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP
     * or CCS to send messages to your app. Not needed for this demo since the
     * device sends upstream messages to a server that echoes back the message
     * using the 'from' address in the message.
     */
    private void sendRegistrationIdToBackend() {
        // Your implementation here.

        //TODO
        //send id to own backend


    }

    /**
     * Stores the registration ID and app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param context application's context.
     * @param regId   registration ID
     */
    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGCMPreferences();
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.apply();
    }

}
