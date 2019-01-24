package com.dlvn.mcustomerportal.activity;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.base.BaseActivity;
import com.dlvn.mcustomerportal.common.Constant;
import com.dlvn.mcustomerportal.common.CustomPref;
import com.dlvn.mcustomerportal.services.ServicesGenerator;
import com.dlvn.mcustomerportal.services.ServicesRequest;
import com.dlvn.mcustomerportal.services.model.BaseRequest;
import com.dlvn.mcustomerportal.services.model.request.GetFeeByClientIDRequest;
import com.dlvn.mcustomerportal.services.model.request.GetFeeOfBasicPolByCLIIDRequest;
import com.dlvn.mcustomerportal.services.model.response.FeeBasicOfPolicy;
import com.dlvn.mcustomerportal.services.model.response.GetFeeByClientIDResponse;
import com.dlvn.mcustomerportal.services.model.response.GetFeeByClientIDResult;
import com.dlvn.mcustomerportal.services.model.response.GetFeeOfBasicPolByCLIIDResponse;
import com.dlvn.mcustomerportal.services.model.response.GetFeeOfBasicPolByCLIIDResult;
import com.dlvn.mcustomerportal.services.model.response.PaymentDetailModel;
import com.dlvn.mcustomerportal.utils.Utilities;
import com.dlvn.mcustomerportal.utils.myLog;
import com.dlvn.mcustomerportal.view.MyCustomDialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Response;

public class Bonus_NopPhiBH_Step01_Activity extends BaseActivity implements AdapterView.OnItemSelectedListener {

    Spinner spnHopDong;
    TextView tvTenKhachHang, tvMaKhachHang, tvDiem;
    TextView soTienPhaiThanhToanPhiDinhKy, soTienPhaiThanhToanKhoanTamUngDeDongPhi, soTienPhaiThanhToanKhoanTamUng;
    EditText edtPhiDinhKy, edtKhoanTamUng, edtGiaTriHoanLai;
    Button btnTiep;

    ServicesRequest svRequester;
    ProgressDialog dialog;

