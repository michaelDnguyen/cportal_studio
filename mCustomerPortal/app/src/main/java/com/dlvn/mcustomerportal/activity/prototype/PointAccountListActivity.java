package com.dlvn.mcustomerportal.activity.prototype;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.adapter.PointAccountAdapter;
import com.dlvn.mcustomerportal.adapter.listener.RecyclerViewClickListener;
import com.dlvn.mcustomerportal.adapter.listener.RecyclerViewTouchListener;
import com.dlvn.mcustomerportal.base.BaseActivity;
import com.dlvn.mcustomerportal.common.Constant;
import com.dlvn.mcustomerportal.common.CustomPref;
import com.dlvn.mcustomerportal.services.ServicesGenerator;
import com.dlvn.mcustomerportal.services.ServicesRequest;
import com.dlvn.mcustomerportal.services.model.BaseRequest;
import com.dlvn.mcustomerportal.services.model.request.GetPointAccountByCLIIDRequest;
import com.dlvn.mcustomerportal.services.model.response.GetPointAccountByCLIIDResponse;
import com.dlvn.mcustomerportal.services.model.response.GetPointAccountByCLIIDResult;
import com.dlvn.mcustomerportal.services.model.response.GetPointAccountModel;
import com.dlvn.mcustomerportal.utils.Utilities;
import com.dlvn.mcustomerportal.utils.myLog;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;

public class PointAccountListActivity extends BaseActivity {

    private static final String TAG = "PointAccountListActivity";

    List<GetPointAccountModel> listData;
    PointAccountAdapter adapter;
    RecyclerView rvPointAccount;

    SwipeRefreshLayout swipeContainer;
    LinearLayout lloBack;

    ServicesRequest svRequester;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_list);
        getViews();
        initData();
        setListener();
    }

    private void getViews() {
        svRequester = ServicesGenerator.createService(ServicesRequest.class);

        lloBack = this.findViewById(R.id.lloBack);
        swipeContainer = this.findViewById(R.id.swipeContainer);
        rvPointAccount = this.findViewById(R.id.rvPointAccount);
    }

    private void initData() {

        if (listData == null)
            listData = new ArrayList<>();

        if (adapter == null)
            adapter = new PointAccountAdapter(this, listData);
        rvPointAccount.setAdapter(adapter);

        new getPointAccountTask(this).execute();
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

        rvPointAccount.addOnItemTouchListener(new RecyclerViewTouchListener(this, rvPointAccount, new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {

                if (listData.get(position).getCodeGroup() != PointAccountAdapter.CODE_TITLE) {
                    Intent intent = new Intent(PointAccountListActivity.this, PointAccountDetailActivity.class);
                    intent.putExtra(Constant.INT_KEY_POINT_ACCOUNT, listData.get(position));
                    startActivity(intent);
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    /**
     * Task get List Point Account Loyalty Point
     *
     * @modify 31/10/2018
     */
    private class getPointAccountTask extends AsyncTask<Void, Void, Response<GetPointAccountByCLIIDResponse>> {

        Context context;

        public getPointAccountTask(Context c) {
            context = c;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            swipeContainer.setRefreshing(true);
        }

        @Override
        protected Response<GetPointAccountByCLIIDResponse> doInBackground(Void... voids) {
            Response<GetPointAccountByCLIIDResponse> res = null;

            try {
                GetPointAccountByCLIIDRequest data = new GetPointAccountByCLIIDRequest();

                data.setClientID(CustomPref.getUserID(context));
                if (TextUtils.isEmpty(CustomPref.getUserName(context)))
                    data.setUserLogin(CustomPref.getUserID(context));
                else
                    data.setUserLogin(CustomPref.getUserName(context));

                data.setAPIToken(CustomPref.getAPIToken(context));
                data.setDeviceID(Utilities.getDeviceID(context));
                data.setOS(Utilities.getDeviceOS());
                data.setProject(Constant.Project_ID);
                data.setAuthentication(Constant.Project_Authentication);

                data.setStartDate("");
                data.setEndDate("");

                BaseRequest request = new BaseRequest();
                request.setJsonDataInput(data);

                Call<GetPointAccountByCLIIDResponse> call = svRequester.GetPointAccountByCLIID(request);
                res = call.execute();

            } catch (IOException e) {
                myLog.printTrace(e);
            }

            return res;
        }

        @Override
        protected void onPostExecute(Response<GetPointAccountByCLIIDResponse> res) {
            super.onPostExecute(res);
            swipeContainer.setRefreshing(false);
            if (res != null)
                if (res.isSuccessful()) {
                    GetPointAccountByCLIIDResponse response = res.body();
                    if (response != null) {
                        GetPointAccountByCLIIDResult result = response.getCPGetPointAccountByCLIIDResult();
                        if (result != null) {
                            if (result.getResult().equalsIgnoreCase("false")) {

                                myLog.e(TAG, "Get Point Account: " + result.getMessage());

                                if (result.getNewAPIToken().equalsIgnoreCase(Constant.ERROR_TOKENINVALID)) {
                                    Utilities.processLoginAgain(context, getString(R.string.message_alert_relogin));
                                }
                            } else if (result.getResult().equalsIgnoreCase("true")) {

                                if (result.getPointAccounts() != null) {
                                    if (listData != null)
                                        listData.clear();

                                    //filter by month date
                                    List<GetPointAccountModel> lsTemp = result.getPointAccounts();

                                    List<GetPointAccountModel> lsHnay = new ArrayList<>();
                                    List<GetPointAccountModel> lsHqua = new ArrayList<>();
                                    List<GetPointAccountModel> lsLauhon = new ArrayList<>();

                                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                                    Date dtNow = new Date();

                                    for (GetPointAccountModel mo : lsTemp) {
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
                                        lsHnay.add(0, new GetPointAccountModel("Hôm nay", PointAccountAdapter.CODE_TITLE));
                                        listData.addAll(lsHnay);
                                    }
                                    if (lsHqua.size() > 0) {
                                        lsHqua.add(0, new GetPointAccountModel("Hôm qua", PointAccountAdapter.CODE_TITLE));
                                        listData.addAll(lsHqua);
                                    }
                                    if (lsLauhon.size() > 0) {
                                        lsLauhon.add(0, new GetPointAccountModel("Trước đó", PointAccountAdapter.CODE_TITLE));
                                        listData.addAll(lsLauhon);
                                    }

                                    adapter.setData(listData);
                                    rvPointAccount.setAdapter(adapter);
                                }
                            }
                        }
                    }
                }
        }
    }
}
