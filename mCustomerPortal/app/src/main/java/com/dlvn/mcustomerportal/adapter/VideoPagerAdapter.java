package com.dlvn.mcustomerportal.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.activity.prototype.VideoYoutubeActivity;
import com.dlvn.mcustomerportal.adapter.model.VideoDetails;
import com.dlvn.mcustomerportal.utils.myLog;

import java.util.List;

public class VideoPagerAdapter extends PagerAdapter {

    List<VideoDetails> data;
    Context context;
    LayoutInflater inflater;
    float ratioOfItem = 1.0f;

    private int mCurrentPosition = -1;
    Point sizeScreen;

    public VideoPagerAdapter(Context c, List<VideoDetails> lst, float ratioWidth) {
        super();
        context = c;
        data = lst;
        ratioOfItem = ratioWidth;
        inflater = LayoutInflater.from(context);

        WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        sizeScreen = new Point();
        display.getSize(sizeScreen);
    }

    public VideoPagerAdapter(Context c, List<VideoDetails> lst) {
        super();
        context = c;
        data = lst;
        ratioOfItem = 1.0f;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // get views
        View v = inflater.inflate(R.layout.item_page_video, null);
        final VideoDetails item = data.get(position);
        final ViewHolder holder = new ViewHolder();

        holder.tvTitle = v.findViewById(R.id.tvVideoName);
        holder.imvImage = v.findViewById(R.id.imvThumb);
        holder.imvImage.post(new Runnable() {
            @Override
            public void run() {
                int width = (int) (sizeScreen.x * ratioOfItem);
                int height = (int) (width * 0.72);
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(width, height);

                holder.imvImage.setLayoutParams(param);
//                holder.imvImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                holder.imvImage.getParent().requestLayout();
            }
        });

        // fill data
        if (item != null) {
            holder.tvTitle.setText(item.getVideoName());
            Glide.with(context).load(item.getURL())
                    .apply(new RequestOptions()
                            .centerInside()
                            .format(DecodeFormat.PREFER_ARGB_8888)).into(holder.imvImage);

            holder.imvImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myLog.e("Adapter Videolink = " + item.getVideoId());
                    Intent intent = new Intent(context, VideoYoutubeActivity.class);
                    intent.putExtra(VideoYoutubeActivity.INT_VIDEO_ID, item.getVideoId());
                    context.startActivity(intent);
                }
            });
        }

        container.addView(v);
        return v;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.setPrimaryItem(container, position, object);
//        if (position != mCurrentPosition) {
//            View item = (View) object;
//            ScrollerViewPager pager = (ScrollerViewPager) container;
//
//            if (item != null) {
//                mCurrentPosition = position;
//                pager.measureCurrentView(item);
//            }
//        }
    }

    @Override
    public float getPageWidth(int position) {
        return (ratioOfItem);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        // TODO Auto-generated method stub
        super.finishUpdate(container);
    }

    @Override
    public void startUpdate(ViewGroup container) {
        // TODO Auto-generated method stub
        super.startUpdate(container);
    }

    private class ViewHolder {
        TextView tvTitle;
        ImageView imvImage;
    }
}
