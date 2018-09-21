package com.dlvn.mcustomerportal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.adapter.model.OfficeAddressModel;
import com.dlvn.mcustomerportal.utils.Utilities;

import java.util.List;

public class OfficeListAdapter extends BaseAdapter {

    List<OfficeAddressModel> lstData;
    Context context;

    public OfficeListAdapter(Context c, List<OfficeAddressModel> data) {
        context = c;
        lstData = data;
    }

    @Override
    public int getCount() {
        return lstData.size();
    }

    @Override
    public Object getItem(int position) {
        return lstData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setData(List<OfficeAddressModel> data) {
        if (lstData != data)
            lstData.addAll(data);
        notifyDataSetChanged();
    }

    public void reFreshData(List<OfficeAddressModel> data) {
        if (lstData != data) {
            lstData.clear();
            lstData.addAll(data);
            notifyDataSetChanged();
        }
    }

    public List<OfficeAddressModel> getData() {
        return lstData;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        OfficeAddressModel item = lstData.get(position);
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_list_office, null);
            holder = new ViewHolder();

            holder.tvOffice = (TextView) convertView.findViewById(R.id.tvOffice);
            holder.tvDistance = (TextView) convertView.findViewById(R.id.tvDistance);

            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        if (item != null) {
            holder.tvOffice.setText(item.getName());
            holder.tvDistance.setText(Utilities.FormatDistance(item.getDistance()) + " km");
        }
        return convertView;
    }

    private class ViewHolder {
        TextView tvOffice, tvDistance;
    }
}
