package com.dlvn.mcustomerportal.adapter;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.adapter.model.ContractModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Adapter danh sách hợp đồng
 * @author nn.tai
 * @date Dec 7, 2017
 */
public class ContractListAdapter extends BaseAdapter {

	List<ContractModel> lstData;
	Context context;

	public ContractListAdapter(Context c, List<ContractModel> data) {
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
	
	public void setData(List<ContractModel> data){
		if(lstData != data)
			lstData.addAll(data);
		notifyDataSetChanged();
	}
	
	public void reFreshData(List<ContractModel> data){
		if(lstData != data){
			lstData.clear();
			lstData.addAll(data);
			notifyDataSetChanged();
		}
	}
	
	public List<ContractModel> getData(){
		return lstData;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ContractModel item = lstData.get(position);
		ViewHolder holder;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.item_contract, null);
			holder = new ViewHolder();

			holder.tvStatus = (TextView) convertView.findViewById(R.id.tvStatus);
			holder.tvProposal = (TextView) convertView.findViewById(R.id.tvProposal);
			holder.tvProduct = (TextView) convertView.findViewById(R.id.tvProduct);
			holder.tvAmount = (TextView) convertView.findViewById(R.id.tvAmount);
			holder.tvActiveDate = (TextView) convertView.findViewById(R.id.tvActiveDate);
			holder.tvEndDate = (TextView) convertView.findViewById(R.id.tvEndDate);

			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();

		if (item != null) {
			if (item.isActive())
				holder.tvStatus.setText("Đang hiệu lực");
			else
				holder.tvStatus.setText("Hết hiệu lực");
			
			holder.tvProposal.setText(item.getSoHopDong());
			holder.tvProduct.setText(item.getTenSanPham());
			holder.tvAmount.setText(NumberFormat.getInstance(Locale.US).format(item.getAmount()) + " VND");
			holder.tvActiveDate.setText(item.getActiveDate());
			holder.tvEndDate.setText(item.getEndDate());
		}

		return convertView;
	}

	private class ViewHolder {
		TextView tvStatus, tvProposal, tvProduct, tvAmount, tvActiveDate, tvEndDate;
	}
}
