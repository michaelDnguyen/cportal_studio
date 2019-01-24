package com.dlvn.mcustomerportal.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.adapter.model.HorizontalRecyclerItemModel;
import com.dlvn.mcustomerportal.common.Constant;
import com.dlvn.mcustomerportal.database.entity.DocumentEntity;
import com.dlvn.mcustomerportal.utils.myLog;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class HorizontalRecyclerAdapter extends RecyclerView.Adapter<HorizontalRecyclerAdapter.RecyclerHolder> {

    private static final String TAG = "HorizontalRecyclerAdapter";

    List<HorizontalRecyclerItemModel> data;
    Context context;

    public HorizontalRecyclerAdapter(Context context, List<HorizontalRecyclerItemModel> data) {
        this.data = data;
        this.context = context;
    }

    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_horizontal_recycler, parent, false);
        return new RecyclerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position) {
        holder.bind(holder, data.get(position), position);
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

    public void setData(List<HorizontalRecyclerItemModel> data) {
        if (this.data != data) {
            this.data = data;
            notifyItemRangeChanged(0, data.size());
        }
    }

    public List<HorizontalRecyclerItemModel> getData() {
        return data;
    }

    public void removeAt(int position) {
        data.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(0, data.size());
    }

    public class RecyclerHolder extends RecyclerView.ViewHolder {

        ImageView imageView, imvDelete, imvStatus;
        ProgressBar progress;
        TextView tvThemMoi;

        public RecyclerHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.imvImage);
            imvDelete = view.findViewById(R.id.imvDelete);
            imvStatus = view.findViewById(R.id.imvStatus);
            progress = view.findViewById(R.id.progressBar);
            tvThemMoi = view.findViewById(R.id.tvThemMoi);
        }

        public void bind(final RecyclerHolder holder, final HorizontalRecyclerItemModel item, final int postition) {

            myLog.e(TAG, "bind data state " + item.getStatus());

            if (item.getStatus() != HorizontalRecyclerItemModel.STATE_ADD_IMAGE) {

                holder.tvThemMoi.setVisibility(View.VISIBLE);
                holder.tvThemMoi.setText((postition + 1) + "");

                String filePath = item.getDocItem().getPath();
                // measure size of bitmap into ImageView: portrait and landscape
                new AsyncTask<String, Void, BitmapFactory.Options>() {

                    @Override
                    protected BitmapFactory.Options doInBackground(String... params) {

                        BitmapFactory.Options opt = null;
                        try {
                            opt = new BitmapFactory.Options();
                            opt.inJustDecodeBounds = true;
                            BitmapFactory.decodeFile(params[0], opt);
                            myLog.e(TAG, "size bitmap load = " + opt.outWidth + "x" + opt.outHeight + " pixel");
                        } catch (Exception e) {
                            myLog.printTrace(e);
                        }
                        return opt;
                    }

                    @Override
                    protected void onPostExecute(final BitmapFactory.Options result) {

                        imageView.post(new Runnable() {

                            @Override
                            public void run() {
                                if (result != null) {
                                    if (result.outWidth > result.outHeight) {

                                        RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(
                                                (int) (imageView.getHeight() * 1.2), imageView.getHeight());
                                        imageView.setLayoutParams(param);
                                        imageView.getParent().requestLayout();
                                    }
                                }
                            }
                        });
                    }
                }.execute(filePath);

                progress.setVisibility(View.VISIBLE);
                myLog.e(TAG, "MimeType = " + MimeTypeMap.getFileExtensionFromUrl(filePath));

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
                }

                if (!TextUtils.isEmpty(item.getDocItem().getStatus())) {
                    holder.imvStatus.setVisibility(View.VISIBLE);
                    if (item.getDocItem().getStatus().equals(DocumentEntity.STATUS_UPLOAD_SUCCESS))
                        holder.imvStatus.setBackgroundColor(Color.GREEN);
                    else if (item.getDocItem().getStatus().equals(DocumentEntity.STATUS_UPLOAD_FAILED))
                        holder.imvStatus.setBackgroundColor(Color.RED);
                    else if (item.getDocItem().getStatus().equals(DocumentEntity.STATUS_UPLOADING))
                        holder.imvStatus.setBackgroundColor(Color.YELLOW);
                    else if (item.getDocItem().getStatus().equals(DocumentEntity.STATUS_NOT_UPLOAD))
                        holder.imvStatus.setBackgroundColor(Color.GRAY);
                    else
                        holder.imvStatus.setBackgroundColor(Color.WHITE);
                } else
                    holder.imvStatus.setVisibility(View.GONE);

            } else {// if item add photo
                holder.imageView.setImageResource(R.drawable.ic_add_black_24dp);
                holder.tvThemMoi.setVisibility(View.VISIBLE);
                holder.progress.setVisibility(View.GONE);
            }

            // set Tag to check single choice
            holder.itemView.setTag(item.toString());
        }
    }
}
