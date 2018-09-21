package com.dlvn.mcustomerportal.activity.prototype;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSeekBar;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.activity.PaymentInputActivity;
import com.dlvn.mcustomerportal.activity.WebNapasActivity;
import com.dlvn.mcustomerportal.base.BaseActivity;
import com.dlvn.mcustomerportal.common.Constant;
import com.dlvn.mcustomerportal.services.ServicesGenerator;
import com.dlvn.mcustomerportal.services.ServicesRequest;
import com.dlvn.mcustomerportal.services.model.BaseRequest;
import com.dlvn.mcustomerportal.services.model.request.CPGetPolicyInfoByPOLIDRequest;
import com.dlvn.mcustomerportal.services.model.request.SavePaymentRequest;
import com.dlvn.mcustomerportal.services.model.response.CPGetPolicyInfoByPOLIDResponse;
import com.dlvn.mcustomerportal.services.model.response.CPGetPolicyInfoByPOLIDResult;
import com.dlvn.mcustomerportal.services.model.response.PaymentDetailModel;
import com.dlvn.mcustomerportal.services.model.response.SavePaymentResponse;
import com.dlvn.mcustomerportal.utils.Utilities;
import com.dlvn.mcustomerportal.utils.myLog;
import com.dlvn.mcustomerportal.view.clearable_edittext.ClearableEditText;

import java.io.UnsupportedEncodingException;

import retrofit2.Call;
import retrofit2.Response;

public class PaymentNoLoginActivity extends BaseActivity {

    private static final String TAG = PaymentNoLoginActivity.class.getName();

    LinearLayout lloBack, lloContentINput, lloContentDetail;
    Button btnTiepTuc;

    TextView tvDetailPolicyNo, tvDetailClientNo, tvDetailClientName, tvDetailFeeBasic, tvDetailFeeAPL, tvDetailFeeOPL, tvDetailTotal;

    TextView tvStep, tvTitle, tvError;
    ClearableEditText cedtInput;

    AppCompatSeekBar sbStep;

    int step = 1;
    int percent = 10;
    double total = 0.0;
    ServicesRequest svRequester;

    PaymentDetailModel paymentModel = null;

    ProgressDialog dialog;

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
    private void initViews() {
        lloBack = findViewById(R.id.lloBack);
        lloContentINput = findViewById(R.id.lloContentInput);
        lloContentDetail = findViewById(R.id.lloContentDetail);

        tvDetailPolicyNo = findViewById(R.id.tvDetaiPolicyNo);
        tvDetailClientName = findViewById(R.id.tvDetaiClientName);
        tvDetailClientNo = findViewById(R.id.tvDetaiClientID);
        tvDetailFeeBasic = findViewById(R.id.tvDetaiFeeBasic);
        tvDetailFeeAPL = findViewById(R.id.tvDetaiFeeAPL);
        tvDetailFeeOPL = findViewById(R.id.tvDetaiFeeOPL);
        tvDetailTotal = findViewById(R.id.tvDetaiTotal);

        btnTiepTuc = findViewById(R.id.btnTiepTuc);
        tvStep = findViewById(R.id.tvStep);
        tvTitle = findViewById(R.id.tvTitle);
        tvError = findViewById(R.id.tvError);

        sbStep = findViewById(R.id.sbStep);
        sbStep.setThumb(null);
        sbStep.setEnabled(false);

        cedtInput = findViewById(R.id.cedtInput);
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

    }

