package com.dlvn.mcustomerportal.adapter;

import java.util.List;

import com.bumptech.glide.Glide;
import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.adapter.model.HomeItemModel;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private static final String TAG = "HomeListAdapter";

	public static final int ITEM_HEADER = 666;
	public static final int ITEM_MIDDLE = 665;
	public static final int ITEM_FOOTER = 664;

	List<HomeItemModel> data;
	Context context;

	public HomeListAdapter(Context c, List<HomeItemModel> obj) {
		context = c;
		data = obj;
	}

	/**
	 * ViewHolder for header item
	 * 
	 * @author nn.tai
	 * @date Dec 5, 2017
	 */
	public class HomeHeadViewHolder extends RecyclerView.ViewHolder {

		ImageView imvImage;
		TextView tvTitle, tvContent;

		public HomeHeadViewHolder(View itemView) {
			super(itemView);
			imvImage = (ImageView) itemView.findViewById(R.id.imvImage);
			tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
			tvContent = (TextView) itemView.findViewById(R.id.tvContent);
		}

		public void bind(HomeHeadViewHolder holder, final HomeItemModel item, final int postition) {
			holder.tvTitle.setText(item.get_title());
			holder.tvContent.setText(item.get_content());
			Glide.with(context).load(item.getImgPath()).into(holder.imvImage);

			holder.itemView.setTag(postition);
		}

	}

	@Override
	public int getItemCount() {
		return data.size();
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemViewType(int position) {
		if (position == 0)
			return ITEM_HEADER;
		else if (position >= data.size() - 1)
			return ITEM_FOOTER;
		else
			return ITEM_MIDDLE;
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		HomeHeadViewHolder header = (HomeHeadViewHolder) holder;
		header.bind(header, data.get(position), position);
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

		if (viewType == ITEM_HEADER) {
			View v = LayoutInflater.from(context).inflate(R.layout.item_head_home, parent, false);
			return new HomeHeadViewHolder(v);
		} else {
			View v = LayoutInflater.from(context).inflate(R.layout.item_middle_home, parent, false);
			return new HomeHeadViewHolder(v);
		}
	}

	public void setData(List<HomeItemModel> obj) {
		if (this.data != obj) {
			this.data = obj;
			notifyItemRangeChanged(0, data.size());
		}
	}

	public List<HomeItemModel> getData() {
		return data;
	}

	public void removeAt(int position) {
		data.remove(position);
		notifyItemRemoved(position);
		notifyItemRangeChanged(0, data.size());
	}
}
