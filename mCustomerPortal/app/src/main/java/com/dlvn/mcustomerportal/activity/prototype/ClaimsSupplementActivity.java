package com.dlvn.mcustomerportal.activity.prototype;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dlvn.mcustomerportal.BuildConfig;
import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.adapter.ClaimListDocAdapter;
import com.dlvn.mcustomerportal.adapter.HorizontalRecyclerAdapter;
import com.dlvn.mcustomerportal.adapter.SingleListAdapter;
import com.dlvn.mcustomerportal.adapter.ViewImagePagerAdapter;
import com.dlvn.mcustomerportal.adapter.listener.OnItemHorizontalViewClick;
import com.dlvn.mcustomerportal.adapter.model.ClaimListDocModel;
import com.dlvn.mcustomerportal.adapter.model.HorizontalRecyclerItemModel;
import com.dlvn.mcustomerportal.adapter.model.SingleChoiceModel;
import com.dlvn.mcustomerportal.base.BaseActivity;
import com.dlvn.mcustomerportal.common.Constant;
import com.dlvn.mcustomerportal.common.CustomPref;
import com.dlvn.mcustomerportal.database.DataClient;
import com.dlvn.mcustomerportal.database.entity.ClaimDocEntity;
import com.dlvn.mcustomerportal.database.entity.ClaimEntity;
import com.dlvn.mcustomerportal.database.entity.DocumentEntity;
import com.dlvn.mcustomerportal.services.ServicesGenerator;
import com.dlvn.mcustomerportal.services.ServicesRequest;
import com.dlvn.mcustomerportal.services.model.BaseRequest;
import com.dlvn.mcustomerportal.services.model.claims.CPClaimDocType;
import com.dlvn.mcustomerportal.services.model.claims.CPGetClaimIDRequest;
import com.dlvn.mcustomerportal.services.model.claims.CPGetClaimIDResponse;
import com.dlvn.mcustomerportal.services.model.claims.CPGetClaimIDResult;
import com.dlvn.mcustomerportal.services.model.claims.CPGetDocTypeRequest;
import com.dlvn.mcustomerportal.services.model.claims.CPGetDocTypeResponse;
import com.dlvn.mcustomerportal.services.model.claims.CPGetDocTypeResult;
import com.dlvn.mcustomerportal.services.model.claims.ClaimsFromRequest;
import com.dlvn.mcustomerportal.services.model.claims.DocTypeComment;
import com.dlvn.mcustomerportal.services.model.claims.GetClaimsStatusRequest;
import com.dlvn.mcustomerportal.services.model.claims.GetClaimsStatusResponse;
import com.dlvn.mcustomerportal.services.model.claims.GetClaimsStatusResult;
import com.dlvn.mcustomerportal.services.model.claims.UploadClaimImageRequest;
import com.dlvn.mcustomerportal.services.model.claims.UploadClaimImageResponse;
import com.dlvn.mcustomerportal.services.model.response.CPSubmitFormResponse;
import com.dlvn.mcustomerportal.services.model.response.CPSubmitFormResult;
import com.dlvn.mcustomerportal.utils.FileUtils;
import com.dlvn.mcustomerportal.utils.Utilities;
import com.dlvn.mcustomerportal.utils.myLog;
import com.dlvn.mcustomerportal.view.MyCustomDialog;
import com.dlvn.mcustomerportal.view.RecyclerSmoothLayoutManager;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Thông tin giấy tờ cần thiết/ chứng từ claims cần bổ sung
 */
public class ClaimsSupplementActivity extends BaseActivity implements OnItemHorizontalViewClick {

    private static final String TAG = "ClaimsSupplementActivity";

    LinearLayout lloBack, lloToolbar;
    TextView tvAddDoc;
    Button btnCapNhat;
    RecyclerView rvContent;

    //adapter list dccument
    ClaimListDocAdapter rvAdapter;
    //Danh sách các loại giấy tờ của claim
    List<ClaimListDocModel> lsData;
    //danh sách Doctype theo quyền lợi đã chọn
    List<CPClaimDocType> lsDocType;
    //loại giấy tờ đang chọn
    ClaimListDocModel claimDocSelected = null;
    //Data model of Claim
    ClaimEntity claimSuppEntity = null;
    //status of Claims
    Constant.StatusSubmit statusSubmit = Constant.StatusSubmit.CHUAGUI;
    //status of Doc item
    Constant.StatusDoc statusDoc = Constant.StatusDoc.DANG_TAO_HO_SO;
    //ClaimsID của thằng cha
    long parentSubmissID = 0;

    //uri path capture from camera or gallery
    Uri pathCapture = null;
    String spathCapture = null;

    //flag for Bo sung ho so
    boolean isSupplement = false;
    //flag for EDIT or VIEW MODE
    boolean isEditMode = true;
    //flag for Claims Rejected
    boolean isRejected = false;

    //doctype comment
    ClaimsFromRequest claimsFromRequest;

