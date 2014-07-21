package osoc.leiedal.android.aandacht.View;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.astuetz.PagerSlidingTabStrip;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

import osoc.leiedal.android.aandacht.MyCursorAdaptor;
import osoc.leiedal.android.aandacht.R;
import osoc.leiedal.android.aandacht.View.fragments.ReportTabFragment;
import osoc.leiedal.android.aandacht.contentproviders.AandachtContentProvider;
import osoc.leiedal.android.aandacht.database.ReportsTable;
import osoc.leiedal.android.aandacht.views.FontTextView;

public class ViewReportsActivity extends ParentActivity implements View.OnCreateContextMenuListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get reports in area
        setContentView(R.layout.activity_view_reports);


        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());
        tabs.setIndicatorColor(Color.parseColor("#0075c6"));
        tabs.setTextColor(Color.parseColor("#0075c6"));
        tabs.setBackgroundColor(Color.parseColor("#FFFFFF"));

        pager.setAdapter(adapter);

        tabs.setViewPager(pager);
    }

    public void call(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.reports_popup_title);
        builder.setMessage(R.string.reports_popup_text);
        builder.setPositiveButton(R.string.reports_btnCall, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton){
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:0478927411"));
                startActivity(callIntent);
            }
        });
        builder.setNegativeButton(R.string.reports_popup_btnCancel, null);

        AlertDialog dialog = builder.show();
        TextView messageText = (TextView)dialog.findViewById(android.R.id.message);
        messageText.setGravity(Gravity.CENTER);
        dialog.show();
    }

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

 /*   public static class MyFrag extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

        private final static String ARG_POSITION = "arg_position";
        private int mPosition;
        private ListView listReports;
        private MyCursorAdaptor myCursorAdaptor;
        private static final int LOADER_REQUEST = 1;

        public static MyFrag instantiate(int number)
        {
            final MyFrag frag = new MyFrag();
            final Bundle args = new Bundle();
            args.putInt(ARG_POSITION, number);
            frag.setArguments(args);
            return frag;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if(getArguments().containsKey(ARG_POSITION))
            {
                mPosition = getArguments().getInt(ARG_POSITION);
            }

            getLoaderManager().initLoader(LOADER_REQUEST, null, this);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View v = getLayoutInflater(savedInstanceState).inflate(R.layout.fragment_test, container, false);
            //final FontTextView textView = (FontTextView) v.findViewById(R.id.textview_test);
            //textView.setText(String.format("position %d", mPosition));
            listReports = (ListView) v.findViewById(R.id.list_report);
            myCursorAdaptor = new MyCursorAdaptor( getActivity(), null, 0 );
            listReports.setAdapter(myCursorAdaptor);
            return v;
        }

        @Override
        public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
            switch (i) {
                case LOADER_REQUEST:
                    final Uri uri = AandachtContentProvider.CONTENT_URI_REPORTS;
                    String selection;
                    if(mPosition == 0) {

                    }
                    if(mPosition == 2) {

                    }
                    return new CursorLoader(getActivity(), uri, null, null, null, null);
                default:
                    return null;
            }
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        public void onLoadFinished(Loader<Cursor> objectLoader, Cursor o) {
            switch (objectLoader.getId()) {
                case LOADER_REQUEST:
                    if ( o.moveToFirst()) {
                        myCursorAdaptor.swapCursor(o);
                    }
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> objectLoader) {

        }
    }*/

    private class MyPagerAdapter extends FragmentPagerAdapter {

        private final String[] TITLES = { "Actief", "Alle", "Mijn" };

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

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //clear login data
        getSharedPreferences(getResources().getString(R.string.app_pref),0).edit().clear().commit();

        this.startActivity(new Intent(this,LoginActivity.class));
    }

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
}


