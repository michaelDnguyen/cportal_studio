package com.dlvn.mcustomerportal.adapter;

import java.util.List;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.adapter.model.ProductDetailModel;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ProductListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "ProductListAdapter";

    public static final int ITEM_MAIN = 555;
    public static final int ITEM_NORMAL = 556;
    public static final int ITEM_FOOTER = 554;
    public static final int ITEM_TITLE = 550;

    List<ProductDetailModel> data;
    Context context;

    RequestOptions cropOptions;

    public ProductListAdapter(Context c, List<ProductDetailModel> obj) {
        context = c;
        data = obj;

        cropOptions = new RequestOptions().centerCrop();
    }

    /**
     * ViewHolder for header item
     * @author nn.tai
     * @date Dec 5, 2017
     */
    public class ProductViewHolder extends RecyclerView.ViewHolder {

        ImageView imvImage;
        TextView tvTitle, tvContent, tvDate, tvNext;

        public ProductViewHolder(View itemView) {
            super(itemView);
            imvImage = itemView.findViewById(R.id.imvImage);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvContent = itemView.findViewById(R.id.tvContent);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvNext = itemView.findViewById(R.id.tvNext);
        }

        public void bind(ProductViewHolder holder, final ProductDetailModel item, final int postition) {
            if (item != null) {
                holder.tvTitle.setText(item.getTitle());
                holder.tvContent.setText(item.getDescription());
                holder.tvDate.setText(item.getPublishDate());
//                Glide.with(context).load(item.getThumbnailURL()).apply(cropOptions).into(holder.imvImage);
            }

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
            return ITEM_MAIN;
        else if (position == 1)
            return ITEM_TITLE;
        else if (position >= data.size() - 1)
            return ITEM_FOOTER;
        else
            return ITEM_NORMAL;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ProductViewHolder header = (ProductViewHolder) holder;
        header.bind(header, data.get(position), position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == ITEM_MAIN) {
            View v = LayoutInflater.from(context).inflate(R.layout.item_productlist_main, parent, false);
            return new ProductViewHolder(v);
        } else {
            View v = LayoutInflater.from(context).inflate(R.layout.item_productlist_normal, parent, false);
            return new ProductViewHolder(v);
        }
    }

    public void setData(List<ProductDetailModel> obj) {
        if (this.data != obj) {
            this.data = obj;
            notifyItemRangeChanged(0, data.size());
        }
    }

    public List<ProductDetailModel> getData() {
        return data;
    }

    public void removeAt(int position) {
        data.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(0, data.size());
    }
}
