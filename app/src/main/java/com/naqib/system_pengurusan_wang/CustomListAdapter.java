package com.naqib.system_pengurusan_wang;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class CustomListAdapter extends ArrayAdapter<String> {
    private Context context;
    private ArrayList<String> items;

    public CustomListAdapter(Context context, int resource, ArrayList<String> items) {
        super(context, resource, items);
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        }

        // Set the text for the item in the custom layout
        TextView itemTextView = convertView.findViewById(R.id.itemText);
        itemTextView.setText(items.get(position));

        return convertView;
    }
}