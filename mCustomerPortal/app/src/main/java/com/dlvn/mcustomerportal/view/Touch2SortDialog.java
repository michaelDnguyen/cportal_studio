package com.dlvn.mcustomerportal.view;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.adapter.Touch2SortRecyclerAdapter;
import com.dlvn.mcustomerportal.adapter.listener.ItemTouchHelperCallback;
import com.dlvn.mcustomerportal.adapter.listener.OnRecycleModelChangedListener;
import com.dlvn.mcustomerportal.adapter.listener.OnStartDragListener;
import com.dlvn.mcustomerportal.adapter.model.HorizontalRecyclerItemModel;
import com.dlvn.mcustomerportal.database.DataClient;
import com.dlvn.mcustomerportal.database.entity.DocumentEntity;
import com.dlvn.mcustomerportal.utils.myLog;

public class Touch2SortDialog extends Dialog implements OnRecycleModelChangedListener, OnStartDragListener {

    Context context;
    private ItemTouchHelper mItemTouchHelper;

    RecyclerView rvData;
    Button btnOK;
    ImageButton btnBack;
    TextView tvTitle, tvDelete;

    Touch2SortRecyclerAdapter adapter;
    List<HorizontalRecyclerItemModel> lstData = new ArrayList<>();
    String title = "";
    boolean canDelete = false;

    // luu docID cua item ProcessDocType
    long docID;

    public interface SortDoneDialogListener {
        void onFinishSortDialog(List<HorizontalRecyclerItemModel> lst);

        void onCancelSortDialog();
    }

    private SortDoneDialogListener sortDoneListener;

    public Touch2SortDialog(Context context, long docID, String title, boolean isDelete, List<HorizontalRecyclerItemModel> lstImage,
                            SortDoneDialogListener listener) {
        super(context);
        this.context = context;
        this.title = title;
        this.docID = docID;
        this.canDelete = isDelete;

        lstData.addAll(lstImage);
        sortDoneListener = listener;

        for (int i = lstData.size() - 1; i >= 0; i--)
            if (lstData.get(i).getStatus() == HorizontalRecyclerItemModel.STATE_ADD_IMAGE) {
                lstData.remove(i);
                break;
            }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_sort_image);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(lp);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(context, R.color.red));
        }

        btnOK = (Button) findViewById(R.id.btnOK);
        btnOK.setVisibility(View.GONE);
        btnBack = (ImageButton) findViewById(R.id.btnClose);
        rvData = (RecyclerView) findViewById(R.id.rvDataView);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText(title);

        tvDelete = (TextView) findViewById(R.id.tvDelete);

        adapter = new Touch2SortRecyclerAdapter(context, lstData, this, this);

        // final int spanCount =
        // context.getResources().getInteger(R.integer.grid_columns);
        final int spanCount = 3;
        final GridLayoutManager layoutManager = new GridLayoutManager(context, spanCount);
        rvData.setLayoutManager(layoutManager);
        rvData.setAdapter(adapter);

        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(rvData);

        btnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // sortDoneListener.onCancelSortDialog();
                sortDoneListener.onFinishSortDialog(lstData);
                dismiss();
            }
        });

        btnOK.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setupOnFinish();
                dismiss();
            }
        });

        if (canDelete)
            tvDelete.setVisibility(View.VISIBLE);
        else
            tvDelete.setVisibility(View.GONE);

        tvDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showAlertCheckDeleteAllDocumentDialog(context, Touch2SortDialog.this);
            }
        });
    }

    @Override
    public void onStartDrag(ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void onNoteListChanged(List<HorizontalRecyclerItemModel> customers) {
        lstData = customers;
        // adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        setupOnFinish();
        super.onBackPressed();
    }

    private void setupOnFinish() {
        lstData = adapter.getData();
        sortDoneListener.onFinishSortDialog(lstData);
    }

    public void showAlertCheckDeleteAllDocumentDialog(final Context context, final Touch2SortDialog touch2SortDialog) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_delete_all_document);
        dialog.setCanceledOnTouchOutside(false);

        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (dialog.isShowing())
                    dialog.dismiss();
            }
        });

        Button btnOK = dialog.findViewById(R.id.btnOK);
        btnOK.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                int row = 0;
                if (DataClient.getInstance(context).getAppDatabase().claimDocDao().getListDocTypeByDocID(docID).size() > 0) {

                    List<DocumentEntity> lstDoc = DataClient.getInstance(context).getAppDatabase().documentDao().getDocumentByDocTypeID(docID);
                    for (DocumentEntity doc : lstDoc) {

                        File file = new File(doc.getPath());
                        if (file.exists())
                            file.delete();
                        DataClient.getInstance(context).getAppDatabase().documentDao().deleteDocByDocumentID(doc.getId());
                    }
                    row = DataClient.getInstance(context).getAppDatabase().documentDao().deleteAllDocByDocTypeID(docID);

                    lstData.clear();
                    adapter.notifyDataSetChanged();
                }

                // delete DocType by DocID
                myLog.e("Delete Doctype id" + docID + " row effect = " + row);

                if (dialog.isShowing())
                    dialog.dismiss();

                touch2SortDialog.dismiss();
            }
        });

        if (!((Activity) context).isFinishing())
            dialog.show();
    }

}