    ServicesRequest svRequester;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claims_supplement);

        svRequester = ServicesGenerator.createService(ServicesRequest.class);
        lloBack = findViewById(R.id.lloBack);
        lloBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getViews();
        initDatas();
        setListener();
    }

    private void getViews() {
        lloToolbar = findViewById(R.id.lloToolbar);
        tvAddDoc = findViewById(R.id.tvAddDoc);

        btnCapNhat = findViewById(R.id.btnCapNhat);
        rvContent = findViewById(R.id.rvContent);
        RecyclerView.LayoutManager layout = new RecyclerSmoothLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvContent.setLayoutManager(layout);
    }

    private void initDatas() {
        lsData = new ArrayList<>();
        claimSuppEntity = new ClaimEntity();

        //get claims request info
        if (getIntent().getExtras() != null)

            //check claims is exist
            if (getIntent().getExtras().containsKey(Constant.CLAIMS_INTKEY_CLAIMS_SUPPLEMENT_ID)) {

                long id = getIntent().getLongExtra(Constant.CLAIMS_INTKEY_CLAIMS_SUPPLEMENT_ID, 0);
                parentSubmissID = getIntent().getLongExtra(Constant.CLAIMS_INTKEY_SUPPLEMENT_SUBMISSIONID_PARENT, 0);

                myLog.e(TAG, "Parent ClaimsID/ SubmissionID = " + parentSubmissID);
                myLog.e(TAG, "supplement ID = " + id);

                claimSuppEntity = DataClient.getInstance(this).getAppDatabase().claimDao().getClaimsByID(id);

                if (claimSuppEntity == null) {
                    MyCustomDialog dialog = new MyCustomDialog.Builder(this)
                            .setMessage(getString(R.string.alert_claims_no_claims_supplement_item))
                            .setPositiveButton(getString(R.string.confirm_ok), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    finish();
                                }
                            }).create();
                    dialog.show();
                } else {
                    loadClaimsItemToViews(this);
                }
            }
    }

    private void loadClaimsItemToViews(Context context) {

        statusSubmit = Constant.StatusSubmit.fromValue(claimSuppEntity.getStatus());
        updateStatusViews();

        if (statusSubmit.getStringValue().equalsIgnoreCase(Constant.StatusSubmit.DAGUI.getStringValue())) {
            isEditMode = false;
        } else {
            isEditMode = true;
            new GetDocTypeListTask(this, claimSuppEntity.getClaimsType(), Constant.CLAIMS_ACTION_CHECKHOLD).execute();
        }

        // get all doc in DB if exist and add to list DocType
        List<ClaimDocEntity> ls = DataClient.getInstance(context).getAppDatabase().claimDocDao().getListDocTypeByClaimID(claimSuppEntity.getId());
        if (ls != null) {
            for (ClaimDocEntity mo : ls) {
                List<DocumentEntity> lsDoc = DataClient.getInstance(context).getAppDatabase().documentDao().getDocumentByDocTypeID(mo.getId());
                if (lsDoc.size() > 0) {
                    List<HorizontalRecyclerItemModel> lstTemp = new ArrayList<>();
                    for (int i = 0; i < lsDoc.size(); i++) {
                        lstTemp.add(new HorizontalRecyclerItemModel(lsDoc.get(i), 0));
                    }
                    if (isEditMode)
                        lstTemp.add(new HorizontalRecyclerItemModel(new DocumentEntity(), HorizontalRecyclerItemModel.STATE_ADD_IMAGE));

                    lsData.add(new ClaimListDocModel(ClaimListDocAdapter.ITEM_NORMAL,
                            mo, lstTemp, isEditMode, false, true));
                } else {
                    //Nếu đã có DocType mà chưa có document nào
                    if (isEditMode) {
                        List<HorizontalRecyclerItemModel> lstTemp = new ArrayList<>();
                        lstTemp.add(new HorizontalRecyclerItemModel(new DocumentEntity(),
                                HorizontalRecyclerItemModel.STATE_ADD_IMAGE));
                        lsData.add(new ClaimListDocModel(ClaimListDocAdapter.ITEM_NORMAL,
                                mo, lstTemp, isEditMode, false, true));
                    }
                }
            }
            if (rvAdapter == null) {
                rvAdapter = new ClaimListDocAdapter(context, lsData, ClaimsSupplementActivity.this);
                rvContent.setAdapter(rvAdapter);
            }
            rvAdapter.notifyDataSetChanged();
        }
    }

    private void setListener() {
        btnCapNhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String text = checkDocumentSubmit();
                if (!TextUtils.isEmpty(text)) {
                    showAlertCheckDocumentDialog(text);
                    statusDoc = statusDoc.CHUA_DU_GIAY_TO;
                } else
                    showAlertSubmitDocumentDialog();
            }
        });

        tvAddDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogChoiceDocType(ClaimsSupplementActivity.this);
            }
        });
    }

    private String checkDocumentSubmit() {
        myLog.e(TAG, "checkDocumentSubmit");
        StringBuilder s = new StringBuilder();

        // refresh lại lstData
//        refreshData();

        int pos = 1, min = 2;

        for (int i = 0; i < lsData.size(); i++) {

            if (lsData.get(i).getLstDoc().size() < min) {
                s.append(pos + ". ");
                s.append(lsData.get(i).getClaimDocItem().getDocName());
                s.append("\n");
                pos++;
            }
        }

        return s.toString();
    }

    // check all doc of doctype of proposal has upload success or not
    private boolean checkSubmitDocAllComplete() {
        myLog.e(TAG, "checkSubmitDocAllComplete");
        boolean result = true;

        // check submit per row
        List<ClaimDocEntity> arrDocType = DataClient.getInstance(ClaimsSupplementActivity.this).getAppDatabase().claimDocDao().getListDocTypeByClaimID(claimSuppEntity.getId());

        if (arrDocType != null && arrDocType.size() > 0) {
            for (ClaimDocEntity docType : arrDocType) {

                List<DocumentEntity> arrDoc = DataClient.getInstance(ClaimsSupplementActivity.this).getAppDatabase().documentDao().getDocumentByDocTypeID(docType.getId());
                if (arrDoc != null && arrDoc.size() > 0) {
                    for (DocumentEntity doc : arrDoc) {
                        if (!doc.getStatus().equals(DocumentEntity.STATUS_UPLOAD_SUCCESS)) {
                            result = false;
                            break;
                        }

                        if (doc.getStatus().equals(DocumentEntity.STATUS_UPLOAD_FAILED)) {
                            statusDoc = Constant.StatusDoc.NOP_HO_SO_THAT_BAI;
                            result = false;
                            break;
                        }
                    }
                } else
                    result = false;
            }
        } else
            result = false;

        myLog.e(TAG, "checkSubmitDocAllComplete = " + result);
        return result;
    }

    private void updateClaimSubmitComplete() {
        myLog.e(TAG, "updateClaimSubmitComplete");
        if (checkSubmitDocAllComplete()) {

            statusSubmit = Constant.StatusSubmit.DAGUI;
            statusDoc = statusDoc.DA_BO_SUNG;

            //update status of claims
            claimSuppEntity.setStatus(statusSubmit.getStringValue());
            DataClient.getInstance(ClaimsSupplementActivity.this).getAppDatabase().claimDao().update(claimSuppEntity);

            //Update status of Claim Parent
            ClaimEntity claimParent = DataClient.getInstance(ClaimsSupplementActivity.this).getAppDatabase().claimDao()
                    .getClaimsByID(claimSuppEntity.getMainClaimID());
            if (claimParent != null) {
                claimParent.setStatus(Constant.StatusSubmit.DAGUI.getStringValue());
                DataClient.getInstance(ClaimsSupplementActivity.this).getAppDatabase().claimDao().update(claimParent);
            }

            isSupplement = false;
            isEditMode = false;
            myLog.e(TAG, "Status Submit : " + statusSubmit.toString());
            myLog.e(TAG, "Status Doc : " + statusDoc.toString());

            //notify Data change
            for (int i = 0; i < lsData.size(); i++) {
                lsData.get(i).setEdit(isEditMode);
                for (int a = 0; a < lsData.get(i).getLstDoc().size(); a++)
                    if (lsData.get(i).getLstDoc().get(a).getStatus() == HorizontalRecyclerItemModel.STATE_ADD_IMAGE)
                        lsData.get(i).getLstDoc().remove(a);
            }
            rvAdapter.setData(lsData);

            MyCustomDialog dialog = new MyCustomDialog.Builder(ClaimsSupplementActivity.this)
                    .setMessage("Hồ sơ yêu cầu bảo hiểm đã nộp bổ sung thành công!")
                    .setCancelOnTouchOut(false)
                    .setPositiveButton(getString(R.string.confirm_ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent intent = new Intent(ClaimsSupplementActivity.this, DashboardActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    }).create();
            dialog.show();

        }
        updateStatusViews();
    }

    private void updateStatusViews() {
        myLog.e(TAG, "updateStatusViews");
        // check mode case TẠO MỚI or BỔ SUNG hồ sơ
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (statusSubmit.equals(Constant.StatusSubmit.CHUAGUI)) {
                    if (statusDoc.equals(Constant.StatusDoc.DANG_NOP_HO_SO) || statusDoc.equals(Constant.StatusDoc.NOP_HO_SO_THAT_BAI))
                        isEditMode = false;
                    else
                        isEditMode = true;

                    if (isSupplement)
                        btnCapNhat.setText("Bổ sung");
                    else
                        btnCapNhat.setText("Yêu cầu bảo hiểm");

                    if (statusDoc.equals(Constant.StatusDoc.NOP_HO_SO_THAT_BAI))
                        btnCapNhat.setText("Thử lại");

                } else if (statusSubmit.equals(Constant.StatusSubmit.CHOBOSUNG)) {
                    isEditMode = false;
                    isSupplement = true;
                    btnCapNhat.setText("Bổ sung");

                } else if (statusSubmit.equals(Constant.StatusSubmit.TAMDINHCHI)) {
                    isEditMode = false;
                    isSupplement = false;
                    btnCapNhat.setText("Thực hiện lại");
                } else {
                    isEditMode = false;
                    btnCapNhat.setText("Bổ sung");
                    btnCapNhat.setBackgroundResource(R.drawable.state_btn_grey_rectangle);
                    btnCapNhat.setEnabled(false);
                }

                if (!isEditMode)
                    lloToolbar.setVisibility(View.GONE);

            }
        });
    }

    private void uploadDocuments() {
        myLog.e(TAG, "upload image document claims");
        if (lsData.size() > 0) {

            String text = checkDocumentSubmit();
            if (TextUtils.isEmpty(text)) {

                boolean isEndLst = false;
                for (int i = 0; i < lsData.size(); i++) {

                    List<HorizontalRecyclerItemModel> lst = lsData.get(i).getLstDoc();

                    if (lst != null) {
                        if (i >= lsData.size() - 2)
                            isEndLst = true;

                        for (int k = 0; k < lst.size(); k++) {

                            if (lst.get(k).getStatus() != HorizontalRecyclerItemModel.STATE_ADD_IMAGE) {

                                myLog.e(TAG, "claims Filepath: " + lst.get(k).getDocItem().getPath());
                                sendImageStreamingToServerTask(ClaimsSupplementActivity.this, lst, i, k,
                                        lsData.get(i).getClaimDocItem().getDocTypeID(), lsData.get(i).getClaimDocItem().getDocTypeID(), isEndLst);
                            }
                        }
                    }
                }
            } else {
                MyCustomDialog dialog = new MyCustomDialog.Builder(ClaimsSupplementActivity.this)
                        .setMessage("Anh chị vui lòng hoàn tất hồ sơ trước khi nộp: \n " + text)
                        .setPositiveButton(getString(R.string.confirm_ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create();
                dialog.show();
            }
        }
    }

    private void sendImageStreamingToServerTask(final Context context,
                                                final List<HorizontalRecyclerItemModel> lst, final int posOfItemAdapter,
                                                final int postion, final String docTypeID, final String docTypeName, final boolean isEndLst) {

        new AsyncTask<Void, Integer, String>() {

            @Override
            protected String doInBackground(Void... params) {
                /**
                 * @author nn.tai
                 * @modify Nov 29, 2017
                 */
                File file = new File(lst.get(postion).getDocItem().getPath());

                if (file.exists()) {

                    // check file has upload
                    boolean isUpload = true;
                    if (!TextUtils.isEmpty(lst.get(postion).getDocItem().getStatus())) {
                        if (DataClient.getInstance(context).getAppDatabase().documentDao().getDocumentUploadedByDocTypeID(lst.get(postion).getDocItem().getId(), DocumentEntity.STATUS_UPLOAD_SUCCESS).size() > 0) {
                            isUpload = false;
                            updateClaimSubmitComplete();
                        }
                    }

                    if (isUpload) {
                        try {
                            myLog.e(TAG, "Doc id = " + lst.get(postion).getDocItem().getId());
                            myLog.e(TAG, "Doc pos " + postion);
                            myLog.e(TAG, "DoctypeID " + docTypeID + " - DocName = " + lst.get(postion).getDocItem().getDocEntityID());

                            // change status of Document to uploading
                            lst.get(postion).getDocItem().setStatus(DocumentEntity.STATUS_UPLOADING);
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    rvAdapter.notifyDataSetChanged();
                                }
                            });

                            String encodedString = FileUtils.convertFileToEncodeStringBase64(file);

                            UploadClaimImageRequest data = new UploadClaimImageRequest();
                            data.setUserID(CustomPref.getUserName(context));
                            data.setClientID(CustomPref.getUserID(context));
                            data.setAPIToken(CustomPref.getAPIToken(context));
                            data.setDeviceID(Utilities.getDeviceID(context));
                            data.setOS(Utilities.getDeviceOS());
                            data.setProject(Constant.Project_ID);
                            data.setAuthentication(Constant.Project_Authentication);

                            data.setDocProcessID(Constant.CLAIMS_DOCPROCESS_ID);
                            data.setProposalID(claimSuppEntity.getPolicyNo());
                            data.setSubmissionID(claimSuppEntity.getSubmissionID() + "");
                            data.setDocTypeID(docTypeID);
                            data.setDocNumber((postion + 1) + "");
                            data.setNumberOfPage((postion + 1) + "");
                            data.setImage(encodedString);

                            BaseRequest request = new BaseRequest();
                            request.setJsonDataInput(data);
                            // finally, execute the request
                            Call<UploadClaimImageResponse> call = svRequester.UploadClaimImage(request);
                            call.enqueue(new Callback<UploadClaimImageResponse>() {

                                @Override
                                public void onResponse(Call<UploadClaimImageResponse> callback,
                                                       Response<UploadClaimImageResponse> response) {

                                    if (response.isSuccessful()) {
                                        UploadClaimImageResponse result = response.body();
                                        if (result != null) {
                                            if (!TextUtils.isEmpty(result.getStreamResult().getResult())) {

                                                // if sucess update success status
                                                if (result.getStreamResult().getResult().equals("true")) {

                                                    lst.get(postion).getDocItem().setStatus(DocumentEntity.STATUS_UPLOAD_SUCCESS);
                                                    DataClient.getInstance(context).getAppDatabase().documentDao().update(lst.get(postion).getDocItem());
                                                    uihandler.sendEmptyMessage(1);
                                                } else if (result.getStreamResult().getResult().equals("false")) {

                                                    // update failed status
                                                    lst.get(postion).getDocItem().setStatus(DocumentEntity.STATUS_UPLOAD_FAILED);
                                                    DataClient.getInstance(context).getAppDatabase().documentDao().update(lst.get(postion).getDocItem());
                                                    uihandler.sendEmptyMessage(1);
                                                }
                                                rvAdapter.notifyItemChanged(posOfItemAdapter);
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<UploadClaimImageResponse> callback, Throwable t) {

                                }
                            });
                        } catch (IOException e) {
                            myLog.printTrace(e);
                        }
                    } else
                        myLog.e(TAG, "File STATUS  " + lst.get(postion).getDocItem().getStatus());
                } else
                    myLog.e(TAG, "File NOT EXIST " + lst.get(postion).getDocItem().getPath());
                return null;
            }
        }.execute();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View v = getCurrentFocus();
        if (v != null && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE)
                && v instanceof EditText) {
            int scrcoords[] = new int[2];
            v.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + v.getLeft() - scrcoords[0];
            float y = ev.getRawY() + v.getTop() - scrcoords[1];

            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom())
                Utilities.hideSoftInputKeyboard(this, v);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void OnItemClaimClick(ClaimListDocModel claimListDocItem,
                                 int position, HorizontalRecyclerAdapter adapter) {
        if (claimListDocItem.getLstDoc().get(position) != null) {
            myLog.e(TAG, "OnItemClaimClick ID = " + claimListDocItem.getClaimDocItem().getId() + " titleCode = " + claimListDocItem.getClaimDocItem().getSubDocID() + " Name = " + claimListDocItem.getClaimDocItem().getDocName() + " Pos = " + position);

            claimDocSelected = claimListDocItem;
            // edit Image
            if (claimListDocItem.getLstDoc().get(position).getStatus() != HorizontalRecyclerItemModel.STATE_ADD_IMAGE) {

                showDialogViewImageFromAdapter(ClaimsSupplementActivity.this, claimListDocItem.getClaimDocItem().getDocName(), position, claimListDocItem.getLstDoc(), adapter);
            } else {
                // capture/chose new image
                showDialogPickImage(ClaimsSupplementActivity.this);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String formattedDate = df.format(c.getTime());

        switch (requestCode) {
            case Constant.REQUEST_CODE_CAMERA:
                if (resultCode == Activity.RESULT_OK) {
                    if (pathCapture != null) {
                        myLog.e(TAG, "capture file result = " + spathCapture);

                        DocumentEntity doc = new DocumentEntity();
                        doc.setPath(spathCapture);
                        doc.setStatus(DocumentEntity.STATUS_NOT_UPLOAD);
                        doc.setCreateDate(formattedDate);
                        doc.setUpdatedDate(formattedDate);
                        doc.setDocEntityID(claimDocSelected.getClaimDocItem().getId());

                        long id = DataClient.getInstance(this).getAppDatabase().documentDao().insert(doc);
                        myLog.e(TAG, "save Document item to DB id = " + id);
                        if (id > 0) {
                            doc.setId(id);
                            claimDocSelected.getLstDoc().add(claimDocSelected.getLstDoc().size() - 1, new HorizontalRecyclerItemModel(doc, 0));
                            claimDocSelected.setEdit(true);
                            spathCapture = null;
                        }
                    }
                }

                break;
            case Constant.REQUEST_CODE_GALLERY:
                if (resultCode == Activity.RESULT_OK) {
                    Uri selectedImage = data.getData();
                    if (selectedImage != null) {

                        String path = FileUtils.getRealPathFromURI(this, selectedImage);
                        DocumentEntity doc = new DocumentEntity();
                        doc.setPath(path);
                        doc.setStatus(DocumentEntity.STATUS_NOT_UPLOAD);
                        doc.setCreateDate(formattedDate);
                        doc.setUpdatedDate(formattedDate);
                        doc.setDocEntityID(claimDocSelected.getClaimDocItem().getId());

                        long id = DataClient.getInstance(this).getAppDatabase().documentDao().insert(doc);
                        myLog.e(TAG, "save Document item to DB id = " + id);
                        if (id > 0) {
                            doc.setId(id);
                            claimDocSelected.getLstDoc().add(claimDocSelected.getLstDoc().size() - 1, new HorizontalRecyclerItemModel(doc, 0));
                            claimDocSelected.setEdit(true);
                        }
                    }
                }
                break;
        }

        //find and update doctype item
        if (resultCode == Activity.RESULT_OK) {
            if (claimDocSelected != null) {
                myLog.e(TAG, "ActivityResult update claimDoc selected");
                for (int i = 0; i < lsData.size(); i++) {
                    if (lsData.get(i).getClaimDocItem().getSubDocID().equalsIgnoreCase(claimDocSelected.getClaimDocItem().getSubDocID())) {
                        lsData.set(i, claimDocSelected);
                    }
                }
                rvAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * Get Claim ID for supplement from server Task
     */
    private class GetSubmissionIDTask extends AsyncTask<Void, Void, Response<CPGetClaimIDResponse>> {

        Context context;
        ProgressDialog process;
        String submisParentID, claimParentID;

        public GetSubmissionIDTask(Context c, String submissID, String claimID) {
            context = c;
            submisParentID = submissID;
            claimParentID = claimID;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (process == null)
                process = new ProgressDialog(context);
            process.show();
        }

        @Override
        protected Response<CPGetClaimIDResponse> doInBackground(Void... voids) {
            Response<CPGetClaimIDResponse> res = null;

            try {
                CPGetClaimIDRequest data = new CPGetClaimIDRequest();
                if (TextUtils.isEmpty(CustomPref.getUserName(context)))
                    data.setUserLogin(CustomPref.getUserID(context));
                else
                    data.setUserLogin(CustomPref.getUserName(context));
                data.setClientID(CustomPref.getUserID(context));
                data.setAPIToken(CustomPref.getAPIToken(context));
                data.setDeviceId(Utilities.getDeviceID(context));
                data.setOS(Utilities.getDeviceOS());
                data.setProject(Constant.Project_ID);
                data.setAuthentication(Constant.Project_Authentication);

                data.setDocProcessID(Constant.CLAIMS_DOCPROCESS_ID);
                data.setPolicyNo(claimSuppEntity.getPolicyNo());
                data.setClaimType(claimSuppEntity.getClaimsType());
                data.setClaimID(claimParentID);
                data.setSubmissionID(submisParentID);

                BaseRequest request = new BaseRequest();
                request.setJsonDataInput(data);

                Call<CPGetClaimIDResponse> call = svRequester.GetClaimID(request);
                res = call.execute();
            } catch (IOException e) {
                myLog.printTrace(e);
            }

            return res;
        }

        @Override
        protected void onPostExecute(Response<CPGetClaimIDResponse> res) {
            super.onPostExecute(res);
            process.dismiss();

            if (res.isSuccessful()) {
                CPGetClaimIDResult result = res.body().getResponse();
                if (result != null) {
                    if (result.getResult().equalsIgnoreCase("true")) {
                        String mes = result.getMessage();
                        if (!TextUtils.isEmpty(mes)) {

                            myLog.e(TAG, "getSubmissionID Bổ sung = " + mes);
                            //split String: <ClaimID>;<SubmissionID>
                            String[] s = mes.split(";");
                            if (s.length == 2) {
                                try {
                                    Calendar c = Calendar.getInstance();
                                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                    String formattedDate = df.format(c.getTime());

                                    claimSuppEntity.setClaimsID(s[0]);

                                    claimSuppEntity.setSubmissionID(Long.valueOf(s[1]));
                                    claimSuppEntity.setUpdateDate(formattedDate);
                                    DataClient.getInstance(context).getAppDatabase().claimDao().update(claimSuppEntity);

                                    if (claimsFromRequest == null) {
                                        claimsFromRequest = new ClaimsFromRequest();
                                    }

                                    ClaimEntity parent = DataClient.getInstance(context).getAppDatabase().claimDao().getClaimsByID(claimSuppEntity.getMainClaimID());
                                    if (parent != null) {
                                        claimsFromRequest.setSubmissionID(parent.getSubmissionID() + "");

                                        List<DocTypeComment> lsDoc = new ArrayList<>();
                                        for (int i = 0; i < lsData.size(); i++) {

                                            //If ds Doc <=1 thì có nghĩa case này gửi Doc Missing
                                            if (lsData.get(i).getLstDoc().size() > 1)
                                                lsDoc.add(new DocTypeComment(lsData.get(i).getClaimDocItem().getDocTypeID(), lsData.get(i).getClaimDocItem().getWfAddDocID(),
                                                        lsData.get(i).getClaimDocItem().getSubDocID(), lsData.get(i).getClaimDocItem().getClaimClientComment(), ""));
                                            else
                                                lsDoc.add(new DocTypeComment(lsData.get(i).getClaimDocItem().getDocTypeID(), lsData.get(i).getClaimDocItem().getWfAddDocID(),
                                                        lsData.get(i).getClaimDocItem().getSubDocID(), lsData.get(i).getClaimDocItem().getClaimClientComment(), Constant.CLAIMS_DOCPTYPE_MISSING));
                                        }
                                        new sendClaimsFormInfoTask(context, lsDoc).execute();
                                    }
                                } catch (NumberFormatException e) {
                                    myLog.printTrace(e);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private class GetClaimStatusTask extends AsyncTask<Void, Void, Response<GetClaimsStatusResponse>> {

        Context context;
        ProgressDialog process;

        public GetClaimStatusTask(Context c) {
            context = c;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (process == null)
                process = new ProgressDialog(context);
            process.show();
        }

        @Override
        protected Response<GetClaimsStatusResponse> doInBackground(Void... voids) {
            Response<GetClaimsStatusResponse> res = null;

            try {
                GetClaimsStatusRequest data = new GetClaimsStatusRequest();
                if (TextUtils.isEmpty(CustomPref.getUserName(context)))
                    data.setUserLogin(CustomPref.getUserID(context));
                else
                    data.setUserLogin(CustomPref.getUserName(context));
                data.setClientID(CustomPref.getUserID(context));
                data.setAPIToken(CustomPref.getAPIToken(context));
                data.setDeviceId(Utilities.getDeviceID(context));
                data.setOs(Utilities.getDeviceOS());
                data.setProject(Constant.Project_ID);
                data.setAuthentication(Constant.Project_Authentication);

                data.setSubmissionID(claimSuppEntity.getSubmissionID() + "");

                BaseRequest request = new BaseRequest();
                request.setJsonDataInput(data);

                Call<GetClaimsStatusResponse> call = svRequester.GetClaimsStatus(request);
                res = call.execute();
            } catch (IOException e) {
                myLog.printTrace(e);
            }

            return res;
        }

        @Override
        protected void onPostExecute(Response<GetClaimsStatusResponse> res) {
            super.onPostExecute(res);
            process.dismiss();

            if (res.isSuccessful()) {
                GetClaimsStatusResult result = res.body().getResponse();
                if (result != null) {
                    if (result.getResult().equalsIgnoreCase("true")) {
                        String[] sarr = result.getMessage().split(";");

                        if (sarr.length > 0) {
                            String message = sarr[0];
                            if (!TextUtils.isEmpty(message)) {
                                if (!isSupplement) {
                                    if (message.toUpperCase().equalsIgnoreCase("WAITWF")) {
                                        statusSubmit = Constant.StatusSubmit.DANGXULY;
                                        statusDoc = Constant.StatusDoc.DA_NOP_HO_SO;
                                    } else if (message.toUpperCase().equalsIgnoreCase("WFHOLD")) {
                                        statusSubmit = Constant.StatusSubmit.CHOBOSUNG;
                                        statusDoc = Constant.StatusDoc.CHO_BO_SUNG;

                                    } else if (message.toUpperCase().equals("INVALID")) {
                                        statusDoc = Constant.StatusDoc.CHUA_DU_GIAY_TO;
                                    } else if (message.toUpperCase().equals("REJECTED")) {
                                        statusSubmit = Constant.StatusSubmit.CHUAGUI;
                                        statusDoc = Constant.StatusDoc.TU_CHOI_BOI_PS;
                                        isRejected = true;

                                        MyCustomDialog dialog = new MyCustomDialog.Builder(context)
                                                .setMessage("Hồ sơ bị từ chối, vui lòng thực hiện lại.")
                                                .setPositiveButton(getString(R.string.confirm_ok), new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                }).create();
                                        dialog.show();

                                    } else if (message.toUpperCase().equals("SUBMITTED")) {
                                        statusSubmit = Constant.StatusSubmit.DAGUI;
                                        statusDoc = Constant.StatusDoc.DA_NOP_HO_SO;
                                    }
                                } else {

                                }
                            }
                            updateStatusViews();
                        }

                    }
                }
            }
        }
    }

    /**
     * Task get LifeInsuredList
     */
    private class GetDocTypeListTask extends AsyncTask<Void, Void, Response<CPGetDocTypeResponse>> {

        Context context;
        String claimType;
        String action;
        ProgressDialog process;

        public GetDocTypeListTask(Context c, String claimsType, String action) {
            this.context = c;
            this.claimType = claimsType;
            this.action = action;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (process == null)
                process = new ProgressDialog(context);
            process.show();
        }

        @Override
        protected Response<CPGetDocTypeResponse> doInBackground(Void... voids) {
            Response<CPGetDocTypeResponse> response = null;

            try {
                CPGetDocTypeRequest data = new CPGetDocTypeRequest();
                if (TextUtils.isEmpty(CustomPref.getUserName(context)))
                    data.setUserLogin(CustomPref.getUserID(context));
                else
                    data.setUserLogin(CustomPref.getUserName(context));
                data.setAPIToken(CustomPref.getAPIToken(context));
                data.setDeviceId(Utilities.getDeviceID(context));
                data.setOS(Utilities.getDeviceOS());
                data.setProject(Constant.Project_ID);
                data.setAuthentication(Constant.Project_Authentication);

                data.setClaimType(claimType);
                data.setAction(action);
                data.setSubmissionID(parentSubmissID + "");

                BaseRequest request = new BaseRequest();
                request.setJsonDataInput(data);

                Call<CPGetDocTypeResponse> call = svRequester.CPGetDocType(request);
                response = call.execute();

            } catch (IOException e) {
                myLog.printTrace(e);
            }

            return response;
        }

        @Override
        protected void onPostExecute(Response<CPGetDocTypeResponse> response) {
            super.onPostExecute(response);
            process.dismiss();

            if (response != null)
                if (response.isSuccessful()) {
                    CPGetDocTypeResponse rp = response.body();
                    if (rp != null) {
                        CPGetDocTypeResult result = rp.getResponse();
                        if (result != null) {
                            if (result.getResult().equalsIgnoreCase("true")) {
                                if (result.getClaimDocTypes() != null) {
                                    lsDocType = result.getClaimDocTypes();
                                    if (lsDocType != null) {

                                        List<ClaimDocEntity> ls = DataClient.getInstance(context).getAppDatabase().claimDocDao().getListDocTypeByClaimID(claimSuppEntity.getId());

                                        //remove all doctype đã có trong supplement dưới db
                                        for (int i = 0; i < lsDocType.size(); i++) {
                                            for (ClaimDocEntity mo : ls) {
                                                if (lsDocType.get(i).getSubDocId().equalsIgnoreCase(mo.getSubDocID())) {
                                                    lsDocType.remove(i);
                                                    i--;
                                                    break;
                                                }
                                            }
                                        }

                                        //add các doctype sau khi loại trùng và có used = 1 và0 DB và list
                                        for (int i = 0; i < lsDocType.size(); i++) {
                                            if (lsDocType.get(i).getUsed().equalsIgnoreCase("1")) {

                                                List<HorizontalRecyclerItemModel> lstTemp = new ArrayList<>();

                                                ClaimDocEntity claimDoc = new ClaimDocEntity();
                                                claimDoc.setClaimsEntityID(claimSuppEntity.getId());
                                                claimDoc.setDocID(lsDocType.get(i).getDocID());
                                                claimDoc.setDocName(lsDocType.get(i).getDocName());
                                                claimDoc.setDocTypeID(lsDocType.get(i).getDocTypeID());
                                                claimDoc.setDocTypeName(lsDocType.get(i).getDocTypeName());
                                                claimDoc.setSubDocID(lsDocType.get(i).getSubDocId());
                                                claimDoc.setOrder(lsDocType.get(i).getOrder());
                                                claimDoc.setSubOrder(lsDocType.get(i).getSubDocOrder());
                                                claimDoc.setUsed(lsDocType.get(i).getUsed());
                                                claimDoc.setClaimAdminComment(lsDocType.get(i).getClaimAdminComment());
                                                claimDoc.setWfAddDocID(lsDocType.get(i).getWFAddDocID());

                                                if (!claimDoc.getDocTypeID().equalsIgnoreCase(Constant.CLAIMS_DOCTYPE_QUESTION))
                                                    lstTemp.add(new HorizontalRecyclerItemModel(new DocumentEntity(),
                                                            HorizontalRecyclerItemModel.STATE_ADD_IMAGE));

                                                long id = DataClient.getInstance(context).getAppDatabase().claimDocDao().insert(claimDoc);
                                                myLog.e(TAG, "save ClaimDoc id = " + id);
                                                if (id > 0) {
                                                    claimDoc.setId(id);

                                                    lsData.add(new ClaimListDocModel(ClaimListDocAdapter.ITEM_NORMAL,
                                                            claimDoc, lstTemp, isEditMode, true, true));
                                                    lsDocType.remove(i);
                                                    i--;
                                                }
                                            }
                                        }
                                        if (rvAdapter == null) {
                                            rvAdapter = new ClaimListDocAdapter(context, lsData, ClaimsSupplementActivity.this);
                                            rvContent.setAdapter(rvAdapter);
                                        }
                                        rvAdapter.notifyDataSetChanged();

                                        if (lsDocType.size() <= 0) {
                                            tvAddDoc.setTextColor(Color.LTGRAY);
                                            tvAddDoc.setEnabled(false);
                                        }
                                    }
                                }
                            } else {

                                if (result.getNewAPIToken().equalsIgnoreCase(Constant.ERROR_TOKENINVALID)) {
                                    Utilities.processLoginAgain(context, getString(R.string.message_alert_relogin));
                                }
//                                else {
//                                    MyCustomDialog dialog = new MyCustomDialog.Builder(context)
//                                            .setMessage("Yêu cầu đã được gửi không thành công! Xin vui lòng thử lại.")
//                                            .setPositiveButton(getString(R.string.confirm_ok), new DialogInterface.OnClickListener() {
//                                                @Override
//                                                public void onClick(DialogInterface dialog, int which) {
//                                                    dialog.dismiss();
//                                                }
//                                            }).create();
//                                    dialog.show();
//                                }
                            }
                        }
                    }
                }
        }
    }

    /**
     * Task send Form CLaims to server
     */
    private class sendClaimsFormInfoTask extends AsyncTask<Void, Void, Response<CPSubmitFormResponse>> {

        Context context;
        ProgressBar processBar;
        List<DocTypeComment> lsComment;

        public sendClaimsFormInfoTask(Context c, List<DocTypeComment> ls) {
            this.context = c;
            this.lsComment = ls;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (processBar == null)
                processBar = new ProgressBar(context, null, android.R.attr.progressBarStyleSmall);
            processBar.setIndeterminate(true);
            processBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Response<CPSubmitFormResponse> doInBackground(Void... voids) {
            Response<CPSubmitFormResponse> response = null;

            try {

                if (TextUtils.isEmpty(CustomPref.getUserName(context)))
                    claimsFromRequest.setUserLogin(CustomPref.getUserID(context));
                else
                    claimsFromRequest.setUserLogin(CustomPref.getUserName(context));
                claimsFromRequest.setClientID(CustomPref.getUserID(context));
                claimsFromRequest.setAPIToken(CustomPref.getAPIToken(context));
                claimsFromRequest.setDeviceId(Utilities.getDeviceID(context));
                claimsFromRequest.setOs(Utilities.getDeviceOS());
                claimsFromRequest.setProject(Constant.Project_ID);
                claimsFromRequest.setAuthentication(Constant.Project_Authentication);

                claimsFromRequest.setAction(Constant.CLAIMS_ACTION_HOLD_REQUEST);
                claimsFromRequest.setClaimType(claimSuppEntity.getClaimsType());
                //truyền SubmissionID của thằng cha
                claimsFromRequest.setSubmissionID(parentSubmissID + "");
                claimsFromRequest.setDocTypeComments(lsComment);

                BaseRequest request = new BaseRequest();
                request.setJsonDataInput(claimsFromRequest);

                Call<CPSubmitFormResponse> call = svRequester.SubmitFormContact(request);
                response = call.execute();

            } catch (IOException e) {
                myLog.printTrace(e);
            }

            return response;
        }

        @Override
        protected void onPostExecute(Response<CPSubmitFormResponse> response) {
            super.onPostExecute(response);
            processBar.setVisibility(View.GONE);

            if (response != null)
                if (response.isSuccessful()) {
                    CPSubmitFormResponse rp = response.body();
                    if (rp != null) {
                        CPSubmitFormResult result = rp.getResponse();
                        if (result != null) {
                            if (result.getResult().equalsIgnoreCase("true")) {

                                for (int i = 0; i < lsData.size(); i++) {
                                    DataClient.getInstance(context).getAppDatabase().claimDocDao().update(lsData.get(i).getClaimDocItem());
                                }
                                uploadDocuments();
                            } else {
                                if (result.getNewAPIToken().equalsIgnoreCase(Constant.ERROR_TOKENINVALID)) {
                                    Utilities.processLoginAgain(context, getString(R.string.message_alert_relogin));
                                } else {
                                    MyCustomDialog dialog = new MyCustomDialog.Builder(context)
                                            .setMessage("Lỗi: " + result.getErrLog())
                                            .setPositiveButton(getString(R.string.confirm_ok), new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                    finish();
                                                }
                                            }).create();
                                    dialog.show();
                                }
                            }
                        }
                    }
                }
        }
    }

    /**
     * Dialog option pick image by Camera OR Gallery
     *
     * @param context
     */
    private void showDialogPickImage(final Context context) {

        AlertDialog.Builder alerDialog = new AlertDialog.Builder(context);
        LayoutInflater li = LayoutInflater.from(context);
        View view = li.inflate(R.layout.dialog_pick_image, null);
        alerDialog.setView(view);
        final AlertDialog dialog = alerDialog.create();
        dialog.show();

        Button btnCamera = dialog.findViewById(R.id.btnCamera);
        Button btnGallery = dialog.findViewById(R.id.btnGallery);
        RelativeLayout rllQuayLai = dialog.findViewById(R.id.rllQuayLai);

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String file = Calendar.getInstance().getTimeInMillis() + ".jpg";
                File newfile = null;
                try {
                    newfile = new File(getExternalCacheDir().getAbsolutePath() + "/" + file);
                    newfile.createNewFile();
                    spathCapture = newfile.getAbsolutePath();
                    myLog.e(TAG, "create file Capture = " + spathCapture);
                } catch (IOException e) {
                    myLog.printTrace(e);
                }
                pathCapture = FileProvider.getUriForFile(context,
                        BuildConfig.APPLICATION_ID + ".provider", newfile);

                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                takePicture.putExtra(MediaStore.EXTRA_OUTPUT, pathCapture);
                startActivityForResult(takePicture, Constant.REQUEST_CODE_CAMERA);
                dialog.dismiss();
            }
        });

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, Constant.REQUEST_CODE_GALLERY);
                dialog.dismiss();
            }
        });

        rllQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    /**
     * Dialog view image choosen
     *
     * @param context
     * @param title
     * @param position
     * @param lstImage
     * @param adapterImage
     */
    public void showDialogViewImageFromAdapter(final Context context, final String title,
                                               final int position,
                                               final List<HorizontalRecyclerItemModel> lstImage,
                                               final HorizontalRecyclerAdapter adapterImage) {

        AlertDialog.Builder alerDialog = new AlertDialog.Builder(context);
        LayoutInflater li = LayoutInflater.from(context);
        View view = li.inflate(R.layout.dialog_view_image, null);
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

        TextView tvTitle = dialog.findViewById(R.id.tvTitle);
        tvTitle.setText(title);

        final TextView tvPage = dialog.findViewById(R.id.tvPage);

        ViewPager viewPager = dialog.findViewById(R.id.vpPages);

        viewPager.setAdapter(new ViewImagePagerAdapter(context, lstImage));
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int pos) {
                tvPage.setText("Trang " + (pos + 1));
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
        viewPager.setCurrentItem(position);
        tvPage.setText("Trang " + (position + 1));

        TextView btnEdit = dialog.findViewById(R.id.btnEditImage);
        btnEdit.setVisibility(View.GONE);

        ImageButton btnClose = dialog.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    /**
     * Dialog show list doctype choice
     *
     * @param context
     */
    public void showDialogChoiceDocType(final Context context) {
        SingleListAdapter adapter = null;
        ListView lvList;

        AlertDialog.Builder alerDialog = new AlertDialog.Builder(context);
        LayoutInflater li = LayoutInflater.from(context);
        View view = li.inflate(R.layout.dialog_choice_doctype, null);
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
        dialog.setCanceledOnTouchOutside(true);

        ImageButton btnClose = dialog.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        lvList = dialog.findViewById(R.id.lvList);
        final List<SingleChoiceModel> lsDocTypeTemp = new ArrayList<>();
        if (lsDocType != null) {


            for (int i = 0; i < lsDocType.size(); i++)
                lsDocTypeTemp.add(new SingleChoiceModel(0, lsDocType.get(i).getSubDocId(), lsDocType.get(i).getDocTypeName() + " - " + lsDocType.get(i).getDocName()));

            if (adapter == null)
                adapter = new SingleListAdapter(context, R.layout.item_list_singlechoice, lsDocTypeTemp);
            lvList.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                SingleChoiceModel item = lsDocTypeTemp.get(position);
                if (!TextUtils.isEmpty(item.getCode())) {

                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    String formattedDate = df.format(c.getTime());

                    for (CPClaimDocType mo : lsDocType)
                        if (mo.getSubDocId().equalsIgnoreCase(item.getCode())) {
                            //initDocType
                            List<HorizontalRecyclerItemModel> lstTemp = new ArrayList<>();

                            ClaimDocEntity claimDoc = new ClaimDocEntity();
                            claimDoc.setClaimsEntityID(claimSuppEntity.getId());
                            claimDoc.setDocID(mo.getDocID());
                            claimDoc.setDocName(mo.getDocName());
                            claimDoc.setDocTypeID(mo.getDocTypeID());
                            claimDoc.setDocTypeName(mo.getDocTypeName());
                            claimDoc.setSubDocID(mo.getSubDocId());
                            claimDoc.setOrder(mo.getOrder());
                            claimDoc.setSubOrder(mo.getSubDocOrder());
                            claimDoc.setUsed(mo.getUsed());
                            claimDoc.setCreateDate(formattedDate);
                            claimDoc.setUpdateDate(formattedDate);

                            lstTemp.add(new HorizontalRecyclerItemModel(new DocumentEntity(),
                                    HorizontalRecyclerItemModel.STATE_ADD_IMAGE));

                            long idDoc = DataClient.getInstance(context).getAppDatabase().claimDocDao().insert(claimDoc);
                            if (idDoc > 0) {
                                claimDoc.setId(idDoc);

                                lsData.add(new ClaimListDocModel(ClaimListDocAdapter.ITEM_NORMAL,
                                        claimDoc, lstTemp, true, false, true));
                                rvAdapter.notifyDataSetChanged();
                                rvContent.smoothScrollToPosition(lsData.size() - 1);

                                lsDocType.remove(mo);
                                break;
                            }
                        }

                    if (dialog.isShowing())
                        dialog.dismiss();
                }
            }
        });
    }

    public void showAlertCheckDocumentDialog(String alertMsg) {

        final Dialog dialog = new Dialog(ClaimsSupplementActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_check_document);
        dialog.setCanceledOnTouchOutside(false);

        TextView tvAlert = dialog.findViewById(R.id.tvAlert);
        tvAlert.setText(alertMsg);

        Button cancel = dialog.findViewById(R.id.btnOK);
        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (dialog.isShowing())
                    dialog.dismiss();
            }
        });

        Button continues = dialog.findViewById(R.id.btnContinue);
        continues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ClaimEntity parent = DataClient.getInstance(ClaimsSupplementActivity.this).getAppDatabase().claimDao().getClaimsByID(claimSuppEntity.getMainClaimID());
                if (parent != null) {
                    // Kiem tra xem submissionID da ton tai chua
                    if (claimSuppEntity.getSubmissionID() > 0) {
                        if (claimsFromRequest == null) {
                            claimsFromRequest = new ClaimsFromRequest();
                        }

                        claimsFromRequest.setSubmissionID(parent.getSubmissionID() + "");

                        List<DocTypeComment> lsDoc = new ArrayList<>();
                        for (int i = 0; i < lsData.size(); i++) {

                            //update client comment before upload supplement
                            DataClient.getInstance(ClaimsSupplementActivity.this).getAppDatabase().claimDocDao().update(lsData.get(i).getClaimDocItem());

                            //If ds Doc <=1 thì có nghĩa case này gửi Doc Missing
                            if (lsData.get(i).getLstDoc().size() > 1)
                                lsDoc.add(new DocTypeComment(lsData.get(i).getClaimDocItem().getDocTypeID(), lsData.get(i).getClaimDocItem().getWfAddDocID(),
                                        lsData.get(i).getClaimDocItem().getSubDocID(), lsData.get(i).getClaimDocItem().getClaimClientComment(), ""));
                            else
                                lsDoc.add(new DocTypeComment(lsData.get(i).getClaimDocItem().getDocTypeID(), lsData.get(i).getClaimDocItem().getWfAddDocID(),
                                        lsData.get(i).getClaimDocItem().getSubDocID(), lsData.get(i).getClaimDocItem().getClaimClientComment(), Constant.CLAIMS_DOCPTYPE_MISSING));
                        }
                        new sendClaimsFormInfoTask(ClaimsSupplementActivity.this, lsDoc).execute();
                    } else {
                        new GetSubmissionIDTask(ClaimsSupplementActivity.this, String.valueOf(parent.getSubmissionID()), parent.getClaimsID()).execute();
                    }
                }

                if (dialog.isShowing())
                    dialog.dismiss();
            }
        });

        if (!(ClaimsSupplementActivity.this).isFinishing())
            dialog.show();
    }

    public void showAlertSubmitDocumentDialog() {

        final Dialog dialog = new Dialog(ClaimsSupplementActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_submiss_document);
        dialog.setCanceledOnTouchOutside(false);

        Button btnOK = dialog.findViewById(R.id.btnOK);
        btnOK.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (dialog.isShowing())
                    dialog.dismiss();

                ClaimEntity parent = DataClient.getInstance(ClaimsSupplementActivity.this).getAppDatabase().claimDao().getClaimsByID(claimSuppEntity.getMainClaimID());
                if (parent != null) {
                    // Kiem tra xem submissionID da ton tai chua
                    if (claimSuppEntity.getSubmissionID() > 0) {
                        if (claimsFromRequest == null) {
                            claimsFromRequest = new ClaimsFromRequest();
                        }

                        claimsFromRequest.setSubmissionID(parent.getSubmissionID() + "");

                        List<DocTypeComment> lsDoc = new ArrayList<>();
                        for (int i = 0; i < lsData.size(); i++) {

                            //update client comment before upload supplement
                            DataClient.getInstance(ClaimsSupplementActivity.this).getAppDatabase().claimDocDao().update(lsData.get(i).getClaimDocItem());

                            //If ds Doc <=1 thì có nghĩa case này gửi Doc Missing
                            if (lsData.get(i).getLstDoc().size() > 1)
                                lsDoc.add(new DocTypeComment(lsData.get(i).getClaimDocItem().getDocTypeID(), lsData.get(i).getClaimDocItem().getWfAddDocID(),
                                        lsData.get(i).getClaimDocItem().getSubDocID(), lsData.get(i).getClaimDocItem().getClaimClientComment(), ""));
                            else
                                lsDoc.add(new DocTypeComment(lsData.get(i).getClaimDocItem().getDocTypeID(), lsData.get(i).getClaimDocItem().getWfAddDocID(),
                                        lsData.get(i).getClaimDocItem().getSubDocID(), lsData.get(i).getClaimDocItem().getClaimClientComment(), Constant.CLAIMS_DOCPTYPE_MISSING));
                        }
                        new sendClaimsFormInfoTask(ClaimsSupplementActivity.this, lsDoc).execute();
                    } else {
                        new GetSubmissionIDTask(ClaimsSupplementActivity.this, String.valueOf(parent.getSubmissionID()), parent.getClaimsID()).execute();
                    }
                }
            }
        });

        Button cancel = (Button) dialog.findViewById(R.id.btnCancel);
        cancel.setOnClickListener(new View.OnClickListener()

        {

            @Override
            public void onClick(View v) {
                if (dialog.isShowing())
                    dialog.dismiss();
            }
        });

        if (!(ClaimsSupplementActivity.this).
                isFinishing())
            dialog.show();
    }

    /**
     * UI handler for control Status Document Upload
     */
    private Handler uihandler = new Handler() {
        /*
         * @see android.os.Handler#handleMessage(android.os.Message)
         */
        @Override
        public void handleMessage(Message msg) {
            /**
             * @author nn.tai
             * @modify Nov 27, 2017
             */
            switch (msg.what) {
                case 1:
                    statusDoc = Constant.StatusDoc.DANG_NOP_HO_SO;
                    updateStatusViews();
                    rvAdapter.notifyDataSetChanged();

                    // if (isEndLst)
                    updateClaimSubmitComplete();
                    break;

                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
}
