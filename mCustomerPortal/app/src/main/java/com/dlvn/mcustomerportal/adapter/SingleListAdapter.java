package com.dlvn.mcustomerportal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.adapter.model.SingleChoiceModel;

import java.util.List;

public class SingleListAdapter
        extends ArrayAdapter<SingleChoiceModel>

{

    List<SingleChoiceModel> data;
    Context context;
    LayoutInflater inflater;

    public SingleListAdapter(Context context, int resource, List<SingleChoiceModel> objects) {
        super(context, resource, objects);
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
            convertView = inflater.inflate(R.layout.item_list_single, null);
            holder = new ViewHolder();
            holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
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
