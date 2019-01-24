package com.dlvn.mcustomerportal.adapter;

import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.adapter.model.VideoDetails;
import com.dlvn.mcustomerportal.utils.myLog;

import java.util.List;

public class VideoListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "VideoListAdapter";

    List<VideoDetails> data;
    Context context;

    RequestOptions cropOptions;
    Point sizeScreen;

    public VideoListAdapter(Context c, List<VideoDetails> obj) {
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
    public class SlideViewHolder extends RecyclerView.ViewHolder {

        ImageView imvImage;
        TextView tvTitle;

        public SlideViewHolder(View itemView) {
            super(itemView);
            imvImage = itemView.findViewById(R.id.imvImage);
            tvTitle = itemView.findViewById(R.id.tvTitle);
        }

        public void bind(final SlideViewHolder holder, final VideoDetails item, final int postition) {

            myLog.e(TAG,item.getVideoName() + " *** " + item.getVideoId());
            holder.tvTitle.setText(item.getVideoName());

            // resize of layout/image
            holder.imvImage.post(new Runnable() {

                @Override
                public void run() {

                    int width = sizeScreen.x;
                    int size = (int) (width * 0.4);

                    LinearLayout.LayoutParams param = new LinearLayout.LayoutParams((int) size, (int) size);
//					lloContent.setLayoutParams(param);

                    imvImage.setLayoutParams(param);
                    imvImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    imvImage.getParent().requestLayout();

                    Glide.with(context).load(item.getURL()).into(holder.imvImage);
                }
            });

            // Glide.with(context).asBitmap().load(item.getImgPath()).apply(cropOptions).listener(new
            // RequestListener<Bitmap>() {
            //
            // @Override
            // public boolean onLoadFailed(GlideException arg0, Object arg1,
            // Target<Bitmap> arg2, boolean arg3) {
            // myLog.e("Load Image Failed!");
            // return false;
            // }
            //
            // @Override
            // public boolean onResourceReady(Bitmap arg0, Object arg1,
            // Target<Bitmap> arg2, DataSource arg3,
            // boolean arg4) {
            // holder.imvImage.setImageBitmap(arg0);
            // myLog.e("Load Image Ready!");
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
        SlideViewHolder header = (SlideViewHolder) holder;
        header.bind(header, data.get(position), position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.item_page_slide, parent, false);
        return new SlideViewHolder(v);
    }

    public void setData(List<VideoDetails> obj) {
        if (this.data != obj) {
            this.data = obj;
            notifyItemRangeChanged(0, data.size());
        }
    }

    public List<VideoDetails> getData() {
        return data;
    }

    public void removeAt(int position) {
        data.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(0, data.size());
    }
}
