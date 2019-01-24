package com.dlvn.mcustomerportal.activity.prototype;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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
import com.dlvn.mcustomerportal.services.model.claims.ClaimsFromRequest;
import com.dlvn.mcustomerportal.services.model.claims.CPGetClaimIDRequest;
import com.dlvn.mcustomerportal.services.model.claims.CPGetDocTypeRequest;
import com.dlvn.mcustomerportal.services.model.claims.GetClaimsStatusRequest;
import com.dlvn.mcustomerportal.services.model.claims.GetClaimsStatusResponse;
import com.dlvn.mcustomerportal.services.model.claims.GetClaimsStatusResult;
import com.dlvn.mcustomerportal.services.model.claims.SyncClaimDetailRequest;
import com.dlvn.mcustomerportal.services.model.claims.SyncClaimDetailResponse;
import com.dlvn.mcustomerportal.services.model.claims.SyncClaimDetailResult;
import com.dlvn.mcustomerportal.services.model.claims.SyncClaimDocType;
import com.dlvn.mcustomerportal.services.model.claims.SyncClaimDocTypeHold;
import com.dlvn.mcustomerportal.services.model.claims.SyncClaimSub;
import com.dlvn.mcustomerportal.services.model.claims.SyncCLaimDetailModel;
import com.dlvn.mcustomerportal.services.model.claims.UploadClaimImageRequest;
import com.dlvn.mcustomerportal.services.model.claims.CPClaimDocType;
import com.dlvn.mcustomerportal.services.model.claims.CPGetClaimIDResponse;
import com.dlvn.mcustomerportal.services.model.claims.CPGetClaimIDResult;
import com.dlvn.mcustomerportal.services.model.claims.CPGetDocTypeResponse;
import com.dlvn.mcustomerportal.services.model.claims.CPGetDocTypeResult;
import com.dlvn.mcustomerportal.services.model.response.CPSubmitFormResponse;
import com.dlvn.mcustomerportal.services.model.response.CPSubmitFormResult;
import com.dlvn.mcustomerportal.services.model.claims.UploadClaimImageResponse;
import com.dlvn.mcustomerportal.utils.CPSaveLogTask;
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
 * Thông tin giấy tờ cần thiết/ chứng từ claims
 */
public class ClaimsAddPhotoActivity extends BaseActivity implements OnItemHorizontalViewClick {

    private static final String TAG = "ClaimsAddPhotoActivity";

    LinearLayout lloBack, lloToolbar;
    TextView tvStep, tvAddDoc;
    AppCompatSeekBar sbStep;
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
    ClaimEntity claimEntity = null;
    //status of Claims
    Constant.StatusSubmit statusSubmit = Constant.StatusSubmit.CHUAGUI;
    //status of Doc item
    Constant.StatusDoc statusDoc = Constant.StatusDoc.DANG_TAO_HO_SO;

    //uri path capture from camera or gallery
    Uri pathCapture = null;
    String spathCapture = null;
    //Objective contain request form
    ClaimsFromRequest claimsFromRequest;
    //Claim Detail Info
    SyncCLaimDetailModel syncCLaimDetailModel;

    //flag for Bo sung ho so
    boolean isSupplement = false;
    //flag for EDIT or VIEW MODE
    boolean isEditMode = true;
    //flag for Claims Rejected
    boolean isRejected = false;

