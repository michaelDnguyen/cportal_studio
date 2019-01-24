package com.dlvn.mcustomerportal.activity.prototype;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatSeekBar;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.activity.WebNapasActivity;
import com.dlvn.mcustomerportal.base.BaseActivity;
import com.dlvn.mcustomerportal.common.Constant;
import com.dlvn.mcustomerportal.services.GoogleServiceGenerator;
import com.dlvn.mcustomerportal.services.GoogleServiceRequest;
import com.dlvn.mcustomerportal.services.NetworkUtils;
import com.dlvn.mcustomerportal.services.ServicesGenerator;
import com.dlvn.mcustomerportal.services.ServicesRequest;
import com.dlvn.mcustomerportal.services.model.BaseRequest;
import com.dlvn.mcustomerportal.services.model.request.CPGetPolicyInfoByPOLIDRequest;
import com.dlvn.mcustomerportal.services.model.response.CPGetPolicyInfoByPOLIDResponse;
import com.dlvn.mcustomerportal.services.model.response.CPGetPolicyInfoByPOLIDResult;
import com.dlvn.mcustomerportal.services.model.response.PaymentDetailModel;
import com.dlvn.mcustomerportal.services.model.response.SiteVerifyResponse;
import com.dlvn.mcustomerportal.utils.CPSaveLogTask;
import com.dlvn.mcustomerportal.utils.Utilities;
import com.dlvn.mcustomerportal.utils.myLog;
import com.dlvn.mcustomerportal.view.MyCustomDialog;
import com.dlvn.mcustomerportal.view.clearable_edittext.ClearableEditText;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Response;

public class PaymentNoLoginActivity extends BaseActivity {

    private static final String TAG = PaymentNoLoginActivity.class.getName();

    LinearLayout lloBack, lloStep1, lloStep2, lloStep3;
    Button btnTiepTuc;

    TextView tvDetailPolicyNo, tvDetailClientNo, tvDetailClientName, tvDetailTotal;
    TextView tvDieuChinh;
    ClearableEditText cedtDetailFeeBasic, cedtDetailFeeAPL, cedtDetailFeeOPL;

    TextView tvStep, tvTitle, tvError, tvAgreePolicy;
    ClearableEditText cedtPolicyNo, cedtPOID, cedtDOB, cedtFullName, cedtEmail, cedtPhone;

    ProgressDialog dialog;
    AppCompatSeekBar sbStep;

    int step = 1;
    int percent = 10;
    double total = 0.0;

