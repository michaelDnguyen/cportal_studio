package com.dlvn.mcustomerportal.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dlvn.mcustomerportal.BuildConfig;
import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.adapter.HorizontalRecyclerAdapter;
import com.dlvn.mcustomerportal.adapter.listener.OnItemHorizontalViewClick;
import com.dlvn.mcustomerportal.adapter.listener.RecyclerViewClickListener;
import com.dlvn.mcustomerportal.adapter.listener.RecyclerViewTouchListener;
import com.dlvn.mcustomerportal.adapter.model.ClaimListDocModel;
import com.dlvn.mcustomerportal.adapter.model.HorizontalRecyclerItemModel;
import com.dlvn.mcustomerportal.common.Constant;
import com.dlvn.mcustomerportal.database.DataClient;
import com.dlvn.mcustomerportal.database.entity.DocumentEntity;
import com.dlvn.mcustomerportal.utils.myLog;
import com.dlvn.mcustomerportal.view.clearable_edittext.ClearableEditText;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

public class HorizontalListItemView extends LinearLayout implements Touch2SortDialog.SortDoneDialogListener {

    private static final String TAG = "HorizontalListItemView";

    Context context;
    private LayoutInflater mInflater;

    TextView tvTitle;
    TextView btnSapxep;
    ImageView imvComment;
    RecyclerView rcView;
    HorizontalRecyclerAdapter rcAdapter;
    ClaimListDocModel claimListDocModel = null;

    OnItemHorizontalViewClick onItemSubmiss;

    public HorizontalListItemView(Context context, OnItemHorizontalViewClick onItemSubmissViewClick) {
        super(context);
        this.context = context;
        this.onItemSubmiss = onItemSubmissViewClick;
        initView(null);
    }

    private void initView(List<HorizontalRecyclerItemModel> data) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(R.layout.item_horizontal_view, this);

        tvTitle = findViewById(R.id.tvTitle);
        btnSapxep = findViewById(R.id.btnSapxep);
        imvComment = findViewById(R.id.imvComment);
        rcView = findViewById(R.id.rvDataView);

        // add icon add photo
        claimListDocModel = new ClaimListDocModel();

        if (!claimListDocModel.isEdit()) {
            btnSapxep.setVisibility(View.INVISIBLE);
        }

        if (claimListDocModel.isComment())
            if (!TextUtils.isEmpty(claimListDocModel.getClaimDocItem().getClaimAdminComment()))
                imvComment.setVisibility(View.VISIBLE);
            else
                imvComment.setVisibility(View.GONE);

        if (claimListDocModel.isEdit())
            claimListDocModel.getLstDoc().add(new HorizontalRecyclerItemModel(new DocumentEntity(), HorizontalRecyclerItemModel.STATE_ADD_IMAGE));

        if (data != null) {
            if (claimListDocModel.getLstDoc().size() > 1) {
                if (claimListDocModel.isEdit())
                    claimListDocModel.getLstDoc().addAll(claimListDocModel.getLstDoc().size() - 1, data);
                else
                    claimListDocModel.getLstDoc().addAll(data);
            } else
                claimListDocModel.getLstDoc().addAll(data);
        }
        setUpSortButton();

        rcAdapter = new HorizontalRecyclerAdapter(context, claimListDocModel.getLstDoc());
        rcView.setAdapter(rcAdapter);

