package osoc.leiedal.android.aandacht;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.View;

import osoc.leiedal.android.aandacht.contentproviders.AandachtContentProvider;
import osoc.leiedal.android.aandacht.database.ReportsTable;

/**
 * Created by Maarten on 22/07/2014.
 */
public class ButtonListener implements View.OnClickListener {

    public static final int TYPE_CONFIRM = 0;
    public static final int TYPE_DENY = 1;

    private Context context;
    private int id;
    private int type;

    public ButtonListener(Context context, int id, int type) {
        this.context = context;
        this.id = id;
        this.type = type;
    }

    @Override
    public void onClick(View view) {
        Uri queryUri = Uri.withAppendedPath(AandachtContentProvider.CONTENT_URI_REPORTS_ID, Integer.toString(id));
        Cursor cursor = context.getContentResolver().query(queryUri, null, null, null, null);
        if(cursor.moveToFirst()) {
            String currentStatus = cursor.getString(cursor.getColumnIndex(ReportsTable.COLUMN_STATUS));
            String newStatus = decideNewStatus(currentStatus);
            updateStatus(id, newStatus);
        }
    }

    private void updateStatus(int id, String status) {
        if(status.equals(ReportsTable.STATUS_DELETED)) {
            Uri uri = Uri.withAppendedPath(AandachtContentProvider.CONTENT_URI_REPORTS_ID, Integer.toString(id));
            context.getContentResolver().delete(uri, null, null);
            Uri uri2 = Uri.withAppendedPath(AandachtContentProvider.CONTENT_URI_MESSAGES_REPORT, Integer.toString(id));
            context.getContentResolver().delete(uri2, null, null);
        } else {
            Uri uri = Uri.withAppendedPath(AandachtContentProvider.CONTENT_URI_REPORTS_ID, Integer.toString(id));
            ContentValues contentValues = new ContentValues();
            contentValues.put(ReportsTable.COLUMN_STATUS, status);
            context.getContentResolver().update(uri, contentValues, null, null);
        }
    }

    private String decideNewStatus(String status) {
        if(type == TYPE_CONFIRM)
            return decideNewStatusOnConfirm(status);
        else
            return decideNewStatusOnDeny(status);
    }

    private String decideNewStatusOnConfirm(String status) {
        return ReportsTable.STATUS_ACTIVE;
    }

    private String decideNewStatusOnDeny(String status) {
        if(status.equals(ReportsTable.STATUS_FINISHED))
            return ReportsTable.STATUS_DELETED;
        else
            return ReportsTable.STATUS_DENIED;
    }

}