    ServicesRequest svRequester;
    PaymentDetailModel paymentModel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_payment_nologin);

        svRequester = ServicesGenerator.createService(ServicesRequest.class);

        initViews();
        setListener();
    }

    /**
     * initialize for views
     */
    @TargetApi(Build.VERSION_CODES.O)
    private void initViews() {
        lloBack = findViewById(R.id.lloBack);
        lloStep1 = findViewById(R.id.lloStep1);
        lloStep2 = findViewById(R.id.lloStep2);
        lloStep3 = findViewById(R.id.lloStep3);
        lloStep1.setVisibility(View.VISIBLE);
        lloStep2.setVisibility(View.GONE);
        lloStep3.setVisibility(View.GONE);

        tvDetailPolicyNo = findViewById(R.id.tvDetaiPolicyNo);
        tvDetailClientName = findViewById(R.id.tvDetaiClientName);
        tvDetailClientNo = findViewById(R.id.tvDetaiClientID);
        tvDetailTotal = findViewById(R.id.tvDetaiTotal);
        tvDieuChinh = findViewById(R.id.tvDieuChinh);

        cedtDetailFeeBasic = findViewById(R.id.cedtDetaiFeeBasic);
        cedtDetailFeeAPL = findViewById(R.id.cedtDetaiFeeAPL);
        cedtDetailFeeOPL = findViewById(R.id.cedtDetaiFeeOPL);
        cedtDetailFeeBasic.setEnabled(false);
        cedtDetailFeeAPL.setEnabled(false);
        cedtDetailFeeOPL.setEnabled(false);

        btnTiepTuc = findViewById(R.id.btnTiepTuc);
        tvStep = findViewById(R.id.tvStep);
        tvTitle = findViewById(R.id.tvTitle);
        tvError = findViewById(R.id.tvError);
        tvError.setText(null);
        tvAgreePolicy = findViewById(R.id.tvAgreePolicy);
        tvAgreePolicy.setText(Html.fromHtml(getString(R.string.txt_payment_term)));
        tvAgreePolicy.setMovementMethod(LinkMovementMethod.getInstance());
        tvAgreePolicy.setVisibility(View.GONE);


        sbStep = findViewById(R.id.sbStep);
        sbStep.setThumb(null);
        sbStep.setEnabled(false);

        cedtPolicyNo = findViewById(R.id.cedtPolicy);
        cedtPOID = findViewById(R.id.cedtPOID);
        cedtDOB = findViewById(R.id.cedtDOB);
        cedtFullName = findViewById(R.id.cedtFullName);
        cedtEmail = findViewById(R.id.cedtEmail);
        cedtPhone = findViewById(R.id.cedtPhone);
    }

    /**
     * set listener for view
     */
    private void setListener() {

        lloBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnTiepTuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attempNextStep();
            }
        });

        cedtDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR) - 18;
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

                DatePickerDialog dialog = new DatePickerDialog(PaymentNoLoginActivity.this,
                        android.R.style.Theme_Material_Light_Dialog_Alert, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // TODO Auto-generated method stub
                        cal.set(year, month, dayOfMonth);
                        cedtDOB.setText(dateFormat.format(cal.getTime()));
                    }
                }, year, month, day);
                dialog.show();
            }
        });

        tvDieuChinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cedtDetailFeeBasic.setEnabled(true);
                cedtDetailFeeAPL.setEnabled(true);
                cedtDetailFeeOPL.setEnabled(true);
                cedtDetailFeeBasic.requestFocus();
            }
        });

        cedtDetailFeeBasic.addTextChangedListener(new TextWatcher() {
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
                    cedtDetailFeeBasic.removeTextChangedListener(this);

                    String cleanString = s.toString().replaceAll("[$,.]", "");
                    double parsed = Double.parseDouble(cleanString);
                    String formatted = NumberFormat.getNumberInstance(Locale.US).format(parsed);
                    current = formatted;
                    cedtDetailFeeBasic.setText(formatted);
                    cedtDetailFeeBasic.setSelection(formatted.length());

                    cedtDetailFeeBasic.addTextChangedListener(this);
                    calculaTotal(PaymentNoLoginActivity.this);
                }
            }
        });

        cedtDetailFeeAPL.addTextChangedListener(new TextWatcher() {
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
                    cedtDetailFeeAPL.removeTextChangedListener(this);

                    String cleanString = s.toString().replaceAll("[$,.]", "");
                    double parsed = Double.parseDouble(cleanString);
                    String formatted = NumberFormat.getNumberInstance(Locale.US).format(parsed);
                    current = formatted;
                    cedtDetailFeeAPL.setText(formatted);
                    cedtDetailFeeAPL.setSelection(formatted.length());

                    cedtDetailFeeAPL.addTextChangedListener(this);
                    calculaTotal(PaymentNoLoginActivity.this);
                }
            }
        });

        cedtDetailFeeOPL.addTextChangedListener(new TextWatcher() {
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
                    cedtDetailFeeOPL.removeTextChangedListener(this);

                    String cleanString = s.toString().replaceAll("[$,.]", "");
                    double parsed = Double.parseDouble(cleanString);
                    String formatted = NumberFormat.getNumberInstance(Locale.US).format(parsed);
                    current = formatted;
                    cedtDetailFeeOPL.setText(formatted);
                    cedtDetailFeeOPL.setSelection(formatted.length());

                    cedtDetailFeeOPL.addTextChangedListener(this);
                    calculaTotal(PaymentNoLoginActivity.this);
                }
            }
        });
    }

    /**
     * setup view show/hide follow by step
     */
    private void setUpStepPayment() {
        myLog.e(TAG, "setUpStepPaymentNoLogin Step = " + step);
        switch (step) {
            case 1:
                //Nhập số PolicyNo
                tvTitle.setText(getString(R.string.title_payment_nologin_step01));
                lloStep1.setVisibility(View.VISIBLE);
                lloStep2.setVisibility(View.GONE);
                lloStep3.setVisibility(View.GONE);
                tvAgreePolicy.setVisibility(View.GONE);

                if (paymentModel == null) {
                    cedtPolicyNo.setText("");
                    cedtPOID.setText("");
                    cedtDOB.setText("");
                } else {
                    cedtPolicyNo.setText(paymentModel.getSoHopDong());
                    cedtPOID.setText(paymentModel.getCmnd());
                    cedtDOB.setText(paymentModel.getDob());
                }

                break;
            case 2:
                lloStep1.setVisibility(View.GONE);
                lloStep2.setVisibility(View.VISIBLE);
                lloStep3.setVisibility(View.GONE);
                tvAgreePolicy.setVisibility(View.VISIBLE);

                tvDetailPolicyNo.setText(paymentModel.getSoHopDong());
                tvDetailClientName.setText(paymentModel.getTenKhachHang());
                tvDetailClientNo.setText(paymentModel.getMaKhachHang());

                cedtDetailFeeBasic.setEnabled(false);
                cedtDetailFeeAPL.setEnabled(false);
                cedtDetailFeeOPL.setEnabled(false);

                try {
                    double feeBasic = Double.parseDouble(paymentModel.getPhiBHGoc());
                    double feeApl = Double.parseDouble(paymentModel.getPhiAplGoc());
                    double feeOpl = Double.parseDouble(paymentModel.getPhiOplGoc());

                    cedtDetailFeeBasic.setText(Utilities.formatMoneyToVND(feeBasic + ""));
                    cedtDetailFeeAPL.setText(Utilities.formatMoneyToVND(feeApl + ""));
                    cedtDetailFeeOPL.setText(Utilities.formatMoneyToVND(feeOpl + ""));

                    total = feeBasic + feeApl + feeOpl;
                    tvDetailTotal.setText(Utilities.formatMoneyToVND(total + ""));
                } catch (NumberFormatException e) {
                    myLog.printTrace(e);
                }

                break;
            case 3:
                lloStep1.setVisibility(View.GONE);
                lloStep2.setVisibility(View.GONE);
                lloStep3.setVisibility(View.VISIBLE);
                tvAgreePolicy.setVisibility(View.GONE);

                if (paymentModel == null) {
                    cedtFullName.setText("");
                    cedtEmail.setText("");
                    cedtPhone.setText("");
                } else {
                    cedtFullName.setText(paymentModel.getTenNguoiNop());
                    cedtEmail.setText(paymentModel.getEmail());
                    cedtPhone.setText(paymentModel.getPhone());
                }

                break;
            case 4:
                lloStep1.setVisibility(View.GONE);
                lloStep2.setVisibility(View.GONE);
                lloStep3.setVisibility(View.GONE);
                tvAgreePolicy.setVisibility(View.GONE);

                break;

            default:
                break;
        }
        sbStep.setProgress(percent);
        tvStep.setText(step + "/3");
        tvError.setText(null);
    }

    /**
     * attemp data input follow by step
     */
    private void attempNextStep() {

        myLog.e("attempNextStep Step = " + step);

        boolean cancel = false;
        View focusView = null;

        if (step == 1) {
            cedtPolicyNo.setError(null);
            cedtPOID.setError(null);
            cedtDOB.setError(null);

            String strPolicyNo = cedtPolicyNo.getText().toString();
            String strPOID = cedtPOID.getText().toString();
            String strDoB = cedtDOB.getText().toString();

            if (TextUtils.isEmpty(strPolicyNo)) {
                cedtPolicyNo.setError(getString(R.string.error_field_required));
                focusView = cedtPolicyNo;
                cancel = true;
            }

            if (TextUtils.isEmpty(strPOID)) {
                cedtPOID.setError(getString(R.string.error_field_required));
                focusView = cedtPOID;
                cancel = true;
            }

            if (TextUtils.isEmpty(strDoB)) {
                cedtDOB.setError(getString(R.string.error_field_required));
                focusView = cedtDOB;
                cancel = true;
            }
        }

        if (step == 2) {

        }

        if (step == 3) {
            //valid info fullname, email, phone
            cedtFullName.setError(null);
            cedtEmail.setError(null);
            cedtPhone.setError(null);

            String email = cedtEmail.getText().toString();
            String fullname = cedtFullName.getText().toString();
            String phone = cedtPhone.getText().toString();

            if (TextUtils.isEmpty(fullname)) {
                cedtFullName.setError(getString(R.string.error_field_required));
//                tvError.setText(getString(R.string.error_field_required));
                focusView = cedtFullName;
                cancel = true;
            }

            if (!Utilities.validateEmail(email)) {
                tvError.setText(getString(R.string.error_payment_nologin_step05));
                cedtEmail.setError("Anh/chị cần nhập đúng địa chỉ email.");
                focusView = cedtEmail;
                cancel = true;
            }
            if (!Utilities.isPhoneNumberValid(phone)) {
                tvError.setText(getString(R.string.error_payment_nologin_step06));
                cedtPhone.setError("Anh/chị cần nhập đúng số điện thoại");
                focusView = cedtPhone;
                cancel = true;
            }
        }

        if (cancel)
            focusView.requestFocus();
        else {

            if (step == 1) {
                if (NetworkUtils.isConnectedHaveDialog(PaymentNoLoginActivity.this))
                    verifyWithReCaptcha();

            } else if (step == 2) {

                if (total > 0)
                    paymentModel.setTongSoTien(total + "");

                String fee = cedtDetailFeeBasic.getText().toString().replace(",", "");
                String apl = cedtDetailFeeAPL.getText().toString().replace(",", "");
                String opl = cedtDetailFeeOPL.getText().toString().replace(",", "");
                if (TextUtils.isEmpty(fee))
                    fee = "0";
                if (TextUtils.isEmpty(apl))
                    apl = "0";
                if (TextUtils.isEmpty(opl))
                    opl = "0";
                paymentModel.setPhiBaoHiem(fee);
                paymentModel.setPhiApl(apl);
                paymentModel.setPhiOpl(opl);

                step++;
                percent += 10;
                if (step > 3) {
                    step = 3;
                }
                setUpStepPayment();
            } else if (step == 3) {

                paymentModel.setTenNguoiNop(cedtFullName.getText().toString());
                paymentModel.setEmail(cedtEmail.getText().toString());
                paymentModel.setPhone(cedtPhone.getText().toString());

                step++;
                percent += 10;
                if (step > 3) {
                    step = 3;
                }

                String url = null;
                try {
                    url = initPaymentRequest();

                    Intent napas = new Intent(PaymentNoLoginActivity.this, WebNapasActivity.class);

                    napas.putExtra(Constant.KEY_NAPAS_PAYMENT_URL, url);
                    napas.putExtra(Constant.KEY_NAPAS_PAYMENT_MODEL, paymentModel);

                    startActivityForResult(napas, Constant.REQUEST_NAPAS_PAYMENT);

                } catch (UnsupportedEncodingException e) {
                    myLog.printTrace(e);
                }

            } else if (step == 4) {

                String url = null;
                try {
                    url = initPaymentRequest();

                    Intent napas = new Intent(PaymentNoLoginActivity.this, WebNapasActivity.class);

                    napas.putExtra(Constant.KEY_NAPAS_PAYMENT_URL, url);
                    napas.putExtra(Constant.KEY_NAPAS_PAYMENT_MODEL, paymentModel);

                    startActivityForResult(napas, Constant.REQUEST_NAPAS_PAYMENT);

                } catch (UnsupportedEncodingException e) {
                    myLog.printTrace(e);
                }
            }
        }
    }

    private void calculaTotal(Context context) {
        double phi = 0;
        if (!TextUtils.isEmpty(cedtDetailFeeBasic.getText().toString()))
            phi = Integer.parseInt(cedtDetailFeeBasic.getText().toString().replace(",", ""));
        double phiGoc = Double.parseDouble(paymentModel.getPhiBHGoc());
        if (phiGoc < phi) {
            MyCustomDialog.Builder builder = new MyCustomDialog.Builder(context);
            builder.setMessage(getString(R.string.alert_input_gthl_amount_payment_p1)).setPositiveButton("Đồng ý",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            cedtDetailFeeBasic.setText(Utilities.formatMoneyToVND(paymentModel.getPhiBHGoc()));
                            dialog.dismiss();
                        }
                    });
            builder.create().show();
        }

        double tamung = 0;
        if (!TextUtils.isEmpty(cedtDetailFeeAPL.getText().toString()))
            tamung = Integer.parseInt(cedtDetailFeeAPL.getText().toString().replace(",", ""));
        double tamungGoc = Double.parseDouble(paymentModel.getPhiAplGoc());
        if (tamungGoc < tamung) {
            MyCustomDialog.Builder builder = new MyCustomDialog.Builder(context);
            builder.setMessage(getString(R.string.alert_input_gthl_amount_payment_p2)).setPositiveButton("Đồng ý",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            cedtDetailFeeAPL.setText(Utilities.formatMoneyToVND(paymentModel.getPhiAplGoc()));
                            dialog.dismiss();
                        }
                    });
            builder.create().show();
        }

        double gthl = 0;
        if (!TextUtils.isEmpty(cedtDetailFeeOPL.getText().toString()))
            gthl = Integer.parseInt(cedtDetailFeeOPL.getText().toString().replace(",", ""));

        double gthlGoc = Double.parseDouble(paymentModel.getPhiOplGoc());
        if (gthlGoc < gthl) {
            MyCustomDialog.Builder builder = new MyCustomDialog.Builder(context);
            builder.setMessage(getString(R.string.alert_input_gthl_amount_payment_p3)).setPositiveButton("Đồng ý",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            cedtDetailFeeOPL.setText(Utilities.formatMoneyToVND(paymentModel.getPhiOplGoc()));
                            dialog.dismiss();
                        }
                    });
            builder.create().show();
        }

        tvDetailTotal.setText(Utilities.formatMoneyToVND(phi + tamung + gthl));
    }

    /**
     * Task call API get List Policy Bu CLient ID
     */
    public class getPolicyIDInquiry extends AsyncTask<Void, Void, Response<CPGetPolicyInfoByPOLIDResponse>> {

        Context context;
        String policyID, poID, dob;

        public getPolicyIDInquiry(Context c, String pol, String poid, String dob) {
            context = c;
            this.policyID = pol;
            this.poID = poid;
            this.dob = dob;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (dialog == null)
                dialog = new ProgressDialog(context);
            dialog.setMessage("Kiểm tra số hợp đồng...");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        @Override
        protected Response<CPGetPolicyInfoByPOLIDResponse> doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            Response<CPGetPolicyInfoByPOLIDResponse> result = null;
            try {
                CPGetPolicyInfoByPOLIDRequest data = new CPGetPolicyInfoByPOLIDRequest();

                data.setAction(Constant.POINFO_ACTION_POLICY_CLIENT);
                data.setPolID(policyID);
                data.setPoID(poID);

                try {
                    SimpleDateFormat formattoDate = new SimpleDateFormat("dd/mm/yyyy");
                    Date date = formattoDate.parse(dob);

                    SimpleDateFormat formattoStr = new SimpleDateFormat("yyyy-mm-dd");
                    String toString = formattoStr.format(date);

                    data.setDoB(toString);
                } catch (ParseException e) {
                    myLog.printTrace(e);
                    data.setDoB(dob);
                }

                data.setDeviceId(Utilities.getDeviceID(context));
                data.setOS(Utilities.getDeviceOS());
                data.setProject(Constant.Project_ID);
                data.setAuthentication(Constant.Project_Authentication);

                BaseRequest request = new BaseRequest();
                request.setJsonDataInput(data);

                Call<CPGetPolicyInfoByPOLIDResponse> call = svRequester.CPGetPolicyInfoByPOLID(request);
                result = call.execute();

            } catch (Exception e) {
                myLog.printTrace(e);
                return null;
            }
            return result;
        }

        @Override
        protected void onPostExecute(final Response<CPGetPolicyInfoByPOLIDResponse> success) {

            if (dialog.isShowing())
                dialog.dismiss();

            if (success != null) {
                try {
                    if (success.isSuccessful()) {
                        CPGetPolicyInfoByPOLIDResponse response = success.body();
                        if (response != null)
                            if (response.getResponse() != null) {
                                CPGetPolicyInfoByPOLIDResult result = response.getResponse();
                                if (result != null) {

                                    if (result.getResult() != null && result.getResult().equals("false")) {
                                        //If account not exits --> link to register
                                        Toast.makeText(context, "Truy vấn thông tin hợp đồng không thành công: " + result.getErrLog(), Toast.LENGTH_LONG).show();
                                    } else if (result.getResult() != null && result.getResult().equals("true")) {

                                        if (result.getErrLog().equalsIgnoreCase("EMPTY")) {
                                            tvError.setText(getString(R.string.error_payment_nologin_step01));
                                        }

                                        if (result.getErrLog().equalsIgnoreCase(Constant.ERR_CPLOGIN_SUCCESSFUL)) {

                                            paymentModel = new PaymentDetailModel();
                                            paymentModel.setSoHopDong(policyID);
                                            boolean isPOID = false, isDOB = false;

                                            if (result.getCpPolicyInfos() != null) {
                                                paymentModel.setMaKhachHang(result.getCpPolicyInfos().get(0).getClientID().trim());
                                                paymentModel.setCmnd(result.getCpPolicyInfos().get(0).getPOID().trim());
                                                paymentModel.setTenKhachHang(result.getCpPolicyInfos().get(0).getFullName().trim());

                                                try {
                                                    myLog.e(TAG, "date get : " + result.getCpPolicyInfos().get(0).getDOB());
                                                    SimpleDateFormat formattoDate = new SimpleDateFormat("yyyy-mm-dd");
                                                    Date date = formattoDate.parse(result.getCpPolicyInfos().get(0).getDOB().trim());

                                                    SimpleDateFormat formattoString = new SimpleDateFormat("dd/mm/yyyy");
                                                    String toDate = formattoString.format(date);
                                                    myLog.e(TAG, "Date formatted " + toDate);

                                                    paymentModel.setDob(toDate);
                                                } catch (ParseException e) {
                                                    myLog.printTrace(e);
                                                }
                                            }
                                            if (result.getPolicyInfo() != null) {
                                                paymentModel.setPhiBHGoc(result.getPolicyInfo().get(0).getPolSndryAmt().trim());
                                                paymentModel.setPhiAplGoc(result.getPolicyInfo().get(0).getAPL().trim());
                                                paymentModel.setPhiOplGoc(result.getPolicyInfo().get(0).getOPL().trim());
                                            }

                                            if (!TextUtils.isEmpty(paymentModel.getCmnd()))
                                                if (!paymentModel.getCmnd().trim().equalsIgnoreCase(poID)) {
                                                    tvError.setText(R.string.error_message_input_payment_nologin);
                                                    isPOID = false;
                                                    return;
                                                } else
                                                    isPOID = true;

                                            if (!TextUtils.isEmpty(paymentModel.getDob()))
                                                if (!paymentModel.getDob().trim().equalsIgnoreCase(dob)) {
                                                    tvError.setText(R.string.error_message_input_payment_nologin);
                                                    isDOB = false;
                                                    return;
                                                } else
                                                    isDOB = true;

                                            if (isPOID && isDOB) {
                                                //goto next step
                                                step++;
                                                percent += 10;
                                                if (step > 6) {
                                                    step = 6;
                                                }
                                                setUpStepPayment();
                                            }
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

            }
        }

        @Override
        protected void onCancelled() {

        }
    }

    private void verifyWithReCaptcha() {
        myLog.e(TAG, "verifyWithReCaptcha");
        SafetyNet.getClient(this).verifyWithRecaptcha(getString(R.string.api_sitekey_recaptcha))
                .addOnSuccessListener(this, new OnSuccessListener<SafetyNetApi.RecaptchaTokenResponse>() {
                    @Override
                    public void onSuccess(SafetyNetApi.RecaptchaTokenResponse response) {
                        if (!response.getTokenResult().isEmpty()) {
                            myLog.e(TAG, "Token result " + response.getTokenResult());
                            getSiteVerifyReCaptCha(PaymentNoLoginActivity.this, response.getTokenResult());
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (e instanceof ApiException) {
                            ApiException apiException = (ApiException) e;
                            myLog.e(TAG, "Error message: " +
                                    CommonStatusCodes.getStatusCodeString(apiException.getStatusCode()));
                        } else {
                            myLog.e(TAG, "Unknown type of error: " + e.getMessage());
                        }
                    }
                });
    }

    private void getSiteVerifyReCaptCha(final Context c, final String responseCaptcha) {
        new AsyncTask<Void, Void, Response<SiteVerifyResponse>>() {

            @Override
            protected Response<SiteVerifyResponse> doInBackground(Void... voids) {

                GoogleServiceGenerator svgenerate = new GoogleServiceGenerator();
                GoogleServiceRequest service = svgenerate.createService(c, GoogleServiceRequest.class);

                Call<SiteVerifyResponse> call = service.getSiteVerifyReCaptCha2(getString(R.string.api_secrectkey_recaptcha), responseCaptcha);
                Response<SiteVerifyResponse> response = null;

                try {
                    response = call.execute();
                } catch (IOException e) {
                    myLog.printTrace(e);
                }

                return response;
            }

            @Override
            protected void onPostExecute(Response<SiteVerifyResponse> result) {
                super.onPostExecute(result);
                if (result != null) {
                    if (result.isSuccessful()) {
                        SiteVerifyResponse rs = result.body();
                        if (rs != null) {
                            if (rs.getSuccess()) {

                                if (paymentModel != null) {
                                    String policyNo = cedtPolicyNo.getText().toString();
                                    String poid = cedtPOID.getText().toString();
                                    String dob = cedtDOB.getText().toString();

                                    if (!policyNo.equalsIgnoreCase(paymentModel.getSoHopDong()))
                                        new getPolicyIDInquiry(PaymentNoLoginActivity.this, cedtPolicyNo.getText().toString(), cedtPOID.getText().toString(), cedtDOB.getText().toString()).execute();
                                    else if (!poid.equalsIgnoreCase(paymentModel.getCmnd())) {

                                        tvError.setText(R.string.error_message_input_payment_nologin);
                                    } else if (!dob.equalsIgnoreCase(paymentModel.getDob())) {

                                        tvError.setText(R.string.error_message_input_payment_nologin);
                                    } else {
                                        step++;
                                        percent += 10;
                                        if (step > 6) {
                                            step = 6;
                                        }
                                        setUpStepPayment();
                                    }
                                } else
                                    new getPolicyIDInquiry(PaymentNoLoginActivity.this, cedtPolicyNo.getText().toString(), cedtPOID.getText().toString(), cedtDOB.getText().toString()).execute();
                            } else {
                                MyCustomDialog dialog = new MyCustomDialog.Builder(PaymentNoLoginActivity.this)
                                        .setMessage(getString(R.string.alert_message_verify_captcha))
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
        }.execute();
    }

    private String initPaymentRequest() throws UnsupportedEncodingException {

        StringBuffer buf = new StringBuffer();
        buf.append(Constant.URL_PAYMENT_NOLOGIN);
        buf.append("?");

        buf.append("PolicyNo=" + Base64.encodeToString(paymentModel.getSoHopDong().getBytes("UTF-8"), Base64.DEFAULT));
        buf.append("&");

        buf.append("sPayer=" + Base64.encodeToString(paymentModel.getTenNguoiNop().getBytes("UTF-8"), Base64.DEFAULT));
        buf.append("&");
        buf.append("sPhoneNumber=" + Base64.encodeToString(paymentModel.getPhone().getBytes("UTF-8"), Base64.DEFAULT));
        buf.append("&");
        buf.append("sPOName=" + Base64.encodeToString(paymentModel.getTenKhachHang().getBytes("UTF-8"), Base64.DEFAULT));

        buf.append("&");
        buf.append("Amount=" + Base64.encodeToString((paymentModel.getPhiBaoHiem()).getBytes("UTF-8"), Base64.DEFAULT));

//        if (!TextUtils.isEmpty(paymentModel.getPhiApl()) && !paymentModel.getPhiApl().equalsIgnoreCase("0")) {
        buf.append("&");
        buf.append("APL=" + Base64.encodeToString(paymentModel.getPhiApl().getBytes("UTF-8"), Base64.DEFAULT));
//        }
//        if (!TextUtils.isEmpty(paymentModel.getPhiOpl()) && !paymentModel.getPhiOpl().equalsIgnoreCase("0")) {
        buf.append("&");
        buf.append("OPL=" + Base64.encodeToString(paymentModel.getPhiOpl().getBytes("UTF-8"), Base64.DEFAULT));
//        }

//        buf.append("&");
//        buf.append("sFCCode=" + Base64.encodeToString("110555".getBytes("UTF-8"), Base64.DEFAULT));
//        buf.append("&");
//        buf.append("sAgentName=" + Base64.encodeToString("Nguyen Van Teo".getBytes("UTF-8"), Base64.DEFAULT));
//        buf.append("&");
//        buf.append("sAgentType=" + Base64.encodeToString("BM".getBytes("UTF-8"), Base64.DEFAULT));
//        // Add fee frequency
//        buf.append("&");
//        buf.append("sFeeFrequency=" + Base64.encodeToString("Yearly".getBytes("UTF-8"), Base64.DEFAULT));
        return buf.toString();
    }

    @Override
    protected void onResume() {
        super.onResume();
        new CPSaveLogTask(this, Constant.CPLOG_PAYMENT_OPEN).execute();
    }

    @Override
    protected void onStop() {
        super.onStop();
        new CPSaveLogTask(this, Constant.CPLOG_PAYMENT_CLOSE).execute();
    }

    @Override
    public void onBackPressed() {
        if (step > 1) {
            step--;
            setUpStepPayment();
        } else
            super.onBackPressed();
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
}
