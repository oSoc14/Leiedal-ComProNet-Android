package osoc.leiedal.android.aandacht.View.fragments;

import android.annotation.TargetApi;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import osoc.leiedal.android.aandacht.MyCursorAdaptor;
import osoc.leiedal.android.aandacht.R;
import osoc.leiedal.android.aandacht.contentproviders.AandachtContentProvider;
import osoc.leiedal.android.aandacht.database.ReportsTable;

/**
 * Created by Maarten on 21/07/2014.
 */
public class ReportTabFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private final static String ARG_POSITION = "arg_position";
    private static final int LOADER_REQUEST = 1;

    private static final int TAB_ACTIVE = 0;
    private static final int TAB_ALL = 1;
    private static final int TAB_MINE = 2;

    private int mPosition;
    private ListView listReports;
    private MyCursorAdaptor myCursorAdaptor;

    public static ReportTabFragment instantiate(int number) {
        final ReportTabFragment frag = new ReportTabFragment();
        final Bundle args = new Bundle();
        args.putInt(ARG_POSITION, number);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(ARG_POSITION)) {
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
        myCursorAdaptor = new MyCursorAdaptor(getActivity(), null, 0);
        listReports.setAdapter(myCursorAdaptor);
        return v;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        switch (i) {
            case LOADER_REQUEST:
                final Uri uri = AandachtContentProvider.CONTENT_URI_REPORTS;
                String selection = null;
                if (mPosition == TAB_ACTIVE) {
                    selection = ReportsTable.COLUMN_STATUS + "=\"" + ReportsTable.STATUS_ACTIVE + "\"";
                }
                else if (mPosition == TAB_MINE) {
                    selection = ReportsTable.COLUMN_STATUS + " IN ('" + ReportsTable.STATUS_ACTIVE + "','" + ReportsTable.STATUS_PENDING + "')";
                }
                /*else if (mPosition == TAB_ALL) {
                    // Not necessary; selection can stay null in this case
                }*/
                return new CursorLoader(getActivity(), uri, null, selection, null, null);
            default:
                return null;
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onLoadFinished(Loader<Cursor> objectLoader, Cursor o) {
        switch (objectLoader.getId()) {
            case LOADER_REQUEST:
                if (o.moveToFirst()) {
                    myCursorAdaptor.swapCursor(o);
                }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> objectLoader) {

    }
}
