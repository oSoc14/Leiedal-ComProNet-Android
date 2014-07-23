package osoc.leiedal.android.aandacht.View;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

import osoc.leiedal.android.aandacht.R;
import osoc.leiedal.android.aandacht.View.fragments.ReportTabFragment;

public class ViewReportsActivity extends ParentActivity implements View.OnCreateContextMenuListener {

    /* ============================================================================================
        NESTED CLASSES
    ============================================================================================ */

    private class MyPagerAdapter extends FragmentPagerAdapter {

        private final String[] TITLES = {
                getResources().getString(R.string.tab_reports_active),
                getResources().getString(R.string.tab_reports_all),
                getResources().getString(R.string.tab_reports_mine)
        };

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public Fragment getItem(int position) {

            return ReportTabFragment.instantiate(position);
        }
    }

    /* ============================================================================================
        METHODS
    ============================================================================================ */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.view_reports, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //clear login data
        getSharedPreferences(getResources().getString(R.string.app_pref), 0).edit().clear().commit();

        this.startActivity(new Intent(this, LoginActivity.class));
    }

    public void call(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.reports_popup_title);
        builder.setMessage(R.string.reports_popup_text);
        builder.setPositiveButton(R.string.reports_btnCall, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:0478927411"));
                startActivity(callIntent);
            }
        });
        builder.setNegativeButton(R.string.reports_popup_btnCancel, null);

        AlertDialog dialog = builder.show();
        TextView messageText = (TextView) dialog.findViewById(android.R.id.message);
        messageText.setGravity(Gravity.CENTER);
        dialog.show();
    }

    // --------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get reports in area
        setContentView(R.layout.activity_view_reports);


        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());
        tabs.setIndicatorColor(getResources().getColor(R.color.aandacht_dark_blue));
        tabs.setTextColor(getResources().getColor(R.color.aandacht_dark_blue));
        tabs.setBackgroundColor(getResources().getColor(R.color.aandacht_background));

        pager.setAdapter(adapter);

        tabs.setViewPager(pager);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    // UNUSED METHOD
    /**
    public void send(final View view) {
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                String msg = "";
                try {
                    Bundle data = new Bundle();
                    data.putString("my_message", "Hello World");
                    data.putString("my_action",
                            "com.google.android.gcm.demo.app.ECHO_NOW");
                    String id = Long.toString(System.currentTimeMillis());
                    GoogleCloudMessaging.getInstance(getApplicationContext())
                            .send(LoginActivity.SENDER_ID + "@gcm.googleapis.com", id, data);
                    msg = "Sent message";
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            }
        }.execute(null, null, null);
    }
    */

}


