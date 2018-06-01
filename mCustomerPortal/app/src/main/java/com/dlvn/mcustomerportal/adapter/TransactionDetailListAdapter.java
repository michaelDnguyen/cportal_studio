package com.dlvn.mcustomerportal.adapter;

import java.util.List;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.adapter.model.TransactionDetailModel;

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
public class TransactionDetailListAdapter extends BaseAdapter {

	List<TransactionDetailModel> lstData;
	String soHopDong, ngayGiaoDich;
	Context context;

	public TransactionDetailListAdapter(Context c, List<TransactionDetailModel> data, String soHD, String ngayGD) {
		context = c;
		lstData = data;
		soHopDong = soHD;
		ngayGiaoDich = ngayGD;
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

	public void setData(List<TransactionDetailModel> data) {
		if (lstData != data)
			lstData.addAll(data);
		notifyDataSetChanged();
	}

	public void reFreshData(List<TransactionDetailModel> data) {
		if (lstData != data) {
			lstData.clear();
			lstData.addAll(data);
			notifyDataSetChanged();
		}
	}

	public List<TransactionDetailModel> getData() {
		return lstData;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		TransactionDetailModel item = lstData.get(position);
		ViewHolder holder;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.item_transaction_detail, null);
			holder = new ViewHolder();

			holder.tvSoHD = (TextView) convertView.findViewById(R.id.tvSoHD);
			holder.tvSoTien = (TextView) convertView.findViewById(R.id.tvSoTien);
			holder.tvLoaiGD = (TextView) convertView.findViewById(R.id.tvLoaiGD);
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

			holder.tvSoHD.setText("Số hợp đồng " + soHopDong);
			holder.tvLoaiGD.setText(item.getLoaiGD());
			holder.tvSoTien.setText(item.getSoTien());
			holder.tvTrangThai.setText(item.getTrangThai());
			holder.tvNgayGD.setText(ngayGiaoDich);
		}

		return convertView;
	}

	private class ViewHolder {
		TextView tvSoHD, tvLoaiGD, tvSoTien, tvTrangThai, tvNgayGD;
		LinearLayout lloParent;
	}
}
