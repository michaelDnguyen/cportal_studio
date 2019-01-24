package com.dlvn.mcustomerportal.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.adapter.model.SingleChoiceModel;

import java.util.List;

public class SingleSpinnerAdapter
        extends ArrayAdapter<SingleChoiceModel>

{

    List<SingleChoiceModel> data;
    Context context;
    LayoutInflater inflater;

    public SingleSpinnerAdapter(Context context, List<SingleChoiceModel> objects) {
        super(context, R.layout.item_list_singlechoice, objects);
        this.context = context;
        this.data = objects;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public SingleChoiceModel getItem(int position) {
        return data.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_spinner_singlechoice, null);
            holder = new ViewHolder();
            holder.tvName = convertView.findViewById(R.id.tvName);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.tvName.setText(data.get(position).getName());
        holder.tvName.setPaintFlags(holder.tvName.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_spinner_singlechoice, null);
            holder = new ViewHolder();
            holder.tvName = convertView.findViewById(R.id.tvName);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.tvName.setText(data.get(position).getName());
        return convertView;
    }

    private class ViewHolder {
        TextView tvName;
    }

}
