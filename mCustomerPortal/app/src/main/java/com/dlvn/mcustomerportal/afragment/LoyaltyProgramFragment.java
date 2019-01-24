package com.dlvn.mcustomerportal.afragment;

import java.util.ArrayList;
import java.util.List;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.activity.Bonus_NopPhiBH_Step01_Activity;
import com.dlvn.mcustomerportal.activity.prototype.DashboardActivity;
import com.dlvn.mcustomerportal.activity.prototype.PointDonActivity;
import com.dlvn.mcustomerportal.activity.prototype.ProductByCategoryActivity;
import com.dlvn.mcustomerportal.adapter.BonusListAdapter;
import com.dlvn.mcustomerportal.adapter.listener.RecyclerViewClickListener;
import com.dlvn.mcustomerportal.adapter.listener.RecyclerViewTouchListener;
import com.dlvn.mcustomerportal.afragment.prototype.LoyaltyPointDetailFragment;
import com.dlvn.mcustomerportal.common.Constant;
import com.dlvn.mcustomerportal.common.CustomPref;
import com.dlvn.mcustomerportal.services.ServicesGenerator;
import com.dlvn.mcustomerportal.services.ServicesRequest;
import com.dlvn.mcustomerportal.services.model.BaseRequest;
import com.dlvn.mcustomerportal.services.model.request.GetMasterDataByTypeRequest;
import com.dlvn.mcustomerportal.services.model.request.GetPointByCLIIDRequest;
import com.dlvn.mcustomerportal.services.model.response.GetMasterData_Category_Response;
import com.dlvn.mcustomerportal.services.model.response.GetMaterData_Category_Result;
import com.dlvn.mcustomerportal.services.model.response.GetPointByCLIIDResponse;
import com.dlvn.mcustomerportal.services.model.response.GetPointByCLIIDResult;
import com.dlvn.mcustomerportal.services.model.response.MasterData_Category;
import com.dlvn.mcustomerportal.utils.CPSaveLogTask;
import com.dlvn.mcustomerportal.utils.Utilities;
import com.dlvn.mcustomerportal.utils.listerner.OnFragmentInteractionListener;
import com.dlvn.mcustomerportal.utils.myLog;
import com.dlvn.mcustomerportal.view.MyCustomDialog;
import com.dlvn.mcustomerportal.view.RecyclerSmoothGridLayoutManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Response;

public class LoyaltyProgramFragment extends Fragment {
    //COMPLETE: declare progress dialog
    ProgressDialog dialog;

    //COMPLETE: testing
    OnFragmentInteractionListener onAddFragment;

    //COMPLETE: Testing, author: Tan
    private LinearLayout lloLoyaltyDetail;

    private OnFragmentInteractionListener mListener;

    LinearLayout lloHeader;
    View view;
    RecyclerView rvContent;
    ImageView imvAvatar;
    TextView tvDiemNho, tvTenKH, tvRank;

    BonusListAdapter rvAdapter;
    List<MasterData_Category> lstData;
    List<MasterData_Category> lsCategory;

    ServicesRequest svRequester;

    public LoyaltyProgramFragment() {
        // Required empty public constructor
    }

    public static LoyaltyProgramFragment newInstance(String param1, String param2) {
        LoyaltyProgramFragment fragment = new LoyaltyProgramFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
        svRequester = ServicesGenerator.createService(ServicesRequest.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view == null) {
            /**
             * TODO: thay đổi layout từ bonus_program sang loyalty_point Ä‘á»ƒ chÃ¨n pháº§n header má»›i vÃ o
             */
//			view = inflater.inflate(R.layout.fragment_bonus_program, container, false);
            view = inflater.inflate(R.layout.fragment_loyalty_point, container, false);
            getViews(view);

            initData();
            setListener();
        }

        return view;
    }

    private void getViews(View v) {
        rvContent = v.findViewById(R.id.rvContent);
        RecyclerView.LayoutManager layout = new RecyclerSmoothGridLayoutManager(getActivity(), 2);
        rvContent.setLayoutManager(layout);
        rvContent.setNestedScrollingEnabled(true);

        //TODO: get lloRank view
        lloLoyaltyDetail = v.findViewById(R.id.lloLoyaltyDetail);

        //TODO: find view for tvDiemLon tvDiemNho
        tvDiemNho = v.findViewById(R.id.tvDiemNho);
        tvRank = v.findViewById(R.id.tvRank);

        tvTenKH = v.findViewById(R.id.tenKhachHang);

        //TODO: find view for linearlayout header
        lloHeader = getActivity().findViewById(R.id.lloHeader);

        imvAvatar = v.findViewById(R.id.avatar);
    }

