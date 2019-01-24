package com.dlvn.mcustomerportal.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.adapter.model.BonusItemModel;
import com.dlvn.mcustomerportal.utils.myLog;

import java.util.List;

public class SlideListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "SlideListAdapter";

    List<BonusItemModel> data;
    Context context;

    RequestOptions cropOptions;

    public SlideListAdapter(Context c, List<BonusItemModel> obj) {
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
    public class SlideViewHolder extends RecyclerView.ViewHolder {

        ImageView imvImage;
        TextView tvTitle;

        public SlideViewHolder(View itemView) {
            super(itemView);
            imvImage = itemView.findViewById(R.id.imvImage);
            tvTitle = itemView.findViewById(R.id.tvTitle);
        }

        public void bind(final SlideViewHolder holder, final BonusItemModel item, final int postition) {

            myLog.e(TAG, item.get_title() + " *** " + item.get_content());
            holder.tvTitle.setText(item.get_title());

            // resize of layout/image
//            holder.imvImage.post(new Runnable() {
//
//                @Override
//                public void run() {
//
//                    int width = (int) (sizeScreen.x * ratioWidth);
//                    int height = (int) (width * 0.45);
//
//                    LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(width, height);
//                    holder.imvImage.setLayoutParams(param);
//
//                    LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
//                    holder.tvTitle.setLayoutParams(param2);
//
//                    holder.imvImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
//                    holder.imvImage.getParent().requestLayout();
//
//                    Glide.with(context).load(item.getResourceID()).into(holder.imvImage);
//                }
//            });

            Glide.with(context).load(item.getResourceID()).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull final Drawable resource, @Nullable Transition<? super Drawable> transition) {

                    holder.imvImage.setImageDrawable(resource);
                    holder.imvImage.post(new Runnable() {
                        @Override
                        public void run() {

                            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            holder.imvImage.setLayoutParams(param);

                            int width = resource.getIntrinsicWidth();
                            LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT);
                            holder.tvTitle.setLayoutParams(param2);

                            holder.imvImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                            holder.imvImage.getParent().requestLayout();
                        }
                    });
                }
            });

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
        SlideViewHolder header = (SlideViewHolder) holder;
        header.bind(header, data.get(position), position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.item_page_slide, parent, false);
        return new SlideViewHolder(v);
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
