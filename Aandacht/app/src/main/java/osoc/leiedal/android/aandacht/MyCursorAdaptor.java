package osoc.leiedal.android.aandacht;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.SQLOutput;
import java.util.GregorianCalendar;

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
        ImageView imgView = (ImageView) view.findViewById(R.id.imgView_status);
        TextView txtName = (TextView) view.findViewById(R.id.tatView_address);
        TextView txtUpdate = (TextView) view.findViewById(R.id.txtView_update);
        TextView txtTimestamp = (TextView) view.findViewById(R.id.txtView_timestamp);

        String name = cursor.getString(cursor.getColumnIndex(ReportsTable.COLUMN_ADDRESS));
        String update = cursor.getString(cursor.getColumnIndex(ReportsTable.COLUMN_DESCRIPTION));
        String status = cursor.getString(cursor.getColumnIndex(ReportsTable.COLUMN_STATUS));
        int timestamp = cursor.getInt(cursor.getColumnIndex(ReportsTable.COLUMN_TIMESTAMP_START));

        txtName.setText(name);
        txtUpdate.setText(wrapText(update, 40));
        txtTimestamp.setText(wrapTimestamp(System.currentTimeMillis() / 1000 - timestamp));

        int color = context.getResources().getColor(R.color.status_standard);
        if(status.equals(ReportsTable.STATUS_ACTIVE)) {
            color = context.getResources().getColor(R.color.status_active);
        } else if(status.equals(ReportsTable.STATUS_PENDING)) {
            color = context.getResources().getColor(R.color.status_pending);
        } else if(status.equals(ReportsTable.STATUS_DENIED)) {
            color = context.getResources().getColor(R.color.status_denied);
        } else if(status.equals(ReportsTable.STATUS_FINISHED)) {
            color = context.getResources().getColor(R.color.status_finished);
        }

        GradientDrawable background = (GradientDrawable) imgView.getBackground();
        background.setColor(color);
    }

    private String wrapText(String text, int length) {
        char[] charArray = text.toCharArray();
        String textBuff = "";
        String wordBuff = "";
        for(int i = 0; i < length; i++) {
            wordBuff += charArray[i];
            if(charArray[i] == ' ') {
                textBuff += wordBuff;
                wordBuff = "";
            }
        }
        textBuff += "[...]";
        return textBuff;
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
/*
        textBuff = addTimePart(timeBuff, 604800, textBuff, "en", "week", "weken");
        timeBuff %= 604800;
        textBuff = addTimePart(timeBuff, 86400, textBuff, "en", "dag", "dagen");
        timeBuff %= 86400;
        textBuff = addTimePart(timeBuff, 3600, textBuff, "en", "uur", "uren");
        timeBuff %= 3600;
        textBuff = addTimePart(timeBuff, 60, textBuff, "en", "minuut", "minuten");
        timeBuff %= 60;
        textBuff = addTimePart(timeBuff, 1, textBuff, "en", "seconde", "seconden");
        timeBuff %= 1;
        textBuff += " geleden";
        return textBuff;*/
    }

    private String addTimePart(long time, long threshold, String target, String and, String singular, String plural) {
        String buff = "";
        if(time >= threshold) {
            long h = time / threshold;
            buff += (target.equals("") ? "" : " " + and + " ") + h + " " + (h > 1 ? plural : singular);
        }
        return target += buff;
    }

}
