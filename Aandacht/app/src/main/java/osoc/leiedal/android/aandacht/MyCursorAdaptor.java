package osoc.leiedal.android.aandacht;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import osoc.leiedal.android.aandacht.database.ReportsTable;

public class MyCursorAdaptor extends CursorAdapter {

    public MyCursorAdaptor(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return inflater.inflate(R.layout.list_item_report, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView txtName = (TextView) view.findViewById(R.id.txtView_adress);
        TextView txtUpdate = (TextView) view.findViewById(R.id.txtView_update);
        txtName.setText(cursor.getString(cursor.getColumnIndex(ReportsTable.COLUMN_ADDRESS)));
        txtUpdate.setText(cursor.getString(cursor.getColumnIndex(ReportsTable.COLUMN_DESCRIPTION)));
    }
}