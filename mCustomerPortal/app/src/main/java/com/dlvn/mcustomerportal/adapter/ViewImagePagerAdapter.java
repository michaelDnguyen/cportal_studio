package com.dlvn.mcustomerportal.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.adapter.model.HorizontalRecyclerItemModel;
import com.dlvn.mcustomerportal.common.Constant;
import com.dlvn.mcustomerportal.utils.myLog;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class ViewImagePagerAdapter extends PagerAdapter {

    private static final String TAG = "ViewImagePagerAdapter";

    Context context;
    LayoutInflater mLayoutInflater;
    List<HorizontalRecyclerItemModel> lstData = new ArrayList<>();

    public ViewImagePagerAdapter(Context context, List<HorizontalRecyclerItemModel> data) {
        this.context = context;
        lstData.addAll(data);
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (int i = lstData.size() - 1; i >= 0; i--)
            if (lstData.get(i).getStatus() == HorizontalRecyclerItemModel.STATE_ADD_IMAGE) {
                lstData.remove(i);
                break;
            }
    }

    @Override
    public int getCount() {
        return lstData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        HorizontalRecyclerItemModel item = lstData.get(position);

        View itemView = mLayoutInflater.inflate(R.layout.item_view_image_pager, container, false);

        final ImageView imageView = itemView.findViewById(R.id.imvImage);
        final ProgressBar progress = itemView.findViewById(R.id.progressBar);

        if (item.getStatus() != HorizontalRecyclerItemModel.STATE_ADD_IMAGE) {
            String filePath = item.getDocItem().getPath();

            progress.setVisibility(View.VISIBLE);
            if (MimeTypeMap.getFileExtensionFromUrl(filePath).equalsIgnoreCase("tif")) {
                filePath = Constant.URL_FILE + filePath;
                myLog.e(TAG, filePath);

                Glide.with(context).asBitmap().load(filePath).apply(new RequestOptions().override(imageView.getWidth(), imageView.getHeight()))
                        .listener(new RequestListener<Bitmap>() {

                            @Override
                            public boolean onLoadFailed(GlideException arg0, Object arg1, Target<Bitmap> arg2,
                                                        boolean arg3) {
                                progress.setVisibility(View.GONE);
                                myLog.e("Load Image Failed!");
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Bitmap arg0, Object arg1, Target<Bitmap> arg2,
                                                           DataSource arg3, boolean arg4) {
                                progress.setVisibility(View.GONE);
                                myLog.e("Load Image Ready = " + arg0.getByteCount() + " Byte");
                                return false;
                            }
                        }).into(imageView);
            } else {
                Glide.with(context).asBitmap().load(new File(filePath)).apply(new RequestOptions().override(imageView.getWidth(), imageView.getHeight())).listener(new RequestListener<Bitmap>() {

                    @Override
                    public boolean onLoadFailed(GlideException arg0, Object arg1, Target<Bitmap> arg2, boolean arg3) {
                        progress.setVisibility(View.GONE);
                        myLog.e(TAG, "Load Image Failed!");
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap arg0, Object arg1, Target<Bitmap> arg2, DataSource arg3,
                                                   boolean arg4) {
                        progress.setVisibility(View.GONE);
                        myLog.e(TAG, "Load Image Ready = " + arg0.getByteCount());
                        return false;
                    }
                }).into(imageView);
            }
        }
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}
