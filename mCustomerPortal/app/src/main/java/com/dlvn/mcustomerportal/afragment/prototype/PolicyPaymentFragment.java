package com.dlvn.mcustomerportal.afragment.prototype;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.activity.Bonus_NopPhiBH_Step01_Activity;
import com.dlvn.mcustomerportal.activity.WebNapasActivity;
import com.dlvn.mcustomerportal.common.Constant;
import com.dlvn.mcustomerportal.common.CustomPref;
import com.dlvn.mcustomerportal.services.ServicesGenerator;
import com.dlvn.mcustomerportal.services.ServicesRequest;
import com.dlvn.mcustomerportal.services.model.BaseRequest;
import com.dlvn.mcustomerportal.services.model.request.CPGetPolicyInfoByPOLIDRequest;
import com.dlvn.mcustomerportal.services.model.response.CPGetPolicyInfoByPOLIDResponse;
import com.dlvn.mcustomerportal.services.model.response.CPGetPolicyInfoByPOLIDResult;
import com.dlvn.mcustomerportal.services.model.response.PaymentDetailModel;
import com.dlvn.mcustomerportal.utils.Utilities;
import com.dlvn.mcustomerportal.utils.myLog;
import com.dlvn.mcustomerportal.view.MyCustomDialog;
import com.dlvn.mcustomerportal.view.clearable_edittext.ClearableEditText;

import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Response;

public class PolicyPaymentFragment extends Fragment {

    private static final String TAG = "PolicyPaymentFragment";

    Bundle bundleForPayment;
    ServicesRequest svRequester;
    View view;

    TextView tvPolicy, tvSoHD, tvMaKhachHang, tvTenKhachHang, tvTongGiaTri;
    ClearableEditText cedtPhiDinhKy, cedtTamUngDongPhi, cedtTamUng;
    TextView tvDieuChinh;
    Button btnTiepTuc;

    //Nút "Quay Lại"
    LinearLayout lloBack;

    ProgressDialog dialog;
    String policyNo;
    PaymentDetailModel paymentModel;