    /**
     * setup view show/hide follow by step
     */
    private void setUpStepPayment() {
        myLog.E(TAG, "setUpStepPaymentNoLogin Step = " + step);
        switch (step) {
            case 1:
                //Nhập số PolicyNo
                tvTitle.setText(getString(R.string.title_payment_nologin_step01));
                cedtInput.setInputType(InputType.TYPE_CLASS_NUMBER);
                cedtInput.setEnabled(true);
                cedtInput.setHint(getString(R.string.hint_payment_nologin_01));

                if (paymentModel == null)
                    cedtInput.setText(null);
                else
                    cedtInput.setText(paymentModel.getSoHopDong());


                break;
            case 2:
                lloContentDetail.setVisibility(View.GONE);
                lloContentINput.setVisibility(View.VISIBLE);
                //Nhập CMND
                tvTitle.setText(getString(R.string.title_payment_nologin_step02));
                cedtInput.setHint(getString(R.string.hint_payment_nologin_02));
                cedtInput.setInputType(InputType.TYPE_CLASS_NUMBER);
                cedtInput.setEnabled(true);
                if (paymentModel == null)
                    cedtInput.setText(null);
                else
                    cedtInput.setText(paymentModel.getCmnd());
                cedtInput.setVisibility(View.VISIBLE);

                break;
            case 3:
                lloContentDetail.setVisibility(View.VISIBLE);
                lloContentINput.setVisibility(View.GONE);

                tvDetailPolicyNo.setText(paymentModel.getSoHopDong());
                tvDetailClientName.setText(paymentModel.getTenKhachHang());
                tvDetailClientNo.setText(paymentModel.getMaKhachHang());

                try {
                    double feeBasic = Double.parseDouble(paymentModel.getPhiBHGoc());
                    double feeApl = Double.parseDouble(paymentModel.getPhiAplGoc());
                    double feeOpl = Double.parseDouble(paymentModel.getPhiOplGoc());

                    tvDetailFeeBasic.setText(Utilities.formatMoneyToVND(feeBasic + ""));
                    tvDetailFeeAPL.setText(Utilities.formatMoneyToVND(feeApl + ""));
                    tvDetailFeeOPL.setText(Utilities.formatMoneyToVND(feeOpl + ""));

                    total = feeBasic + feeApl + feeOpl;
                    tvDetailTotal.setText(Utilities.formatMoneyToVND(total + ""));
                } catch (NumberFormatException e) {
                    myLog.printTrace(e);
                }

                break;
            case 4:
                lloContentDetail.setVisibility(View.GONE);
                lloContentINput.setVisibility(View.VISIBLE);

                //Nhập Họ tên
                tvTitle.setText(getString(R.string.title_payment_nologin_step04));
                cedtInput.setHint(getString(R.string.hint_payment_nologin_04));
                cedtInput.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                cedtInput.setEnabled(true);
                if (paymentModel == null)
                    cedtInput.setText(null);
                else
                    cedtInput.setText(paymentModel.getTenNguoiNop());
                cedtInput.setVisibility(View.VISIBLE);
                break;

            case 5:
                //Nhập Email
                tvTitle.setText(getString(R.string.title_payment_nologin_step05));
                cedtInput.setHint(getString(R.string.hint_payment_nologin_05));
                cedtInput.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                cedtInput.setEnabled(true);
                if (paymentModel == null)
                    cedtInput.setText(null);
                else
                    cedtInput.setText(paymentModel.getEmail());
                break;

            case 6:
                //Nhập số ĐT
                tvTitle.setText(getString(R.string.title_payment_nologin_step06));
                cedtInput.setHint(getString(R.string.hint_payment_nologin_06));
                cedtInput.setInputType(InputType.TYPE_CLASS_PHONE);
                cedtInput.setEnabled(true);
                if (paymentModel == null)
                    cedtInput.setText(null);
                else
                    cedtInput.setText(paymentModel.getPhone());
                break;
            default:
                break;
        }
        sbStep.setProgress(percent);
        tvStep.setText(step + "/6");
        tvError.setText(null);
    }

    /**
     * attemp data input follow by step
     */
    private void attempNextStep() {

        myLog.E("attempNextStep Step = " + step);
        cedtInput.setError(null);
        String strInput = cedtInput.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(strInput)) {
            cedtInput.setError(getString(R.string.error_field_required));
            focusView = cedtInput;
            cancel = true;
        }

        if (step == 1) {

        }

        if (step == 2)
            if (strInput.length() > 9) {
//                    cedtInput.setError(getString(R.string.error_incorrect_fullname));
                tvError.setText(getString(R.string.error_payment_nologin_step02));
                focusView = cedtInput;
                cancel = true;
            }

        if (step == 3) {
            //valid info payment detail

        }

        if (step == 4) {
            //valid info FullName

        }

        if (step == 5)
            //valid email
            if (!Utilities.validateEmail(strInput)) {
//                    cedtInput.setError(getString(R.string.error_incorrect_fullname));
                tvError.setText(getString(R.string.error_payment_nologin_step05));
                focusView = cedtInput;
                cancel = true;
            }

        if (step == 6)
            if (!Utilities.isPhoneNumberValid(strInput)) {
                tvError.setText(getString(R.string.error_payment_nologin_step06));
                focusView = cedtInput;
                cancel = true;
            }

