package com.dlvn.mcustomerportal.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.adapter.model.QuaTrinhTichLuyModel;

import java.util.List;

import static android.media.CamcorderProfile.get;

public class QuaTrinhTichLuyAdapter extends ArrayAdapter<QuaTrinhTichLuyModel> {

    List<QuaTrinhTichLuyModel> data;
    Context context;
    LayoutInflater inflater;

    public QuaTrinhTichLuyAdapter(@NonNull Context context, int resource, List<QuaTrinhTichLuyModel> objects) {
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
    public QuaTrinhTichLuyModel getItem(int position) {
        return data.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        QuaTrinhTichLuyModel item = data.get(position);

        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_qua_trinh_tich_luy, null);
            holder = new ViewHolder();

            holder.tvDate = (TextView) convertView.findViewById(R.id.tvDate);
            holder.tvCode = (TextView) convertView.findViewById(R.id.tvCode);
            holder.tvStatus = (TextView) convertView.findViewById(R.id.tvStatus);
            holder.tvPoint = (TextView) convertView.findViewById(R.id.tvPoint);

            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        if (item != null) {
            holder.tvCode.setText(item.getCode());
            holder.tvDate.setText(item.getDate());
            holder.tvStatus.setText(item.getStatus());
            holder.tvPoint.setText(item.getPoint());
        }
        return convertView;
    }

    private class ViewHolder{
        TextView tvCode, tvDate, tvStatus, tvPoint;
    }
}
