package com.dlvn.mcustomerportal.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.activity.prototype.ClaimsSupplementActivity;
import com.dlvn.mcustomerportal.adapter.listener.OnItemHorizontalViewClick;
import com.dlvn.mcustomerportal.adapter.model.ClaimListDocModel;
import com.dlvn.mcustomerportal.common.Constant;
import com.dlvn.mcustomerportal.utils.myLog;
import com.dlvn.mcustomerportal.view.HorizontalListItemView;

import java.util.List;

public class ClaimListDocAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "ClaimListDocAdapter";

    //position of item in recycler
    public static final int ITEM_NORMAL = 6543;
    public static final int ITEM_HEADER = 6544;
    public static final int ITEM_FOOTER = 6545;

    List<ClaimListDocModel> data;
    Context context;

    OnItemHorizontalViewClick onItemSubmiss;

    public ClaimListDocAdapter(Context context, List<ClaimListDocModel> data, OnItemHorizontalViewClick listener) {
        this.data = data;
        this.context = context;
        this.onItemSubmiss = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        myLog.e(TAG, "onCreateViewHolder viewType = " + viewType);
        if (viewType == ITEM_FOOTER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_footer_claimlist, parent, false);
            return new FooterViewHolder(v);
        } else {
            RecyclerHolder holder;

            HorizontalListItemView child = new HorizontalListItemView(context, onItemSubmiss);
            child.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            holder = new RecyclerHolder(child);

            return holder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        myLog.e(TAG, "onBindViewHolder postion = " + position);
        if (holder instanceof FooterViewHolder) {
            FooterViewHolder footer = (FooterViewHolder) holder;
            footer.bind(footer, data.get(position), position);
        } else {
            RecyclerHolder reholder = (RecyclerHolder) holder;
            reholder.bind((RecyclerHolder) holder, data.get(position), position);
        }
    }

    @Override
    public void registerAdapterDataObserver(@NonNull RecyclerView.AdapterDataObserver observer) {
        super.registerAdapterDataObserver(observer);
        myLog.e(TAG, "registerAdapterDataObserver");
        observer.onChanged();
    }

    @Override
    public void unregisterAdapterDataObserver(@NonNull RecyclerView.AdapterDataObserver observer) {
        super.unregisterAdapterDataObserver(observer);
        myLog.e(TAG, "unregisterAdapterDataObserver");
        observer.onChanged();
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

        if (data.get(position).getStatusItem() == ITEM_FOOTER)
            return ITEM_FOOTER;
        else
            return position;
    }

    public void setData(List<ClaimListDocModel> data) {
        if (this.data != data) {
            this.data = data;
            notifyItemRangeChanged(0, data.size());
        }
    }

    public List<ClaimListDocModel> getData() {
        return data;
    }

    public void removeAt(int position) {
        data.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(0, data.size());
    }

    /**
     * View Holder contain horizontal view submission
     *
     * @author nn.tai
     * @modify Jul 29, 2017
     */
    public class RecyclerHolder extends RecyclerView.ViewHolder {

        HorizontalListItemView view;

        public RecyclerHolder(View v) {
            super(v);
            view = (HorizontalListItemView) v;
        }

        public HorizontalListItemView getView() {
            return view;
        }

        public void bind(RecyclerHolder holder, final ClaimListDocModel item, final int postition) {

            myLog.e(TAG, "Tag = " + item.getClaimDocItem().getSubDocID() + " - pos = " + postition + " ** edit=" + item.isEdit());

            holder.view.setClaimListDocModel(item);
            holder.view.setEditMode(item.isEdit());
            holder.view.setTag(item.toString());
            holder.itemView.setTag(item.toString());
        }
    }

    /**
     * ViewHolder contain for footer
     *
     * @author nn.tai
     * @modify Jul 29, 2017
     */
    class FooterViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitleFooter, tvStatus;

        public FooterViewHolder(View itemView) {
            super(itemView);
            this.txtTitleFooter = itemView.findViewById(R.id.tvName);
            this.tvStatus = itemView.findViewById(R.id.tvStatus);
        }

        public void bind(FooterViewHolder holder, final ClaimListDocModel item, final int postition) {

            myLog.e(TAG, "Update = " + item.getClaimDocItem().getUpdateDate() + " - suppID = " + item.getClaimDocItem().getId());

            holder.txtTitleFooter.setText("BỔ SUNG " + item.getClaimDocItem().getUpdateDate());
            holder.tvStatus.setText("Status : " + item.getClaimDocItem().getNote());

            holder.itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {


                    Intent intent = new Intent(context, ClaimsSupplementActivity.class);
                    intent.putExtra(Constant.CLAIMS_INTKEY_CLAIMS_SUPPLEMENT_ID, item.getClaimDocItem().getId());
                    intent.putExtra(Constant.CLAIMS_INTKEY_SUPPLEMENT_SUBMISSIONID_PARENT, item.getClaimDocItem().getClaimsEntityID());
                    context.startActivity(intent);

                }
            });

            holder.itemView.setTag(item.getClaimDocItem().getSubDocID());
        }
    }
}
