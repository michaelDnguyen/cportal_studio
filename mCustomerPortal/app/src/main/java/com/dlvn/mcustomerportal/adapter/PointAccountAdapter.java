package com.dlvn.mcustomerportal.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.common.Constant;
import com.dlvn.mcustomerportal.services.model.response.GetPointAccountModel;
import com.dlvn.mcustomerportal.utils.Utilities;
import com.dlvn.mcustomerportal.utils.myLog;

import java.util.List;

import static android.media.CamcorderProfile.get;

public class PointAccountAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "PointAccountAdapter";

    public static final int CODE_TITLE = 9998;
    public static final int CODE_ITEM = 9997;

    List<GetPointAccountModel> data;
    Context context;

    public PointAccountAdapter(Context c, List<GetPointAccountModel> obj) {
        context = c;
        data = obj;
    }

    /**
     * ViewHolder for item
     *
     * @author nn.tai
     * @date 31/10/2018
     */
    public class PointItemViewHolder extends RecyclerView.ViewHolder {

        ImageView imvImage;
        TextView tvDateTrans, tvPoint, tvPointBalance;

        public PointItemViewHolder(View itemView) {
            super(itemView);
            imvImage = itemView.findViewById(R.id.imvImage);
            tvDateTrans = itemView.findViewById(R.id.tvDateTrans);
            tvPoint = itemView.findViewById(R.id.tvPoint);
            tvPointBalance = itemView.findViewById(R.id.tvPointBalance);
        }

        public void bind(final PointItemViewHolder holder, final GetPointAccountModel item, final int postition) {

            myLog.e(TAG, item.getTransTypeCD() + " *** " + item.getDescription() + " *** " + item.getPoint());
            holder.tvDateTrans.setText(item.getCreateDate());
            if (item.getTransTypeCD().equalsIgnoreCase(Constant.POINTACCOUNT_DEGEN)) {

                holder.tvPoint.setText("-" + (Utilities.formatMoneyToVND(item.getPoint() / 1000)).replace("VNĐ", ""));
                holder.tvPoint.setTextColor(Color.DKGRAY);
            } else {

                holder.tvPoint.setText("+" + (Utilities.formatMoneyToVND(item.getPoint() / 1000)).replace("VNĐ", ""));
                holder.tvPoint.setTextColor(Color.GREEN);
            }
            holder.tvPointBalance.setText("" + (Utilities.formatMoneyToVND(item.getPointBalance() / 1000)).replace("VNĐ", ""));

            holder.itemView.setTag(postition);
        }
    }

    /**
     * ViewHolder for group item
     *
     * @author nn.tai
     * @date 31/10/2018
     */
    public class GroupItemViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;

        public GroupItemViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
        }

        public void bind(final GroupItemViewHolder holder, final GetPointAccountModel item, final int postition) {
            myLog.e(TAG, item.getTransTypeCD() + " *** " + item.getDescription() + " *** " + item.getPoint());
            holder.tvTitle.setText(item.getTitleGroup());

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
        return data.get(position).getCodeGroup();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (data.get(position).getCodeGroup() == CODE_TITLE) {
            GroupItemViewHolder header = (GroupItemViewHolder) holder;
            header.bind(header, data.get(position), position);
        } else {
            PointItemViewHolder header = (PointItemViewHolder) holder;
            header.bind(header, data.get(position), position);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == CODE_TITLE) {
            View v = LayoutInflater.from(context).inflate(R.layout.item_point_account_loyalty_title, parent, false);
            return new GroupItemViewHolder(v);
        } else {
            View v = LayoutInflater.from(context).inflate(R.layout.item_point_account_loyalty, parent, false);
            return new PointItemViewHolder(v);
        }
    }

    public void setData(List<GetPointAccountModel> obj) {
        if (this.data != obj) {
            this.data = obj;
            notifyItemRangeChanged(0, data.size());
        }
    }

    public List<GetPointAccountModel> getData() {
        return data;
    }

    public void removeAt(int position) {
        data.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(0, data.size());
    }
}
