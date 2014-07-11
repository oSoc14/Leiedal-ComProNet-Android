package osoc.leiedal.android.aandacht;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import osoc.leiedal.android.aandacht.database.ReportsTable;

/**
 * Created by ruben on 11-7-2014.
 */
public class MyCursorAdaptor extends CursorAdapter {
    public MyCursorAdaptor(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_report, parent, false);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView txtName = (TextView) view.findViewById(R.id.txtView_adress);
        txtName.setText(cursor.getString(cursor.getColumnIndex(ReportsTable.COLUMN_ADDRESS)));


    }
}
