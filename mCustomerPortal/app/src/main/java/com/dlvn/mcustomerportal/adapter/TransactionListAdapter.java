package com.dlvn.mcustomerportal.adapter;

import java.util.List;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.adapter.model.TransactionModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Adapter
 * 
 * @author nn.tai
 * @date Jan 4, 2018
 */
public class TransactionListAdapter extends BaseAdapter {

	List<TransactionModel> lstData;
	Context context;

	public TransactionListAdapter(Context c, List<TransactionModel> data) {
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

	public void setData(List<TransactionModel> data) {
		if (lstData != data)
			lstData.addAll(data);
		notifyDataSetChanged();
	}

	public void reFreshData(List<TransactionModel> data) {
		if (lstData != data) {
			lstData.clear();
			lstData.addAll(data);
			notifyDataSetChanged();
		}
	}

	public List<TransactionModel> getData() {
		return lstData;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		TransactionModel item = lstData.get(position);
		ViewHolder holder;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.item_transaction, null);
			holder = new ViewHolder();

			holder.tvMaGD = (TextView) convertView.findViewById(R.id.tvMaGiaoDich);
			holder.tvSoTien = (TextView) convertView.findViewById(R.id.tvSoTien);
			holder.tvSoHD = (TextView) convertView.findViewById(R.id.tvSoHopDong);
			holder.tvTrangThai = (TextView) convertView.findViewById(R.id.tvTinhTrang);
			holder.tvNgayGD = (TextView) convertView.findViewById(R.id.tvNgayGiaoDich);
			holder.lloParent = (LinearLayout) convertView.findViewById(R.id.lloParent);

			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();

		if(position%2 == 0)
			holder.lloParent.setBackgroundResource(R.color.row_list_color_other);
		else
			holder.lloParent.setBackgroundResource(R.color.white);
		
		if (item != null) {

			holder.tvMaGD.setText("Mã giao dịch " + item.getMaGiaoDich());
			holder.tvSoHD.setText("Số hợp đồng " + item.getSoHD());
			holder.tvSoTien.setText(item.getSoTien() + " VND");
			holder.tvTrangThai.setText(item.getTrangThai());
			holder.tvNgayGD.setText(item.getNgayGD());
		}

		return convertView;
	}

	private class ViewHolder {
		TextView tvMaGD, tvSoHD, tvSoTien, tvTrangThai, tvNgayGD;
		LinearLayout lloParent;
	}
}
