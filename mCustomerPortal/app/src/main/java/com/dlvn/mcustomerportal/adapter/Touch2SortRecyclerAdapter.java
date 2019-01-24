package com.dlvn.mcustomerportal.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.adapter.listener.ItemTouchHelperAdapter;
import com.dlvn.mcustomerportal.adapter.listener.ItemTouchHelperViewHolder;
import com.dlvn.mcustomerportal.adapter.listener.OnRecycleModelChangedListener;
import com.dlvn.mcustomerportal.adapter.listener.OnStartDragListener;
import com.dlvn.mcustomerportal.adapter.model.HorizontalRecyclerItemModel;
import com.dlvn.mcustomerportal.common.Constant;
import com.dlvn.mcustomerportal.database.DataClient;
import com.dlvn.mcustomerportal.utils.myLog;
import com.dlvn.mcustomerportal.view.GridImageItemView;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class Touch2SortRecyclerAdapter extends RecyclerView.Adapter<Touch2SortRecyclerAdapter.RecyclerHolder>
        implements ItemTouchHelperAdapter {

    private static final String TAG = "Touch2SortRecyclerAdapter";

    List<HorizontalRecyclerItemModel> data;
    Context context;

    private OnStartDragListener mDragStartListener;
    private OnRecycleModelChangedListener mListChangedListener;

    Point sizeScreen;

    public Touch2SortRecyclerAdapter(Context context, List<HorizontalRecyclerItemModel> data, OnStartDragListener dragLlistener,
                                     OnRecycleModelChangedListener listChangedListener) {
        this.data = data;
        this.context = context;

        mDragStartListener = dragLlistener;
        mListChangedListener = listChangedListener;

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        sizeScreen = new Point();
        display.getSize(sizeScreen);
    }

    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_sort, parent, false);

        return new RecyclerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RecyclerHolder holder, final int position) {

        holder.bind(holder, data.get(position), position);

        holder.rlHandle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    mDragStartListener.onStartDrag(holder);
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setData(List<HorizontalRecyclerItemModel> data) {
        this.data = data;
        notifyItemRangeChanged(0, data.size());
    }

    public List<HorizontalRecyclerItemModel> getData() {
        return data;
    }

    public void removeAt(int position) {
        data.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(0, data.size());
    }

    public class RecyclerHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {

        ImageView imvDelete;
        GridImageItemView imageView;
        RelativeLayout rlHandle;
        TextView tvSTT;

        ProgressBar progress;

        public RecyclerHolder(View view) {
            super(view);
            imageView = (GridImageItemView) view.findViewById(R.id.imvImage);
            imvDelete = (ImageView) view.findViewById(R.id.imvDelete);
            progress = (ProgressBar) view.findViewById(R.id.progressBar);
            rlHandle = (RelativeLayout) view.findViewById(R.id.rlHandle);
            tvSTT = (TextView) view.findViewById(R.id.tvSTT);
        }

        public void bind(final RecyclerHolder holder, final HorizontalRecyclerItemModel item, final int postition) {

            holder.tvSTT.setText("Trang " + (postition + 1));

            if (item.getStatus() != HorizontalRecyclerItemModel.STATE_ADD_IMAGE) {
                String filePath = item.getDocItem().getPath();

                // resize of imageview
                holder.imageView.post(new Runnable() {

                    @Override
                    public void run() {
                        int width = sizeScreen.x;
                        int size = (width - 100) / 3;

                        LayoutParams param = new LayoutParams((int) size, (int) (size * 1.3));
                        imageView.setLayoutParams(param);
                        imageView.getParent().requestLayout();
                    }
                });

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
                    Glide.with(context).asBitmap().load(new File(filePath)).apply(new RequestOptions().override(imageView.getWidth(), imageView.getHeight()))
                            .listener(new RequestListener<Bitmap>() {

                                @Override
                                public boolean onLoadFailed(GlideException arg0, Object arg1, Target<Bitmap> arg2, boolean arg3) {
                                    progress.setVisibility(View.GONE);
                                    myLog.e("Load Image Failed!");
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Bitmap arg0, Object arg1, Target<Bitmap> arg2, DataSource arg3,
                                                               boolean arg4) {
                                    progress.setVisibility(View.GONE);
                                    myLog.e("Load Image Ready!");
                                    return false;
                                }
                            }).into(imageView);
                }
            } else {// if item add photo

                // imageView.setImageResource(R.drawable.img_add_image);
                Glide.with(context).load(R.drawable.ic_add_black_24dp).into(holder.imageView);
                holder.progress.setVisibility(View.GONE);
            }

            holder.imvDelete.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // showAlertCheckDeleteDocumentDialog(context,
                    // item.getDocItem(), postition);
                    if (DataClient.getInstance(context).getAppDatabase().documentDao().getDocumentByDocumentID(item.getDocItem().getId()).size() > 0) {
                        File file = new File(item.getDocItem().getPath());
                        if (file.exists())
                            file.delete();
                        DataClient.getInstance(context).getAppDatabase().documentDao().deleteDocByDocumentID(item.getDocItem().getId());
                        data.remove(postition);
                    }
                    notifyDataSetChanged();
                }
            });

            // set Tag to check single choice
            holder.itemView.setTag(postition);

        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.TRANSPARENT);
        }

        @Override
        public void onItemClear() {
            // itemView.setBackgroundColor(Color.GREEN);
            itemView.setBackgroundColor(Color.TRANSPARENT);
            notifyDataSetChanged();
        }

    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        myLog.e(TAG, "onItemMove");
        Collections.swap(data, fromPosition, toPosition);
        mListChangedListener.onNoteListChanged(data);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        myLog.e(TAG, "onItemDismiss");
    }

}