    private void initData() {

        tvTenKH.setText(CustomPref.getFullName(getActivity()));
        if (CustomPref.getUserRank(getActivity()).equalsIgnoreCase("NORANK"))
            tvRank.setText("");
        else
            tvRank.setText(CustomPref.getUserRank(getActivity()));

        if (!TextUtils.isEmpty(CustomPref.getUserPhoto(getActivity())))
            Glide.with(getActivity()).load(CustomPref.getUserPhoto(getActivity())).apply(RequestOptions.circleCropTransform()).into(imvAvatar);
        else
            Glide.with(getActivity()).load(R.drawable.avatar_user).apply(RequestOptions.circleCropTransform()).into(imvAvatar);

        lstData = new ArrayList<>();
        lsCategory = new ArrayList<>();

        lstData.add(new MasterData_Category("Mã giảm giá Tiki",
                "Quý khách có thể sử dụng mã giảm giá của tiki mua hàng tại www.tiki.vn, bao gồm cả các sản phẩm trong chương trình khuyến mãi. ",
                R.drawable.tiki_voucher, "8"));
        lstData.add(new MasterData_Category("Nộp phí bảo hiểm / Hoàn trả tạm ứng",
                "Quý khách có thể sử dụng tiền thưởng để nộp phí bảo hiểm định kỳ hoặc hoàn trả tạm ứng cho hợp đồng bảo hiểm của Quý khách.",
                R.drawable.nop_phi_bao_hiem, "3"));
        lstData.add(new MasterData_Category("Nhận Thẻ quà tặng",
                "Quý khách có thể sử dụng tiền thưởng để nhận Thẻ quà tặng và chuyển tiền thưởng vào tài khoản Thẻ để trải nghiệm các tiện ích ưu đãi của thẻ trả trước đồng thương hiệu Dai-ichi Life Việt Nam & HDBank được phát hành bởi HDBank dành cho khách hàng của Dai-ichi Life Việt Nam.",
                R.drawable.nhan_the_qua_tang, "6"));
        lstData.add(new MasterData_Category("Nạp Thẻ điện thoại",
                "Quý khách có thể sử dụng tiền thưởng để nạp tiền cho các thuê bao di động trả trước của mạng Vinaphone, Mobifone và Viettel.",
                R.drawable.nap_the_dien_thoai, "2"));
        lstData.add(new MasterData_Category("Nhận Phiếu mua hàng",
                "Quý khách có thể sử dụng tiền thưởng để nhận Phiếu mua hàng để mua sắm tại các siêu thị hàng đầu Việt Nam hiện nay là Co.opMart và Big C.",
                R.drawable.nhan_phieu_mua_hang, "1"));
        lstData.add(new MasterData_Category("Nhận Phiếu kiểm tra y tế",
                "Quý khách có thể sử dụng Phiếu kiểm tra y tế do Dai-ichi Life Việt Nam phát hành để kiểm tra sức khỏe tại các cơ sở y tế ở các địa chỉ này.",
                R.drawable.phieu_kiem_tra_y_te, "5"));
        lstData.add(new MasterData_Category("Tặng điểm",
                "Quý khách có thể sử dụng tiền thưởng để tặng điểm thưởng tích lũy của Quý khách cho người khác là Bên mua bảo hiểm có hợp đồng đang có hiệu lực với Dai-ichi Life Việt Nam. ",
                R.drawable.tang_diem, "4"));
        lstData.add(new MasterData_Category("Quà tặng DLVN", "", R.drawable.su_dung_diem, "7"));

        new getClientPointTask(getActivity()).execute();
        new getMasterDataTask(getActivity(), Constant.MASTER_DATA_TYPE_CATEGORY).execute();
    }

