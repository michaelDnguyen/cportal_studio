package com.dlvn.mcustomerportal.adapter;

import java.util.List;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.adapter.model.ElectricBillModel;
import com.dlvn.mcustomerportal.view.MyCustomDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Adapter danh sách hợp đồng
 * 
 * @author nn.tai
 * @date Dec 7, 2017
 */
public class ElectricBillListAdapter extends BaseAdapter {

	List<ElectricBillModel> lstData;
	Context context;

	public ElectricBillListAdapter(Context c, List<ElectricBillModel> data) {
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

	public void setData(List<ElectricBillModel> data) {
		if (lstData != data)
			lstData.addAll(data);
		notifyDataSetChanged();
	}

	public void reFreshData(List<ElectricBillModel> data) {
		if (lstData != data) {
			lstData.clear();
			lstData.addAll(data);
			notifyDataSetChanged();
		}
	}

	public List<ElectricBillModel> getData() {
		return lstData;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ElectricBillModel item = lstData.get(position);
		ViewHolder holder;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.item_electricbill, null);
			holder = new ViewHolder();

			holder.tvMaSoHD = (TextView) convertView.findViewById(R.id.tvSoHoaDon);
			holder.tvSoHopDong = (TextView) convertView.findViewById(R.id.tvSoHopDong);
			holder.tvKhachHang = (TextView) convertView.findViewById(R.id.tvKhachHang);
			holder.tvSoTien = (TextView) convertView.findViewById(R.id.tvAmount);
			holder.tvNgayLap = (TextView) convertView.findViewById(R.id.tvNgayLap);

			holder.btnPDF = (LinearLayout) convertView.findViewById(R.id.lloPDF);
			holder.btnXML = (LinearLayout) convertView.findViewById(R.id.lloXML);

			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();

		if (item != null) {
			holder.tvMaSoHD.setText("Mã hóa đơn: " + item.getMaHoaDon());
			holder.tvSoHopDong.setText("Số hợp đồng: " + item.getMaHopDong());
			holder.tvKhachHang.setText(item.getTenKhachHang());
			holder.tvSoTien.setText(item.getSoTienBH());
			holder.tvNgayLap.setText(item.getNgayLap());

			holder.btnPDF.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					MyCustomDialog.Builder builder = new MyCustomDialog.Builder(context);
					builder.setMessage(context.getString(R.string.message_alert_download_file))
							.setPositiveButton("Có", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();

								}
							}).setNegativeButton("Không", new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
								}
							});
					builder.create().show();
				}
			});

			holder.btnXML.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					MyCustomDialog.Builder builder = new MyCustomDialog.Builder(context);
					builder.setMessage(context.getString(R.string.message_alert_download_file))
							.setPositiveButton("Có", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();

								}
							}).setNegativeButton("Không", new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
								}
							});
					builder.create().show();
				}
			});
		}

		return convertView;
	}

	private class ViewHolder {
		TextView tvMaSoHD, tvSoHopDong, tvKhachHang, tvSoTien, tvNgayLap;
		LinearLayout btnPDF, btnXML;
	}
}