    public PolicyPaymentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PolicyPaymentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PolicyPaymentFragment newInstance(String param1, String param2) {
        PolicyPaymentFragment fragment = new PolicyPaymentFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        bundleForPayment = getArguments();
        svRequester = ServicesGenerator.createService(ServicesRequest.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_policy_payment, container, false);
            getViews(view);
            setListener();

            //setup conpany info & policy
            tvPolicy.setText(Html.fromHtml(getString(R.string.txt_policy_term)));
            tvPolicy.setMovementMethod(LinkMovementMethod.getInstance());

            //setup text view
            tvMaKhachHang.setText(CustomPref.getUserID(getActivity()));
            tvTenKhachHang.setText(CustomPref.getFullName(getActivity()));

            if (bundleForPayment.containsKey(Constant.INT_KEY_POLICY_NO)) {
                myLog.e(TAG, "Contains Policy Info For Payment");
                policyNo = bundleForPayment.getString(Constant.INT_KEY_POLICY_NO);
                tvSoHD.setText(policyNo);
            }

            if (bundleForPayment.containsKey(Constant.INT_KEY_PAYMENT_MODEL)) {
                paymentModel = bundleForPayment.getParcelable(Constant.INT_KEY_PAYMENT_MODEL);
            }

            if (paymentModel == null)
                new getPolicyIDInquiry(getActivity(), policyNo).execute();
            else {
                cedtPhiDinhKy.setText(Utilities.formatMoneyToVND(paymentModel.getPhiBHGoc()));
                cedtTamUngDongPhi.setText(Utilities.formatMoneyToVND(paymentModel.getPhiAplGoc()));
                cedtTamUng.setText(Utilities.formatMoneyToVND(paymentModel.getPhiOplGoc()));

                double phiDinhKy = 0, tamUngDongPhi = 0, tamUng = 0;
                phiDinhKy = Double.parseDouble(paymentModel.getPhiBHGoc());
                tamUngDongPhi = Double.parseDouble(paymentModel.getPhiAplGoc());
                tamUng = Double.parseDouble(paymentModel.getPhiOplGoc());

                /**
                 * Tính tổng giá trị
                 */
                String total = String.valueOf(phiDinhKy + tamUngDongPhi + tamUng);
                tvTongGiaTri.setText(Utilities.formatMoneyToVND(total));
                paymentModel.setTongSoTien(total);
            }
        }
        return view;
    }

    private void getViews(View view) {
        // Complete: set View
        tvPolicy = view.findViewById(R.id.tvPolicy);
        tvSoHD = view.findViewById(R.id.tvSoHD);
        tvMaKhachHang = view.findViewById(R.id.tvMaKhachHang);
        tvTenKhachHang = view.findViewById(R.id.tvTenKhachHang);

        cedtPhiDinhKy = view.findViewById(R.id.cedtPhiDinhKy);
        cedtPhiDinhKy.setEnabled(false);
        cedtTamUngDongPhi = view.findViewById(R.id.cedtTamUngDongPhi);
        cedtTamUngDongPhi.setEnabled(false);
        cedtTamUng = view.findViewById(R.id.cedtTamUng);
        cedtTamUng.setEnabled(false);

        tvTongGiaTri = view.findViewById(R.id.tvTongGiaTri);
        lloBack = view.findViewById(R.id.lloBack);
        btnTiepTuc = view.findViewById(R.id.btnTiepTuc);
        tvDieuChinh = view.findViewById(R.id.tvDieuChinh);

        /**
         * Complete: Hide Header of Dashboard Activity
         */
        LinearLayout lloHeader = (LinearLayout) getActivity().findViewById(R.id.lloHeader);
        if (lloHeader != null)
            lloHeader.setVisibility(View.GONE);
    }

    private void setListener() {
        /**
         * Set action for the Back LinearLayout (Quay lai)
         */
        lloBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        btnTiepTuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String totalAmount = tvTongGiaTri.getText().toString().replace(",", "");

                String fee = cedtPhiDinhKy.getText().toString().replace(",", "");
                String apl = cedtTamUngDongPhi.getText().toString().replace(",", "");
                String opl = cedtTamUng.getText().toString().replace(",", "");
                if (TextUtils.isEmpty(fee))
                    fee = "0";
                if (TextUtils.isEmpty(apl))
                    apl = "0";
                if (TextUtils.isEmpty(opl))
                    opl = "0";
                paymentModel.setPhiBaoHiem(fee);
                paymentModel.setPhiApl(apl);
                paymentModel.setPhiOpl(opl);

                if (!TextUtils.isEmpty(totalAmount))
                    paymentModel.setTongSoTien(totalAmount);
                showDialogLuaChonThanhToan(getActivity());
            }
        });

        tvDieuChinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cedtPhiDinhKy.setEnabled(true);
                cedtTamUngDongPhi.setEnabled(true);
                cedtTamUng.setEnabled(true);
                cedtPhiDinhKy.requestFocus();
            }
        });

        cedtPhiDinhKy.addTextChangedListener(new TextWatcher() {
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
                    cedtPhiDinhKy.removeTextChangedListener(this);

                    String cleanString = s.toString().replaceAll("[$,.]", "");
                    double parsed = Double.parseDouble(cleanString);
                    String formatted = NumberFormat.getNumberInstance(Locale.US).format(parsed);
                    current = formatted;
                    cedtPhiDinhKy.setText(formatted);
                    cedtPhiDinhKy.setSelection(formatted.length());

                    cedtPhiDinhKy.addTextChangedListener(this);
                    calculaTotal();
                }
            }
        });

        cedtTamUngDongPhi.addTextChangedListener(new TextWatcher() {
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
                    cedtTamUngDongPhi.removeTextChangedListener(this);

                    String cleanString = s.toString().replaceAll("[$,.]", "");
                    double parsed = Double.parseDouble(cleanString);
                    String formatted = NumberFormat.getNumberInstance(Locale.US).format(parsed);
                    current = formatted;
                    cedtTamUngDongPhi.setText(formatted);
                    cedtTamUngDongPhi.setSelection(formatted.length());

                    cedtTamUngDongPhi.addTextChangedListener(this);
                    calculaTotal();
                }
            }
        });

        cedtTamUng.addTextChangedListener(new TextWatcher() {
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
                    cedtTamUng.removeTextChangedListener(this);

                    String cleanString = s.toString().replaceAll("[$,.]", "");
                    double parsed = Double.parseDouble(cleanString);
                    String formatted = NumberFormat.getNumberInstance(Locale.US).format(parsed);
                    current = formatted;
                    cedtTamUng.setText(formatted);
                    cedtTamUng.setSelection(formatted.length());

                    cedtTamUng.addTextChangedListener(this);
                    calculaTotal();
                }
            }
        });
    }

    private void calculaTotal() {
        double phi = 0;
        if (!TextUtils.isEmpty(cedtPhiDinhKy.getText().toString()))
            phi = Integer.parseInt(cedtPhiDinhKy.getText().toString().replace(",", ""));
        double phiGoc = Double.parseDouble(paymentModel.getPhiBHGoc());
        if (phiGoc < phi) {
            MyCustomDialog.Builder builder = new MyCustomDialog.Builder(getActivity());
            builder.setMessage(getString(R.string.alert_input_gthl_amount_payment_p1)).setPositiveButton("Đồng ý",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            cedtPhiDinhKy.setText(Utilities.formatMoneyToVND(paymentModel.getPhiBHGoc()));
                            dialog.dismiss();
                        }
                    });
            builder.create().show();
        }

        double tamung = 0;
        if (!TextUtils.isEmpty(cedtTamUngDongPhi.getText().toString()))
            tamung = Integer.parseInt(cedtTamUngDongPhi.getText().toString().replace(",", ""));
        double tamungGoc = Double.parseDouble(paymentModel.getPhiAplGoc());
        if (tamungGoc < tamung) {
            MyCustomDialog.Builder builder = new MyCustomDialog.Builder(getActivity());
            builder.setMessage(getString(R.string.alert_input_gthl_amount_payment_p2)).setPositiveButton("Đồng ý",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            cedtTamUngDongPhi.setText(Utilities.formatMoneyToVND(paymentModel.getPhiAplGoc()));
                            dialog.dismiss();
                        }
                    });
            builder.create().show();
        }

        double gthl = 0;
        if (!TextUtils.isEmpty(cedtTamUng.getText().toString()))
            gthl = Integer.parseInt(cedtTamUng.getText().toString().replace(",", ""));

        double gthlGoc = Double.parseDouble(paymentModel.getPhiOplGoc());
        if (gthlGoc < gthl) {
            MyCustomDialog.Builder builder = new MyCustomDialog.Builder(getActivity());
            builder.setMessage(getString(R.string.alert_input_gthl_amount_payment_p3)).setPositiveButton("Đồng ý",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            cedtTamUng.setText(Utilities.formatMoneyToVND(paymentModel.getPhiOplGoc()));
                            dialog.dismiss();
                        }
                    });
            builder.create().show();
        }

        tvTongGiaTri.setText(Utilities.formatMoneyToVND(phi + tamung + gthl));
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
                data.setPolID(policyID.trim());

                data.setUserID(CustomPref.getUserID(context));
                data.setPassword(CustomPref.getPassword(context));

                data.setDeviceId(Utilities.getDeviceID(context));
                data.setOS(Utilities.getDeviceOS());
                data.setProject(Constant.Project_ID);
                data.setAuthentication(Constant.Project_Authentication);
                data.setAPIToken(CustomPref.getAPIToken(context));

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

                                        if (result.getNewAPIToken().equalsIgnoreCase(Constant.ERROR_TOKENINVALID)) {
                                            Utilities.processLoginAgain(context, getString(R.string.message_alert_relogin));
                                        }

                                    } else if (result.getResult() != null && result.getResult().equals("true")) {

                                        if (result.getErrLog().equalsIgnoreCase("EMPTY")) {

                                            MyCustomDialog dialog = new MyCustomDialog.Builder(context)
                                                    .setMessage("Không lấy được phí bảo hiểm và hoàn trả tạm ứng của hợp đồng " + policyID)
                                                    .setPositiveButton(getString(R.string.confirm_ok), new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.dismiss();
                                                        }
                                                    }).create();
                                            dialog.show();

                                            btnTiepTuc.setEnabled(false);
                                            tvTongGiaTri.setText("0");
                                        }

                                        if (result.getErrLog().equalsIgnoreCase("SUCCESSFUL")) {

                                            btnTiepTuc.setEnabled(true);
                                            paymentModel = new PaymentDetailModel();
                                            paymentModel.setSoHopDong(policyID);
                                            if (result.getCpPolicyInfos() != null) {
                                                paymentModel.setMaKhachHang(result.getCpPolicyInfos().get(0).getClientID());
                                                paymentModel.setTenKhachHang(result.getCpPolicyInfos().get(0).getFullName());
                                                paymentModel.setTenNguoiNop("");
                                                paymentModel.setPhone(CustomPref.getPhoneNumber(getActivity()));
                                            }
                                            if (result.getPolicyInfo() != null) {
                                                paymentModel.setPhiBHGoc(result.getPolicyInfo().get(0).getPolSndryAmt());
                                                paymentModel.setPhiAplGoc(result.getPolicyInfo().get(0).getAPL());
                                                paymentModel.setPhiOplGoc(result.getPolicyInfo().get(0).getOPL());
                                            }

                                            if (paymentModel.getPhiBHGoc() != null) {
                                                cedtPhiDinhKy.setText(Utilities.formatMoneyToVND(paymentModel.getPhiBHGoc()));
                                                cedtTamUngDongPhi.setText(Utilities.formatMoneyToVND(paymentModel.getPhiAplGoc()));
                                                cedtTamUng.setText(Utilities.formatMoneyToVND(paymentModel.getPhiOplGoc()));

                                                double phiDinhKy = 0, tamUngDongPhi = 0, tamUng = 0;
                                                phiDinhKy = Double.parseDouble(paymentModel.getPhiBHGoc());
                                                tamUngDongPhi = Double.parseDouble(paymentModel.getPhiAplGoc());
                                                tamUng = Double.parseDouble(paymentModel.getPhiOplGoc());

                                                /**
                                                 * Tính tổng giá trị
                                                 */
                                                String total = String.valueOf(phiDinhKy + tamUngDongPhi + tamUng);
                                                tvTongGiaTri.setText(Utilities.formatMoneyToVND(total));
                                                paymentModel.setTongSoTien(total);
                                            }
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

        buf.append("&");
        buf.append("APL=" + Base64.encodeToString(paymentModel.getPhiApl().getBytes("UTF-8"), Base64.DEFAULT));
        buf.append("&");
        buf.append("OPL=" + Base64.encodeToString(paymentModel.getPhiOpl().getBytes("UTF-8"), Base64.DEFAULT));

        return buf.toString();
    }

    /**
     * TODO: pop lên bảng Dialog cho người dùng chọn phương thức thanh toán
     *
     * @param context
     */
    private void showDialogLuaChonThanhToan(final Context context) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_policy_payment);
        dialog.setCanceledOnTouchOutside(true);

        /**
         * TODO: kê khai các RadioButton
         */
        final RadioButton radioButtonDiemThuong = (RadioButton) dialog.findViewById(R.id.rdbDiemThuong);
        final RadioButton radioButtonQuaThe = (RadioButton) dialog.findViewById(R.id.rdbTheOnline);
        final RadioButton radioButtonQuaNganHang = (RadioButton) dialog.findViewById(R.id.rdbQuaNH);

        RelativeLayout layoutTiepTuc = dialog.findViewById(R.id.quit_layout);

        radioButtonDiemThuong.setText("Điểm thưởng (" + Utilities.formatMoneyToVND((CustomPref.getUserPoint(context) * 1000) + "") + " VNĐ)");

        /**
         * COMPLETE: set hiệu ứng cho các RadioButton
         */
        radioButtonDiemThuong.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    compoundButton.setButtonTintList(ColorStateList.valueOf(Color.RED));
                else
                    compoundButton.setButtonTintList(ColorStateList.valueOf(Color.GRAY));
            }
        });

        radioButtonQuaThe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    compoundButton.setButtonTintList(ColorStateList.valueOf(Color.RED));
                else
                    compoundButton.setButtonTintList(ColorStateList.valueOf(Color.GRAY));
            }
        });

        radioButtonQuaNganHang.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    compoundButton.setButtonTintList(ColorStateList.valueOf(Color.RED));
                else
                    compoundButton.setButtonTintList(ColorStateList.valueOf(Color.GRAY));
            }
        });

        layoutTiepTuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioButtonDiemThuong.isChecked()) {

                    double total = Double.parseDouble(paymentModel.getTongSoTien());
                    if (CustomPref.getUserPoint(getActivity()) * 1000 >= total) {
                        //Đóng phí/Hoàn trả tạm ứng
                        Intent intent = new Intent(getActivity(), Bonus_NopPhiBH_Step01_Activity.class);
                        intent.putExtra(Constant.INT_KEY_PAYMENT_MODEL, paymentModel);
                        startActivity(intent);
                    } else {
                        MyCustomDialog dialog = new MyCustomDialog.Builder(context)
                                .setMessage("Điểm thưởng của Anh/Chị không đủ để thanh toán phí bảo hiểm. Anh/Chị vui lòng chọn phương thức khác.")
                                .setPositiveButton(getString(R.string.confirm_ok), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).create();
                        dialog.show();
                    }
                } else if (radioButtonQuaNganHang.isChecked() || radioButtonQuaThe.isChecked()) {
                    String url = null;
                    try {
                        url = initPaymentRequest();

                        Intent napas = new Intent(getActivity(), WebNapasActivity.class);

                        napas.putExtra(Constant.KEY_NAPAS_PAYMENT_URL, url);
                        napas.putExtra(Constant.KEY_NAPAS_PAYMENT_MODEL, paymentModel);

                        startActivityForResult(napas, Constant.REQUEST_NAPAS_PAYMENT);

                    } catch (UnsupportedEncodingException e) {
                        myLog.printTrace(e);
                    }
                }
            }
        });

        dialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        LinearLayout lloHeader = getActivity().findViewById(R.id.lloHeader);
        if (lloHeader != null)
            lloHeader.setVisibility(View.GONE);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
