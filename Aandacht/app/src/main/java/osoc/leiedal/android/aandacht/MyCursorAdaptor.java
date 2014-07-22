package osoc.leiedal.android.aandacht;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.media.Image;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.SQLOutput;
import java.util.GregorianCalendar;

import osoc.leiedal.android.aandacht.contentproviders.AandachtContentProvider;
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
    public void bindView(View view, final Context context, final Cursor cursor) {
        ImageView imgView = (ImageView) view.findViewById(R.id.imgView_status);
        TextView txtName = (TextView) view.findViewById(R.id.tatView_address);
        TextView txtUpdate = (TextView) view.findViewById(R.id.txtView_update);
        TextView txtTimestamp = (TextView) view.findViewById(R.id.txtView_timestamp);
        ImageButton btnConfirm = (ImageButton) view.findViewById(R.id.imgButton_confirm);
        ImageButton btnDeny = (ImageButton) view.findViewById(R.id.imgButton_deny);

        int id = cursor.getInt(cursor.getColumnIndex(ReportsTable.COLUMN_ID));
        String name = cursor.getString(cursor.getColumnIndex(ReportsTable.COLUMN_ADDRESS));
        String update = cursor.getString(cursor.getColumnIndex(ReportsTable.COLUMN_DESCRIPTION));
        String status = cursor.getString(cursor.getColumnIndex(ReportsTable.COLUMN_STATUS));
        int timestamp = cursor.getInt(cursor.getColumnIndex(ReportsTable.COLUMN_TIMESTAMP_START));

        txtName.setText(name);
        txtUpdate.setText(update);
        txtTimestamp.setText(wrapTimestamp(System.currentTimeMillis() / 1000 - timestamp));

        int color = context.getResources().getColor(R.color.status_standard);
        int confirmBg = R.drawable.button_confirm_inactive;
        int confirmVisibility = View.VISIBLE;
        int denyBg = R.drawable.button_deny_inactive;
        int denyVisibility = View.VISIBLE;
        if(status.equals(ReportsTable.STATUS_ACTIVE)) {
            color = context.getResources().getColor(R.color.status_active);
            confirmBg = R.drawable.button_confirm_active;
        } else if(status.equals(ReportsTable.STATUS_PENDING)) {
            color = context.getResources().getColor(R.color.status_pending);
        } else if(status.equals(ReportsTable.STATUS_DENIED)) {
            color = context.getResources().getColor(R.color.status_denied);
            denyBg = R.drawable.button_deny_active;
        } else if(status.equals(ReportsTable.STATUS_FINISHED)) {
            color = context.getResources().getColor(R.color.status_finished);
            confirmVisibility = View.INVISIBLE;
        }

        GradientDrawable background = (GradientDrawable) imgView.getBackground();
        background.setColor(color);
        btnConfirm.setBackgroundResource(confirmBg);
        btnConfirm.setVisibility(confirmVisibility);
        btnDeny.setBackgroundResource(denyBg);
        btnDeny.setVisibility(denyVisibility);

        btnConfirm.setOnClickListener(new ButtonListener(context, id, ButtonListener.TYPE_CONFIRM));
        btnDeny.setOnClickListener(new ButtonListener(context, id, ButtonListener.TYPE_DENY));

    }

    private String wrapText(String text, int length) {
        if(text.length() <= length) {
            return text;
        } else {
            try {
                char[] charArray = text.toCharArray();
                String textBuff = "";
                String wordBuff = "";
                for (int i = 0; i < length; i++) {
                    wordBuff += charArray[i];
                    if (charArray[i] == ' ') {
                        textBuff += wordBuff;
                        wordBuff = "";
                    }
                }
                textBuff += "...";
                return textBuff;
            } catch(ArrayIndexOutOfBoundsException aioobe) {
                return text;
            }
        }
    }

    private String wrapTimestamp(long timestamp) {
        String textBuff = "";
        if(timestamp >= 604800) {
            long h = timestamp / 604800;
            textBuff += h + (h > 1 ? " weken" : " week");
        } else if(timestamp >= 86400) {
            long h = timestamp / 86400;
            textBuff += h + (h > 1 ? " dagen" : " dag");
        } else if(timestamp >= 3600) {
            long h = timestamp / 3600;
            textBuff += h + (h > 1 ? " uren" : " uur");
        } else if(timestamp >= 60) {
            long h = timestamp / 60;
            textBuff += h + (h > 1 ? " minuten" : " minuut");
        } else {
            textBuff += "enkele ogenblikken";
        }
        textBuff+= " geleden";
        return textBuff;
    }

}