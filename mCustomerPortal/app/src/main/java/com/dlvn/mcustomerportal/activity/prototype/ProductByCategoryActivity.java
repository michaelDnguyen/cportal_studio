package com.dlvn.mcustomerportal.activity.prototype;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.adapter.ProductLoyaltyListAdapter;
import com.dlvn.mcustomerportal.base.BaseActivity;
import com.dlvn.mcustomerportal.common.Constant;
import com.dlvn.mcustomerportal.common.CustomPref;
import com.dlvn.mcustomerportal.services.ServicesGenerator;
import com.dlvn.mcustomerportal.services.ServicesRequest;
import com.dlvn.mcustomerportal.services.model.BaseRequest;
import com.dlvn.mcustomerportal.services.model.request.GetProductByCategoryRequest;
import com.dlvn.mcustomerportal.services.model.response.GetProductByCategoryResponse;
import com.dlvn.mcustomerportal.services.model.response.GetProductByCategoryResult;
import com.dlvn.mcustomerportal.services.model.response.MasterData_Category;
import com.dlvn.mcustomerportal.services.model.response.ProductLoyaltyModel;
import com.dlvn.mcustomerportal.utils.Utilities;
import com.dlvn.mcustomerportal.utils.myLog;
import com.dlvn.mcustomerportal.view.MyCustomDialog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class ProductByCategoryActivity extends BaseActivity {

    public static final String INT_CATEGORY_PRODUCT = "category_product";

    LinearLayout lloBack;
    ListView lvData;
    TextView tvNoData;
    SwipeRefreshLayout swipeContainer;

    List<ProductLoyaltyModel> lsProduct;
    ProductLoyaltyListAdapter adapter;
    MasterData_Category category;

    ServicesRequest svRequester;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_category);

        getViews();
        initData();
        setListener();
    }

    private void getViews() {
        lloBack = findViewById(R.id.lloBack);

        tvNoData = (TextView) findViewById(R.id.tvNoData);
        tvNoData.setVisibility(View.GONE);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        lvData = (ListView) findViewById(R.id.lvData);
    }

    private void initData() {
        svRequester = ServicesGenerator.createService(ServicesRequest.class);

        if (getIntent().getExtras() != null)
            if (getIntent().getExtras().containsKey(INT_CATEGORY_PRODUCT))
                category = getIntent().getParcelableExtra(INT_CATEGORY_PRODUCT);

        if (category != null)
            if (!TextUtils.isEmpty(category.getPRODUCTCATEGORYCD())) {
                setTitle(category.getProductTitle());
                new getProductByCategoryTask(this, category.getPRODUCTCATEGORYCD()).execute();
            }

        lsProduct = new ArrayList<>();
        if (category != null)
            setTitle(category.getPRODUCTCATEGORYNAME());
    }

    private void initListview(Context c) {
        if (adapter == null) {
            adapter = new ProductLoyaltyListAdapter(c, lsProduct);
            lvData.setAdapter(adapter);
        } else
            adapter.reFreshData(lsProduct);
        adapter.notifyDataSetChanged();
    }

    private void setListener() {
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (category != null)
                    if (!TextUtils.isEmpty(category.getPRODUCTCATEGORYCD())) {
                        swipeContainer.setRefreshing(true);
                        new getProductByCategoryTask(ProductByCategoryActivity.this, category.getPRODUCTCATEGORYCD()).execute();
                    }
            }
        });

        lvData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProductLoyaltyModel item = lsProduct.get(position);
                if (item.getProductID().equalsIgnoreCase("1072") || item.getProductID().equalsIgnoreCase("1073")) {
                    //Todo: change acction for HDBank Card

                } else {
                    Intent intent = new Intent(ProductByCategoryActivity.this, ProductCategoryDetailActivity.class);
                    intent.putExtra(ProductCategoryDetailActivity.INT_CATEGORY_PRODUCT_DETAIL, item);
                    intent.putExtra(INT_CATEGORY_PRODUCT, category);
                    startActivity(intent);
                }
            }
        });

        lloBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    /**
     * @author nn.tai
     * @modify 30/08/2018
     */
    public class getProductByCategoryTask extends AsyncTask<Void, Void, Response<GetProductByCategoryResponse>> {

        Context context;
        String category;

        public getProductByCategoryTask(Context context, String c) {
            this.context = context;
            category = c;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            swipeContainer.setRefreshing(true);
        }

        @Override
        protected Response<GetProductByCategoryResponse> doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            Response<GetProductByCategoryResponse> result = null;
            try {

                GetProductByCategoryRequest data = new GetProductByCategoryRequest();
                data.setUserLogin(CustomPref.getUserName(context));
                data.setAPIToken(CustomPref.getAPIToken(context));
                data.setClientID(CustomPref.getUserID(context));
                data.setCategory(category);

                data.setDeviceId(Utilities.getDeviceID(context));
                data.setOS(Utilities.getDeviceOS());
                data.setProject(Constant.Project_ID);
                data.setAuthentication(Constant.Project_Authentication);

                BaseRequest request = new BaseRequest();
                request.setJsonDataInput(data);

                Call<GetProductByCategoryResponse> call = svRequester.GetProductByCategory(request);
                result = call.execute();

            } catch (Exception e) {
                myLog.printTrace(e);
                return null;
            }

            // TODO: register the new account here.
            return result;
        }

        @Override
        protected void onPostExecute(final Response<GetProductByCategoryResponse> success) {
            swipeContainer.setRefreshing(false);

            if (success != null) {
                try {
                    if (success.isSuccessful()) {
                        GetProductByCategoryResponse response = success.body();
                        if (response != null)
                            if (response.getCPGetProductByCategoryResult() != null) {
                                GetProductByCategoryResult result = response.getCPGetProductByCategoryResult();
                                if (result != null) {

                                    if (result.getResult() != null && result.getResult().equals("false")) {
                                        //If account not exits --> link to register
                                        myLog.e("ProductByCategoryAct", "Get Point: " + result.getErrLog());
                                    } else if (result.getResult() != null && result.getResult().equals("true")) {

                                        if (!TextUtils.isEmpty(result.getNewAPIToken()))
                                            //Save Token
                                            CustomPref.saveAPIToken(context, result.getNewAPIToken());

                                        if (result.getProductLoyalty() != null) {

                                            lsProduct.clear();
                                            lsProduct = result.getProductLoyalty();
                                            if (lsProduct.size() > 0) {
                                                tvNoData.setVisibility(View.GONE);
                                                initListview(context);
                                            }
                                        } else {
                                            tvNoData.setVisibility(View.VISIBLE);
                                        }
                                    } else {
                                        if (result.getNewAPIToken().equalsIgnoreCase(Constant.ERROR_TOKENINVALID)) {
                                            Utilities.processLoginAgain(context, getString(R.string.message_alert_relogin));
                                        }
                                    }
                                }
                            }
                    }
                } catch (Exception e) {
                    myLog.printTrace(e);
                }
            } else {
                MyCustomDialog dialog = new MyCustomDialog.Builder(context).setMessage(getString(R.string.alert_cannot_connect_server)).setTitle(getString(R.string.title_error_connected_server)).setPositiveButton(getString(R.string.confirm_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
                dialog.show();
            }
        }

        @Override
        protected void onCancelled() {
            swipeContainer.setRefreshing(false);
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
