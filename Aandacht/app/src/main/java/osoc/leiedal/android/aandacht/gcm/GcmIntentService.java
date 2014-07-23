package osoc.leiedal.android.aandacht.gcm;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import osoc.leiedal.android.aandacht.R;
import osoc.leiedal.android.aandacht.View.LoginActivity;
import osoc.leiedal.android.aandacht.database.DummyData;
import osoc.leiedal.android.aandacht.database.model.reports.Report;

public class GcmIntentService extends IntentService {

    /* ============================================================================================
        STATIC MEMBERS
    ============================================================================================ */

    public static final int NOTIFICATION_ID = 1;

    // --------------------------------------------------------------------------------------------

    private static final String TAG = "GcmIntentService";

    /* ============================================================================================
        CONSTRUCTORS
    ============================================================================================ */

    public GcmIntentService() {
        super("GcmIntentService");
    }

    /* ============================================================================================
        METHODS
    ============================================================================================ */

    /*
        There are no public methods
    */

    // --------------------------------------------------------------------------------------------

    @Override
    protected void onHandleIntent(Intent intent) {
        final Bundle extras = intent.getExtras();
        final GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // the getMessageType() intent parameter must be the intent you received in your BroadcastReceiver
        final String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM
             * will be extended in the future with new message types, just ignore
             * any message types you're not interested in, or that you don't
             * recognize.
             */
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification("Deleted messages on server: " +
                        extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                //add report to SQLite
                addReport(extras);
                if (getSharedPreferences(getResources().getString(R.string.app_pref), 0).getBoolean(getResources().getString(R.string.settings_option_notif), true)) {
                    // Post notification of received message.
                    sendNotification(extras.toString());
                }
                Log.i(TAG, "Received: " + extras.toString());
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    // --------------------------------------------------------------------------------------------

    /**
     * Put the message into a notification and post it. This is just one simple example of what
     * you might choose to do with a GCM message.
     *
     * @param msg the message to display in the notification
     */
    private void sendNotification(String msg) {
        final NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        final PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, LoginActivity.class), 0);

        final NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.logo)//ic_stat_gcm)
                        .setContentTitle("GCM Notification")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setContentText(msg)
                        .setLights(Color.BLUE, 500, 500);

        if (getSharedPreferences(getResources().getString(R.string.app_pref), 0).getBoolean(getResources().getString(R.string.settings_option_sound), true)) {
            final Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            mBuilder.setSound(alarmSound);
        }

        if (getSharedPreferences(getResources().getString(R.string.app_pref), 0).getBoolean(getResources().getString(R.string.settings_option_vibrate), true)) {
            final long[] pattern = {500, 500, 500, 500, 500, 500, 500, 500, 500};
            mBuilder.setVibrate(pattern);
        }


        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    private void addReport(Bundle bundle) {
        DummyData.injectReport(getContentResolver(), new Report(bundle));
    }

}
