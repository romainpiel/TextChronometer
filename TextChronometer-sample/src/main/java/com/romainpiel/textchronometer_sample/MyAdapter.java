package com.romainpiel.textchronometer_sample;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.romainpiel.textchronometer.TextChronometer;

import java.util.ArrayList;

/**
 * MPme
 * User: romainpiel
 * Date: 22/07/2013
 * Time: 09:24
 */
public class MyAdapter extends ArrayAdapter<Entry> {

    private ArrayList<Entry> entries;
    private Activity activity;

    public MyAdapter(Activity a, int textViewResourceId, ArrayList<Entry> entries) {
        super(a, textViewResourceId, entries);
        this.entries = entries;
        this.activity = a;
    }

    public static class ViewHolder {
        public TextView item1;
        public TextChronometer item2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ViewHolder holder;
        if (v == null) {
            LayoutInflater vi =
                    (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.my_item, null);
            holder = new ViewHolder();
            holder.item1 = (TextView) v.findViewById(R.id.title);
            holder.item2 = (TextChronometer) v.findViewById(R.id.timestamp);
            v.setTag(holder);
        } else
            holder = (ViewHolder) v.getTag();

        final Entry entry = entries.get(position);
        if (entry != null) {
            holder.item1.setText(entry.title);
            holder.item2.setTime(entry.time);
        }
        return v;
    }

}