    FeeBasicOfPolicy feeChoice;
    PaymentDetailModel paymentModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bonus__nop_phi_bh);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getViews();
        initDatas();
        setListener();
    }

    private void getViews() {
        // TODO Auto-generated method stub
        spnHopDong = findViewById(R.id.spnHopDong);

        tvMaKhachHang = findViewById(R.id.tvMaKhachHang);
        tvTenKhachHang = findViewById(R.id.tvTenKhachHang);
        tvDiem = findViewById(R.id.tvDiem);

        edtPhiDinhKy = findViewById(R.id.edtPhiBaoHiem);
        edtKhoanTamUng = findViewById(R.id.edtKhoanTamUng);
        edtGiaTriHoanLai = findViewById(R.id.edtGiaTriHoanLai);

        btnTiep = findViewById(R.id.btnTiep);

        soTienPhaiThanhToanPhiDinhKy = findViewById(R.id.soTienPhaiThanhToanPhiDinhKy);
        soTienPhaiThanhToanKhoanTamUngDeDongPhi = findViewById(R.id.soTienPhaiThanhToanKhoanTamUngDeDongPhi);
        soTienPhaiThanhToanKhoanTamUng = findViewById(R.id.soTienPhaiThanhToanKhoanTamUng);

    }

    private void initDatas() {
        svRequester = ServicesGenerator.createService(ServicesRequest.class);

        if (CustomPref.haveLogin(this)) {
            tvTenKhachHang.setText(CustomPref.getFullName(this));
            tvMaKhachHang.setText(CustomPref.getUserID(this));
            tvDiem.setText(CustomPref.getUserPoint(this) + "");
        }

        if (getIntent().getExtras() != null)
            if (getIntent().getExtras().containsKey(Constant.INT_KEY_PAYMENT_MODEL))
                paymentModel = getIntent().getParcelableExtra(Constant.INT_KEY_PAYMENT_MODEL);

        if (paymentModel == null)
            new GetFeeByClientIDTask(this).execute();
        else {
            List<String> list = new ArrayList<String>();
            list.add(paymentModel.getSoHopDong());

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnHopDong.setAdapter(dataAdapter);

            soTienPhaiThanhToanPhiDinhKy.setText(Utilities.formatMoneyToVND(paymentModel.getPhiBHGoc()));
            soTienPhaiThanhToanKhoanTamUngDeDongPhi.setText(Utilities.formatMoneyToVND(paymentModel.getPhiAplGoc()));
            soTienPhaiThanhToanKhoanTamUng.setText(Utilities.formatMoneyToVND(paymentModel.getPhiOplGoc()));

            edtPhiDinhKy.setText(Utilities.formatMoneyToVND(paymentModel.getPhiBHGoc()));
            edtKhoanTamUng.setText(Utilities.formatMoneyToVND(paymentModel.getPhiAplGoc()));
            edtGiaTriHoanLai.setText(Utilities.formatMoneyToVND(paymentModel.getPhiOplGoc()));

            feeChoice = new FeeBasicOfPolicy();
            feeChoice.setPOLSNDRYAMTCODE(Constant.CONTS_FEE_SNDRY_AMT_CODE);
            feeChoice.setAPLCODE(Constant.CONTS_FEE_APL_CODE);
            feeChoice.setOPLCODE(Constant.CONTS_FEE_OPL_CODE);
            feeChoice.setPOLSNDRYAMT(paymentModel.getPhiBHGoc());
            feeChoice.setAPL(paymentModel.getPhiAplGoc());
            feeChoice.setOPL(paymentModel.getPhiOplGoc());
            feeChoice.setPOLID(paymentModel.getSoHopDong());
            feeChoice.setCLIID(CustomPref.getUserID(this));
        }
    }

    private void setListener() {
        // TODO Auto-generated method stub
        edtPhiDinhKy.addTextChangedListener(new TextWatcher() {
            private String current = "";

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current) && !TextUtils.isEmpty(s.toString())) {
                    edtPhiDinhKy.removeTextChangedListener(this);

                    String cleanString = s.toString().replaceAll("[$,.]", "");
                    double parsed = Double.parseDouble(cleanString);
                    String formatted = NumberFormat.getNumberInstance(Locale.US).format(parsed);
                    current = formatted;
                    edtPhiDinhKy.setText(formatted);
                    edtPhiDinhKy.setSelection(formatted.length());

                    edtPhiDinhKy.addTextChangedListener(this);
                }
            }
        });

        edtKhoanTamUng.addTextChangedListener(new TextWatcher() {
            private String current = "";

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current) && !TextUtils.isEmpty(s.toString())) {
                    edtKhoanTamUng.removeTextChangedListener(this);

                    String cleanString = s.toString().replaceAll("[$,.]", "");
                    double parsed = Double.parseDouble(cleanString);
                    String formatted = NumberFormat.getNumberInstance(Locale.US).format(parsed);
                    current = formatted;
                    edtKhoanTamUng.setText(formatted);
                    edtKhoanTamUng.setSelection(formatted.length());

                    edtKhoanTamUng.addTextChangedListener(this);
                }
            }
        });

        edtGiaTriHoanLai.addTextChangedListener(new TextWatcher() {
            private String current = "";

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current) && !TextUtils.isEmpty(s.toString())) {
                    edtGiaTriHoanLai.removeTextChangedListener(this);

                    String cleanString = s.toString().replaceAll("[$,.]", "");
                    double parsed = Double.parseDouble(cleanString);
                    String formatted = NumberFormat.getNumberInstance(Locale.US).format(parsed);
                    current = formatted;
                    edtGiaTriHoanLai.setText(formatted);
                    edtGiaTriHoanLai.setSelection(formatted.length());

                    edtGiaTriHoanLai.addTextChangedListener(this);
                }
            }
        });

        btnTiep.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String shd = (String) spnHopDong.getSelectedItem();
                String phibh = edtPhiDinhKy.getText().toString();
                String tamungdongphi = edtKhoanTamUng.getText().toString();
                String tamung = edtGiaTriHoanLai.getText().toString();

                if (TextUtils.isEmpty(phibh)) {
                    phibh = "0";
                    edtPhiDinhKy.setText("0");
                }
                if (TextUtils.isEmpty(tamungdongphi)) {
                    tamungdongphi = "0";
                    edtKhoanTamUng.setText("0");
                }
                if (TextUtils.isEmpty(tamung)) {
                    tamung = "0";
                    edtGiaTriHoanLai.setText("0");
                }

                String phiBHBasic = soTienPhaiThanhToanPhiDinhKy.getText().toString().replace(",", "");
                String tamUngDPBasic = soTienPhaiThanhToanKhoanTamUngDeDongPhi.getText().toString().replace(",", "");
                String tamUngBasic = soTienPhaiThanhToanKhoanTamUng.getText().toString().replace(",", "");

                double ipbh = Double.parseDouble(phibh.replace(",", ""));
                double itamungdp = Double.parseDouble(tamungdongphi.replace(",", ""));
                double itamung = Double.parseDouble(tamung.replace(",", ""));
                double total = ipbh + itamung + itamungdp;

                if (CustomPref.getUserPoint(Bonus_NopPhiBH_Step01_Activity.this) * 1000 >= total) {

                    if (TextUtils.isEmpty(phiBHBasic))
                        phiBHBasic = "0";
                    if (TextUtils.isEmpty(tamUngDPBasic))
                        tamUngDPBasic = "0";
                    if (TextUtils.isEmpty(tamUngBasic))
                        tamUngBasic = "0";

                    Intent intent = new Intent(Bonus_NopPhiBH_Step01_Activity.this, Bonus_NopPhiBH_Step02_Activity.class);
                    intent.putExtra(Constant.NOPPHI_SOHOPDONG, shd);
                    intent.putExtra(Constant.NOPPHI_PHIBAOHIEM, phibh);
                    intent.putExtra(Constant.NOPPHI_TAMUNG_DONGPHI, tamungdongphi);
                    intent.putExtra(Constant.NOPPHI_TAMUNG, tamung);
                    intent.putExtra("soTienPhaiThanhToanPhiDinhKy", phiBHBasic);
                    intent.putExtra("soTienPhaiThanhToanKhoanTamUngDeDongPhi", tamUngDPBasic);
                    intent.putExtra("soTienPhaiThanhToanKhoanTamUng", tamUngBasic);

                    intent.putExtra("FeeBasicConform", feeChoice);

                    startActivity(intent);
                } else {
                    String message = "Số điểm hiện tại của Anh/Chị (" + Utilities.formatMoneyToVND(CustomPref.getUserPoint(Bonus_NopPhiBH_Step01_Activity.this) * 1000)
                            + ") không đủ để đóng phí (" + Utilities.formatMoneyToVND(total) + ").";

                    MyCustomDialog dialog = new MyCustomDialog.Builder(Bonus_NopPhiBH_Step01_Activity.this)
                            .setMessage(message)
                            .setPositiveButton(getString(R.string.confirm_ok), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).create();
                    dialog.show();
                }
            }
        });

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
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    public class GetFeeByClientIDTask extends AsyncTask<Void, Void, Response<GetFeeByClientIDResponse>> {
        Context context;

        public GetFeeByClientIDTask(Context c) {
            context = c;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //TODO: progress dialog
            dialog = new ProgressDialog(context);
            dialog.setMessage("Xin vui lòng chờ dữ liệu...");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        @Override
        protected Response<GetFeeByClientIDResponse> doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            Response<GetFeeByClientIDResponse> result = null;
            try {
                GetFeeByClientIDRequest request = new GetFeeByClientIDRequest();
                request.setClientID(CustomPref.getUserID(context));

                Call<GetFeeByClientIDResponse> call = svRequester.getFeeByClientID(request);
                result = call.execute();

            } catch (Exception e) {
                myLog.printTrace(e);
                return null;
            }

            // TODO: register the new account here.
            return result;
        }

        @Override
        protected void onPostExecute(final Response<GetFeeByClientIDResponse> success) {
            dialog.dismiss();

            if (success != null) {
                try {
                    if (success.isSuccessful()) {
                        GetFeeByClientIDResponse response = success.body();
                        if (response != null)
                            if (response.getGetFeeByClientID() != null) {
                                //TODO: set result to be final
                                final GetFeeByClientIDResult result = response.getGetFeeByClientID();
                                if (result != null) {

                                    if (result.getResult() != null && result.getResult().equals("false")) {
                                        //If account not exits --> link to register
                                        myLog.e("Error when getFee by ClientID " + result.getErrLog());

                                    } else if (result.getResult() != null && result.getResult().equals("true")) {

                                        if (result.getLstFeeByPolicy() != null) {


                                            // TODO: list to store FeeBasicByPolicy
                                            List<String> list = new ArrayList<String>();

                                            if (result.getLstFeeByPolicy().size() > 0) {
                                                for (int i = 0; i < result.getLstFeeByPolicy().size(); i++) {
                                                    list.add(result.getLstFeeByPolicy().get(i).getPolicyID());
                                                }
                                            }

                                            //TODO: tạo adapter cho spinner
                                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, list);
                                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                            spnHopDong.setAdapter(dataAdapter);

                                            spnHopDong.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                    //TODO: testing pushing policyID
                                                    soTienPhaiThanhToanPhiDinhKy.setText(Utilities.formatMoneyToVND(result.getLstFeeByPolicy().get(position).getFee().getPolSndryAmt()));
                                                    soTienPhaiThanhToanKhoanTamUngDeDongPhi.setText(Utilities.formatMoneyToVND(result.getLstFeeByPolicy().get(position).getFee().getApl()));
                                                    soTienPhaiThanhToanKhoanTamUng.setText(Utilities.formatMoneyToVND(result.getLstFeeByPolicy().get(position).getFee().getOpl()));

                                                    edtPhiDinhKy.setText(Utilities.formatMoneyToVND(result.getLstFeeByPolicy().get(position).getFee().getPolSndryAmt()));
                                                    edtKhoanTamUng.setText(Utilities.formatMoneyToVND(result.getLstFeeByPolicy().get(position).getFee().getApl()));
                                                    edtGiaTriHoanLai.setText(Utilities.formatMoneyToVND(result.getLstFeeByPolicy().get(position).getFee().getOpl()));

                                                    feeChoice = new FeeBasicOfPolicy();
                                                    feeChoice.setPOLID(result.getLstFeeByPolicy().get(position).getPolicyID());
                                                    feeChoice.setPOLSNDRYAMTCODE(Constant.CONTS_FEE_SNDRY_AMT_CODE);
                                                    feeChoice.setAPLCODE(Constant.CONTS_FEE_APL_CODE);
                                                    feeChoice.setOPLCODE(Constant.CONTS_FEE_OPL_CODE);
                                                    feeChoice.setPOLSNDRYAMT(result.getLstFeeByPolicy().get(position).getFee().getPolSndryAmt());
                                                    feeChoice.setAPL(result.getLstFeeByPolicy().get(position).getFee().getApl());
                                                    feeChoice.setOPL(result.getLstFeeByPolicy().get(position).getFee().getOpl());

                                                }

                                                public void onNothingSelected(AdapterView<?> parent) {
                                                }
                                            });
                                            //END Adapter spinner
                                        }
                                    }
                                }
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
                } catch (Exception e) {
                    myLog.printTrace(e);
                }
            }
        }
    }

    /**
     * Task call API get client point By CLient ID
     */
    public class GetFeeOfBasicPolByCLIIDTask extends AsyncTask<Void, Void, Response<GetFeeOfBasicPolByCLIIDResponse>> {
        Context context;

        public GetFeeOfBasicPolByCLIIDTask(Context c) {
            context = c;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //TODO: progress dialog
            dialog = new ProgressDialog(context);
            dialog.setMessage("Xin vui lòng chờ dữ liệu...");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        @Override
        protected Response<GetFeeOfBasicPolByCLIIDResponse> doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            Response<GetFeeOfBasicPolByCLIIDResponse> result = null;
            try {
                GetFeeOfBasicPolByCLIIDRequest data = new GetFeeOfBasicPolByCLIIDRequest();

                data.setClientID(CustomPref.getUserID(context));
                data.setAPIToken(CustomPref.getAPIToken(context));
                data.setDeviceID(Utilities.getDeviceID(context));
                data.setOS(Utilities.getDeviceOS());
                data.setProject(Constant.Project_ID);
                data.setAuthentication(Constant.Project_Authentication);
                data.setUserLogin(CustomPref.getUserName(context));

                BaseRequest request = new BaseRequest();
                request.setJsonDataInput(data);

                Call<GetFeeOfBasicPolByCLIIDResponse> call = svRequester.GetFeeOfBasicPolByCLIID(request);
                result = call.execute();

            } catch (Exception e) {
                myLog.printTrace(e);
                return null;
            }

            // TODO: register the new account here.
            return result;
        }

        @Override
        protected void onPostExecute(final Response<GetFeeOfBasicPolByCLIIDResponse> success) {
            dialog.dismiss();

            if (success != null) {
                try {
                    if (success.isSuccessful()) {
                        GetFeeOfBasicPolByCLIIDResponse response = success.body();
                        if (response != null)
                            if (response.getResponse() != null) {
                                //TODO: set result to be final
                                final GetFeeOfBasicPolByCLIIDResult result = response.getResponse();
                                if (result != null) {

                                    if (result.getResult() != null && result.getResult().equals("false")) {
                                        //If account not exits --> link to register
                                        if (result.getNewAPIToken().equalsIgnoreCase(Constant.ERROR_TOKENINVALID)) {
                                            Utilities.processLoginAgain(context, getString(R.string.message_alert_relogin));
                                        }
                                    } else if (result.getResult() != null && result.getResult().equals("true")) {

                                        //Save Token
                                        if (!TextUtils.isEmpty(result.getNewAPIToken()))
                                            CustomPref.saveAPIToken(context, result.getNewAPIToken());

                                        if (result.getLstFeeBasic() != null) {


                                            // TODO: list to store FeeBasicByPolicy
                                            List<String> list = new ArrayList<String>();

                                            if (result.getLstFeeBasic().size() > 0) {
                                                for (int i = 0; i < result.getLstFeeBasic().size(); i++) {
                                                    list.add(result.getLstFeeBasic().get(i).getPOLID());
                                                }
                                            }

                                            //TODO: tạo adapter cho spinner
                                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, list);
                                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                            spnHopDong.setAdapter(dataAdapter);

                                            spnHopDong.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                    //TODO: testing pushing policyID
                                                    soTienPhaiThanhToanPhiDinhKy.setText(Utilities.formatMoneyToVND(result.getLstFeeBasic().get(position).getPOLSNDRYAMT()));
                                                    soTienPhaiThanhToanKhoanTamUngDeDongPhi.setText(Utilities.formatMoneyToVND(result.getLstFeeBasic().get(position).getAPL()));
                                                    soTienPhaiThanhToanKhoanTamUng.setText(Utilities.formatMoneyToVND(result.getLstFeeBasic().get(position).getOPL()));

                                                    edtPhiDinhKy.setText(Utilities.formatMoneyToVND(result.getLstFeeBasic().get(position).getPOLSNDRYAMT()));
                                                    edtKhoanTamUng.setText(Utilities.formatMoneyToVND(result.getLstFeeBasic().get(position).getAPL()));
                                                    edtGiaTriHoanLai.setText(Utilities.formatMoneyToVND(result.getLstFeeBasic().get(position).getOPL()));

                                                    feeChoice = result.getLstFeeBasic().get(position);
                                                }

                                                public void onNothingSelected(AdapterView<?> parent) {
                                                }
                                            });
                                            //END Adapter spinner
                                        }
                                    }
                                }
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
                } catch (Exception e) {
                    myLog.printTrace(e);
                }
            }
        }
    }
}
