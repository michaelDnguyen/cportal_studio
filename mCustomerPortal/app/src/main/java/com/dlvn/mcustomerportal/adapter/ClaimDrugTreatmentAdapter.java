package com.dlvn.mcustomerportal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.common.Constant;
import com.dlvn.mcustomerportal.services.model.claims.TreatmentHistory;

import java.util.List;

public class ClaimDrugTreatmentAdapter extends BaseAdapter {

    List<TreatmentHistory> data;
    Context context;

    public ClaimDrugTreatmentAdapter(Context context, List<TreatmentHistory> objects) {
        this.context = context;
        this.data = objects;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public TreatmentHistory getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public void setData(List<TreatmentHistory> data) {
        if (this.data != data)
            this.data.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        TreatmentHistory item = data.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_claim_treatment, null);
            holder = new ViewHolder();

            holder.tvDrugName = convertView.findViewById(R.id.tvHospital);
            holder.tvDianogtic = convertView.findViewById(R.id.tvDianogtic);
            holder.tvType = convertView.findViewById(R.id.tvType);

            holder.tvType.setVisibility(View.GONE);
            holder.tvTime = convertView.findViewById(R.id.tvTime);
            holder.tvTime.setVisibility(View.GONE);

            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        if (item != null) {
            holder.tvDrugName.setText(item.getDrugName());
            holder.tvDianogtic.setText(item.getDiagnostic());
        }
        return convertView;
    }

    private class ViewHolder {
        TextView tvDrugName, tvDianogtic, tvType, tvTime;
    }

}