    private void setListener() {
        rvContent.addOnItemTouchListener(
                new RecyclerViewTouchListener(getActivity(), rvContent, new RecyclerViewClickListener() {

                    @Override
                    public void onLongClick(View view, int position) {

                    }

                    @Override
                    public void onClick(View view, int position) {
                        if (lsCategory.get(position) != null) {
                            myLog.e("category: " + lsCategory.get(position).getPRODUCTCATEGORYCD() + " ** PCP Code " + lsCategory.get(position).getPCPCode());

                            if (lsCategory.get(position).getPCPCode().equalsIgnoreCase(Constant.Category_PCPCode.PCP_002.getStringValue())) {
                                //Hàng hóa giao online
                                Intent intent = new Intent(getActivity(), ProductByCategoryActivity.class);
                                intent.putExtra(ProductByCategoryActivity.INT_CATEGORY_PRODUCT, lsCategory.get(position));
                                startActivity(intent);
                            } else if (lsCategory.get(position).getPCPCode().equalsIgnoreCase(Constant.Category_PCPCode.PCP_001.getStringValue())) {
                                //Hàng hóa giao địa chỉ
                                Intent intent = new Intent(getActivity(), ProductByCategoryActivity.class);
                                intent.putExtra(ProductByCategoryActivity.INT_CATEGORY_PRODUCT, lsCategory.get(position));
                                startActivity(intent);

                            } else if (lsCategory.get(position).getPCPCode().equalsIgnoreCase(Constant.Category_PCPCode.PCP_003.getStringValue())) {
                                //Đóng phí/Hoàn trả tạm ứng
                                Intent intent = new Intent(getActivity(), Bonus_NopPhiBH_Step01_Activity.class);
                                startActivity(intent);

                            } else if (lsCategory.get(position).getPCPCode().equalsIgnoreCase(Constant.Category_PCPCode.PCP_004.getStringValue())) {
                                //Tặng điểm
                                Intent intent = new Intent(getActivity(), PointDonActivity.class);
                                startActivity(intent);
                            }
                        }
                    }
                }));


        //TODO: set listener for lloRank
        lloLoyaltyDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoyaltyPointDetailFragment fragment = new LoyaltyPointDetailFragment();
                onAddFragment.onFragmentAddstackListener(DashboardActivity.TAB_FUNDBONUS, fragment, true);
            }
        });

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof com.dlvn.mcustomerportal.utils.listerner.OnFragmentInteractionListener) {
            onAddFragment = (com.dlvn.mcustomerportal.utils.listerner.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }


    /**
     * Task call API get client point By CLient ID
     */
    public class getClientPointTask extends AsyncTask<Void, Void, Response<GetPointByCLIIDResponse>> {
        Context context;

        public getClientPointTask(Context c) {
            context = c;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //TODO: progress dialog
            if (dialog == null)
                dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Đang tải dữ liệu ...");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        @Override
        protected Response<GetPointByCLIIDResponse> doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            Response<GetPointByCLIIDResponse> result = null;
            try {
                GetPointByCLIIDRequest data = new GetPointByCLIIDRequest();

                data.setClientID(CustomPref.getUserID(context));
                data.setAPIToken(CustomPref.getAPIToken(context));
                data.setDeviceID(Utilities.getDeviceID(context));
                data.setOS(Utilities.getDeviceOS());
                data.setProject(Constant.Project_ID);
                data.setAuthentication(Constant.Project_Authentication);
                if (!TextUtils.isEmpty(CustomPref.getUserName(context)))
                    data.setUserLogin(CustomPref.getUserName(context));
                else
                    data.setUserLogin(CustomPref.getUserID(context));

                BaseRequest request = new BaseRequest();
                request.setJsonDataInput(data);

                Call<GetPointByCLIIDResponse> call = svRequester.GetPointByCLIID(request);
                result = call.execute();

            } catch (Exception e) {
                myLog.printTrace(e);
                return null;
            }

            // TODO: register the new account here.
            return result;
        }

        @Override
        protected void onPostExecute(final Response<GetPointByCLIIDResponse> success) {
            dialog.dismiss();
            if (success != null) {
                try {
                    if (success.isSuccessful()) {
                        GetPointByCLIIDResponse response = success.body();
                        if (response != null)
                            if (response.getResponse() != null) {
                                GetPointByCLIIDResult result = response.getResponse();
                                if (result != null) {

                                    if (result.getResult() != null && result.getResult().equals("false")) {
                                        //If account not exits --> link to register
                                        myLog.e("LoyaltyProgramFragment", "Get Point: " + result.getErrLog());

                                        if (result.getNewAPIToken().equalsIgnoreCase(Constant.ERROR_TOKENINVALID)) {
                                            Utilities.processLoginAgain(context, getString(R.string.message_alert_relogin));
                                        }

                                    } else if (result.getResult() != null && result.getResult().equals("true")) {

                                        //Save Token
                                        if (!TextUtils.isEmpty(result.getNewAPIToken()))
                                            CustomPref.saveAPIToken(context, result.getNewAPIToken());

                                        if (result.getPoint() != null) {
                                            String point = result.getPoint();
                                            String rank = result.getClassPO();

                                            tvDiemNho.setText(String.valueOf(Double.parseDouble(point) / 1000));
                                            tvRank.setText(rank);
                                            CustomPref.saveUserPoint(getContext(), Float.parseFloat(point) / 1000);
                                            CustomPref.saveUserRank(context, rank);

                                        }
                                    }
                                }
                            }
                    }
                } catch (Exception e) {
                    myLog.printTrace(e);
                }
            } else {
                MyCustomDialog dialog = new MyCustomDialog.Builder(context).setMessage("Không kết nối được server!").setTitle("Lỗi mạng").setPositiveButton(getString(R.string.confirm_ok), new DialogInterface.OnClickListener() {
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
            this.cancel(true);
            dialog.dismiss();
        }
    }

    /**
     * @author nn.tai
     * @modify 30/08/2018
     * get Master Data by type
     */
    public class getMasterDataTask extends AsyncTask<Void, Void, Response<GetMasterData_Category_Response>> {
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
                dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Đang tải dữ liệu ...");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        @Override
        protected Response<GetMasterData_Category_Response> doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            Response<GetMasterData_Category_Response> result = null;
            try {
                GetMasterDataByTypeRequest data = new GetMasterDataByTypeRequest();

                data.setProject(Constant.Project_ID);
                data.setType(type);

                Call<GetMasterData_Category_Response> call = svRequester.GetMasterDataByType(data);
                result = call.execute();

            } catch (Exception e) {
                myLog.printTrace(e);
                return null;
            }

            // TODO: register the new account here.
            return result;
        }

        @Override
        protected void onPostExecute(final Response<GetMasterData_Category_Response> success) {
            dialog.dismiss();
            if (success != null) {
                try {
                    if (success.isSuccessful()) {
                        GetMasterData_Category_Response response = success.body();
                        if (response != null)
                            if (response.getGetMasterDataByTypeResult() != null) {
                                GetMaterData_Category_Result result = response.getGetMasterDataByTypeResult();
                                if (result != null) {

                                    if (result.getResult() != null && result.getResult().equals("false")) {
                                        myLog.e("LoyaltyProgramFragment", "Get Master Data List Category: " + result.getMessage());

                                    } else if (result.getResult() != null && result.getResult().equals("true")) {

                                        if (result.getLstItem() != null) {
                                            lsCategory = result.getLstItem();
                                            int posRemove = -1;

                                            if (lstData != null) {
                                                for (int i = 0; i < lstData.size(); i++) {
                                                    for (int j = 0; j < lsCategory.size(); j++) {
                                                        if (lsCategory.get(j).getPRODUCTCATEGORYCD().equals(lstData.get(i).getPRODUCTCATEGORYCD())) {
                                                            lsCategory.get(j).setProductTitle(lstData.get(i).getProductTitle());
                                                            lsCategory.get(j).setProductContent(lstData.get(i).getProductContent());
                                                            lsCategory.get(j).setResourceID(lstData.get(i).getResourceID());
                                                        }

                                                        //Remove category Qua tang DLVN
                                                        if (lsCategory.get(j).getPRODUCTCATEGORYCD().equals("7"))
                                                            posRemove = j;
                                                    }
                                                }

                                                if (posRemove > 0)
                                                    lsCategory.remove(posRemove);

                                                rvAdapter = new BonusListAdapter(getActivity(), lsCategory);
                                                rvContent.setAdapter(rvAdapter);
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

    @Override
    public void onStart() {
        /**
         * TODO: Hide Header of Dashboard Activity
         */
        if (lloHeader != null)
            lloHeader.setVisibility(View.GONE);
        super.onStart();
    }

    @Override
    public void onPause() {
        /**
         * TODO: Show Header of Dashboard Activity
         */
        lloHeader.setVisibility(View.VISIBLE);
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        new CPSaveLogTask(getActivity(), Constant.CPLOG_LOYALTY_OPEN).execute();
        new getClientPointTask(getActivity()).execute();
        new getMasterDataTask(getActivity(), Constant.MASTER_DATA_TYPE_CATEGORY).execute();
    }

    @Override
    public void onStop() {
        super.onStop();
        new CPSaveLogTask(getActivity(), Constant.CPLOG_LOYALTY_CLOSE).execute();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
