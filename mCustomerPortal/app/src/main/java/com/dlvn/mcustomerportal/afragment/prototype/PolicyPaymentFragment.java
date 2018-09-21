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
import android.text.Html;
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
import com.dlvn.mcustomerportal.activity.prototype.PaymentNoLoginActivity;
import com.dlvn.mcustomerportal.common.Constant;
import com.dlvn.mcustomerportal.common.CustomPref;
import com.dlvn.mcustomerportal.services.ServicesGenerator;
import com.dlvn.mcustomerportal.services.ServicesRequest;
import com.dlvn.mcustomerportal.services.model.BaseRequest;
import com.dlvn.mcustomerportal.services.model.request.CPGetPolicyInfoByPOLIDRequest;
import com.dlvn.mcustomerportal.services.model.response.CPGetPolicyInfoByPOLIDResponse;
import com.dlvn.mcustomerportal.services.model.response.CPGetPolicyInfoByPOLIDResult;
import com.dlvn.mcustomerportal.services.model.response.CPPolicy;
import com.dlvn.mcustomerportal.services.model.response.PaymentDetailModel;
import com.dlvn.mcustomerportal.utils.Utilities;
import com.dlvn.mcustomerportal.utils.myLog;
import com.dlvn.mcustomerportal.view.MyCustomDialog;

import java.io.UnsupportedEncodingException;

import retrofit2.Call;
import retrofit2.Response;

public class PolicyPaymentFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    Bundle bundleForPayment;
    ServicesRequest svRequester;
    CPPolicy itemPolicy;
    View view;

    TextView tvPolicy, tvCopyRight, tvSoHD, tvMaKhachHang, tvTenKhachHang, tvPhiDinhKy, tvTamUngDongPhi, tvTamUng, tvTongGiaTri;
    Button btnTiepTuc, test;
    //Nút "Quay Lại"
    LinearLayout lloBack;

    ProgressDialog dialog;
    PaymentDetailModel paymentModel;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        paymentModel = new PaymentDetailModel();

        paymentModel.setSoHopDong("");
        paymentModel.setTenKhachHang("");
        paymentModel.setMaKhachHang("");
        paymentModel.setTenNguoiNop("");
        paymentModel.setTongSoTien("0");
        paymentModel.setPhone("");

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
                paymentModel.setSoHopDong(bundleForPayment.getString(Constant.INT_KEY_POLICY_NO));
                tvSoHD.setText(paymentModel.getSoHopDong());
            }

            new getPolicyIDInquiry(getActivity(), paymentModel.getSoHopDong()).execute();
        }
        return view;
    }

    private void getViews(View view) {
        // Complete: set View
        tvPolicy = (TextView) view.findViewById(R.id.tvPolicy);
        tvSoHD = (TextView) view.findViewById(R.id.tvSoHD);
        tvMaKhachHang = (TextView) view.findViewById(R.id.tvMaKhachHang);
        tvTenKhachHang = (TextView) view.findViewById(R.id.tvTenKhachHang);
        tvPhiDinhKy = (TextView) view.findViewById(R.id.tvPhiDinhKy);
        tvTamUngDongPhi = (TextView) view.findViewById(R.id.tvTamUngDongPhi);
        tvTamUng = (TextView) view.findViewById(R.id.tvTamUng);
        tvTongGiaTri = (TextView) view.findViewById(R.id.tvTongGiaTri);
        lloBack = (LinearLayout) view.findViewById(R.id.lloBack);
        btnTiepTuc = (Button) view.findViewById(R.id.btnTiepTuc);

        /**
         * Complete: Hide Header of Dashboard Activity
         */
        LinearLayout lloHeader = (LinearLayout) getActivity().findViewById(R.id.lloHeader);
        if (lloHeader != null)
            lloHeader.setVisibility(View.GONE);
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

                                            if (bundleForPayment.containsKey("phiBaoHiemDinhKy")) {

                                                paymentModel.setPhiBHGoc(bundleForPayment.getString("phiBaoHiemDinhKy"));
                                                paymentModel.setPhiAplGoc("0");
                                                paymentModel.setPhiOplGoc("0");

                                                tvPhiDinhKy.setText(Utilities.formatMoneyToVND(paymentModel.getPhiBHGoc()));
                                                tvTamUngDongPhi.setText(Utilities.formatMoneyToVND(paymentModel.getPhiAplGoc()));
                                                tvTamUng.setText(Utilities.formatMoneyToVND(paymentModel.getPhiOplGoc()));

                                            }

                                            tvTongGiaTri.setText(Utilities.formatMoneyToVND(paymentModel.getPhiBHGoc()));
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

                                            if (paymentModel.getPhiBHGoc() != null) {
                                                tvPhiDinhKy.setText(Utilities.formatMoneyToVND(paymentModel.getPhiBHGoc()));
                                                tvTamUngDongPhi.setText(Utilities.formatMoneyToVND(paymentModel.getPhiAplGoc()));
                                                tvTamUng.setText(Utilities.formatMoneyToVND(paymentModel.getPhiOplGoc()));

                                                long phiDinhKy = 0, tamUngDongPhi = 0, tamUng = 0;
                                                phiDinhKy = Long.parseLong(paymentModel.getPhiBHGoc());
                                                tamUngDongPhi = Long.parseLong(paymentModel.getPhiAplGoc());
                                                tamUng = Long.parseLong(paymentModel.getPhiOplGoc());

                                                /**
                                                 * Tính tổng giá trị
                                                 */
                                                tvTongGiaTri.setText(Utilities.formatMoneyToVND(String.valueOf(phiDinhKy + tamUngDongPhi + tamUng)));
                                            }
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
                showDialogLuaChonThanhToan(getActivity());
            }
        });
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

    /**
     * TODO: pop lên bảng Dialog cho người dùng chọn phương thức thanh toán
     *
     * @param context
     */
    private void showDialogLuaChonThanhToan(Context context) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_policy_payment);
        dialog.setCanceledOnTouchOutside(true);

        /**
         * TODO: kê khai các RadioButton
         */
        final RadioButton radioButtonDiemThuong = (RadioButton) dialog.findViewById(R.id.rdbDiemThuong);
        final RadioButton radioButtonQuaThe = (RadioButton) dialog.findViewById(R.id.rdbTheOnline);
        final RadioButton radioButtonQuaNganHang = (RadioButton) dialog.findViewById(R.id.rdbQuaNH);

        RelativeLayout quit_layout = dialog.findViewById(R.id.quit_layout);

        radioButtonDiemThuong.setText("Điểm thưởng (" + Utilities.formatMoneyToVND((CustomPref.getUserPoint(context) * 1000) + "") + " VNĐ)");

        /**
         * TODO: set hiệu ứng cho các RadioButton
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

        quit_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioButtonDiemThuong.isChecked()) {

                    //Đóng phí/Hoàn trả tạm ứng
                    Intent intent = new Intent(getActivity(), Bonus_NopPhiBH_Step01_Activity.class);
                    startActivity(intent);

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
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
