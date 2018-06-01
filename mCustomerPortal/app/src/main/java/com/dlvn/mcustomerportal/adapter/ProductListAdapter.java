package com.dlvn.mcustomerportal.adapter;

import java.util.List;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.adapter.model.HomeItemModel;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ProductListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private static final String TAG = "ProductListAdapter";

	List<HomeItemModel> data;
	Context context;

	RequestOptions cropOptions;
	
	public ProductListAdapter(Context c, List<HomeItemModel> obj) {
		context = c;
		data = obj;
		
		cropOptions = new RequestOptions().centerCrop();
	}

	/**
	 * ViewHolder for header item
	 * 
	 * @author nn.tai
	 * @date Dec 5, 2017
	 */
	public class ProductViewHolder extends RecyclerView.ViewHolder {

		ImageView imvImage;
		TextView tvTitle, tvContent;

		public ProductViewHolder(View itemView) {
			super(itemView);
			imvImage = (ImageView) itemView.findViewById(R.id.imvImage);
			tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
			tvContent = (TextView) itemView.findViewById(R.id.tvContent);
		}

		public void bind(ProductViewHolder holder, final HomeItemModel item, final int postition) {
			holder.tvTitle.setText(item.get_title());
			holder.tvContent.setText(item.get_content());
			Glide.with(context).load(item.getImgPath()).apply(cropOptions).into(holder.imvImage);

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
		return position;
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		ProductViewHolder header = (ProductViewHolder) holder;
		header.bind(header, data.get(position), position);
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

		View v = LayoutInflater.from(context).inflate(R.layout.item_productlist, parent, false);
		return new ProductViewHolder(v);
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
