package com.dlvn.mcustomerportal.activity.prototype;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import android.widget.TextView;
import android.widget.Toast;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.adapter.SingleListAdapter;
import com.dlvn.mcustomerportal.adapter.model.SingleChoiceModel;
import com.dlvn.mcustomerportal.base.BaseActivity;
import com.dlvn.mcustomerportal.common.Constant;
import com.dlvn.mcustomerportal.common.CustomPref;
import com.dlvn.mcustomerportal.services.ServicesGenerator;
import com.dlvn.mcustomerportal.services.ServicesRequest;
import com.dlvn.mcustomerportal.services.model.BaseRequest;
import com.dlvn.mcustomerportal.services.model.request.CartItemModel;
import com.dlvn.mcustomerportal.services.model.request.CartModel;
import com.dlvn.mcustomerportal.services.model.request.GetMasterDataByTypeRequest;
import com.dlvn.mcustomerportal.services.model.request.LoyaltyPointConformRequest;
import com.dlvn.mcustomerportal.services.model.response.GetMasterData_City_Response;
import com.dlvn.mcustomerportal.services.model.response.GetMasterData_City_Result;
import com.dlvn.mcustomerportal.services.model.response.LoyaltyPointConformResponse;
import com.dlvn.mcustomerportal.services.model.response.LoyaltyPointConformResult;
import com.dlvn.mcustomerportal.services.model.response.MasterData_City;
import com.dlvn.mcustomerportal.services.model.response.MasterData_District;
import com.dlvn.mcustomerportal.services.model.response.ProductLoyaltyModel;
import com.dlvn.mcustomerportal.services.model.response.SearchPolicyHolderModel;
import com.dlvn.mcustomerportal.utils.Utilities;
import com.dlvn.mcustomerportal.utils.myLog;
import com.dlvn.mcustomerportal.view.MyCustomDialog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class DeliveryInputActivity extends BaseActivity {

    private static final String TAG = DeliveryInputActivity.class.getName();

    LinearLayout lloDeliveryConfirm, lloEmailCOnfirm;
    EditText edtHoten, edtDiaChi, edtSoDT, edtPhuongXa, edtEmail, edtPhone;
    TextView tvTinhThanh, tvQuanHuyen;
    Button btnThanhToan;

    MasterData_City categoryCity;
    MasterData_District categoryDistrict;
    List<MasterData_City> lsCity;
    List<MasterData_District> lsDistrict;

    List<SingleChoiceModel> Cities;
    List<SingleChoiceModel> Districts;
    SingleChoiceModel choiceCity;
    SingleChoiceModel choiceDistrict;
    CartModel myCart;

    //Category for PointDon
    CartItemModel categoryPointDone = null;
    SearchPolicyHolderModel policyHolder = null;

    String pointUserType = null;
    ServicesRequest svRequester;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_input);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(getString(R.string.title_activity_deliveryInput));

        getViews();
        initData();
        setListener();
    }

    @Override
    protected void onStop() {
        super.onStop();
        CustomPref.saveShoppingCart(this, myCart);
    }

    private void getViews() {
        edtHoten = findViewById(R.id.edtHoTen);
        edtDiaChi = findViewById(R.id.edtDiachi);
        edtSoDT = findViewById(R.id.edtSoDT);
        edtPhuongXa = findViewById(R.id.edtPhuongXa);

        edtEmail = findViewById(R.id.edtEmail);
        edtPhone = findViewById(R.id.edtPhone);

        tvTinhThanh = findViewById(R.id.tvTinhThanh);
        tvQuanHuyen = findViewById(R.id.tvQuanHuyen);
        btnThanhToan = findViewById(R.id.btnThanhToan);

        lloDeliveryConfirm = findViewById(R.id.lloDeliveryConfirm);
        lloEmailCOnfirm = findViewById(R.id.lloEmailConfirm);
    }

    private void initData() {
        svRequester = ServicesGenerator.createService(ServicesRequest.class);

        edtEmail.setText(CustomPref.getEmail(this));
        edtPhone.setText(CustomPref.getPhoneNumber(this));

        if (getIntent().getExtras() != null) {
            if (getIntent().getExtras().containsKey(Constant.INT_USEPOINT_TYPE))
                pointUserType = getIntent().getStringExtra(Constant.INT_USEPOINT_TYPE);

            if (getIntent().getExtras().containsKey(Constant.INT_USEPOINT_CATEGORY))
                categoryPointDone = getIntent().getParcelableExtra(Constant.INT_USEPOINT_CATEGORY);

            if (getIntent().getExtras().containsKey(Constant.INT_USEPOINT_USER_POINTDON))
                policyHolder = getIntent().getParcelableExtra(Constant.INT_USEPOINT_USER_POINTDON);
        }

        if (TextUtils.isEmpty(pointUserType)) {
            lloDeliveryConfirm.setVisibility(View.VISIBLE);
            new getMasterDataTask(this, Constant.MASTER_DATA_TYPE_CITY_DISTRICT).execute();
        } else if (pointUserType.equalsIgnoreCase(Constant.Category_PCPCode.PCP_004.getStringValue())) {
            lloDeliveryConfirm.setVisibility(View.GONE);
        }

    }

    private void setListener() {

        tvTinhThanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Cities != null)
                    if (Cities.size() > 0)
                        showDialogSingleChoice(DeliveryInputActivity.this, Cities, true);
                    else
                        Toast.makeText(DeliveryInputActivity.this, "Không tải được danh sách tỉnh thành!", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(DeliveryInputActivity.this, "Không tải được danh sách tỉnh thành!", Toast.LENGTH_LONG).show();
            }
        });

        tvQuanHuyen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Districts != null)
                    if (Districts.size() > 0)
                        showDialogSingleChoice(DeliveryInputActivity.this, Districts, false);
                    else
                        Toast.makeText(DeliveryInputActivity.this, "Không tải được danh sách quận huyện!", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(DeliveryInputActivity.this, "Không tải được danh sách quận huyện!", Toast.LENGTH_LONG).show();
            }
        });

        btnThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attempValueInput();
            }
        });
    }

    private void attempValueInput() {

        if (pointUserType.equalsIgnoreCase(Constant.Category_PCPCode.PCP_004.getStringValue())) {

            edtPhone.setError(null);
            String phone = edtPhone.getText().toString();
            if (TextUtils.isEmpty(phone)) {
                edtPhone.setError(getString(R.string.error_delivery_inputphone));
                edtPhone.requestFocus();
                return;
            }

            new LoyaltyConformTask(DeliveryInputActivity.this, null, null, null, null, choiceCity, choiceDistrict, categoryPointDone).execute();

        } else {
            edtHoten.setError(null);
            edtDiaChi.setError(null);
            edtSoDT.setError(null);

            String hoten = edtHoten.getText().toString();
            String diachi = edtDiaChi.getText().toString();
            String sodt = edtSoDT.getText().toString();
            String phuongxa = edtPhuongXa.getText().toString();

            String email = edtEmail.getText().toString();
            String phone = edtPhone.getText().toString();

            boolean cancel = false;
            View focusView = null;

            if (choiceCity == null) {
                tvTinhThanh.setError(getString(R.string.error_delivery_inputcity));
                focusView = tvTinhThanh;
                cancel = true;
            }
            if (choiceDistrict == null) {
                tvQuanHuyen.setError(getString(R.string.error_delivery_inputdistrict));
                focusView = tvQuanHuyen;
                cancel = true;
            }
            if (TextUtils.isEmpty(hoten)) {
                edtHoten.setError(getString(R.string.error_delivery_inputname));
                focusView = edtHoten;
                cancel = true;
            }
            if (TextUtils.isEmpty(diachi)) {
                edtDiaChi.setError(getString(R.string.error_delivery_inputaddress));
                focusView = edtDiaChi;
                cancel = true;
            }
            if (TextUtils.isEmpty(sodt)) {
                edtSoDT.setError(getString(R.string.error_delivery_inputphone));
                focusView = edtSoDT;
                cancel = true;
            }

            if (cancel) {
                focusView.requestFocus();
            } else {

                //load my cart
                myCart = CustomPref.getShoppingCart(DeliveryInputActivity.this);

                List<CartItemModel> cItems = new ArrayList<>();
                if (myCart != null) {
                    if (myCart.getLsCategory() != null) {

                        //find category have product is checked
                        for (int i = 0; i < myCart.getLsCategory().size(); i++) {

                            CartItemModel category = null;
                            List<ProductLoyaltyModel> lsProduct = new ArrayList<>();
                            //find product in category have chekced
                            for (int j = 0; j < myCart.getLsCategory().get(i).getLsItems().size(); j++) {
                                if (myCart.getLsCategory().get(i).getLsItems().get(j).getIsChecked().equalsIgnoreCase(Constant.CHECK_BOX_CHECKED_TRUE)) {
                                    ProductLoyaltyModel mo = myCart.getLsCategory().get(i).getLsItems().get(j);
                                    lsProduct.add(mo);
                                }
                            }
                            if (lsProduct.size() > 0) {
                                category = new CartItemModel();
                                category.setCategory(myCart.getLsCategory().get(i).getCategory());
                                category.setCategoryName(myCart.getLsCategory().get(i).getCategoryName());
                                category.setLsItems(lsProduct);
                                cItems.add(category);

                                new LoyaltyConformTask(DeliveryInputActivity.this, hoten, diachi, sodt, phuongxa, choiceCity, choiceDistrict, category).execute();
                            }
                        }
                    }
                }
            }
        }
    }

    private void removeCategoryInMyCart(CartItemModel category) {
        if (myCart != null) {
            if (myCart.getLsCategory() != null) {
                for (int i = 0; i < myCart.getLsCategory().size(); i++) {

                    if (myCart.getLsCategory().get(i).getCategory().equalsIgnoreCase(category.getCategory())) {
                        for (int j = 0; j < myCart.getLsCategory().get(i).getLsItems().size(); j++) {

                            for (int a = 0; a < category.getLsItems().size(); a++) {
                                if (myCart.getLsCategory().get(i).getLsItems().get(j).getProductID().equalsIgnoreCase(category.getLsItems().get(a).getProductID())) {
                                    myCart.getLsCategory().get(i).getLsItems().remove(j);
                                    j--;
                                    break;
                                }
                            }
                        }
                    }
                }
            }

            CustomPref.saveShoppingCart(DeliveryInputActivity.this, myCart);
        }
    }

    /**
     * @author nn.tai
     * @modify 30/08/2018
     * get Master Data by type
     */
    public class getMasterDataTask extends AsyncTask<Void, Void, Response<GetMasterData_City_Response>> {
        Context context;
        String type;

        public getMasterDataTask(Context c, String type) {
            context = c;
            this.type = type;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //TODO: progress dialog
            if (dialog == null)
                dialog = new ProgressDialog(context);
            dialog.setMessage("Đang tải dữ liệu ...");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        @Override
        protected Response<GetMasterData_City_Response> doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            Response<GetMasterData_City_Response> result = null;
            try {
                GetMasterDataByTypeRequest data = new GetMasterDataByTypeRequest();

                data.setProject(Constant.Project_ID);
                data.setType(type);

                Call<GetMasterData_City_Response> call = svRequester.GetListCityMasterData(data);
                result = call.execute();

            } catch (Exception e) {
                myLog.printTrace(e);
                return null;
            }

            // TODO: register the new account here.
            return result;
        }

        @Override
        protected void onPostExecute(final Response<GetMasterData_City_Response> success) {
            dialog.dismiss();
            if (success != null) {
                try {
                    if (success.isSuccessful()) {
                        GetMasterData_City_Response response = success.body();
                        if (response != null)
                            if (response.getGetMasterDataByTypeResult() != null) {
                                GetMasterData_City_Result result = response.getGetMasterDataByTypeResult();
                                if (result != null) {

                                    if (result.getResult() != null && result.getResult().equals("false")) {
                                        myLog.E("DeliveryInputAct", "Get Point: " + result.getMessage());
                                        ;
                                    } else if (result.getResult() != null && result.getResult().equals("true")) {
                                        if (result.getLstItem() != null) {

                                            lsCity = result.getLstItem();
                                            if (lsCity != null) {

                                                if (Cities == null)
                                                    Cities = new ArrayList<>();

                                                for (MasterData_City item : lsCity) {
                                                    SingleChoiceModel mo = new SingleChoiceModel();
                                                    mo.setCode(item.getSTTTinhTP() + "");
                                                    mo.setId(item.getIDTinhTP());
                                                    mo.setName(item.getTenTinhTP());

                                                    Cities.add(mo);
                                                }
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
            dialog.dismiss();
        }
    }

    /**
     * @author nn.tai
     * @modify: 05/09/2018
     * Submis request đơn hàng to services
     */
    public class LoyaltyConformTask extends AsyncTask<Void, Void, Response<LoyaltyPointConformResponse>> {
        Context context;
        String hoten, diachi, sodt, phuongxa;
        SingleChoiceModel tinhthanh, quanhuyen;
        CartItemModel category;

        /**
         * @param c   Context
         * @param ht  Ho va ten
         * @param dc  Dia chi giao hang
         * @param sdt so dien thoai nguoi nhan
         * @param px  phuong xa
         * @param th  tinh / thanh pho
         * @param qh  quan / huyen
         */
        public LoyaltyConformTask(Context c, String ht, String dc, String sdt, String px, SingleChoiceModel th, SingleChoiceModel qh, CartItemModel category) {
            context = c;
            hoten = ht;
            diachi = dc;
            sodt = sdt;
            phuongxa = px;
            tinhthanh = th;
            quanhuyen = qh;
            this.category = category;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //TODO: progress dialog
            if (dialog == null)
                dialog = new ProgressDialog(context);
            dialog.setMessage("Đang gửi đơn hàng ...");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        @Override
        protected Response<LoyaltyPointConformResponse> doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            Response<LoyaltyPointConformResponse> result = null;
            try {

                LoyaltyPointConformRequest data = new LoyaltyPointConformRequest();

                data.setUserLogin(CustomPref.getUserID(context));
                data.setAPIToken(CustomPref.getAPIToken(context));
                data.setClientID(CustomPref.getUserID(context));
                data.setDeviceID(Utilities.getDeviceID(context));
                data.setDeviceToken(Utilities.getDeviceName() + "-" + Utilities.getVersion());
                data.setProject(Constant.Project_ID);
                data.setAuthentication(Constant.Project_Authentication);

                if (!pointUserType.equalsIgnoreCase(Constant.Category_PCPCode.PCP_004.getStringValue())) {
                    String[] temp = hoten.split(" ");
                    if (temp.length >= 2) {
                        data.setShippingFirstName(temp[temp.length - 1]);
                        data.setShippingLastName(temp[0]);
                    } else
                        data.setShippingFirstName(hoten);

                    data.setShippingPhone(sodt);
                    data.setDeliveryPhone(sodt);
                    data.setShippingAddress(diachi);
                    data.setShippingWard(phuongxa);
                    data.setShippingCity(tinhthanh.getCode());
                    data.setShippingDistrict(quanhuyen.getCode());
                } else {
                    if (policyHolder != null) {
                        data.setShippingFirstName(policyHolder.getFullName());
                        data.setShippingLastName(policyHolder.getClientID());
                    } else {
                        data.setShippingFirstName(null);
                        data.setShippingLastName(null);
                    }
                }

                data.setDeliveryFeeGros("0");
                data.setCategory(category.getCategory());
                data.setProductItems(category.getLsItems());

                data.setEmailconfirm(edtEmail.getText().toString());
                String phone = edtPhone.getText().toString();
                if (!TextUtils.isEmpty(phone))
                    data.setSmsconfirm(phone);
                else
                    data.setSmsconfirm(sodt);

                BaseRequest request = new BaseRequest();
                request.setJsonDataInput(data);
                Call<LoyaltyPointConformResponse> call = svRequester.LoyaltyPointConform(request);
                result = call.execute();

            } catch (Exception e) {
                myLog.printTrace(e);
                return null;
            }

            // TODO: register the new account here.
            return result;
        }

        @Override
        protected void onPostExecute(final Response<LoyaltyPointConformResponse> success) {
            dialog.dismiss();
            if (success != null) {
                try {
                    if (success.isSuccessful()) {
                        LoyaltyPointConformResponse response = success.body();
                        if (response != null)
                            if (response.getCPLoyaltyPointConfirmationResult() != null) {
                                LoyaltyPointConformResult result = response.getCPLoyaltyPointConfirmationResult();
                                if (result.getResult().equalsIgnoreCase("true")) {
                                    String message = result.getMessage();
                                    String[] temp = message.split("#");
                                    if (temp.length == 3) {
                                        if (temp[temp.length - 1].equalsIgnoreCase("1")) {
                                            MyCustomDialog dialog = new MyCustomDialog.Builder(context)
                                                    .setTitle("Đơn hàng thành công")
                                                    .setMessage("Đơn hàng của bạn đã được thực hiện thành công.\n Mã số đơn hàng: " + temp[1])
                                                    .setPositiveButton(getString(R.string.confirm_ok), new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            removeCategoryInMyCart(category);
                                                            dialog.dismiss();

                                                            Intent intent = new Intent(context, DashboardActivity.class);
                                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                            startActivity(intent);
                                                        }
                                                    }).create();
                                            dialog.show();
                                        } else {
                                            MyCustomDialog dialog = new MyCustomDialog.Builder(context)
                                                    .setTitle("Đơn hàng thất bại")
                                                    .setMessage("Đơn hàng của bạn đã được thực hiện không thành công.\n Mã số đơn hàng: " + temp[1])
                                                    .setPositiveButton(getString(R.string.confirm_ok), new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.dismiss();
                                                        }
                                                    }).create();
                                            dialog.show();
                                        }
                                    }
                                } else {
                                    if (result.getNewAPIToken().equalsIgnoreCase("invalidtoken")) {
                                        Utilities.processLoginAgain(context, getString(R.string.message_alert_relogin));
                                    } else
                                        myLog.E("DeliveryInputAct", "Get Point: " + result.getErrLog());
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
            dialog.dismiss();
        }
    }

    /**
     * Show dialog cho user chon single choice
     *
     * @param context
     */
    public void showDialogSingleChoice(final Context context, final List<SingleChoiceModel> lstData, final boolean isCity) {
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

        ImageButton btnClose = (ImageButton) dialog.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        lvList = (ListView) dialog.findViewById(R.id.lvList);
        if (adapter == null)
            adapter = new SingleListAdapter(context, R.layout.item_list_singlechoice, lstData);
        lvList.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (lstData.get(position) != null) {
                    if (isCity) {
                        tvTinhThanh.setText(lstData.get(position).getName());
                        choiceCity = lstData.get(position);

                        if (Districts == null)
                            Districts = new ArrayList<>();
                        else
                            Districts.clear();

                        for (MasterData_City city : lsCity)
                            if (city.getIDTinhTP() == lstData.get(position).getId()) {
                                for (MasterData_District dis : city.getLsQuanHuyen()) {
                                    SingleChoiceModel mo = new SingleChoiceModel();
                                    mo.setId(dis.getID());
                                    mo.setCode(dis.getMaQuanHuyen());
                                    mo.setName(dis.getTenQuanHuyen());

                                    Districts.add(mo);
                                }
                            }
                    } else {
                        tvQuanHuyen.setText(lstData.get(position).getName());
                        choiceDistrict = lstData.get(position);
                    }
                }

                if (dialog.isShowing())
                    dialog.dismiss();
            }
        });
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