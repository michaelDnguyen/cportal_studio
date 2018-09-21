package com.dlvn.mcustomerportal.adapter;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.adapter.model.ContractModel;
import com.dlvn.mcustomerportal.services.model.response.CPPolicy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Adapter danh sách hợp đồng
 * @author nn.tai
 * @date Dec 7, 2017
 */
public class ContractListAdapter extends BaseAdapter {

	List<CPPolicy> lstData;
	Context context;

	public ContractListAdapter(Context c, List<CPPolicy> data) {
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
	
	public void setData(List<CPPolicy> data){
		if(lstData != data)
			lstData.addAll(data);
		notifyDataSetChanged();
	}
	
	public void reFreshData(List<CPPolicy> data){
		if(lstData != data){
			lstData.clear();
			lstData.addAll(data);
			notifyDataSetChanged();
		}
	}
	
	public List<CPPolicy> getData(){
		return lstData;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		CPPolicy item = lstData.get(position);
		ViewHolder holder;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.item_contract, null);
			holder = new ViewHolder();

			holder.tvStatus = (TextView) convertView.findViewById(R.id.tvStatus);
			holder.tvProposal = (TextView) convertView.findViewById(R.id.tvProposal);
			holder.tvProduct = (TextView) convertView.findViewById(R.id.tvProduct);
			holder.tvAmount = (TextView) convertView.findViewById(R.id.tvAmount);
			holder.imvProduct = (ImageView) convertView.findViewById(R.id.imvProduct);

			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();

		if (item != null) {
			if (item.getPolicyStatus().contains("1"))
				holder.tvStatus.setText("Đang hiệu lực");
			else
				holder.tvStatus.setText("Hết hiệu lực");
			
			holder.tvProposal.setText(item.getPolicyID());
			
			/**
             * From line 100 to 104, used to get rid of blank spaces in the title from server (An Tam Hung Thich to center)
             */
            try {
                holder.tvProduct.setText(item.getProductName().trim());
            } catch (Exception e) {
                Toast.makeText(context, "null value at tvProduct", Toast.LENGTH_SHORT).show();
            }

			try {
				holder.tvAmount.setText(NumberFormat.getInstance(Locale.US).format(Double.parseDouble(item.getFaceAmount())) + " VND");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return convertView;
	}

	private class ViewHolder {
		TextView tvStatus, tvProposal, tvProduct, tvAmount;
		ImageView imvProduct;
	}
}
