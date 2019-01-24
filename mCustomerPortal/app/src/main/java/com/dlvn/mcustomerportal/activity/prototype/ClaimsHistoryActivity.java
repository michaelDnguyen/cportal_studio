package com.dlvn.mcustomerportal.activity.prototype;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.activity.OTPActivity;
import com.dlvn.mcustomerportal.adapter.ClaimsHistoryAdapter;
import com.dlvn.mcustomerportal.adapter.PointAccountAdapter;
import com.dlvn.mcustomerportal.adapter.listener.RecyclerViewClickListener;
import com.dlvn.mcustomerportal.adapter.listener.RecyclerViewTouchListener;
import com.dlvn.mcustomerportal.base.BaseActivity;
import com.dlvn.mcustomerportal.common.Constant;
import com.dlvn.mcustomerportal.common.CustomPref;
import com.dlvn.mcustomerportal.database.DataClient;
import com.dlvn.mcustomerportal.database.entity.ClaimEntity;
import com.dlvn.mcustomerportal.services.ServicesGenerator;
import com.dlvn.mcustomerportal.services.ServicesRequest;
import com.dlvn.mcustomerportal.services.model.BaseRequest;
import com.dlvn.mcustomerportal.services.model.claims.SyncClaimDetailResponse;
import com.dlvn.mcustomerportal.services.model.claims.SyncClaimDetailResult;
import com.dlvn.mcustomerportal.services.model.claims.SyncCLaimDetailModel;
import com.dlvn.mcustomerportal.services.model.request.GetClientProfileByCLIIDRequest;
import com.dlvn.mcustomerportal.utils.CPSaveLogTask;
import com.dlvn.mcustomerportal.utils.DialogUtils;
import com.dlvn.mcustomerportal.utils.Utilities;
import com.dlvn.mcustomerportal.utils.myLog;
import com.dlvn.mcustomerportal.view.MyCustomDialog;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;

public class ClaimsHistoryActivity extends BaseActivity {

    private static final String TAG = "ClaimsHistoryActivity";

    List<ClaimEntity> listData;
    ClaimsHistoryAdapter adapter;
    RecyclerView rvClaims;

    SwipeRefreshLayout swipeContainer;
    LinearLayout lloBack;

