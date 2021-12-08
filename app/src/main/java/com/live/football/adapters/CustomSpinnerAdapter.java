package com.hoanmy.football.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.ctrlplusz.anytextview.AnyTextView;
import com.hoanmy.football.R;

import java.util.List;

public class CustomSpinnerAdapter extends ArrayAdapter<String> {
    private List<String> objects;
    private Context context;

    public CustomSpinnerAdapter(@NonNull Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        this.objects = objects;
        this.context = context;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.item_list_server, parent, false);
        AnyTextView label = (AnyTextView) row.findViewById(R.id.txt_server);
        label.setText(objects.get(position));

        if (position == 0) {//Special style for dropdown header
            label.setTextColor(context.getResources().getColor(R.color.white));
        }

        return row;
    }
}
