package com.dlvn.mcustomerportal.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.adapter.model.NotificationModel;
import com.dlvn.mcustomerportal.services.model.response.CPPolicy;

import java.util.List;

public class NotificationListAdapter extends ArrayAdapter<NotificationModel> {

    List<NotificationModel> data;
    Context context;
    LayoutInflater inflater;

    public NotificationListAdapter(Context context, int resource, List<NotificationModel> objects) {
        super(context, resource, objects);
        this.context = context;
        this.data = objects;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(List<NotificationModel> dta) {
        if (data != dta)
            data.addAll(dta);
        notifyDataSetChanged();
    }

    public void reFreshData(List<NotificationModel> dta) {
        if (data != dta) {
            data.clear();
            data.addAll(dta);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public NotificationModel getItem(int position) {
        return data.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        NotificationModel item = data.get(position);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_notification, null);
            holder = new ViewHolder();

            holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            holder.tvDate = (TextView) convertView.findViewById(R.id.tvDate);

            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        if (item != null) {
            holder.tvName.setText(item.getTitle() + "\n" + item.getContent());
            holder.tvDate.setText(item.getCreateDate());
            if (item.isRead())
                holder.tvName.setTextColor(Color.GRAY);
            else
                holder.tvName.setTextColor(Color.RED);
        }
        return convertView;
    }

    private class ViewHolder {
        TextView tvName, tvDate;
    }

}