    ServicesRequest svRequester;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claims_history);
        getViews();
        setListener();

    }

    @Override
    protected void onResume() {
        super.onResume();
        new CPSaveLogTask(this, Constant.CPLOG_CLAIMLIST_OPEN).execute();
        initData();
    }

    @Override
    protected void onStop() {
        super.onStop();
        new CPSaveLogTask(this, Constant.CPLOG_CLAIMLIST_CLOSE).execute();
    }

    private void getViews() {
        svRequester = ServicesGenerator.createService(ServicesRequest.class);

        lloBack = this.findViewById(R.id.lloBack);
        swipeContainer = this.findViewById(R.id.swipeContainer);
        rvClaims = this.findViewById(R.id.rvClaims);
    }

    private void initData() {
        if (listData == null)
            listData = new ArrayList<>();

        if (adapter == null)
            adapter = new ClaimsHistoryAdapter(this, listData);
        rvClaims.setAdapter(adapter);

        //test number item
        List<ClaimEntity> lsTemp1 = new ArrayList<>();
        lsTemp1 = DataClient.getInstance(ClaimsHistoryActivity.this).getAppDatabase().claimDao().getAllClaims();
        if (lsTemp1 != null) {
            lsTemp1.clear();
            lsTemp1 = null;
        }
        //end test

        List<ClaimEntity> lsTemp = new ArrayList<>();
        lsTemp = DataClient.getInstance(ClaimsHistoryActivity.this).getAppDatabase().claimDao().getAllClaimsNoSupplement();
        //nếu ko có data local thì check online sync về
        new GetClaimsListTask(this, lsTemp).execute();
    }

    private void setListener() {
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                initData();
            }
        });

        lloBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        rvClaims.addOnItemTouchListener(new RecyclerViewTouchListener(this, rvClaims, new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {

                if (listData.get(position).getCodeGroup() != PointAccountAdapter.CODE_TITLE) {
                    ClaimEntity item = listData.get(position);
                    if (item != null) {
                        myLog.e(TAG, "Claims Selected ID = " + item.getId() + " - SubID = " + item.getSubmissionID());
                        Intent intent = new Intent(ClaimsHistoryActivity.this, ClaimsAddPhotoActivity.class);
                        intent.putExtra(Constant.CLAIMS_INTKEY_CLAIMS_ID, item.getId());
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private void filterDataToList(List<ClaimEntity> lsTemp1) {

        if (listData != null)
            listData.clear();

        List<ClaimEntity> lsTemp = new ArrayList<>();
        lsTemp = DataClient.getInstance(ClaimsHistoryActivity.this).getAppDatabase().claimDao().getAllClaimsNoSupplement();
        //filter by month date
        if (lsTemp.size() > 0) {
            List<ClaimEntity> lsHnay = new ArrayList<>();
            List<ClaimEntity> lsHqua = new ArrayList<>();
            List<ClaimEntity> lsLauhon = new ArrayList<>();

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date dtNow = new Date();

            for (ClaimEntity mo : lsTemp) {
                try {
                    Date cur = format.parse(mo.getCreateDate());
                    long days = TimeUnit.DAYS.convert(dtNow.getTime() - cur.getTime(), TimeUnit.MILLISECONDS);
                    if (days == 0)
                        lsHnay.add(mo);
                    else if (days == 1)
                        lsHqua.add(mo);
                    else
                        lsLauhon.add(mo);

                } catch (ParseException e) {
                    myLog.printTrace(e);
                }
            }

            if (lsHnay.size() > 0) {
                lsHnay.add(0, new ClaimEntity("Hôm nay", ClaimsHistoryAdapter.CODE_TITLE));
                listData.addAll(lsHnay);
            }
            if (lsHqua.size() > 0) {
                lsHqua.add(0, new ClaimEntity("Hôm qua", ClaimsHistoryAdapter.CODE_TITLE));
                listData.addAll(lsHqua);
            }
            if (lsLauhon.size() > 0) {
                lsLauhon.add(0, new ClaimEntity("Trước đó", ClaimsHistoryAdapter.CODE_TITLE));
                listData.addAll(lsLauhon);
            }
        }

        adapter.setData(listData);
        rvClaims.setAdapter(adapter);
    }

    private class GetClaimsListTask extends AsyncTask<Void, Void, Response<SyncClaimDetailResponse>> {

        Context context;
        List<ClaimEntity> lsClaims;

        public GetClaimsListTask(Context c, List<ClaimEntity> ls) {
            this.context = c;
            lsClaims = ls;
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            swipeContainer.setRefreshing(true);
        }

        @Override
        protected Response<SyncClaimDetailResponse> doInBackground(Void... voids) {
            Response<SyncClaimDetailResponse> response = null;

            try {
                GetClientProfileByCLIIDRequest data = new GetClientProfileByCLIIDRequest();

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

                data.setAction(Constant.CLAIMS_ACTION_SYNCLISTCLAIMS);

                BaseRequest request = new BaseRequest();
                request.setJsonDataInput(data);

                Call<SyncClaimDetailResponse> call = svRequester.SyncClaimListByCLIID(request);
                response = call.execute();

            } catch (IOException e) {
                myLog.printTrace(e);
            }

            return response;
        }

        @Override
        protected void onPostExecute(Response<SyncClaimDetailResponse> response) {
            super.onPostExecute(response);
            swipeContainer.setRefreshing(false);

            List<SyncCLaimDetailModel> lst = null;
            if (response != null)
                if (response.isSuccessful()) {
                    SyncClaimDetailResponse rp = response.body();
                    if (rp != null) {
                        SyncClaimDetailResult result = rp.getResponse();
                        if (result != null) {
                            if (result.getResult().equalsIgnoreCase("true")) {
                                if (result.getLsClaimDetail() != null)
                                    if (result.getLsClaimDetail().size() > 0) {
                                        lst = result.getLsClaimDetail();
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

            //check and remove exist
            if (lst != null) {

                //remove duplicate by SubmissionID & ClaimID
                for (ClaimEntity mo : lsClaims) {
                    for (int i = 0; i < lst.size(); i++)
                        if (lst.get(i).getClaimSub().getSubmissionID().equalsIgnoreCase(String.valueOf(mo.getSubmissionID()))) {

                            //update status
                            if (lst.get(i).getClaimSub().getStatus().equalsIgnoreCase("WAITWF"))
                                mo.setStatus(Constant.StatusSubmit.DANGXULY.getStringValue());
                            else if (lst.get(i).getClaimSub().getStatus().equalsIgnoreCase("WFHOLD"))
                                mo.setStatus(Constant.StatusSubmit.CHOBOSUNG.getStringValue());
                            else if (lst.get(i).getClaimSub().getStatus().equalsIgnoreCase("TEMPSAVED"))
                                mo.setStatus(Constant.StatusSubmit.DANGCHODUYET.getStringValue());

                            DataClient.getInstance(context).getAppDatabase().claimDao().update(mo);

                            lst.remove(i);
                            i--;
                        }
                }

                //add all item after remove duplicate
                for (SyncCLaimDetailModel mo : lst) {
                    ClaimEntity entity = new ClaimEntity();
                    entity.setPolicyNo(mo.getClaimSub().getPolicyNo());
                    entity.setClaimsType(mo.getClaimSub().getClaimType());
                    entity.setClaimsID(mo.getClaimSub().getClaimID());
                    entity.setSubmissionID(Long.valueOf(mo.getClaimSub().getSubmissionID()));
                    entity.setUpdateDate(mo.getClaimSub().getDateSubmission());
                    entity.setCreateDate(mo.getClaimSub().getDateSubmission());
                    if (mo.getClaimSub().getStatus().equalsIgnoreCase("WFHOLD"))
                        entity.setStatus(Constant.StatusSubmit.CHOBOSUNG.getStringValue());
                    else
                        entity.setStatus(Constant.StatusSubmit.DAGUI.getStringValue());

                    long id = DataClient.getInstance(context).getAppDatabase().claimDao().insert(entity);
                    if (id > 0) {
                        entity.setId(id);
                        myLog.e(TAG, "insert Claim Entity Sync " + entity.getSubmissionID() + " - " + entity.getClaimsID() + " ID=" + id);
                    }
                    lsClaims.add(entity);
                }
            }
            filterDataToList(lsClaims);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
