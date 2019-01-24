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
import com.dlvn.mcustomerportal.database.entity.ClaimEntity;
import com.dlvn.mcustomerportal.utils.myLog;

import java.util.List;

/**
 * Claims History
 */
public class ClaimsHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "ClaimsHistoryAdapter";

    public static final int CODE_TITLE = 9988;
    public static final int CODE_ITEM = 9987;

    List<ClaimEntity> data;
    Context context;

    public ClaimsHistoryAdapter(Context c, List<ClaimEntity> obj) {
        context = c;
        data = obj;
    }

    /**
     * ViewHolder for item
     *
     * @author nn.tai
     * @date 31/10/2018
     */
    public class ClaimsItemViewHolder extends RecyclerView.ViewHolder {

        ImageView imvImage;
        TextView tvDateTrans, tvPoint, tvPointBalance;

        public ClaimsItemViewHolder(View itemView) {
            super(itemView);
            imvImage = itemView.findViewById(R.id.imvImage);
            tvDateTrans = itemView.findViewById(R.id.tvDateTrans);
            tvPoint = itemView.findViewById(R.id.tvPoint);
            tvPointBalance = itemView.findViewById(R.id.tvPointBalance);
        }

        public void bind(final ClaimsItemViewHolder holder, final ClaimEntity item, final int postition) {

            myLog.e(TAG, item.getClaimsType() + " *** " + item.getClaimsID() + " *** " + item.getPolicyNo());

            holder.tvDateTrans.setText(item.getClaimsID() + "\n Po.No: " + item.getPolicyNo());
            holder.tvPoint.setText(Constant.StatusSubmit.fromValue(item.getStatus()).toString());
            holder.tvPointBalance.setText(item.getUpdateDate());

            if (item.getStatus().equalsIgnoreCase(Constant.StatusSubmit.DAGUI.getStringValue()) ||
                    item.getStatus().equalsIgnoreCase(Constant.StatusSubmit.DANGCHODUYET.getStringValue()) ||
                    item.getStatus().equalsIgnoreCase(Constant.StatusSubmit.DANGXULY.getStringValue()) ||
                    item.getStatus().equalsIgnoreCase(Constant.StatusSubmit.CHAPNHANTHANHTOAN.getStringValue()) ||
                    item.getStatus().equalsIgnoreCase(Constant.StatusSubmit.HOANTAT.getStringValue())) {
                holder.tvPoint.setTextColor(Color.GREEN);
            } else {
                holder.tvPoint.setTextColor(Color.RED);
            }
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

        public void bind(final GroupItemViewHolder holder, final ClaimEntity item, final int postition) {
            myLog.e(TAG, item.getClaimsType() + " *** " + item.getClaimsID() + " *** " + item.getPolicyNo());
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
            ClaimsItemViewHolder header = (ClaimsItemViewHolder) holder;
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
            return new ClaimsItemViewHolder(v);
        }
    }

    public void setData(List<ClaimEntity> obj) {
        if (this.data != obj) {
            this.data = obj;
            notifyItemRangeChanged(0, data.size());
        }
    }

    public List<ClaimEntity> getData() {
        return data;
    }

    public void removeAt(int position) {
        data.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(0, data.size());
    }
}
