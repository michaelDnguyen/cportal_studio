package com.dlvn.mcustomerportal.activity.prototype;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.base.BaseActivity;
import com.dlvn.mcustomerportal.common.Constant;
import com.dlvn.mcustomerportal.common.CustomPref;
import com.dlvn.mcustomerportal.services.ServicesGenerator;
import com.dlvn.mcustomerportal.services.ServicesRequest;
import com.dlvn.mcustomerportal.services.model.BaseRequest;
import com.dlvn.mcustomerportal.services.model.request.CartItemModel;
import com.dlvn.mcustomerportal.services.model.request.GetProductByCategoryRequest;
import com.dlvn.mcustomerportal.services.model.request.SearchPolicyHolderByPolicyIDRequest;
import com.dlvn.mcustomerportal.services.model.response.GetProductByCategoryResponse;
import com.dlvn.mcustomerportal.services.model.response.GetProductByCategoryResult;
import com.dlvn.mcustomerportal.services.model.response.ProductLoyaltyModel;
import com.dlvn.mcustomerportal.services.model.response.SearchPolicyHolderByPolicyIDResponse;
import com.dlvn.mcustomerportal.services.model.response.SearchPolicyHolderByPolicyIDResult;
import com.dlvn.mcustomerportal.services.model.response.SearchPolicyHolderModel;
import com.dlvn.mcustomerportal.utils.Utilities;
import com.dlvn.mcustomerportal.utils.myLog;
import com.dlvn.mcustomerportal.view.MyCustomDialog;
import com.dlvn.mcustomerportal.view.indicator.Point;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class PointDonActivity extends BaseActivity {

    private static final String TAG = "PointDonActivity";

    Button btnTiepTuc;
    LinearLayout lloPolicy, lloInfo;
    EditText edtPolicyNo, edtPointDon;
    TextView tvCusName, tvPoint;
    ImageButton ibtnSearch;

    List<SearchPolicyHolderModel> lsInfo;
    CartItemModel categoryPointDon = null;

    ServicesRequest svRequester;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_don);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Tặng điểm");

        getViews();
        setListener();

    }

    private void getViews() {
        btnTiepTuc = findViewById(R.id.btnTieptuc);
        ibtnSearch = findViewById(R.id.ibtnSearch);

        lloPolicy = findViewById(R.id.lloPolicy);
        lloInfo = findViewById(R.id.lloInfo);
        lloInfo.setVisibility(View.GONE);

        edtPolicyNo = findViewById(R.id.edtPolicyNo);
        edtPointDon = findViewById(R.id.edtPointDon);

        tvCusName = findViewById(R.id.tvCusName);
        tvPoint = findViewById(R.id.tvPoint);
        tvPoint.setText(CustomPref.getUserPoint(this) + "");

        svRequester = ServicesGenerator.createService(ServicesRequest.class);
        new getProductPointDonTask(this).execute();

    }

    private void setListener() {
        ibtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String search = edtPolicyNo.getText().toString();
                edtPolicyNo.setError(null);
                if (!TextUtils.isEmpty(search)) {
                    new SearchPolicyTask(PointDonActivity.this, search).execute();
                } else {
                    edtPolicyNo.requestFocus();
                    edtPolicyNo.setError(getString(R.string.error_pointdon_policyrequired));
                }
            }
        });

        btnTiepTuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sp = edtPointDon.getText().toString();
                try {
                    float point = Float.parseFloat(sp);
                    if (point > CustomPref.getUserPoint(PointDonActivity.this)) {
                        MyCustomDialog dialog = new MyCustomDialog.Builder(PointDonActivity.this)
                                .setMessage(getString(R.string.alert_pointdon_noenoughpoint))
                                .setPositiveButton(getString(R.string.confirm_ok), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).create();
                        dialog.show();
                    } else if (point < 10) {
                        edtPointDon.setError("Số điểm tặng tối thiểu là 10.0");
                        edtPointDon.requestFocus();
                        return;
                    } else {
                        if (categoryPointDon != null) {
                            List<ProductLoyaltyModel> products = categoryPointDon.getLsItems();
                            if (products.size() > 0) {
                                products.get(0).setPrice((int) (point * 1000));
                                categoryPointDon.setLsItems(products);
                            } else
                                Toast.makeText(PointDonActivity.this, "Không lấy được product tặng điểm!!!", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(PointDonActivity.this, "Không lấy được category tặng điểm!!!", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(PointDonActivity.this, DeliveryInputActivity.class);
                        intent.putExtra(Constant.INT_USEPOINT_TYPE, Constant.Category_PCPCode.PCP_004.getStringValue());
                        intent.putExtra(Constant.INT_USEPOINT_CATEGORY, categoryPointDon);
                        intent.putExtra(Constant.INT_USEPOINT_USER_POINTDON, lsInfo.get(0));
                        startActivity(intent);
                    }
                } catch (NumberFormatException e) {
                    myLog.printTrace(e);
                }
            }
        });
    }

    /**
     * Task inquiry policy number of user want to get point
     *
     * @modify 19/09/2018
     */
    private class SearchPolicyTask extends AsyncTask<Void, Void, Response<SearchPolicyHolderByPolicyIDResponse>> {

        ProgressDialog dialog;
        Context context;
        String searxhText;

        public SearchPolicyTask(Context c, String search) {
            context = c;
            searxhText = search;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(context);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("Đang tìm...");
            dialog.show();
        }

        @Override
        protected Response<SearchPolicyHolderByPolicyIDResponse> doInBackground(Void... voids) {
            Response<SearchPolicyHolderByPolicyIDResponse> res = null;

            try {
                SearchPolicyHolderByPolicyIDRequest data = new SearchPolicyHolderByPolicyIDRequest();

                data.setClientID(CustomPref.getUserID(context));
                data.setAPIToken(CustomPref.getAPIToken(context));
                data.setDeviceID(Utilities.getDeviceID(context));
                data.setOS(Utilities.getDeviceName() + "-" + Utilities.getVersion());
                data.setProject(Constant.Project_ID);
                data.setAuthentication(Constant.Project_Authentication);
                data.setUserLogin(CustomPref.getUserName(context));

                data.setPolicyID(searxhText);

                BaseRequest request = new BaseRequest();
                request.setJsonDataInput(data);

                Call<SearchPolicyHolderByPolicyIDResponse> call = svRequester.SearchPolicyHolderByPolicyID(request);
                res = call.execute();
            } catch (IOException e) {
                myLog.printTrace(e);
            }

            return res;
        }

        @Override
        protected void onPostExecute(Response<SearchPolicyHolderByPolicyIDResponse> res) {
            super.onPostExecute(res);
            dialog.dismiss();

            if (res.isSuccessful()) {
                SearchPolicyHolderByPolicyIDResponse response = res.body();
                if (response != null) {
                    SearchPolicyHolderByPolicyIDResult result = response.getCPSearchPolicyHolderByPolicyIDResult();
                    if (result != null)
                        if (result.getResult().equalsIgnoreCase("true")) {
                            lsInfo = result.getLsSearchPolicyHolder();
                            if (lsInfo != null && lsInfo.size() > 0) {
                                lloInfo.setVisibility(View.VISIBLE);
                                tvCusName.setText(lsInfo.get(0).getFullName());
                            }
                        } else {
                            if (result.getNewAPIToken().equalsIgnoreCase("invalidtoken")) {
                                Utilities.processLoginAgain(context, getString(R.string.message_alert_relogin));
                            }
                        }
                }
            }
        }
    }

    /**
     * @author nn.tai
     * @modify 30/08/2018
     */
    public class getProductPointDonTask extends AsyncTask<Void, Void, Response<GetProductByCategoryResponse>> {

        Context context;
        String category;

        public getProductPointDonTask(Context context) {
            this.context = context;
            category = "4";
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
                data.setOS(Utilities.getDeviceName() + "-" + Utilities.getVersion());
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

            if (success != null) {
                try {
                    if (success.isSuccessful()) {
                        GetProductByCategoryResponse response = success.body();
                        if (response != null)
                            if (response.getCPGetProductByCategoryResult() != null) {
                                GetProductByCategoryResult result = response.getCPGetProductByCategoryResult();
                                if (result != null) {

                                    if (result.getResult() != null && result.getResult().equals("false")) {

                                        myLog.E(TAG, "GetProduct PointDon Error request: " + result.getErrLog());

                                        if (result.getNewAPIToken().equalsIgnoreCase("invalidtoken")) {
                                            Utilities.processLoginAgain(context, getString(R.string.message_alert_relogin));
                                        }

                                    } else if (result.getResult() != null && result.getResult().equals("true")) {

                                        if (!TextUtils.isEmpty(result.getNewAPIToken()))
                                            //Save Token
                                            CustomPref.saveToken(context, result.getNewAPIToken());

                                        if (result.getProductLoyalty() != null) {

                                            List<ProductLoyaltyModel> lsProduct = new ArrayList<>();
                                            lsProduct = result.getProductLoyalty();
                                            if (lsProduct.size() > 0) {

                                                lsProduct.get(0).setQuantity(1);

                                                categoryPointDon = new CartItemModel();
                                                categoryPointDon.setCategoryName(lsProduct.get(0).getStrDetail());
                                                categoryPointDon.setCategory("4");
                                                categoryPointDon.setLsItems(lsProduct);
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        myLog.E(TAG, "onOptionsItemSelected");
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

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        myLog.E("dispatchTouchEvent");
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
}
