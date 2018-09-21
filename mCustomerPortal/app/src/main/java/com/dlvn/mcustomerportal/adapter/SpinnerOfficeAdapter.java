package com.dlvn.mcustomerportal.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.adapter.model.SpinnerOfficeModel;

import java.util.List;

public class SpinnerOfficeAdapter extends ArrayAdapter<SpinnerOfficeModel> {

    Context context;
    List<SpinnerOfficeModel> data;
    float fontSize;

    public SpinnerOfficeAdapter(@NonNull Context c, @NonNull List<SpinnerOfficeModel> objects, float fontSize) {
        super(c, android.R.layout.simple_list_item_1, objects);
        context = c;
        data = objects;
        this.fontSize = fontSize;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView v = (TextView) super.getView(position, convertView, parent);

        if (v == null) {
            v = new TextView(context);
        }
        v.setTextColor(Color.WHITE);
        v.setTextSize(fontSize);
        v.setText(data.get(position).getTitle());
        return v;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.view_spinner_office_item, null);
        }

        TextView lbl = (TextView) convertView.findViewById(R.id.tvItem);
        lbl.setTextSize(fontSize);
        lbl.setText(data.get(position).getTitle());
        return convertView;
    }

    @Override
    public SpinnerOfficeModel getItem(int position) {
        return data.get(position);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    public List<SpinnerOfficeModel> getData() {
        return data;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
