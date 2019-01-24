package com.dlvn.mcustomerportal.adapter;

import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.adapter.model.BonusItemModel;
import com.dlvn.mcustomerportal.utils.myLog;
import com.dlvn.mcustomerportal.view.indicator.ScrollerViewPager;

import java.util.List;

public class SlidePagerAdapter extends PagerAdapter {

    private static final String TAG = "SlidePagerAdapter";

    List<BonusItemModel> data;
    Context context;
    LayoutInflater inflater;
    float ratioOfItem = 0.0f;

    private int mCurrentPosition = -1;
    Point sizeScreen;

    public SlidePagerAdapter(Context c, List<BonusItemModel> lst, float ratioWidth) {
        super();
        context = c;
        data = lst;
        inflater = LayoutInflater.from(context);
        ratioOfItem = ratioWidth;

        WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        sizeScreen = new Point();
        display.getSize(sizeScreen);
    }

    public SlidePagerAdapter(Context c, List<BonusItemModel> lst) {
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
        View v = inflater.inflate(R.layout.item_page_slide, null);
        final BonusItemModel item = data.get(position);
        final ViewHolder holder = new ViewHolder();

        holder.tvTitle = v.findViewById(R.id.tvTitle);
        holder.imvImage = v.findViewById(R.id.imvImage);

        // fill data
        if (item != null) {
            myLog.e(TAG, "instantiateItem " + item.get_title());
            holder.tvTitle.setText(item.get_title());
            Glide.with(context).load(item.getResourceID())
                    .apply(new RequestOptions()
                            .format(DecodeFormat.PREFER_ARGB_8888).override(Target.SIZE_ORIGINAL)).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull final Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    holder.imvImage.setImageDrawable(resource);
//                    holder.imvImage.post(new Runnable() {
//                        @Override
//                        public void run() {
////                            myLog.e(TAG, "ratio = " + ratioOfItem);
////                            int width = (int) (sizeScreen.x * (ratioOfItem + 0.05));
////                            int height = (int) (width * 0.4);
//                            int width = resource.getIntrinsicWidth();
//                            int height = resource.getIntrinsicHeight();
//                            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, height);
//
//                            holder.imvImage.setLayoutParams(param);
//                            holder.imvImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                            holder.imvImage.getParent().requestLayout();
//                        }
//                    });
                }
            });
//            Glide.with(context).load(item.getResourceID())
//                    .apply(new RequestOptions()
//                    .fitCenter()).into(holder.imvImage);

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
        myLog.e(TAG, "setPrimaryItem " + position);
        if (position != mCurrentPosition) {
            View item = (View) object;
            ScrollerViewPager pager = (ScrollerViewPager) container;

            if (item != null) {
                mCurrentPosition = position;
                notifyDataSetChanged();
                pager.measureCurrentView(item);
            }
        }
    }

    @Override
    public float getPageWidth(int position) {
        myLog.e(TAG, "getPageWidth *** ratio = " + ratioOfItem);
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
