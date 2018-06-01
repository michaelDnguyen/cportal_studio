package com.dlvn.mcustomerportal.adapter;

import java.util.List;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.adapter.model.NewsModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class NewsListAdapter extends BaseAdapter {

	List<NewsModel> data;
	Context context;

	public NewsListAdapter(Context context, List<NewsModel> objects) {
		this.context = context;
		this.data = objects;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public NewsModel getItem(int position) {
		return data.get(position);
	}
	
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		NewsModel item = data.get(position);

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.item_news, null);
			holder = new ViewHolder();

			holder.tvName = (TextView) convertView.findViewById(R.id.tvTitle);
			holder.tvDate = (TextView) convertView.findViewById(R.id.tvDate);

			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();

		if (item != null) {
			holder.tvName.setText(item.getTitle());
			holder.tvDate.setText(item.getDate());
		}
		return convertView;
	}

	private class ViewHolder {
		TextView tvName, tvDate;
	}

}
