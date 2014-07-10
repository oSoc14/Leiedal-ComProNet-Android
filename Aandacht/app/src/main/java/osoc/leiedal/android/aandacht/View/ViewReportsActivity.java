package osoc.leiedal.android.aandacht.View;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.internal.cu;

import java.io.IOException;

import osoc.leiedal.android.aandacht.R;
import osoc.leiedal.android.aandacht.views.FontTextView;

public class ViewReportsActivity extends ParentActivity implements View.OnCreateContextMenuListener {

    private PagerSlidingTabStrip tabs;
    private ViewPager pager;
    private MyPagerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get reports in area
        setContentView(R.layout.activity_view_reports);


        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        pager = (ViewPager) findViewById(R.id.pager);
        adapter = new MyPagerAdapter(getSupportFragmentManager());

        pager.setAdapter(adapter);

        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                .getDisplayMetrics());

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

    public void callNumber(){

    }

    public void map(View v){
        Intent gotoPref = new Intent(this,ViewReportsActivity.class);
        startActivity(gotoPref);
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
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            //goto settings

            return true;
        }else if(id == R.id.action_profile){
            //goto profile
            this.startActivity(new Intent(this,ViewProfileActivity.class));
            return true;
        }else if(id == R.id.action_logout){
            this.startActivity(new Intent(this,LoginActivity.class));
            return true;
        }else {
            return super.onOptionsItemSelected(item);
        }
    }

    public static class MyFrag extends Fragment {

        private final static String ARG_POSITION = "arg_position";
        private int mPosition;

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
        }

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View v = getLayoutInflater(savedInstanceState).inflate(R.layout.fragment_test, container, false);
            final FontTextView textView = (FontTextView) v.findViewById(R.id.textview_test);
            textView.setText(String.format("position %d", mPosition));
            return v;
        }
    }


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
            return MyFrag.instantiate(position);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

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


