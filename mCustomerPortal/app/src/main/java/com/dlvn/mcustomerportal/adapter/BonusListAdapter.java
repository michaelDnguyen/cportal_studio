package com.dlvn.mcustomerportal.adapter;

import java.util.List;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.adapter.model.BonusItemModel;
import com.dlvn.mcustomerportal.adapter.model.HomeItemModel;

import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class BonusListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private static final String TAG = "BonusListAdapter";

	List<BonusItemModel> data;
	Context context;

	RequestOptions cropOptions;
	Point sizeScreen;

	public BonusListAdapter(Context c, List<BonusItemModel> obj) {
		context = c;
		data = obj;

		cropOptions = new RequestOptions().centerCrop();

		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		sizeScreen = new Point();
		display.getSize(sizeScreen);
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

		public void bind(final ProductViewHolder holder, final BonusItemModel item, final int postition) {
			holder.tvTitle.setText(item.get_title());
			holder.tvContent.setText(item.get_content());

			// resize of imageview
			holder.imvImage.post(new Runnable() {

				@Override
				public void run() {

					int width = sizeScreen.x;
					int size = (width - 20) / 2;

					RelativeLayout.LayoutParams param = new LayoutParams((int) size, (int) size);
					imvImage.setLayoutParams(param);
					imvImage.getParent().requestLayout();

					Glide.with(context).load(item.getResourceID()).into(holder.imvImage);
				}
			});

			// Glide.with(context).asBitmap().load(item.getImgPath()).apply(cropOptions).listener(new
			// RequestListener<Bitmap>() {
			//
			// @Override
			// public boolean onLoadFailed(GlideException arg0, Object arg1,
			// Target<Bitmap> arg2, boolean arg3) {
			// myLog.E("Load Image Failed!");
			// return false;
			// }
			//
			// @Override
			// public boolean onResourceReady(Bitmap arg0, Object arg1,
			// Target<Bitmap> arg2, DataSource arg3,
			// boolean arg4) {
			// holder.imvImage.setImageBitmap(arg0);
			// myLog.E("Load Image Ready!");
			// return false;
			// }
			// });

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

		View v = LayoutInflater.from(context).inflate(R.layout.item_bonuslist, parent, false);
		return new ProductViewHolder(v);
	}

	public void setData(List<BonusItemModel> obj) {
		if (this.data != obj) {
			this.data = obj;
			notifyItemRangeChanged(0, data.size());
		}
	}

	public List<BonusItemModel> getData() {
		return data;
	}

	public void removeAt(int position) {
		data.remove(position);
		notifyItemRemoved(position);
		notifyItemRangeChanged(0, data.size());
	}
}
