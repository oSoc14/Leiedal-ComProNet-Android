package osoc.leiedal.android.aandacht.View;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;

import osoc.leiedal.android.aandacht.R;
import osoc.leiedal.android.aandacht.View.fragments.ViewProfileFragment;
import osoc.leiedal.android.aandacht.views.FontTextView;

public class ViewProfileActivity extends ParentActivity implements ViewProfileFragment.OnFragmentInteractionListener {

    private PagerSlidingTabStrip tabs;
    private ViewPager pager;
    private MyPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        pager = (ViewPager) findViewById(R.id.pager);
        adapter = new MyPagerAdapter(getSupportFragmentManager());

        pager.setAdapter(adapter);

        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                .getDisplayMetrics());

        tabs.setViewPager(pager);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.7
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.view_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        private final String[] TITLES = { "response time", "numbers", "other" };

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
            String[] text = {"response time", "numbers", "other"};

            textView.setText(text[mPosition]);
            return v;
        }
    }

}