    ServicesRequest svRequester;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claims_add_photo);

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

        tvStep = findViewById(R.id.tvStep);
        tvStep.setText("3/4");
        sbStep = findViewById(R.id.sbStep);
        sbStep.setThumb(null);
        sbStep.setEnabled(false);
        sbStep.setMax(4);
        sbStep.setProgress(3);

        tvAddDoc = findViewById(R.id.tvAddDoc);

        btnCapNhat = findViewById(R.id.btnCapNhat);
        rvContent = findViewById(R.id.rvContent);
        RecyclerView.LayoutManager layout = new RecyclerSmoothLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvContent.setLayoutManager(layout);
    }

    private void initDatas() {
        lsData = new ArrayList<>();

        //get claims request info
        if (getIntent().getExtras() != null)

            //check claims is exist
            if (getIntent().getExtras().containsKey(Constant.CLAIMS_INTKEY_CLAIMS_ID)) {
                long id = getIntent().getLongExtra(Constant.CLAIMS_INTKEY_CLAIMS_ID, 0);
                claimEntity = DataClient.getInstance(this).getAppDatabase().claimDao().getClaimsByID(id);

                if (claimEntity == null) {
                    MyCustomDialog dialog = new MyCustomDialog.Builder(this)
                            .setMessage(getString(R.string.alert_claims_no_claims_item))
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
            } else {

                claimsFromRequest = CustomPref.getClaimsTemp(this);
                //check data form request exist
                if (claimsFromRequest != null) {

                    setTitle("Thông tin chi tiết");

                    //Tao moi claimEntity in DB
                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    String formattedDate = df.format(c.getTime());

                    claimEntity = new ClaimEntity();
                    claimEntity.setClaimsType(claimsFromRequest.getClaimType());
                    claimEntity.setClaimsName(claimsFromRequest.getClaimName());
                    claimEntity.setPolicyNo(claimsFromRequest.getPolicyNo());
                    claimEntity.setStatus(statusSubmit.getStringValue());
                    //default ko là con của ai
                    claimEntity.setMainClaimID(0);
                    claimEntity.setCreateDate(formattedDate);
                    claimEntity.setUpdateDate(formattedDate);
                    long id = DataClient.getInstance(this).getAppDatabase().claimDao().insert(claimEntity);
                    if (id > 0) {
                        myLog.e(TAG, "save CLaim to DB id = " + id);
                        claimEntity.setId(id);

                        if (!TextUtils.isEmpty(claimsFromRequest.getClaimType()))
                            //get DocType list by ClaimsType
                            new GetDocTypeListTask(this, claimsFromRequest.getClaimType(), "").execute();
                    }
                } else {
                    MyCustomDialog dialog = new MyCustomDialog.Builder(this)
                            .setMessage(getString(R.string.alert_claims_no_requester_info))
                            .setPositiveButton(getString(R.string.confirm_ok), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    btnCapNhat.setActivated(false);
                                    dialog.dismiss();
                                }
                            }).create();
                    dialog.show();
                }
            }
    }

    private void loadClaimsItemToViews(Context context) {

        if (claimEntity.getStatus().equalsIgnoreCase(Constant.StatusSubmit.DAGUI.getStringValue()) ||
                claimEntity.getStatus().equalsIgnoreCase(Constant.StatusSubmit.DANGXULY.getStringValue()) ||
                claimEntity.getStatus().equalsIgnoreCase(Constant.StatusSubmit.DANGCHODUYET.getStringValue())) {
            statusSubmit = Constant.StatusSubmit.DAGUI;
            statusDoc = Constant.StatusDoc.DA_NOP_HO_SO;
            isEditMode = false;
            isSupplement = false;
        } else if (claimEntity.getStatus().equalsIgnoreCase(Constant.StatusSubmit.CHOBOSUNG.getStringValue()) ||
                claimEntity.getStatus().equalsIgnoreCase(Constant.StatusSubmit.CHONOP_HSG.getStringValue())) {
            statusSubmit = Constant.StatusSubmit.CHOBOSUNG;
            statusDoc = Constant.StatusDoc.CHO_BO_SUNG;
            isEditMode = false;
            isSupplement = true;
        } else {
            statusSubmit = Constant.StatusSubmit.CHUAGUI;
            statusDoc = Constant.StatusDoc.CHUA_NOP_HO_SO;
            isEditMode = true;
            isSupplement = false;
        }

        new GetClaimStatusTask(this).execute();

        new GetClaimsDetailTask(this, claimEntity.getClaimsID()).execute();

        List<ClaimDocEntity> ls = DataClient.getInstance(context).getAppDatabase().claimDocDao().getListDocTypeByClaimID(claimEntity.getId());
        if (ls != null) {
            for (ClaimDocEntity mo : ls) {
                List<DocumentEntity> lsDoc = DataClient.getInstance(context).getAppDatabase().documentDao().getDocumentByDocTypeID(mo.getId());
                if (lsDoc.size() > 0) {
                    List<HorizontalRecyclerItemModel> lstTemp = new ArrayList<>();
                    for (int i = 0; i < lsDoc.size(); i++) {
                        lstTemp.add(new HorizontalRecyclerItemModel(lsDoc.get(i), 0));
                    }

                    lsData.add(new ClaimListDocModel(ClaimListDocAdapter.ITEM_NORMAL,
                            mo, lstTemp, isEditMode, false, false));
                } else {
                    //Nếu có DocType mà chưa chụp document nào
                    List<HorizontalRecyclerItemModel> lstTemp = new ArrayList<>();
                    lstTemp.add(new HorizontalRecyclerItemModel(new DocumentEntity(),
                            HorizontalRecyclerItemModel.STATE_ADD_IMAGE));
                    lsData.add(new ClaimListDocModel(ClaimListDocAdapter.ITEM_NORMAL,
                            mo, lstTemp, isEditMode, false, false));
                }
            }
        }

        //load list supplement into view
        List<ClaimEntity> supps = DataClient.getInstance(ClaimsAddPhotoActivity.this).getAppDatabase().claimDao().getClaimsSuppByID(claimEntity.getId());
        if (supps != null) {
            for (int i = 0; i < supps.size(); i++) {
                //create item add to foooter
                ClaimDocEntity temp = new ClaimDocEntity();
                temp.setCreateDate(supps.get(i).getCreateDate());
                temp.setUpdateDate(supps.get(i).getUpdateDate());
                temp.setId(supps.get(i).getId());
                //send into submisstionID for in supplement request new submiss by parent
                temp.setClaimsEntityID(claimEntity.getSubmissionID());
                //send status of child
                temp.setNote(Constant.StatusSubmit.fromValue(supps.get(i).getStatus()).toString());

                List<HorizontalRecyclerItemModel> lstTemp = new ArrayList<>();

                lsData.add(new ClaimListDocModel(ClaimListDocAdapter.ITEM_FOOTER, temp, lstTemp, isEditMode, false, false));
            }
        }

        if (rvAdapter == null) {
            rvAdapter = new ClaimListDocAdapter(context, lsData, ClaimsAddPhotoActivity.this);
            rvContent.setAdapter(rvAdapter);
        }
        rvAdapter.notifyDataSetChanged();
    }

    private void setListener() {
        btnCapNhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isSupplement) {
                    String text = checkDocumentSubmit();
                    if (!TextUtils.isEmpty(text)) {
                        showAlertCheckDocumentDialog(text);
                        statusDoc = statusDoc.CHUA_DU_GIAY_TO;
                    } else
                        showAlertSubmitDocumentDialog();
                } else if (claimEntity != null) {

                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    String currentTime = format.format(Calendar.getInstance().getTime());

                    ClaimEntity supp = null;
                    List<ClaimEntity> supps = DataClient.getInstance(ClaimsAddPhotoActivity.this).getAppDatabase().claimDao().getClaimsSuppByID(claimEntity.getId());
                    long id = 0;
                    if (supps.size() > 0)
                        for (ClaimEntity mo : supps) {
                            if (!mo.getStatus().equalsIgnoreCase(Constant.StatusSubmit.DAGUI.getStringValue())) {
                                supp = mo;
                            }
                        }

                    if (supp == null) {
                        supp = new ClaimEntity();
                        supp.setClaimsType(claimEntity.getClaimsType());
                        supp.setClaimsName(Constant.CLAIMS_TYPE_SUPPLEMENT_NAME);
                        supp.setStatus(Constant.StatusSubmit.CHUAGUI.getStringValue());
                        supp.setMainClaimID(claimEntity.getId());
                        supp.setPolicyNo(claimEntity.getPolicyNo());
                        supp.setCreateDate(currentTime);
                        supp.setUpdateDate(currentTime);

                        statusSubmit = Constant.StatusSubmit.CHOBOSUNG;
                        claimEntity.setStatus(statusSubmit.getStringValue());
                        DataClient.getInstance(ClaimsAddPhotoActivity.this).getAppDatabase().claimDao().update(claimEntity);

                        id = DataClient.getInstance(ClaimsAddPhotoActivity.this).getAppDatabase().claimDao().insert(supp);
                    } else {
                        id = supp.getId();
                        if (supp.getStatus().equalsIgnoreCase(Constant.StatusSubmit.DAGUI.getStringValue())) {
                            statusSubmit = Constant.StatusSubmit.DANGXULY;
                            claimEntity.setStatus(statusSubmit.getStringValue());
                            DataClient.getInstance(ClaimsAddPhotoActivity.this).getAppDatabase().claimDao().update(claimEntity);
                        }
                    }

                    //open screen for SUPPLEMENT
                    if (id > 0) {
                        Intent intent = new Intent(ClaimsAddPhotoActivity.this, ClaimsSupplementActivity.class);
                        intent.putExtra(Constant.CLAIMS_INTKEY_CLAIMS_SUPPLEMENT_ID, id);
                        intent.putExtra(Constant.CLAIMS_INTKEY_SUPPLEMENT_SUBMISSIONID_PARENT, claimEntity.getSubmissionID());
                        startActivity(intent);
                    }
                }
            }
        });

        tvAddDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogChoiceDocType(ClaimsAddPhotoActivity.this);
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
        List<ClaimDocEntity> arrDocType = DataClient.getInstance(ClaimsAddPhotoActivity.this).getAppDatabase().claimDocDao().getListDocTypeByClaimID(claimEntity.getId());

        if (arrDocType != null && arrDocType.size() > 0) {
            for (ClaimDocEntity docType : arrDocType) {

                List<DocumentEntity> arrDoc = DataClient.getInstance(ClaimsAddPhotoActivity.this).getAppDatabase().documentDao().getDocumentByDocTypeID(docType.getId());
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
            statusDoc = statusDoc.DA_NOP_HO_SO;

            claimEntity.setStatus(statusSubmit.getStringValue());
            DataClient.getInstance(ClaimsAddPhotoActivity.this).getAppDatabase().claimDao().update(claimEntity);

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

            MyCustomDialog dialog = new MyCustomDialog.Builder(ClaimsAddPhotoActivity.this)
                    .setMessage("Hồ sơ yêu cầu bảo hiểm đã nộp thành công!")
                    .setCancelOnTouchOut(false)
                    .setPositiveButton(getString(R.string.confirm_ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            //clear claims temp
                            CustomPref.saveClaimsTemp(ClaimsAddPhotoActivity.this, null);
                            //goto Daskboard
                            Intent intent = new Intent(ClaimsAddPhotoActivity.this, DashboardActivity.class);
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
                    //TH đang nộp
                    if (statusDoc.equals(Constant.StatusDoc.DANG_NOP_HO_SO) || statusDoc.equals(Constant.StatusDoc.NOP_HO_SO_THAT_BAI))
                        isEditMode = false;
                    else
                        isEditMode = true;

                    //TH chưa nộp do chờ bổ sung
                    if (isSupplement) {
                        btnCapNhat.setText("Bổ sung");
                        btnCapNhat.setBackgroundResource(R.drawable.state_btn_red_radius_gradien);
                        btnCapNhat.setEnabled(true);
                    } else
                        btnCapNhat.setText("Yêu cầu bảo hiểm");

                    if (statusDoc.equals(Constant.StatusDoc.NOP_HO_SO_THAT_BAI)) {
                        btnCapNhat.setText("Thử lại");
                        btnCapNhat.setBackgroundResource(R.drawable.state_btn_red_radius_gradien);
                        btnCapNhat.setEnabled(true);
                    }

                } else if (statusSubmit.equals(Constant.StatusSubmit.CHOBOSUNG)) {
                    //TH chờ bổ sung
                    isEditMode = false;
                    isSupplement = true;
                    btnCapNhat.setText("Bổ sung");
                    btnCapNhat.setBackgroundResource(R.drawable.state_btn_red_radius_gradien);
                    btnCapNhat.setEnabled(true);

                } else if (statusSubmit.equals(Constant.StatusSubmit.DANGCHODUYET)) {
                    //TH đang xử lý WFWAIT
                    isEditMode = false;
                    isSupplement = false;
                    btnCapNhat.setText("Bổ sung");
                    btnCapNhat.setBackgroundResource(R.drawable.state_btn_grey_rectangle);
                    btnCapNhat.setEnabled(false);

                } else if (statusSubmit.equals(Constant.StatusSubmit.DANGXULY)) {
                    //TH đang xử lý TEMPSAVED
                    isEditMode = false;
                    isSupplement = false;
                    btnCapNhat.setText("Bổ sung");
                    btnCapNhat.setBackgroundResource(R.drawable.state_btn_grey_rectangle);
                    btnCapNhat.setEnabled(false);

                } else if (statusSubmit.equals(Constant.StatusSubmit.TAMDINHCHI)) {
                    //TH bị Rejected
                    isEditMode = false;
                    isSupplement = false;
                    btnCapNhat.setText("Thực hiện lại");
                    btnCapNhat.setBackgroundResource(R.drawable.state_btn_red_radius_gradien);
                    btnCapNhat.setEnabled(true);
                } else {
                    //TH đã nộp
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

    /**
     * UI handler for control Status Document Upload
     */
    @SuppressLint("HandlerLeak")
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
                                sendImageStreamingToServerTask(ClaimsAddPhotoActivity.this, lst, i, k,
                                        lsData.get(i).getClaimDocItem().getDocTypeID(), lsData.get(i).getClaimDocItem().getDocTypeName(), isEndLst);
                            }
                        }
                    }
                }
            } else {
                MyCustomDialog dialog = new MyCustomDialog.Builder(ClaimsAddPhotoActivity.this)
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
                            data.setProposalID(claimsFromRequest.getPolicyNo());
                            data.setSubmissionID(claimEntity.getSubmissionID() + "");
                            data.setDocTypeID(docTypeID);
                            data.setDocNumber((postion + 1) + "");
                            data.setNumberOfPage((postion + 1) + "");
                            data.setDocTypeName(docTypeName);
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
                                            myLog.e(TAG, "uploadFile = " + result.getStreamResult().toString());
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
    public void setTitle(CharSequence title) {
        super.setTitle(title);

        TextView tvHeaderTitle = findViewById(R.id.tvHeaderTitle);
        if (tvHeaderTitle != null) {
            tvHeaderTitle.setText(title);

            tvHeaderTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (syncCLaimDetailModel != null)
                        showClaimDetailDialog(ClaimsAddPhotoActivity.this, syncCLaimDetailModel);
                    else {
                        if (claimsFromRequest != null) {
                            syncCLaimDetailModel = new SyncCLaimDetailModel();
                            syncCLaimDetailModel.setClaimRequester(claimsFromRequest);
                            syncCLaimDetailModel.setClaimsItem(claimsFromRequest.getClaimDeath().get(0));
                            syncCLaimDetailModel.setTreatmentHistorys(claimsFromRequest.getTreatmentHistorys());
                            SyncClaimSub claimSub = new SyncClaimSub();
                            claimSub.setClaimType(claimsFromRequest.getClaimType());
                            claimSub.setPolicyNo(claimsFromRequest.getPolicyNo());
                            claimSub.setCustomerID(claimsFromRequest.getClientID());
                            claimSub.setClaimID(claimEntity.getClaimsID());
                            claimSub.setSubmissionID(claimEntity.getSubmissionID() + "");
                            claimSub.setStatus("Chưa nộp");
                            syncCLaimDetailModel.setClaimSub(claimSub);
                            showClaimDetailDialog(ClaimsAddPhotoActivity.this, syncCLaimDetailModel);
                        }
                    }
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        new CPSaveLogTask(this, Constant.CPLOG_CLAIMDETAIL_OPEN).execute();
    }

    @Override
    protected void onStop() {
        super.onStop();
        new CPSaveLogTask(this, Constant.CPLOG_CLAIMDETAIL_CLOSE).execute();
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
            myLog.e(TAG, "OnItemClaimClick ID = " + claimListDocItem.getClaimDocItem().getId() + " titleCode = " +
                    claimListDocItem.getClaimDocItem().getSubDocID() + " Name = " + claimListDocItem.getClaimDocItem().getDocName() + " Pos = " + position);

            claimDocSelected = claimListDocItem;
            // edit Image
            if (claimListDocItem.getLstDoc().get(position).getStatus() != HorizontalRecyclerItemModel.STATE_ADD_IMAGE) {

                showDialogViewImageFromAdapter(ClaimsAddPhotoActivity.this, claimListDocItem.getClaimDocItem().getDocName(),
                        position, claimListDocItem.getLstDoc());
            } else {
                // capture/chose new image
                showDialogPickImage(ClaimsAddPhotoActivity.this);
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
     * Get Claim ID from server Task
     */
    private class GetSubmissionIDTask extends AsyncTask<Void, Void, Response<CPGetClaimIDResponse>> {

        Context context;
        ProgressDialog process;

        public GetSubmissionIDTask(Context c) {
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
        protected Response<CPGetClaimIDResponse> doInBackground(Void... voids) {
            Response<CPGetClaimIDResponse> res = null;

            try {
                CPGetClaimIDRequest data = new CPGetClaimIDRequest();
                if (!TextUtils.isEmpty(CustomPref.getUserName(context)))
                    data.setUserLogin(CustomPref.getUserName(context));
                else
                    data.setUserLogin(CustomPref.getUserID(context));
                data.setClientID(CustomPref.getUserID(context));
                data.setAPIToken(CustomPref.getAPIToken(context));
                data.setDeviceId(Utilities.getDeviceID(context));
                data.setOS(Utilities.getDeviceOS());
                data.setProject(Constant.Project_ID);
                data.setAuthentication(Constant.Project_Authentication);

                data.setDocProcessID(Constant.CLAIMS_DOCPROCESS_ID);
                data.setPolicyNo(claimsFromRequest.getPolicyNo());
                data.setClaimSubType(claimsFromRequest.getClaimType());
                if (claimsFromRequest.getClaimType().contains("Healthcare"))
                    data.setClaimType("Healthcare");
                else
                    data.setClaimType(claimsFromRequest.getClaimType());

//                if (claimEntity != null)
//                    data.setSubmissionID(String.valueOf(claimEntity.getSubmissionID()));

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

                            //split string: <ClaimsID>;<SubmissionID>
                            String[] s = mes.split(";");
                            if (s.length == 2) {
                                try {
                                    Calendar c = Calendar.getInstance();
                                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                    String formattedDate = df.format(c.getTime());

                                    claimEntity.setClaimsID(s[0]);
                                    claimEntity.setSubmissionID(Integer.parseInt(s[1]));
                                    claimsFromRequest.setSubmissionID(s[1]);
                                    claimEntity.setUpdateDate(formattedDate);
                                    long id = DataClient.getInstance(context).getAppDatabase().claimDao().update(claimEntity);
                                    if (id > 0)
                                        new sendClaimsFormInfoTask(context).execute();
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
                if (!TextUtils.isEmpty(CustomPref.getUserName(context)))
                    data.setUserLogin(CustomPref.getUserName(context));
                else
                    data.setUserLogin(CustomPref.getUserID(context));
                data.setClientID(CustomPref.getUserID(context));
                data.setAPIToken(CustomPref.getAPIToken(context));
                data.setDeviceId(Utilities.getDeviceID(context));
                data.setOs(Utilities.getDeviceOS());
                data.setProject(Constant.Project_ID);
                data.setAuthentication(Constant.Project_Authentication);

                data.setSubmissionID(claimEntity.getSubmissionID() + "");

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

            if (res != null)
                if (res.isSuccessful()) {
                    GetClaimsStatusResult result = res.body().getResponse();
                    if (result != null) {
                        if (result.getResult().equalsIgnoreCase("true")) {
                            String[] sarr = result.getMessage().split(";");

                            if (sarr.length > 0) {
                                String message = sarr[0];
                                if (!TextUtils.isEmpty(message)) {
//                                if (!isSupplement) {
                                    if (message.toUpperCase().equalsIgnoreCase("WAITWF")) {
                                        statusSubmit = Constant.StatusSubmit.DANGXULY;
                                        statusDoc = Constant.StatusDoc.DA_NOP_HO_SO;

                                    } else if (message.toUpperCase().equalsIgnoreCase("WFHOLD")) {
                                        statusSubmit = Constant.StatusSubmit.CHOBOSUNG;
                                        statusDoc = Constant.StatusDoc.CHO_BO_SUNG;
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
                                    } else if (message.toUpperCase().equals("TEMPSAVED")) {
                                        statusSubmit = Constant.StatusSubmit.DANGCHODUYET;
                                        statusDoc = Constant.StatusDoc.DA_NOP_HO_SO;
                                    }
//                                } else {
//
//                                }
                                }
                            }
                        }
                    }
                }
            updateStatusViews();
        }
    }

    /**
     * Task send Form CLaims to server
     */
    private class sendClaimsFormInfoTask extends AsyncTask<Void, Void, Response<CPSubmitFormResponse>> {

        Context context;
        ProgressBar processBar;

        public sendClaimsFormInfoTask(Context c) {
            this.context = c;
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
                if (!TextUtils.isEmpty(CustomPref.getUserName(context)))
                    claimsFromRequest.setUserLogin(CustomPref.getUserName(context));
                else
                    claimsFromRequest.setUserLogin(CustomPref.getUserID(context));

                claimsFromRequest.setClientID(CustomPref.getUserID(context));
                claimsFromRequest.setAPIToken(CustomPref.getAPIToken(context));
                claimsFromRequest.setDeviceId(Utilities.getDeviceID(context));
                claimsFromRequest.setOs(Utilities.getDeviceOS());
                claimsFromRequest.setProject(Constant.Project_ID);
                claimsFromRequest.setAuthentication(Constant.Project_Authentication);

                claimsFromRequest.setAction(Constant.CLAIMS_ACTION_REQUEST);

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
                                uploadDocuments();
                            } else {
                                if (result.getNewAPIToken().equalsIgnoreCase(Constant.ERROR_TOKENINVALID)) {
                                    Utilities.processLoginAgain(context, getString(R.string.message_alert_relogin));
                                } else {
                                    MyCustomDialog dialog = new MyCustomDialog.Builder(context)
                                            .setTitle("Thông báo")
                                            .setMessage("Yêu cầu đã được gửi không thành công! Xin vui lòng thử lại.")
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
                if (!TextUtils.isEmpty(CustomPref.getUserName(context)))
                    data.setUserLogin(CustomPref.getUserName(context));
                else
                    data.setUserLogin(CustomPref.getUserID(context));
                data.setClientID(CustomPref.getUserID(context));
                data.setAPIToken(CustomPref.getAPIToken(context));
                data.setDeviceId(Utilities.getDeviceID(context));
                data.setOS(Utilities.getDeviceOS());
                data.setProject(Constant.Project_ID);
                data.setAuthentication(Constant.Project_Authentication);

                data.setClaimType(claimType);
                data.setAction(action);

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

                                        for (int i = 0; i < lsDocType.size(); i++) {
                                            if (lsDocType.get(i).getUsed().equalsIgnoreCase("1")) {

                                                List<HorizontalRecyclerItemModel> lstTemp = new ArrayList<>();

                                                ClaimDocEntity claimDoc = new ClaimDocEntity();
                                                claimDoc.setClaimsEntityID(claimEntity.getId());
                                                claimDoc.setDocID(lsDocType.get(i).getDocID());
                                                claimDoc.setDocName(lsDocType.get(i).getDocName());
                                                claimDoc.setDocTypeID(lsDocType.get(i).getDocTypeID());
                                                claimDoc.setDocTypeName(lsDocType.get(i).getDocTypeName());
                                                claimDoc.setSubDocID(lsDocType.get(i).getSubDocId());
                                                claimDoc.setOrder(lsDocType.get(i).getOrder());
                                                claimDoc.setSubOrder(lsDocType.get(i).getSubDocOrder());
                                                claimDoc.setUsed(lsDocType.get(i).getUsed());

                                                lstTemp.add(new HorizontalRecyclerItemModel(new DocumentEntity(),
                                                        HorizontalRecyclerItemModel.STATE_ADD_IMAGE));

                                                long id = DataClient.getInstance(context).getAppDatabase().claimDocDao().insert(claimDoc);
                                                myLog.e(TAG, "save ClaimDoc id = " + id);
                                                if (id > 0) {
                                                    claimDoc.setId(id);

                                                    lsData.add(new ClaimListDocModel(ClaimListDocAdapter.ITEM_NORMAL,
                                                            claimDoc, lstTemp, true, true, false));
                                                    lsDocType.remove(i);
                                                    i--;

                                                }

                                            }
                                        }

                                        rvAdapter = new ClaimListDocAdapter(context, lsData, ClaimsAddPhotoActivity.this);
                                        rvContent.setAdapter(rvAdapter);
                                    }
                                }
                            } else {

                                if (result.getNewAPIToken().equalsIgnoreCase(Constant.ERROR_TOKENINVALID)) {
                                    Utilities.processLoginAgain(context, getString(R.string.message_alert_relogin));
                                } else {
                                    MyCustomDialog dialog = new MyCustomDialog.Builder(context)
                                            .setMessage("Yêu cầu đã được gửi không thành công! Xin vui lòng thử lại.")
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
                    }
                }
        }
    }

    /**
     * Get Claims Detail from Server if Claims exist
     */
    private class GetClaimsDetailTask extends AsyncTask<Void, Void, Response<SyncClaimDetailResponse>> {

        Context context;
        String claimID = null;
        ProgressDialog process;

        public GetClaimsDetailTask(Context c, String claimid) {
            this.context = c;
            claimID = claimid;
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (process == null)
                process = new ProgressDialog(context);
            process.setIndeterminate(true);
            process.setIndeterminateDrawable(getResources().getDrawable(R.drawable.dialog_progress_cercle));
            process.show();
        }

        @Override
        protected Response<SyncClaimDetailResponse> doInBackground(Void... voids) {
            Response<SyncClaimDetailResponse> response = null;

            try {
                SyncClaimDetailRequest data = new SyncClaimDetailRequest();

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

                data.setAction(Constant.CLAIMS_ACTION_SYNCCLAIMS);
                data.setClaimID(claimID);

                BaseRequest request = new BaseRequest();
                request.setJsonDataInput(data);

                Call<SyncClaimDetailResponse> call = svRequester.SyncClaimsDetailByClaimID(request);
                response = call.execute();

            } catch (IOException e) {
                myLog.printTrace(e);
            }

            return response;
        }

        @Override
        protected void onPostExecute(Response<SyncClaimDetailResponse> response) {
            super.onPostExecute(response);
            process.dismiss();

            if (response != null)
                if (response.isSuccessful()) {
                    SyncClaimDetailResponse rp = response.body();
                    if (rp != null) {
                        SyncClaimDetailResult result = rp.getResponse();
                        if (result != null) {
                            if (result.getResult().equalsIgnoreCase("true")) {
                                if (result.getLsClaimDetail() != null) {
                                    List<SyncCLaimDetailModel> lst = result.getLsClaimDetail();
                                    if (lst != null) {
                                        syncCLaimDetailModel = lst.get(0);

                                        if (syncCLaimDetailModel != null) {
                                            setTitle("Thông tin chi tiết");

                                            boolean isRefreshData = false;
                                            //save all doc to DB
                                            if (syncCLaimDetailModel.getLstDocType() != null) {

                                                //lay danh sach doctype local
                                                List<ClaimDocEntity> lsDocLocal = DataClient.getInstance(context).getAppDatabase().claimDocDao().getListDocTypeByClaimID(claimEntity.getId());
                                                //remove all doctype duplicate
                                                for (int i = 0; i < lsDocLocal.size(); i++) {
                                                    //remove all DocType local da co
                                                    for (int a = 0; a < syncCLaimDetailModel.getLstDocType().size(); a++)
                                                        if (syncCLaimDetailModel.getLstDocType().get(a).getDocTypeID().equalsIgnoreCase(lsDocLocal.get(i).getDocTypeID())) {
                                                            syncCLaimDetailModel.getLstDocType().remove(a);
                                                            a--;
                                                        }
                                                }

                                                //insert doctype not exist in DB
                                                for (SyncClaimDocType mo : syncCLaimDetailModel.getLstDocType()) {

                                                    ClaimDocEntity claimDoc = new ClaimDocEntity();
                                                    claimDoc.setClaimsEntityID(claimEntity.getId());
                                                    claimDoc.setDocTypeID(mo.getDocTypeID());
                                                    claimDoc.setDocTypeName(mo.getDocTypeName());
                                                    claimDoc.setUpdateDate(mo.getUpdatedDate());

                                                    long id = DataClient.getInstance(context).getAppDatabase().claimDocDao().insert(claimDoc);
                                                    //insert list document to DB
                                                    if (id > 0) {
                                                        claimDoc.setId(id);
                                                        isRefreshData = true;

                                                        List<String> lsDoc = mo.getLstURL();
                                                        List<DocumentEntity> lsDocEn = new ArrayList<>();
                                                        if (lsDoc != null) {
                                                            for (String url : lsDoc) {
                                                                DocumentEntity doc = new DocumentEntity();
                                                                doc.setStatus(DocumentEntity.STATUS_UPLOAD_SUCCESS);
                                                                doc.setDocEntityID(id);
                                                                doc.setUpdatedDate(mo.getUpdatedDate());
                                                                doc.setPath(url);

                                                                long idDoc = DataClient.getInstance(context).getAppDatabase().documentDao().insert(doc);
                                                                if (idDoc > 0) {
                                                                    doc.setId(idDoc);
                                                                    lsDocEn.add(doc);
                                                                }
                                                                myLog.e(TAG, "insert doc of " + mo.getDocTypeID() + " - " + idDoc + " - " + url);
                                                            }

                                                            List<HorizontalRecyclerItemModel> lstTemp = new ArrayList<>();
                                                            for (int i = 0; i < lsDoc.size(); i++) {
                                                                lstTemp.add(new HorizontalRecyclerItemModel(lsDocEn.get(i), 0));
                                                            }

                                                            lsData.add(new ClaimListDocModel(ClaimListDocAdapter.ITEM_NORMAL,
                                                                    claimDoc, lstTemp, isEditMode, false, false));
                                                        }
                                                    }
                                                }
                                            }

                                            //save all doctype HOLD if exist
                                            if (syncCLaimDetailModel.getLstDocTypeHold().size() > 0) {
                                                for (SyncClaimDocTypeHold docHold : syncCLaimDetailModel.getLstDocTypeHold()) {

                                                    ClaimEntity supp = null;
                                                    supp = DataClient.getInstance(context).getAppDatabase().claimDao().getClaimsBySubmissID(Long.valueOf(docHold.getSubmissionID()));

                                                    //If tồn tại thì lấy Supplement đầu tiên()
                                                    if (supp == null) {
                                                        supp = new ClaimEntity();
                                                        supp.setClaimsType(claimEntity.getClaimsType());
                                                        supp.setClaimsName(Constant.CLAIMS_TYPE_SUPPLEMENT_NAME);
                                                        supp.setClaimsID(claimEntity.getClaimsID());
                                                        supp.setStatus(Constant.StatusSubmit.DAGUI.getStringValue());
                                                        supp.setMainClaimID(claimEntity.getId());
                                                        supp.setPolicyNo(claimEntity.getPolicyNo());
                                                        supp.setCreateDate(claimEntity.getUpdateDate());
                                                        supp.setUpdateDate(claimEntity.getUpdateDate());

                                                        long id = DataClient.getInstance(ClaimsAddPhotoActivity.this).getAppDatabase().claimDao().insert(supp);
                                                        if (id > 0)
                                                            supp.setId(id);
                                                    }

                                                    //Check and Insert Doctype for Supplement
                                                    if (supp != null) {
                                                        List<ClaimDocEntity> lsDocLocal = DataClient.getInstance(context).getAppDatabase().claimDocDao().getListDocTypeByClaimID(supp.getId());
                                                        for (int i = 0; i < lsDocLocal.size(); i++) {
                                                            //remove all DocType local da co
                                                            for (int a = 0; a < docHold.getLstDocType().size(); a++)
                                                                if (docHold.getLstDocType().get(a).getDocTypeID().equalsIgnoreCase(lsDocLocal.get(i).getDocTypeID())) {
                                                                    docHold.getLstDocType().remove(a);
                                                                    a--;
                                                                }
                                                        }

                                                        for (SyncClaimDocType mo : docHold.getLstDocType()) {

                                                            ClaimDocEntity claimDoc = new ClaimDocEntity();
                                                            claimDoc.setClaimsEntityID(supp.getId());
                                                            claimDoc.setDocTypeID(mo.getDocTypeID());
                                                            claimDoc.setDocTypeName(mo.getDocTypeName());
                                                            claimDoc.setUpdateDate(mo.getUpdatedDate());

                                                            long id = DataClient.getInstance(context).getAppDatabase().claimDocDao().insert(claimDoc);
                                                            //insert list document to DB
                                                            if (id > 0) {
                                                                claimDoc.setId(id);
                                                                isRefreshData = true;

                                                                List<String> lsDoc = mo.getLstURL();
                                                                List<DocumentEntity> lsDocEn = new ArrayList<>();
                                                                if (lsDoc != null) {
                                                                    for (String url : lsDoc) {
                                                                        DocumentEntity doc = new DocumentEntity();
                                                                        doc.setStatus(DocumentEntity.STATUS_UPLOAD_SUCCESS);
                                                                        doc.setDocEntityID(id);
                                                                        doc.setUpdatedDate(mo.getUpdatedDate());
                                                                        doc.setPath(url);

                                                                        long idDoc = DataClient.getInstance(context).getAppDatabase().documentDao().insert(doc);
                                                                        if (idDoc > 0) {
                                                                            doc.setId(idDoc);
                                                                            lsDocEn.add(doc);
                                                                        }
                                                                        myLog.e(TAG, "insert doc of " + mo.getDocTypeID() + " - " + idDoc + " - " + url);
                                                                    }
                                                                }
                                                            }
                                                        }

                                                        if (docHold.getLstDocType().size() > 0) {
                                                            //Add supp to footer
                                                            ClaimDocEntity temp = new ClaimDocEntity();
                                                            temp.setCreateDate(supp.getCreateDate());
                                                            temp.setUpdateDate(supp.getUpdateDate());
                                                            temp.setId(supp.getId());
                                                            //send into submisstionID for in supplement request new submiss by parent
                                                            temp.setClaimsEntityID(claimEntity.getSubmissionID());
                                                            //send status of child
                                                            temp.setNote(Constant.StatusSubmit.fromValue(supp.getStatus()).toString());

                                                            List<HorizontalRecyclerItemModel> lstTemp = new ArrayList<>();
                                                            lsData.add(new ClaimListDocModel(ClaimListDocAdapter.ITEM_FOOTER, temp, lstTemp, isEditMode, false, false));
                                                        }
                                                    }
                                                }
                                            }

                                            if (isRefreshData) {
                                                if (rvAdapter == null) {
                                                    rvAdapter = new ClaimListDocAdapter(context, lsData, ClaimsAddPhotoActivity.this);
                                                    rvContent.setAdapter(rvAdapter);
                                                } else {
                                                    rvAdapter.setData(lsData);
                                                }
                                                rvAdapter.notifyDataSetChanged();
                                            }
                                        }
                                    }
                                }
                            } else {
                                if (result.getNewAPIToken().equalsIgnoreCase(Constant.ERROR_TOKENINVALID)) {
                                    Utilities.processLoginAgain(context, getString(R.string.message_alert_relogin));
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
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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
     */
    public void showDialogViewImageFromAdapter(final Context context, final String title,
                                               final int position,
                                               final List<HorizontalRecyclerItemModel> lstImage) {

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
                            claimDoc.setClaimsEntityID(claimEntity.getId());
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
                                        claimDoc, lstTemp, true, false, false));
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

        final Dialog dialog = new Dialog(ClaimsAddPhotoActivity.this);
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

        if (!(ClaimsAddPhotoActivity.this).isFinishing())
            dialog.show();
    }

    public void showAlertSubmitDocumentDialog() {

        final Dialog dialog = new Dialog(ClaimsAddPhotoActivity.this);
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

                // Kiem tra xem submissionID da ton tai chua
                if (claimEntity.getSubmissionID() > 0)
                    uploadDocuments();
                else {
                    new GetSubmissionIDTask(ClaimsAddPhotoActivity.this).execute();
                }
            }
        });

        Button cancel = (Button) dialog.findViewById(R.id.btnCancel);
        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (dialog.isShowing())
                    dialog.dismiss();
            }
        });

        if (!(ClaimsAddPhotoActivity.this).isFinishing())
            dialog.show();
    }

    /**
     * @param context
     * @param claimDetail
     */
    public void showClaimDetailDialog(Context context, final SyncCLaimDetailModel claimDetail) {

        AlertDialog.Builder alerDialog = new AlertDialog.Builder(context);
        LayoutInflater li = LayoutInflater.from(context);
        View view = li.inflate(R.layout.dialog_claim_detail, null);
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

        //init views & fill Data
        if (claimDetail.getClaimSub() != null) {
            TextView tvClaimID = dialog.findViewById(R.id.tvClaimID);
            if (claimDetail.getClaimSub().getClaimID() != null)
                tvClaimID.setText(claimDetail.getClaimSub().getClaimID());

            TextView tvCustomerID = dialog.findViewById(R.id.tvCustomerID);
            if (claimDetail.getClaimSub().getCustomerID() != null)
                tvCustomerID.setText(claimDetail.getClaimSub().getCustomerID());

            TextView tvPolicyNo = dialog.findViewById(R.id.tvPolicyNo);
            if (claimDetail.getClaimSub().getPolicyNo() != null)
                tvPolicyNo.setText(claimDetail.getClaimSub().getPolicyNo());

            TextView tvStatus = dialog.findViewById(R.id.tvStatus);
            if (claimDetail.getClaimSub().getStatus() != null)
                tvStatus.setText(claimDetail.getClaimSub().getStatus());
        }

        if (claimDetail.getClaimRequester() != null) {
            TextView tvLiName = dialog.findViewById(R.id.tvLIName);
            if (claimDetail.getClaimRequester().getLIFullName() != null)
                tvLiName.setText(claimDetail.getClaimRequester().getLIFullName());

            TextView tvLICMND = dialog.findViewById(R.id.tvLICMND);
            if (claimDetail.getClaimRequester().getLIiDNum() != null)
                tvLICMND.setText(claimDetail.getClaimRequester().getLIiDNum());

            TextView tvOccus = dialog.findViewById(R.id.tvOccuspation);
            if (claimDetail.getClaimRequester().getOccupation() != null)
                tvOccus.setText(claimDetail.getClaimRequester().getOccupation());

            TextView tvWorkPlace = dialog.findViewById(R.id.tvWorkPlace);
            if (claimDetail.getClaimRequester().getWorkPlace() != null)
                tvWorkPlace.setText(claimDetail.getClaimRequester().getWorkPlace());

            TextView tvAddress = dialog.findViewById(R.id.tvAddress);
            if (claimDetail.getClaimRequester().getAddress() != null)
                tvAddress.setText(claimDetail.getClaimRequester().getAddress());
        }

        TextView tvClaimName = dialog.findViewById(R.id.tvClaimName);
        if (claimDetail.getClaimSub().getClaimType() != null)
            tvClaimName.setText(claimDetail.getClaimSub().getClaimType());

        if (claimDetail.getClaimsItem() != null) {
            TextView tvLossDate = dialog.findViewById(R.id.tvDateLoss);
            if (claimDetail.getClaimsItem().getLossDate() != null)
                tvLossDate.setText(claimDetail.getClaimsItem().getLossDate());

            TextView tvReason = dialog.findViewById(R.id.tvReason);
            if (claimDetail.getClaimsItem().getAccidentReason() != null)
                tvReason.setText(claimDetail.getClaimsItem().getAccidentReason());
        }

        if (claimDetail.getTreatmentHistorys() != null) {
            LinearLayout lloTreatment = dialog.findViewById(R.id.lloTreament);
            //add list treatment
            if (claimDetail.getTreatmentHistorys().size() > 0) {
                //add Hospital
                for (int i = 0; i < claimDetail.getTreatmentHistorys().size(); i++)
                    if (!TextUtils.isEmpty(claimDetail.getTreatmentHistorys().get(i).getTreatmentHospital())) {
                        TextView tvHis = new TextView(context);
                        tvHis.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        tvHis.setPadding(5, 10, 5, 5);
                        tvHis.setText("Bệnh viện điều trị: " + claimDetail.getTreatmentHistorys().get(i).getTreatmentHospital());
                        lloTreatment.addView(tvHis);

                        TextView tvDate = new TextView(context);
                        tvDate.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        tvDate.setPadding(5, 10, 5, 5);
                        tvDate.setText("Ngày điều trị: " + claimDetail.getTreatmentHistorys().get(i).getTreatmentDateFrom() + " - " + claimDetail.getTreatmentHistorys().get(i).getTreatmentDateTo());
                        lloTreatment.addView(tvDate);
                    }

                //add
                for (int i = 0; i < claimDetail.getTreatmentHistorys().size(); i++)
                    if (!TextUtils.isEmpty(claimDetail.getTreatmentHistorys().get(i).getDrugName())) {
                        TextView tvDrug = new TextView(context);
                        tvDrug.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        tvDrug.setPadding(5, 10, 5, 5);
                        tvDrug.setText("Thuốc điều trị: " + claimDetail.getTreatmentHistorys().get(i).getDrugName());
                        lloTreatment.addView(tvDrug);

                        TextView tvDianostic = new TextView(context);
                        tvDianostic.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        tvDianostic.setPadding(5, 10, 5, 5);
                        tvDianostic.setText("Hướng dẫn điều trị: " + claimDetail.getTreatmentHistorys().get(i).getDiagnostic());
                        lloTreatment.addView(tvDianostic);
                    }
            }
        }

        ImageButton cancel = dialog.findViewById(R.id.btnClose);
        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (dialog.isShowing())
                    dialog.dismiss();
            }
        });

        if (!(ClaimsAddPhotoActivity.this).isFinishing())
            dialog.show();
    }
}
