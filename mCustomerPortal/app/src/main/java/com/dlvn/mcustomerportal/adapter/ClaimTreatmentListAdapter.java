package com.dlvn.mcustomerportal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.adapter.model.NewsModel;
import com.dlvn.mcustomerportal.common.Constant;
import com.dlvn.mcustomerportal.services.model.claims.TreatmentHistory;

import java.util.List;

public class ClaimTreatmentListAdapter extends BaseAdapter {

    List<TreatmentHistory> data;
    Context context;

    public ClaimTreatmentListAdapter(Context context, List<TreatmentHistory> objects) {
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

            holder.tvHospital = convertView.findViewById(R.id.tvHospital);
            holder.tvType = convertView.findViewById(R.id.tvType);
            holder.tvTime = convertView.findViewById(R.id.tvTime);
            holder.tvDianogtic = convertView.findViewById(R.id.tvDianogtic);

            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        if (item != null) {
            holder.tvHospital.setText(item.getTreatmentHospital());
            holder.tvTime.setText(item.getTreatmentDateFrom() + " - " + item.getTreatmentDateTo());
            holder.tvDianogtic.setText(item.getDiagnostic());
            if (item.getPatientType().equalsIgnoreCase(Constant.CLAIMS_TREATMENT_INPATIENT))
                holder.tvType.setText("Nội trú");
            else if (item.getPatientType().equalsIgnoreCase(Constant.CLAIMS_TREATMENT_OUTPATIENT))
                holder.tvType.setText("Ngoại trú");
        }
        return convertView;
    }

    private class ViewHolder {
        TextView tvHospital, tvType, tvTime, tvDianogtic;
    }

}
