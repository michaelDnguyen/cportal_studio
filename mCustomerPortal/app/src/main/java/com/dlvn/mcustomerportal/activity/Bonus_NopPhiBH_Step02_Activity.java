package com.dlvn.mcustomerportal.activity;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.activity.prototype.DashboardActivity;
import com.dlvn.mcustomerportal.activity.prototype.LoginMainActivity;
import com.dlvn.mcustomerportal.adapter.model.SingleChoiceModel;
import com.dlvn.mcustomerportal.base.BaseActivity;
import com.dlvn.mcustomerportal.common.Constant;
import com.dlvn.mcustomerportal.common.CustomPref;
import com.dlvn.mcustomerportal.services.ServicesGenerator;
import com.dlvn.mcustomerportal.services.ServicesRequest;
import com.dlvn.mcustomerportal.services.model.BaseRequest;
import com.dlvn.mcustomerportal.services.model.request.CartItemModel;
import com.dlvn.mcustomerportal.services.model.request.LoyaltyPointConformRequest;
import com.dlvn.mcustomerportal.services.model.response.FeeBasicOfPolicy;
import com.dlvn.mcustomerportal.services.model.response.LoyaltyPointConformResponse;
import com.dlvn.mcustomerportal.services.model.response.LoyaltyPointConformResult;
import com.dlvn.mcustomerportal.services.model.response.ProductLoyaltyModel;
import com.dlvn.mcustomerportal.utils.Utilities;
import com.dlvn.mcustomerportal.utils.myLog;
import com.dlvn.mcustomerportal.view.MyCustomDialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class Bonus_NopPhiBH_Step02_Activity extends BaseActivity {

    Button btnXacNhan;

    TextView tvTenKhachHang, tvMaKhachHang, tvDiem;
    TextView tvHopDong, tvDiemSuDung, tvDiemConLai, tvPhiBaoHiem, tvTamUngDongPhi, tvTamUng;

    //TODO: bonus
    TextView tvPhiBaoHiem1, tvTamUngDongPhi1, tvTamUng1;

    EditText edtEmail, edtPhone;

    String soHopDong, phiBaoHiem, tamUngDP, tamUng;
    float diemTichLuy = 0, diemSuDung = 0;

    ProgressDialog dialog;

    FeeBasicOfPolicy feeChoice;
    ServicesRequest svRequester;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bonus__nop_phi_bh__step02);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getViews();
        initDatas();
        setListener();
    }

    private void getViews() {
        // TODO Auto-generated method stub
        tvMaKhachHang = (TextView) findViewById(R.id.tvMaKhachHang);
        tvTenKhachHang = (TextView) findViewById(R.id.tvTenKhachHang);
        tvDiem = (TextView) findViewById(R.id.tvDiem);

        tvHopDong = (TextView) findViewById(R.id.tvHopDong);
        tvDiemSuDung = (TextView) findViewById(R.id.tvDiemSuDung);
        tvDiemConLai = (TextView) findViewById(R.id.tvDiemConLai);
        tvPhiBaoHiem = (TextView) findViewById(R.id.tvPhiBaoHiem);
        tvTamUngDongPhi = (TextView) findViewById(R.id.tvTamUngDongPhi);
        tvTamUng = (TextView) findViewById(R.id.tvTamUng);

        edtEmail = findViewById(R.id.edtEmail);
        edtPhone = findViewById(R.id.edtPhone);

        btnXacNhan = (Button) findViewById(R.id.btnTiep);

        //TODO: bonus
        tvPhiBaoHiem1 = (TextView) findViewById(R.id.phiBaoHiem);
        tvTamUngDongPhi1 = (TextView) findViewById(R.id.tamUngDongPhi);
        tvTamUng1 = (TextView) findViewById(R.id.tamUng);
    }

    private void initDatas() {
        svRequester = ServicesGenerator.createService(ServicesRequest.class);

        // TODO Auto-generated method stub
        if (CustomPref.haveLogin(this)) {
            tvTenKhachHang.setText(CustomPref.getFullName(this));
            tvMaKhachHang.setText(CustomPref.getUserID(this));
            diemTichLuy = (float) CustomPref.getUserPoint(this);

            tvPhiBaoHiem1.setText(getIntent().getStringExtra("soTienPhaiThanhToanPhiDinhKy"));
            tvTamUngDongPhi1.setText(getIntent().getStringExtra("soTienPhaiThanhToanKhoanTamUngDeDongPhi"));
            tvTamUng1.setText(getIntent().getStringExtra("soTienPhaiThanhToanKhoanTamUng"));
            tvDiem.setText(diemTichLuy + "");

            edtEmail.setText(CustomPref.getEmail(this));
            edtPhone.setText(CustomPref.getPhoneNumber(this));
        }

        if (getIntent().getExtras().containsKey(Constant.NOPPHI_SOHOPDONG)) {
            soHopDong = getIntent().getStringExtra(Constant.NOPPHI_SOHOPDONG);
            phiBaoHiem = getIntent().getStringExtra(Constant.NOPPHI_PHIBAOHIEM);
            tamUngDP = getIntent().getStringExtra(Constant.NOPPHI_TAMUNG_DONGPHI);
            tamUng = getIntent().getStringExtra(Constant.NOPPHI_TAMUNG);

            feeChoice = getIntent().getParcelableExtra("FeeBasicConform");
            // init data
            tvHopDong.setText(soHopDong);
            tvPhiBaoHiem.setText(Utilities.formatMoneyToVND(phiBaoHiem) + " VND");
            tvTamUngDongPhi.setText(Utilities.formatMoneyToVND(tamUngDP) + " VND");
            tvTamUng.setText(Utilities.formatMoneyToVND(tamUng) + " VND");

            if (!TextUtils.isEmpty(phiBaoHiem))
                diemSuDung += Float.parseFloat(phiBaoHiem.replace(",", "")) / 1000;
            if (!TextUtils.isEmpty(tamUngDP))
                diemSuDung += Float.parseFloat(tamUngDP.replace(",", "")) / 1000;
            if (!TextUtils.isEmpty(tamUng))
                diemSuDung += Float.parseFloat(tamUng.replace(",", "")) / 1000;

            tvDiemSuDung.setText(diemSuDung + "");
            tvDiemConLai.setText((diemTichLuy - diemSuDung) + "");

        }
    }

    private void setListener() {
        // TODO Auto-generated method stub
        btnXacNhan.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
//                Intent intent = new Intent(Bonus_NopPhiBH_Step02_Activity.this, Bonus_NopPhiBH_Step03_Activity.class);
//                intent.putExtra(Constant.NOPPHI_SOHOPDONG, soHopDong);
//                intent.putExtra(Constant.NOPPHI_DIEMSUDUNG, diemSuDung);
//                startActivity(intent);

                new LoyaltyConformTask(Bonus_NopPhiBH_Step02_Activity.this, phiBaoHiem.replace(",", ""), tamUngDP.replace(",", ""), tamUng.replace(",", ""), feeChoice).execute();

            }
        });
    }

    /**
     * @author nn.tai
     * @modify: 05/09/2018
     * Submis request đơn hàng to services
     */
    public class LoyaltyConformTask extends AsyncTask<Void, Void, Response<LoyaltyPointConformResponse>> {
        Context context;
        FeeBasicOfPolicy feeBasicOfPolicy;
        String feeBasic, feeApl, feeOpl;

        public LoyaltyConformTask(Context c, String fee, String apl, String opl, FeeBasicOfPolicy feeBasic) {
            context = c;
            this.feeBasicOfPolicy = feeBasic;
            this.feeBasic = fee;
            this.feeApl = apl;
            this.feeOpl = opl;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //TODO: progress dialog
            if (dialog == null)
                dialog = new ProgressDialog(context);
            dialog.setMessage("Đang gửi phí ...");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        @Override
        protected Response<LoyaltyPointConformResponse> doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            Response<LoyaltyPointConformResponse> result = null;
            try {

                if (feeBasicOfPolicy != null) {
                    LoyaltyPointConformRequest data = new LoyaltyPointConformRequest();

                    data.setUserLogin(CustomPref.getUserName(context));
                    data.setClientID(CustomPref.getUserID(context));
                    data.setAPIToken(CustomPref.getAPIToken(context));
                    data.setDeviceID(Utilities.getDeviceID(context));
                    data.setOS(Utilities.getDeviceOS());
                    data.setProject(Constant.Project_ID);
                    data.setAuthentication(Constant.Project_Authentication);

                    data.setDeliveryFeeGros("0");

                    CartItemModel category = new CartItemModel();
                    List<ProductLoyaltyModel> lstItem = new ArrayList<>();

                    ProductLoyaltyModel fee = new ProductLoyaltyModel();
                    fee.setProductID(feeBasicOfPolicy.getPOLSNDRYAMTCODE());
                    fee.setQuantity(1);
                    fee.setPrice(Integer.parseInt(feeBasic));
                    fee.setDiscount(0);
                    fee.setShipping(0);

                    ProductLoyaltyModel apl = new ProductLoyaltyModel();
                    apl.setProductID(feeBasicOfPolicy.getAPLCODE());
                    apl.setQuantity(1);
                    apl.setPrice(Integer.parseInt(feeApl));
                    apl.setDiscount(0);
                    apl.setShipping(0);

                    ProductLoyaltyModel opl = new ProductLoyaltyModel();
                    opl.setProductID(feeBasicOfPolicy.getOPLCODE());
                    opl.setQuantity(1);
                    opl.setPrice(Integer.parseInt(feeOpl));
                    opl.setDiscount(0);
                    opl.setShipping(0);

                    lstItem.add(fee);
                    lstItem.add(apl);
                    lstItem.add(opl);
                    category.setLsItems(lstItem);

                    data.setProductItems(category.getLsItems());
                    data.setCategory(Constant.CATEGORY_FEEBASICOFPOL);

                    BaseRequest request = new BaseRequest();
                    request.setJsonDataInput(data);
                    Call<LoyaltyPointConformResponse> call = svRequester.LoyaltyPointConform(request);
                    result = call.execute();
                }

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

                                                            dialog.dismiss();
                                                            Intent intent = new Intent(context, DashboardActivity.class);
                                                            intent.setFlags(
                                                                    Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
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
                                    if (result.getNewAPIToken().equalsIgnoreCase(Constant.ERROR_TOKENINVALID)) {
                                        Utilities.processLoginAgain(context, getString(R.string.message_alert_relogin));
                                    } else
                                        Toast.makeText(context, result.getErrLog(), Toast.LENGTH_SHORT).show();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            // finish the activity
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