        LinearLayoutManager horizontalLayoutManager1 = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,
                false);
        rcView.setLayoutManager(horizontalLayoutManager1);

        rcView.addOnItemTouchListener(new RecyclerViewTouchListener(context, rcView, new RecyclerViewClickListener() {

            @Override
            public void onLongClick(View view, int position) {

            }

            @Override
            public void onClick(View view, int position) {
                if (onItemSubmiss != null) {
                    myLog.e(TAG, "Horizontal List onClick");
                    if (claimListDocModel.getLstDoc().get(position) != null) {
                        onItemSubmiss.OnItemClaimClick(claimListDocModel, position, rcAdapter);
                    }
                }
            }
        }));

        btnSapxep.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                boolean isDelete = true;
                if (claimListDocModel.isImportant()) {
                    isDelete = false;
                }

                Touch2SortDialog dialog = new Touch2SortDialog(context, claimListDocModel.getClaimDocItem().getId(),
                        claimListDocModel.getClaimDocItem().getDocTypeName() + " - " + claimListDocModel.getClaimDocItem().getDocName(),
                        isDelete, claimListDocModel.getLstDoc(), HorizontalListItemView.this);
                dialog.show();
            }
        });

        imvComment.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogComment(context);
            }
        });
    }

    private void setUpSortButton() {
        if (btnSapxep != null) {
            if (claimListDocModel.isEdit()) {
                if (claimListDocModel.getLstDoc().size() <= 1)
                    if (claimListDocModel.isImportant()) {
                        btnSapxep.setVisibility(View.GONE);
                    } else
                        btnSapxep.setVisibility(View.VISIBLE);
                else
                    btnSapxep.setVisibility(View.VISIBLE);
            } else
                btnSapxep.setVisibility(View.GONE);
        }

        if (claimListDocModel.isComment()) {
            if (!TextUtils.isEmpty(claimListDocModel.getClaimDocItem().getClaimAdminComment())) {
                imvComment.setVisibility(View.VISIBLE);
                if (claimListDocModel.getClaimDocItem().getDocTypeID().equalsIgnoreCase(Constant.CLAIMS_DOCTYPE_QUESTION))
                    imvComment.setImageResource(android.R.drawable.ic_menu_info_details);
                else
                    imvComment.setImageResource(android.R.drawable.stat_notify_chat);
            } else
                imvComment.setVisibility(View.GONE);
        }
    }

    @Override
    protected void dispatchSetPressed(boolean pressed) {
        // TODO Auto-generated method stub
        super.dispatchSetPressed(pressed);
    }

    /**
     * check doctype is important or not
     *
     * @return
     */
    public boolean isImportant() {
        return claimListDocModel.isImportant();
    }

    public void setImportant(boolean isImportant) {
        if (claimListDocModel != null) {
            this.claimListDocModel.setImportant(isImportant);
            refreshView();
        }
    }

    /**
     * check doctype in edit mode or not
     *
     * @return
     */
    public boolean isEditMode() {
        return claimListDocModel.isEdit();
    }

    public void setEditMode(boolean isEditMode) {
        if (claimListDocModel != null) {
            this.claimListDocModel.setEdit(isEditMode);
            setUpSortButton();
            refreshView();
        }
    }

    /**
     * check comment mode for supplement
     *
     * @return
     */
    public boolean isCommentMode() {
        return claimListDocModel.isComment();
    }

    public void setCommentMode(boolean isComment) {
        if (claimListDocModel != null) {
            this.claimListDocModel.setComment(isComment);
            refreshView();
        }
    }

    private void showDialogComment(final Context context) {

        AlertDialog.Builder alerDialog = new AlertDialog.Builder(context);
        LayoutInflater li = LayoutInflater.from(context);
        View view = li.inflate(R.layout.dialog_client_comment, null);
        alerDialog.setView(view);
        final AlertDialog dialog = alerDialog.create();

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.WHITE));

        TextView tvTitle = dialog.findViewById(R.id.title);
        final ClearableEditText cedtComment = dialog.findViewById(R.id.cedtComment);
        Button btnSave = dialog.findViewById(R.id.btnSave);
        ImageButton btnClose = dialog.findViewById(R.id.btnClose);

        if (!TextUtils.isEmpty(claimListDocModel.getClaimDocItem().getClaimAdminComment()))
            tvTitle.setText(claimListDocModel.getClaimDocItem().getClaimAdminComment());

        if (!TextUtils.isEmpty(claimListDocModel.getClaimDocItem().getClaimClientComment()))
            cedtComment.setText(claimListDocModel.getClaimDocItem().getClaimClientComment());

        btnSave.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                claimListDocModel.getClaimDocItem().setClaimClientComment(cedtComment.getText().toString());
                dialog.dismiss();
            }
        });

        btnClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                claimListDocModel.getClaimDocItem().setClaimClientComment(cedtComment.getText().toString());
                dialog.dismiss();
            }
        });
    }

    private void refreshView() {

        int max = 1;
        if (!claimListDocModel.isEdit())
            max = 0;

        String totalDoc = "";
        if (claimListDocModel.getLstDoc().size() > 0)
            if (claimListDocModel.isEdit()) {
                if (claimListDocModel.getLstDoc().size() - 1 > 0)
                    totalDoc = " (" + (claimListDocModel.getLstDoc().size() - 1) + ")";
            } else
                totalDoc = " (" + claimListDocModel.getLstDoc().size() + ")";

//        if (isImportant) {
//            if (claimListDocModel.getLstDoc().size() <= max) {
//                tvTitle.setTextColor(Color.RED);
//                tvTitle.setText(docTypeName + "*" + totalDoc);
//            } else {
//                tvTitle.setTextColor(Color.BLACK);
//                tvTitle.setText(docTypeName + "*" + totalDoc);
//            }
//        } else {
//            tvTitle.setTextColor(Color.BLACK);
//            tvTitle.setText(docTypeName + totalDoc);
//        }

        if (claimListDocModel.isImportant()) {
            if (claimListDocModel.getLstDoc().size() <= max) {
                tvTitle.setTextColor(Color.RED);
                tvTitle.setText(claimListDocModel.getClaimDocItem().getDocTypeName() + "*" + totalDoc);
            } else {
                tvTitle.setTextColor(Color.BLACK);
                tvTitle.setText(claimListDocModel.getClaimDocItem().getDocTypeName() + "*" + totalDoc);
            }
        } else {
            tvTitle.setTextColor(Color.BLACK);
            tvTitle.setText(claimListDocModel.getClaimDocItem().getDocTypeName() + totalDoc);
        }
    }

    public void setData(List<HorizontalRecyclerItemModel> data) {
        myLog.e(TAG, " setData = " + data.size());

        if (claimListDocModel.getLstDoc() != data) {
            myLog.e(TAG, " data old # data new ");
            claimListDocModel.getLstDoc().clear();
            claimListDocModel.getLstDoc().addAll(data);
        }
        setUpSortButton();
        refreshView();

        rcAdapter.setData(data);
        rcAdapter.notifyDataSetChanged();
    }

    public void refreshData(List<HorizontalRecyclerItemModel> data) {
        synchronized (claimListDocModel.getLstDoc()) {

            if (data == null) {
                clear();
            } else {
                // lstData.addAll(data);
            }
            setUpSortButton();
            rcAdapter.notifyDataSetChanged();
        }
    }

    public void appendDataToTop(List<HorizontalRecyclerItemModel> dataAppend) {
        synchronized (claimListDocModel.getLstDoc()) {
            claimListDocModel.getLstDoc().addAll(0, dataAppend);
            setUpSortButton();
            rcAdapter.notifyDataSetChanged();
        }
    }

    public HorizontalRecyclerItemModel getItemAt(int position) {
        if (position <= 0 || position > claimListDocModel.getLstDoc().size()) {
            myLog.e(TAG, "getItemval out of index");
            return null;
        }
        return claimListDocModel.getLstDoc().get(position);
    }

    public void remove(int position) {
        if (position <= 0 || position > claimListDocModel.getLstDoc().size()) {
            myLog.e(TAG, " out of index");
        }
        synchronized (claimListDocModel.getLstDoc()) {
            claimListDocModel.getLstDoc().remove(position);
            setUpSortButton();
            rcAdapter.notifyDataSetChanged();
        }
    }

    public void clear() {
        if (claimListDocModel.getLstDoc() != null)
            this.claimListDocModel.getLstDoc().clear();
    }

    public List<HorizontalRecyclerItemModel> getData() {
        return claimListDocModel.getLstDoc();
    }

    public HorizontalRecyclerAdapter getAdapter() {
        return rcAdapter;
    }

    public ClaimListDocModel getClaimListDocModel() {
        return claimListDocModel;
    }

    public void setClaimListDocModel(ClaimListDocModel claimListDocModel) {
        myLog.e(TAG, "setClaimListModel size = " + claimListDocModel.getLstDoc().size());
        this.claimListDocModel = claimListDocModel;
        tvTitle.setText(claimListDocModel.getClaimDocItem().getDocTypeName() + " - " + claimListDocModel.getClaimDocItem().getDocName());
//        setData(claimListDocModel.getLstDoc());
        setUpSortButton();
        refreshView();

        rcAdapter.setData(claimListDocModel.getLstDoc());
        rcAdapter.notifyDataSetChanged();
    }

    public void setOnItemHorizontalListViewListener(OnItemHorizontalViewClick listener) {
        this.onItemSubmiss = listener;
    }

    @Override
    public void onFinishSortDialog(List<HorizontalRecyclerItemModel> lst) {
        myLog.e("onFinishSortDialog");

        claimListDocModel.getLstDoc().clear();
        claimListDocModel.setLstDoc(lst);

        // update position into DB
        for (int i = 0; i < claimListDocModel.getLstDoc().size(); i++) {
            DocumentEntity doc = claimListDocModel.getLstDoc().get(i).getDocItem();
            doc.setNumSort(i + 1);
            DataClient.getInstance(context).getAppDatabase().documentDao().update(doc);
        }

        claimListDocModel.getLstDoc().add(new HorizontalRecyclerItemModel(new DocumentEntity(), HorizontalRecyclerItemModel.STATE_ADD_IMAGE));
        rcAdapter.setData(claimListDocModel.getLstDoc());
        rcView.invalidate();
    }

    @Override
    public void onCancelSortDialog() {
        myLog.e("onCancelSortDialog");
    }
}