        if (cancel)
            focusView.requestFocus();
        else {

            if (step == 1) {
                if (paymentModel == null)
                    new getPolicyIDInquiry(PaymentNoLoginActivity.this, strInput).execute();
                else {
                    step++;
                    percent += 10;
                    if (step > 6) {
                        step = 6;
                    }
                    setUpStepPayment();
                }
            } else if (step == 2) {
                paymentModel.setCmnd(strInput);
                step++;
                percent += 10;
                if (step > 6) {
                    step = 6;
                }
                setUpStepPayment();
            } else if (step == 3) {
                if (total > 0)
                    paymentModel.setTongSoTien(total + "");

                step++;
                percent += 10;
                if (step > 6) {
                    step = 6;
                }
                setUpStepPayment();
            } else if (step == 4) {
                paymentModel.setTenNguoiNop(strInput);
                step++;
                percent += 10;
                if (step > 6) {
                    step = 6;
                }
                setUpStepPayment();
            } else if (step == 5) {
                paymentModel.setEmail(strInput);
                step++;
                percent += 10;
                if (step > 6) {
                    step = 6;
                }
                setUpStepPayment();
            } else if (step == 6) {
                paymentModel.setPhone(strInput);

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

    /**
     * Task call API get List Policy Bu CLient ID
     */
    public class getPolicyIDInquiry extends AsyncTask<Void, Void, Response<CPGetPolicyInfoByPOLIDResponse>> {

        Context context;
        String policyID;

        public getPolicyIDInquiry(Context c, String pol) {
            context = c;
            this.policyID = pol;
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

                data.setDeviceId(Utilities.getDeviceID(context));
                data.setOS(Utilities.getDeviceName() + "-" + Utilities.getVersion());
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
                                        Toast.makeText(context, "Truy vấn thông tin hợp đồng lỗi: " + result.getErrLog(), Toast.LENGTH_LONG).show();
                                    } else if (result.getResult() != null && result.getResult().equals("true")) {

                                        if (result.getErrLog().equalsIgnoreCase("EMPTY")) {
                                            tvError.setText(getString(R.string.error_payment_nologin_step01));
                                        }

                                        if (result.getErrLog().equalsIgnoreCase("SUCCESSFUL")) {

                                            paymentModel = new PaymentDetailModel();
                                            paymentModel.setSoHopDong(policyID);
                                            if (result.getCpPolicyInfos() != null) {
                                                paymentModel.setMaKhachHang(result.getCpPolicyInfos().get(0).getClientID());
                                                paymentModel.setTenKhachHang(result.getCpPolicyInfos().get(0).getFullName());
                                            }
                                            if (result.getPolicyInfo() != null) {
                                                paymentModel.setPhiBHGoc(result.getPolicyInfo().get(0).getPolSndryAmt());
                                                paymentModel.setPhiAplGoc(result.getPolicyInfo().get(0).getAPL());
                                                paymentModel.setPhiOplGoc(result.getPolicyInfo().get(0).getOPL());
                                            }

                                            //goto next step
                                            step++;
                                            percent += 10;
                                            if (step > 6) {
                                                step = 6;
                                            }
                                            setUpStepPayment();
                                        }

                                    } else {
                                        if (result.getNewAPIToken().equalsIgnoreCase("invalidtoken")) {
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

//    private class savePaymentToServer extends AsyncTask<Void,Void,SavePaymentResponse>{
//
//        private Context context;
//
//        public savePaymentToServer(Context c){
//            this.context = c;
//
//        }
//
//        @Override
//        protected SavePaymentResponse doInBackground(Void... voids) {
//
//            SavePaymentResponse response = null;
//
//            SavePaymentRequest request = new SavePaymentRequest();
//            request.setClientID(paymentModel.getMaKhachHang());
//            request.setPOName(paymentModel.getTenKhachHang());
//            request.setPolId(paymentModel.getSoHopDong());
//            request.setMerchTxnRef(uniqueValue);
//            request.setFrequency("yearly");
//            request.setAmount(edtPhiBaoHiem.getText().toString().replace(",", ""));
//            request.setAPL(edtKhoanTamUng.getText().toString().replace(",", ""));
//            request.setOPL(edtGiaTriHoanLai.getText().toString().replace(",", ""));
//            request.setTotalAmount(tvTongSoTien.getText().toString().replace(",", ""));
//
//            Call<SavePaymentResponse> call = svRequester.savePayment(request);
//
//            return response;
//        }
//
//        @Override
//        protected void onPostExecute(SavePaymentResponse result) {
//            super.onPostExecute(result);
//        }
//    }

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
        buf.append("sAmount=" + Base64.encodeToString((paymentModel.getTongSoTien()).getBytes("UTF-8"), Base64.DEFAULT));

        buf.append("&");
        buf.append("sFCCode=" + Base64.encodeToString("110555".getBytes("UTF-8"), Base64.DEFAULT));
        buf.append("&");
        buf.append("sAgentName=" + Base64.encodeToString("Nguyen Van Teo".getBytes("UTF-8"), Base64.DEFAULT));
        buf.append("&");
        buf.append("sAgentType=" + Base64.encodeToString("BM".getBytes("UTF-8"), Base64.DEFAULT));
        // Add fee frequency
        buf.append("&");
        buf.append("sFeeFrequency=" + Base64.encodeToString("Yearly".getBytes("UTF-8"), Base64.DEFAULT));
        return buf.toString();
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
