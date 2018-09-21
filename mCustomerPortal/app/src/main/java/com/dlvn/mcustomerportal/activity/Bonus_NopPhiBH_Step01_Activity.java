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
import com.dlvn.mcustomerportal.services.model.request.GetFeeOfBasicPolByCLIIDRequest;
import com.dlvn.mcustomerportal.services.model.response.FeeBasicOfPolicy;
import com.dlvn.mcustomerportal.services.model.response.GetFeeOfBasicPolByCLIIDResponse;
import com.dlvn.mcustomerportal.services.model.response.GetFeeOfBasicPolByCLIIDResult;
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
import android.util.Log;
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
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Response;

public class Bonus_NopPhiBH_Step01_Activity extends BaseActivity implements AdapterView.OnItemSelectedListener {
    Spinner spnHopDong;

    TextView tvTenKhachHang, tvMaKhachHang, tvDiem;

    //TODO: bonus tv declaration
    TextView soTienPhaiThanhToanPhiDinhKy, soTienPhaiThanhToanKhoanTamUngDeDongPhi, soTienPhaiThanhToanKhoanTamUng;


    EditText edtPhiDinhKy, edtKhoanTamUng, edtGiaTriHoanLai;
    Button btnTiep;

    //TODO: bonus declaration
    ServicesRequest svRequester;
    GetFeeOfBasicPolByCLIIDTask FeeOfPolicyTask = null;
    ProgressDialog dialog;

    FeeBasicOfPolicy feeChoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bonus__nop_phi_bh);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getViews();
        svRequester = ServicesGenerator.createService(ServicesRequest.class);
        Log.e("testing", "onCreate");
        //TODO:testing
        initDatas();
        Log.e("testing", "onCreate2");
        setListener();
    }

    private void getViews() {
        // TODO Auto-generated method stub
        spnHopDong = (Spinner) findViewById(R.id.spnHopDong);

        tvMaKhachHang = (TextView) findViewById(R.id.tvMaKhachHang);
        tvTenKhachHang = (TextView) findViewById(R.id.tvTenKhachHang);
        tvDiem = (TextView) findViewById(R.id.tvDiem);

        edtPhiDinhKy = (EditText) findViewById(R.id.edtPhiBaoHiem);
        edtKhoanTamUng = (EditText) findViewById(R.id.edtKhoanTamUng);
        edtGiaTriHoanLai = (EditText) findViewById(R.id.edtGiaTriHoanLai);

        btnTiep = (Button) findViewById(R.id.btnTiep);

        soTienPhaiThanhToanPhiDinhKy = (TextView) findViewById(R.id.soTienPhaiThanhToanPhiDinhKy);
        soTienPhaiThanhToanKhoanTamUngDeDongPhi = (TextView) findViewById(R.id.soTienPhaiThanhToanKhoanTamUngDeDongPhi);
        soTienPhaiThanhToanKhoanTamUng = (TextView) findViewById(R.id.soTienPhaiThanhToanKhoanTamUng);

    }

    private void initDatas() {
        if (CustomPref.haveLogin(this)) {
            tvTenKhachHang.setText(CustomPref.getFullName(this));
            tvMaKhachHang.setText(CustomPref.getUserID(this));
            tvDiem.setText(CustomPref.getUserPoint(this) + "");
        }

        new GetFeeOfBasicPolByCLIIDTask(this).execute();
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

                String phiBHBasic = soTienPhaiThanhToanPhiDinhKy.getText().toString().replace(",","");
                String tamUngDPBasic = soTienPhaiThanhToanKhoanTamUngDeDongPhi.getText().toString().replace(",","");
                String tamUngBasic = soTienPhaiThanhToanKhoanTamUng.getText().toString().replace(",","");

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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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
                data.setOS(Utilities.getDeviceName() + "-" + Utilities.getVersion());
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
            FeeOfPolicyTask = null;
            Log.e("onPostExecute", "task completed");
            //TODO: dừng progress dialog
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
                                        if (result.getNewAPIToken().equalsIgnoreCase("invalidtoken")) {
                                            Utilities.processLoginAgain(context, getString(R.string.message_alert_relogin));
                                        }
                                    } else if (result.getResult() != null && result.getResult().equals("true")) {

                                        //Save Token
                                        if (!TextUtils.isEmpty(result.getNewAPIToken()))
                                            CustomPref.saveToken(context, result.getNewAPIToken());

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

        @Override
        protected void onCancelled() {
            FeeOfPolicyTask = null;
        }
    }
}
