package com.dlvn.mcustomerportal.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.adapter.BonusListAdapter;
import com.dlvn.mcustomerportal.adapter.ProductListAdapter;
import com.dlvn.mcustomerportal.adapter.listener.RecyclerViewClickListener;
import com.dlvn.mcustomerportal.adapter.listener.RecyclerViewTouchListener;
import com.dlvn.mcustomerportal.adapter.model.ProductDetailModel;
import com.dlvn.mcustomerportal.base.BaseActivity;
import com.dlvn.mcustomerportal.common.Constant;
import com.dlvn.mcustomerportal.services.ServicesGenerator;
import com.dlvn.mcustomerportal.services.ServicesRequest;
import com.dlvn.mcustomerportal.services.model.request.GetMasterDataByTypeRequest;
import com.dlvn.mcustomerportal.services.model.response.GetMasterData_Category_Response;
import com.dlvn.mcustomerportal.services.model.response.GetMasterData_Product_Response;
import com.dlvn.mcustomerportal.services.model.response.GetMaterData_Category_Result;
import com.dlvn.mcustomerportal.services.model.response.GetMaterData_Product_Result;
import com.dlvn.mcustomerportal.utils.myLog;
import com.dlvn.mcustomerportal.view.DividerItemDecoration;
import com.dlvn.mcustomerportal.view.RecyclerSmoothLayoutManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class ProductListActivity extends BaseActivity {

    private static final String TAG = "ProductListActivity";

    public static final String INT_MASTERDATA_TYPE = "MasterData_Type";
    String MasterType = null;

    LinearLayout lloBack;
    RecyclerView rvContent;
    ProductListAdapter adapter;
    List<ProductDetailModel> lstData;

    ServicesRequest svRequester;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        getViews();
        initData();
        setListener();
    }

    private void getViews() {
        lloBack = findViewById(R.id.lloBack);

        rvContent = findViewById(R.id.rvContent);
        RecyclerView.LayoutManager layout = new RecyclerSmoothLayoutManager(this, LinearLayoutManager.VERTICAL,
                false);
        rvContent.setLayoutManager(layout);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvContent.getContext());
        rvContent.addItemDecoration(dividerItemDecoration);
    }

    private void initData() {
        svRequester = ServicesGenerator.createService(ServicesRequest.class);

        if (getIntent().getExtras() != null)
            if (getIntent().getExtras().containsKey(INT_MASTERDATA_TYPE)) {
                MasterType = getIntent().getExtras().getString(INT_MASTERDATA_TYPE);

                if (!TextUtils.isEmpty(MasterType))
                    new getMasterDataTask(this).execute();
            }
    }

    private void setListener() {

        rvContent.addOnItemTouchListener(new RecyclerViewTouchListener(this, rvContent, new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(ProductListActivity.this, ProductDetailActivity.class);
                intent.putExtra(ProductDetailActivity.INT_PRODUCT_DETAIL, lstData.get(position));
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    /**
     * get List Product
     */
    public class getMasterDataTask extends AsyncTask<Void, Void, Response<GetMasterData_Product_Response>> {
        Context context;
        ProgressDialog process;

        public getMasterDataTask(Context c) {
            context = c;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //TODO: progress dialog
            if (process == null)
                process = new ProgressDialog(context);
            process.setIndeterminate(true);
            process.setIndeterminateDrawable(getResources().getDrawable(R.drawable.dialog_progress_cercle));
            process.show();
        }

        @Override
        protected Response<GetMasterData_Product_Response> doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            Response<GetMasterData_Product_Response> result = null;
            try {
                GetMasterDataByTypeRequest data = new GetMasterDataByTypeRequest();

                data.setProject(Constant.Project_ID);
                data.setAction(Constant.MASTERDATA_PRODUCTNEWS_ACTION);
                data.setType(MasterType);

                Call<GetMasterData_Product_Response> call = svRequester.GetListProductMasterData(data);
                result = call.execute();

            } catch (Exception e) {
                myLog.printTrace(e);
                return null;
            }

            // TODO: register the new account here.
            return result;
        }

        @Override
        protected void onPostExecute(final Response<GetMasterData_Product_Response> success) {
            process.dismiss();

            if (success != null) {
                try {
                    if (success.isSuccessful()) {
                        GetMasterData_Product_Response response = success.body();
                        if (response != null)
                            if (response.getGetMasterDataByTypeResult() != null) {
                                GetMaterData_Product_Result result = response.getGetMasterDataByTypeResult();
                                if (result != null) {

                                    if (result.getResult() != null && result.getResult().equals("false")) {
                                        myLog.e(TAG, "Get Master Data List Product: " + result.getMessage());

                                    } else if (result.getResult() != null && result.getResult().equals("true")) {
                                        if (result.getLstItem() != null) {
                                            lstData = result.getLstItem();

                                            if (lstData != null) {


                                                adapter = new ProductListAdapter(context, lstData);
                                                rvContent.setAdapter(adapter);
                                            }
                                        }
                                    }
                                }
                            }
                    }
                } catch (Exception e) {
                    myLog.printTrace(e);
                }
            }
        }

        @Override
        protected void onCancelled() {
            this.cancel(true);
            process.dismiss();
        }
    }
}
