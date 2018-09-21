package com.dlvn.mcustomerportal.activity;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.adapter.TransactionDetailListAdapter;
import com.dlvn.mcustomerportal.adapter.model.TransactionDetailModel;
import com.dlvn.mcustomerportal.adapter.model.TransactionModel;
import com.dlvn.mcustomerportal.base.BaseActivity;
import com.dlvn.mcustomerportal.services.NetworkUtils;
import com.dlvn.mcustomerportal.services.ServicesGenerator;
import com.dlvn.mcustomerportal.services.ServicesRequest;
import com.dlvn.mcustomerportal.services.model.request.HistoryPaymentDetailRequest;
import com.dlvn.mcustomerportal.services.model.response.HistoryPaymentDetailResponse;
import com.dlvn.mcustomerportal.services.model.response.HistoryPaymentDetailResult;
import com.dlvn.mcustomerportal.utils.myLog;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class TransactionDetailActivity extends BaseActivity {

    public static final String TAG = "TransactionDetailActivity";

    public static final String INT_TRANS_MODEL = "INT_TRANS_MODEL";

    TextView tvMaGD, tvNgayGD;
    SwipeRefreshLayout swRefresh;
    ListView lvData;

    TransactionDetailListAdapter adapter;
    TransactionModel transItem;
    List<TransactionDetailModel> lstTransDetail;

    ServicesRequest svRequester;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getViews();
        initData();
        setListener();
    }

    private void getViews() {
        // TODO Auto-generated method stub
        lvData = (ListView) findViewById(R.id.lvData);

        swRefresh = findViewById(R.id.swRefresh);
        tvNgayGD = (TextView) findViewById(R.id.tvNgayGD);
        tvMaGD = (TextView) findViewById(R.id.tvMaGD);
    }

    private void initData() {
        // TODO Auto-generated method stub
        svRequester = ServicesGenerator.createService(ServicesRequest.class);

        if (getIntent().getExtras().containsKey(INT_TRANS_MODEL)) {
            transItem = getIntent().getParcelableExtra(INT_TRANS_MODEL);
        }

        if (transItem != null) {
            tvMaGD.setText(transItem.getTransactionDetailID());
            tvNgayGD.setText(transItem.getCreateDate());

            if (lstTransDetail == null)
                lstTransDetail = new ArrayList<>();
            initAdapter();
            lvData.setAdapter(adapter);

            if (NetworkUtils.isConnectedHaveDialog(this))
                new GetHistoryDetailTask().execute();
        }
    }

    private void initAdapter() {
        if (adapter == null)
            adapter = new TransactionDetailListAdapter(this, lstTransDetail, transItem.getPolicyNo(), transItem.getCreateDate());
        else
            adapter.reFreshData(lstTransDetail);
    }

    private void setListener() {
        // TODO Auto-generated method stub

    }

    private class GetHistoryDetailTask extends AsyncTask<Void, Void, Response<HistoryPaymentDetailResponse>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            swRefresh.setRefreshing(true);
        }

        @Override
        protected Response<HistoryPaymentDetailResponse> doInBackground(Void... voids) {

            Response<HistoryPaymentDetailResponse> response = null;

            try {
                HistoryPaymentDetailRequest request = new HistoryPaymentDetailRequest();
                request.setTransactionDetailID(transItem.getTransactionDetailID());

                Call<HistoryPaymentDetailResponse> call = svRequester.GetHistoryDetailPayment(request);
                response = call.execute();
            } catch (IOException e) {
                myLog.printTrace(e);
            }

            return response;
        }

        @Override
        protected void onPostExecute(Response<HistoryPaymentDetailResponse> res) {
            super.onPostExecute(res);
            swRefresh.setRefreshing(false);

            if (res.isSuccessful()) {
                HistoryPaymentDetailResponse response = res.body();
                if (response != null) {
                    HistoryPaymentDetailResult result = response.getResponse();
                    if (result != null && result.getResult().equals("true")) {
                        lstTransDetail = result.getListHistoryDetail();
                        if (lstTransDetail != null) {
                            initAdapter();
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
