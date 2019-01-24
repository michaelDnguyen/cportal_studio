package com.dlvn.mcustomerportal.afragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dlvn.mcustomerportal.BuildConfig;
import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.activity.ProductDetailActivity;
import com.dlvn.mcustomerportal.activity.WebTutorialActivity;
import com.dlvn.mcustomerportal.activity.prototype.ClaimsLifeInsureActivity;
import com.dlvn.mcustomerportal.activity.prototype.ClaimsRequesterInfoActivity;
import com.dlvn.mcustomerportal.activity.prototype.DashboardActivity;
import com.dlvn.mcustomerportal.activity.prototype.PaymentNoLoginActivity;
import com.dlvn.mcustomerportal.activity.prototype.SettingsActivity;
import com.dlvn.mcustomerportal.adapter.SingleSpinnerAdapter;
import com.dlvn.mcustomerportal.adapter.SlideListAdapter;
import com.dlvn.mcustomerportal.adapter.VideoPagerAdapter;
import com.dlvn.mcustomerportal.adapter.listener.RecyclerViewClickListener;
import com.dlvn.mcustomerportal.adapter.listener.RecyclerViewTouchListener;
import com.dlvn.mcustomerportal.adapter.model.BonusItemModel;
import com.dlvn.mcustomerportal.adapter.model.SingleChoiceModel;
import com.dlvn.mcustomerportal.adapter.model.VideoDetails;
import com.dlvn.mcustomerportal.common.Constant;
import com.dlvn.mcustomerportal.common.CustomPref;
import com.dlvn.mcustomerportal.services.ServicesGenerator;
import com.dlvn.mcustomerportal.services.ServicesRequest;
import com.dlvn.mcustomerportal.services.model.claims.ClaimsFromRequest;
import com.dlvn.mcustomerportal.utils.CPSaveLogTask;
import com.dlvn.mcustomerportal.utils.listerner.OnFragmentInteractionListener;
import com.dlvn.mcustomerportal.utils.myLog;
import com.dlvn.mcustomerportal.view.MyCustomDialog;
import com.dlvn.mcustomerportal.view.indicator.ScrollerViewPager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author nn.tai
 * @date Dec 21, 2017
 */
public class Home2Fragment extends Fragment {

    private static final String TAG = "Home2Fragment";

    OnFragmentInteractionListener onAddFragment;

    View view;

    LinearLayout llo01, llo02;

    RecyclerView rvSanPham;
    SlideListAdapter sanphamAdapter;
    List<BonusItemModel> lsSanPham;

    Spinner spnLoaiPhi;
    Button btnThanhToanPhi;

    /**
     * Gioi thieu
     */
    RecyclerView rvGioiThieu;
    SlideListAdapter gioiThieuAdapter;
    List<BonusItemModel> lsGioiThieu;

    /**
     * News- Events
     */
    RecyclerView rvNews;
    SlideListAdapter newsAdapter;
    List<BonusItemModel> lsNews;

    /**
     * Youtube
     */
    ScrollerViewPager vpYoutube;
    ArrayList<VideoDetails> videoDetailsArrayList;
    VideoPagerAdapter videoAdapter;

    /**
     * Claims
     */
    Button btnClaims;
    LinearLayout lloClaims;

    ServicesRequest svRequester;

    public Home2Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of this fragment using
     * the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Home2Fragment newInstance(String param1, String param2) {
        myLog.e(TAG, "newInstance");

        Home2Fragment fragment = new Home2Fragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myLog.e(TAG, "onCreate");
        if (getArguments() != null) {
        }
        svRequester = ServicesGenerator.createService(ServicesRequest.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myLog.e(TAG, "onCreateView");

        if (view == null) {
            view = inflater.inflate(R.layout.fragment_home2, container, false);

            getViews(view);
            initData();
            setListener();
        }
        return view;
    }

    /**
     * get view from layout
     *
     * @param view
     * @author nn.tai
     * @date Dec 14, 2017
     */
    private void getViews(View view) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        rvSanPham = view.findViewById(R.id.rvSanPham);
//        rvSanPham.setLayoutManager(layoutManager);
        rvSanPham.setVisibility(View.VISIBLE);

        rvGioiThieu = view.findViewById(R.id.rvGioiThieu);
//        rvGioiThieu.setLayoutManager(layoutManager);
        llo01 = view.findViewById(R.id.llo01);
        llo01.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WebTutorialActivity.class);
                intent.putExtra(Constant.INT_KEY_WEB_URL, Constant.CONST_URL_NEWS);
                intent.putExtra(Constant.INT_KEY_WEB_URL_TITLE, "Tin tức");
                startActivity(intent);
            }
        });

        rvNews = view.findViewById(R.id.rvNews);
//        rvNews.setLayoutManager(layoutManager);
        llo02 = view.findViewById(R.id.llo02);
        llo02.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WebTutorialActivity.class);
                intent.putExtra(Constant.INT_KEY_WEB_URL, Constant.CONST_URL_PRODUCT);
                intent.putExtra(Constant.INT_KEY_WEB_URL_TITLE, "Sản phẩm");
                startActivity(intent);
            }
        });

        /**
         * Payment no login
         */
        btnThanhToanPhi = view.findViewById(R.id.btnThanhToanPhi);
        spnLoaiPhi = view.findViewById(R.id.spnLoaiPhi);

        /**
         * youtube
         */
        vpYoutube = view.findViewById(R.id.vpYoutube);

        /**
         * Claim
         */
        lloClaims = view.findViewById(R.id.lloClaims);
        btnClaims = view.findViewById(R.id.btnClaims);
        btnClaims.setEnabled(true);

    }

    /**
     * init data to views
     *
     * @author nn.tai
     * @date Dec 14, 2017
     */
    private void initData() {

        // init viewpager
        lsSanPham = new ArrayList<>();
        lsSanPham.add(new BonusItemModel("An Thịnh Đầu Tư", "An Tâm Hưng Thịnh là giải pháp tài chính chủ động và linh hoạt giúp bạn dễ dàng cân bằng được những ưu tiên, đạt được các kỳ vọng và hơn hết đảm bảo an toàn tài chính cho chính bạn và gia đình thân yêu để thật sự làm chủ tương lai.",
                R.drawable.product1));
        lsSanPham.add(new BonusItemModel("An Tâm Hưng Thịnh", "Một giải pháp tài chính toàn diện, mang đến sự bảo vệ trước những rủi ro trong cuộc sống và đầu tư sinh lợi hiệu quả với lãi suất theo sát mức lãi suất của thị trường tài chính.",
                R.drawable.product2));
        lsSanPham.add(new BonusItemModel("An Phúc Hưng Thịnh", "Là giải pháp ưu việt kết hợp giữa bảo hiểm và đầu tư mang đến cơ hội gia tăng giá trị tài sản với nhiều danh mục đầu tư khác nhau và linh hoạt trước mọi thay đổi của cuộc sống.",
                R.drawable.product3));

        sanphamAdapter = new SlideListAdapter(getActivity(), lsSanPham);
        rvSanPham.setAdapter(sanphamAdapter);

        /* Gioi thieu */
        lsGioiThieu = new ArrayList<>();
        lsGioiThieu.add(new BonusItemModel("CHÀO MỪNG QUÝ KHÁCH ĐẾN VỚI \"CỔNG THÔNG TIN KHÁCH HÀNG\"", "", R.drawable.intro1));
        lsGioiThieu.add(new BonusItemModel("CHƯƠNG TRÌNH TÍCH LŨY ĐIỂM THƯỞNG GẮN BÓ DÀI LÂU", "", R.drawable.intro2));
        lsGioiThieu.add(new BonusItemModel("CÔNG TY BHNT DAI-ICHI VIỆT NAM VINH DỰ ĐÓN NHẬN HUÂN CHƯƠNG LAO ĐỘNG...", "", R.drawable.intro3));
        gioiThieuAdapter = new SlideListAdapter(getActivity(), lsGioiThieu);
        rvGioiThieu.setAdapter(gioiThieuAdapter);

        /*News*/
        lsNews = new ArrayList<>();
        lsNews.add(new BonusItemModel("QUỸ “VÌ CUỘC SỐNG TƯƠI ĐẸP” CỦA DAI-ICHI LIFE VIỆT NAM PHỐI HỢP CÙNG QUỸ BẢO TRỢ TRẺ EM VIỆT NAM", "", R.drawable.news1));
        lsNews.add(new BonusItemModel("DAI-ICHI LIFE VIỆT NAM KHỞI ĐỘNG CHƯƠNG TRÌNH HIẾN MÁU NHÂN ĐẠO 2018 TẠI TP. HCM...", "", R.drawable.news2));
        lsNews.add(new BonusItemModel("DAI-ICHI LIFE VIỆT NAM VINH DỰ NHẬN GIẢI THƯỞNG QUỐC TẾ “DOANH NGHIỆP TRÁCH NHIỆM CHÂU Á”...", "", R.drawable.news3));
        newsAdapter = new SlideListAdapter(getActivity(), lsNews);
        rvNews.setAdapter(newsAdapter);

        /*Thanh toan phi bao hiem No Login*/
        List<SingleChoiceModel> categories = new ArrayList<>();
        categories.add(new SingleChoiceModel(1, "phi_tai_tuc", "Phí tái tục"));
        categories.add(new SingleChoiceModel(2, "phi_dau_tien", "Phí đầu tiên"));

        // Creating adapter for spinner
        SingleSpinnerAdapter dataAdapter = new SingleSpinnerAdapter(getActivity(), categories);
        spnLoaiPhi.setAdapter(dataAdapter);

        /**
         * Youtube
         */
        videoDetailsArrayList = new ArrayList<>();
        videoAdapter = new VideoPagerAdapter(getActivity(), videoDetailsArrayList, 0.7f);
        vpYoutube.setAdapter(videoAdapter);
        vpYoutube.setClipToPadding(false);
        vpYoutube.setPageMargin(12);
        vpYoutube.setPadding(10, 0, 10, 0);
        vpYoutube.setOffscreenPageLimit(3);

        initYoutube(getActivity());

        /**
         * Claims
         */
        if (CustomPref.haveLogin(getActivity())) {
            lloClaims.setVisibility(View.VISIBLE);
        } else {
            lloClaims.setVisibility(View.GONE);
        }
    }

    /**
     * function set listener for view
     *
     * @author nn.tai
     * @date Dec 14, 2017
     */
    private void setListener() {

        btnThanhToanPhi.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PaymentNoLoginActivity.class);
                startActivity(intent);
            }
        });

        rvSanPham.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WebTutorialActivity.class);
                intent.putExtra(Constant.INT_KEY_WEB_URL, Constant.CONST_URL_PRODUCT);
                intent.putExtra(Constant.INT_KEY_WEB_URL_TITLE, "Sản phẩm");
                startActivity(intent);
            }
        });

        rvGioiThieu.addOnItemTouchListener(new RecyclerViewTouchListener(getActivity(), rvGioiThieu, new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
                intent.putExtra("title", "Chào mừng quý khách đến với cổng thông tin khách hàng");
                intent.putExtra("content", " \n" +
                        "\t<p>\n" +
                        "\t\t<span style=\"color: rgb(255,0,0)\"><span style=\"font-size: 16px\"><span style=\"color: rgb(0,0,0)\"><font size=\"2\" face=\"Arial\"><span class=\"971442906-06102010\">Với mục tiêu phát triển mối quan hệ gắn kết với Khách hàng tham gia bảo hiểm thông qua việc cập nhật thường xuyên cho Khách hàng tất cả những thông tin về hợp đồng bảo hiểm của mình, Dai-ichi Life Việt Nam trân trọng giới thiệu đến toàn thể Quý Khách hàng trang thông tin trực tuyến dành cho Khách hàng <strong><i>\"Cổng thông tin Khách hàng\"</i></strong> </span></font></span></span></span></p>\n" +
                        "\t<p id=\"aui-3-2-0PR1-1137\">\n" +
                        "\t\t<span style=\"color: rgb(255,0,0)\" id=\"aui-3-2-0PR1-1213\"><span style=\"font-size: 16px\" id=\"aui-3-2-0PR1-1210\"><span style=\"color: rgb(0,0,0)\" id=\"aui-3-2-0PR1-1207\"><font id=\"aui-3-2-0PR1-1204\" size=\"2\" face=\"Arial\"><span class=\"971442906-06102010\" id=\"aui-3-2-0PR1-1201\"><span style=\"color: rgb(255,0,0)\" id=\"aui-3-2-0PR1-1198\"><span style=\"font-size: 16px\" id=\"aui-3-2-0PR1-1195\"><span style=\"color: rgb(0,0,0)\" id=\"aui-3-2-0PR1-1192\"><font id=\"aui-3-2-0PR1-1189\" size=\"2\" face=\"Arial\"><span class=\"971442906-06102010\" id=\"aui-3-2-0PR1-1186\"><span style=\"color: rgb(255,0,0)\" id=\"aui-3-2-0PR1-1183\"><span style=\"font-size: 16px\" id=\"aui-3-2-0PR1-1180\"><span style=\"color: rgb(0,0,0)\" id=\"aui-3-2-0PR1-1177\"><font id=\"aui-3-2-0PR1-1174\" size=\"2\" face=\"Arial\"><span class=\"971442906-06102010\" id=\"aui-3-2-0PR1-1171\">Thông qua <strong><i>\"Cổng thông tin Khách hàng\"</i></strong>, dù đang ở bất kỳ nơi đâu, Quý khách đều có thể nhanh chóng và dễ dàng tra cứu những thông tin về hợp đồng bảo hiểm nhân thọ của mình: các sản phẩm bảo hiểm, ngày đáo hạn hợp đồng, ngày đến hạn nộp phí, phí bảo hiểm định kỳ phải nộp, phí bảo hiểm đã nộp, các giá trị của hợp đồng như lãi tích lũy, tiền mặt định kỳ, giá trị hoàn lại … Bên cạnh đó, Quý khách còn có thể tra cứu và sử dụng điểm thưởng tích lũy của chương trình tích lũy điểm thưởng \"Gắn bó dài lâu\". </span></font></span></span></span></span></font></span></span></span></span></font></span></span></span></p>\n" +
                        "\t<p>\n" +
                        "\t\t<span style=\"color: rgb(255,0,0)\"><span style=\"font-size: 16px\"><span style=\"color: rgb(0,0,0)\"><font size=\"2\" face=\"Arial\"><span class=\"971442906-06102010\"><span style=\"color: rgb(255,0,0)\"><span style=\"font-size: 16px\"><span style=\"color: rgb(0,0,0)\"><font size=\"2\" face=\"Arial\"><span class=\"971442906-06102010\"><span style=\"color: rgb(255,0,0)\"><span style=\"font-size: 16px\"><span style=\"color: rgb(0,0,0)\"><font size=\"2\" face=\"Arial\"><span class=\"971442906-06102010\"><span style=\"color: rgb(255,0,0)\"><span style=\"font-size: 16px\"><span style=\"color: rgb(0,0,0)\"><font size=\"2\" face=\"Arial\"><span class=\"971442906-06102010\"><span style=\"color: rgb(255,0,0)\"><span style=\"font-size: 16px\"><span style=\"color: rgb(0,0,0)\"><font size=\"2\" face=\"Arial\"><span class=\"971442906-06102010\">Chúng tôi tin rằng <strong><i>\"Cổng thông tin Khách hàng\"</i></strong> này không chỉ mang lại nhiều tiện ích cho Quý khách hàng trong suốt thời gian tham gia bảo hiểm mà còn là cầu nối mang Dai-ichi Life Việt Nam đến gần Quý Khách hàng hơn.</span></font></span></span></span></span></font></span></span></span></span></font></span></span></span></span></font></span></span></span></span></font></span></span></span></p>\n" +
                        "\t<p>\n" +
                        "\t\t<span style=\"color: rgb(255,0,0)\"><span style=\"font-size: 16px\"><span style=\"color: rgb(0,0,0)\"><font size=\"2\" face=\"Arial\"><span class=\"971442906-06102010\"><span style=\"color: rgb(255,0,0)\"><span style=\"font-size: 16px\"><span style=\"color: rgb(0,0,0)\"><font size=\"2\" face=\"Arial\"><span class=\"971442906-06102010\"><span style=\"color: rgb(255,0,0)\"><span style=\"font-size: 16px\"><span style=\"color: rgb(0,0,0)\"><font size=\"2\" face=\"Arial\"><span class=\"971442906-06102010\"><span style=\"color: rgb(255,0,0)\"><span style=\"font-size: 16px\"><span style=\"color: rgb(0,0,0)\"><font size=\"2\" face=\"Arial\"><span class=\"971442906-06102010\"><span style=\"color: rgb(255,0,0)\"><span style=\"font-size: 16px\"><span style=\"color: rgb(0,0,0)\"><font size=\"2\" face=\"Arial\"><span class=\"971442906-06102010\"><span style=\"color: rgb(255,0,0)\"><span style=\"font-size: 16px\"><span style=\"color: rgb(0,0,0)\"><font size=\"2\" face=\"Arial\"><span class=\"971442906-06102010\"><span style=\"color: rgb(255,0,0)\"><span style=\"font-size: 16px\"><span style=\"color: rgb(0,0,0)\"><font size=\"2\" face=\"Arial\"><span class=\"971442906-06102010\">Quý khách cần trao đổi thêm thông tin, xin vui lòng liên hệ với bất kỳ Văn phòng và Trung tâm Dịch vụ khách hàng, hoặc Tổng đài Dịch vụ khách hàng Dai-ichi Life Việt Nam, điện thoại: <strong>(028) 3810 0888</strong>, bấm phím số 1. Chúng tôi luôn sẵn sàng phục vụ Quý khách.</span></font></span></span></span></span></font></span></span></span></span></font></span></span></span></span></font></span></span></span></span></font></span></span></span></span></font></span></span></span></span></font></span></span></span></p>\n" +
                        "\t<p>\n" +
                        "\t\t<span style=\"color: rgb(255,0,0)\"><span style=\"font-size: 16px\"><span style=\"color: rgb(0,0,0)\"><font size=\"2\" face=\"Arial\"><span class=\"971442906-06102010\"><span style=\"color: rgb(255,0,0)\"><span style=\"font-size: 16px\"><span style=\"color: rgb(0,0,0)\"><font size=\"2\" face=\"Arial\"><span class=\"971442906-06102010\"><span style=\"color: rgb(255,0,0)\"><span style=\"font-size: 16px\"><span style=\"color: rgb(0,0,0)\"><font size=\"2\" face=\"Arial\"><span class=\"971442906-06102010\"><span style=\"color: rgb(255,0,0)\"><span style=\"font-size: 16px\"><span style=\"color: rgb(0,0,0)\"><font size=\"2\" face=\"Arial\"><span class=\"971442906-06102010\"><span style=\"color: rgb(255,0,0)\"><span style=\"font-size: 16px\"><span style=\"color: rgb(0,0,0)\"><font size=\"2\" face=\"Arial\"><span class=\"971442906-06102010\"><span style=\"color: rgb(255,0,0)\"><span style=\"font-size: 16px\"><span style=\"color: rgb(0,0,0)\"><font size=\"2\" face=\"Arial\"><span class=\"971442906-06102010\"><span style=\"color: rgb(255,0,0)\"><span style=\"font-size: 16px\"><span style=\"color: rgb(0,0,0)\"><font size=\"2\" face=\"Arial\"><span class=\"971442906-06102010\"><span style=\"color: rgb(255,0,0)\"><span style=\"font-size: 16px\"><span style=\"color: rgb(0,0,0)\"><font size=\"2\" face=\"Arial\"><span class=\"971442906-06102010\"><span style=\"color: rgb(255,0,0)\"><span style=\"font-size: 16px\"><span style=\"color: rgb(0,0,0)\"><font size=\"2\" face=\"Arial\"><span class=\"971442906-06102010\">Kính chúc Quý khách cùng gia đình thật nhiều sức khỏe, hạnh phúc và thành công.</span></font></span></span></span></span></font></span></span></span></span></font></span></span></span></span></font></span></span></span></span></font></span></span></span></span></font></span></span></span></span></font></span></span></span></span></font></span></span></span></span></font></span></span></span></p>\n" +
                        "\t<p>\n" +
                        "\t\t<span style=\"color: rgb(255,0,0)\"><span style=\"font-size: 16px\"><span style=\"color: rgb(0,0,0)\"><font size=\"2\" face=\"Arial\"><span class=\"971442906-06102010\"><span style=\"color: rgb(255,0,0)\"><span style=\"font-size: 16px\"><span style=\"color: rgb(0,0,0)\"><font size=\"2\" face=\"Arial\"><span class=\"971442906-06102010\"><span style=\"color: rgb(255,0,0)\"><span style=\"font-size: 16px\"><span style=\"color: rgb(0,0,0)\"><font size=\"2\" face=\"Arial\"><span class=\"971442906-06102010\"><span style=\"color: rgb(255,0,0)\"><span style=\"font-size: 16px\"><span style=\"color: rgb(0,0,0)\"><font size=\"2\" face=\"Arial\"><span class=\"971442906-06102010\"><span style=\"color: rgb(255,0,0)\"><span style=\"font-size: 16px\"><span style=\"color: rgb(0,0,0)\"><font size=\"2\" face=\"Arial\"><span class=\"971442906-06102010\"><span style=\"color: rgb(255,0,0)\"><span style=\"font-size: 16px\"><span style=\"color: rgb(0,0,0)\"><font size=\"2\" face=\"Arial\"><span class=\"971442906-06102010\"><span style=\"color: rgb(255,0,0)\"><span style=\"font-size: 16px\"><span style=\"color: rgb(0,0,0)\"><font size=\"2\" face=\"Arial\"><span class=\"971442906-06102010\"><span style=\"color: rgb(255,0,0)\"><span style=\"font-size: 16px\"><span style=\"color: rgb(0,0,0)\"><font size=\"2\" face=\"Arial\"><span class=\"971442906-06102010\"><span style=\"color: rgb(255,0,0)\"><span style=\"font-size: 16px\"><span style=\"color: rgb(0,0,0)\"><font size=\"2\" face=\"Arial\"><span class=\"971442906-06102010\"><span style=\"color: rgb(255,0,0)\"><span style=\"font-size: 16px\"><span style=\"color: rgb(0,0,0)\"><font size=\"2\" face=\"Arial\"><span class=\"971442906-06102010\"><span style=\"color: rgb(255,0,0)\"><span style=\"font-size: 16px\"><span style=\"color: rgb(0,0,0)\"><font size=\"2\" face=\"Arial\"><span class=\"971442906-06102010\">Trân trọng kính chào,</span></font></span></span></span></span></font></span></span></span></span></font></span></span></span></span></font></span></span></span></span></font></span></span></span></span></font></span></span></span></span></font></span></span></span></span></font></span></span></span></span></font></span></span></span></span></font></span></span></span></span></font></span></span></span></p>\n" +
                        "\t<p>\n" +
                        "\t\t<span style=\"color: rgb(255,0,0)\"><span style=\"font-size: 16px\"><span style=\"color: rgb(0,0,0)\"><font size=\"2\" face=\"Arial\"><span class=\"971442906-06102010\"><span style=\"color: rgb(255,0,0)\"><span style=\"font-size: 16px\"><span style=\"color: rgb(0,0,0)\"><font size=\"2\" face=\"Arial\"><span class=\"971442906-06102010\"><span style=\"color: rgb(255,0,0)\"><span style=\"font-size: 16px\"><span style=\"color: rgb(0,0,0)\"><font size=\"2\" face=\"Arial\"><span class=\"971442906-06102010\"><span style=\"color: rgb(255,0,0)\"><span style=\"font-size: 16px\"><span style=\"color: rgb(0,0,0)\"><font size=\"2\" face=\"Arial\"><span class=\"971442906-06102010\"><span style=\"color: rgb(255,0,0)\"><span style=\"font-size: 16px\"><span style=\"color: rgb(0,0,0)\"><font size=\"2\" face=\"Arial\"><span class=\"971442906-06102010\"><span style=\"color: rgb(255,0,0)\"><span style=\"font-size: 16px\"><span style=\"color: rgb(0,0,0)\"><font size=\"2\" face=\"Arial\"><span class=\"971442906-06102010\"><span style=\"color: rgb(255,0,0)\"><span style=\"font-size: 16px\"><span style=\"color: rgb(0,0,0)\"><font size=\"2\" face=\"Arial\"><span class=\"971442906-06102010\"><span style=\"color: rgb(255,0,0)\"><span style=\"font-size: 16px\"><span style=\"color: rgb(0,0,0)\"><font size=\"2\" face=\"Arial\"><span class=\"971442906-06102010\"><span style=\"color: rgb(255,0,0)\"><span style=\"font-size: 16px\"><span style=\"color: rgb(0,0,0)\"><font size=\"2\" face=\"Arial\"><span class=\"971442906-06102010\"><span style=\"color: rgb(255,0,0)\"><span style=\"font-size: 16px\"><span style=\"color: rgb(0,0,0)\"><font size=\"2\" face=\"Arial\"><span class=\"971442906-06102010\"><span style=\"color: rgb(255,0,0)\"><span style=\"font-size: 16px\"><span style=\"color: rgb(0,0,0)\"><font size=\"2\" face=\"Arial\"><span class=\"971442906-06102010\"><span style=\"color: rgb(255,0,0)\"><span style=\"font-size: 1px\"><span style=\"color: rgb(0,0,0)\"><font size=\"2\" face=\"Arial\"><span class=\"971442906-06102010\"><strong><em><em><strong><em><em><em><em><span style=\"color: rgb(255,0,0)\"><span style=\"font-size: 22px\"><span style=\"color: rgb(0,0,0)\"><font size=\"3\" face=\"Arial\"><span class=\"971442906-06102010\"><strong>Dai-ichi Life Việt Nam</strong></span></font></span></span></span></em></em></em></em></strong></em></em></strong></span></font></span></span></span></span></font></span></span></span></span></font></span></span></span></span></font></span></span></span></span></font></span></span></span></span></font></span></span></span></span></font></span></span></span></span></font></span></span></span></span></font></span></span></span></span></font></span></span></span></span></font></span></span></span></span></font></span></span></span></p>\n");
                intent.putExtra("pathImage", R.drawable.intro1);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        rvNews.addOnItemTouchListener(new RecyclerViewTouchListener(getActivity(), rvGioiThieu, new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
                intent.putExtra("title", "QUỸ “VÌ CUỘC SỐNG TƯƠI ĐẸP” CỦA DAI-ICHI LIFE VIỆT NAM PHỐI HỢP CÙNG QUỸ BẢO TRỢ TRẺ EM VIỆT NAM");
                intent.putExtra("content", "Nhằm “tiếp sức đến trường” cho học sinh cả nước trong năm học mới 2018-2019, Quỹ “Vì cuộc sống tươi đẹp” của Công ty Bảo hiểm Nhân thọ (BHNT) Dai-ichi Việt Nam tiếp tục triển khai chương trình “Chào năm học mới tươi đẹp” tại các tỉnh miền Trung và Tây Nguyên. Vào ngày 27/10, Quỹ đã tổ chức tặng 20 xe đạp có tổng trị giá 25 triệu đồng cho 20 học sinh có hoàn cảnh khó khăn tại huyện Tuy Đức, tỉnh Đắk Nông.<br><br>Trước đó, chương trình đã được triển khai tại các tỉnh Ninh Thuận, Quảng Ngãi, Quảng Trị, Thừa Thiên – Huế, Kon Tum, Gia Lai, Đắk Lắk, Bình Định và Phú Yên với 370 phần quà trị giá 250 triệu đồng được trao tặng cho 370 học sinh có hoàn cảnh khó khăn tại miền Trung và Tây Nguyên; nâng tổng số tiền tặng quà cho chương trình này lên đến hơn 600 triệu đồng cho gần 1.000 học sinh trên quy mô cả nước.<br>Ông Trần Đình Quân, Tổng Giám đốc Công ty BHNT Dai-ichi Việt Nam, kiêm Chủ tịch Quỹ “Vì cuộc sống tươi đẹp”, chia sẻ: “Trong quá trình phát triển của đất nước và thành công của mỗi con người, giáo dục được xem là nhân tố quan trọng nhất. Với mong muốn tiếp sức các em học sinh có hoàn cảnh khó khăn trong năm học mới, Chương trình “Chào năm học mới tươi đẹp” đã được Dai-ichi Việt Nam khởi xướng tại nhiều tỉnh thành ở khu vực miền Bắc và miền Nam trong ba tháng vừa qua. Vào cuối tháng 9 và tháng 10 này, Quỹ “Vì cuộc sống tươi đẹp” của Công ty BHNT Dai-ichi Việt Nam tiếp tục đến với người dân miền Trung và Tây Nguyên. Chúng tôi hy vọng rằng những món quà thiết thực được trao tặng cho các em học sinh tại huyện Tuy Đức, tỉnh Đắk Nông hôm nay sẽ giúp các em thêm thuận lợi trên đường đến trường, là nguồn động viên để các em cố gắng đạt thành tích cao trong học tập và mai này trở thành người có ích cho xã hội, đóng góp xây dựng cho quê hương, cộng đồng.”&nbsp;<br><br>Ngày 3/10/2018, Công ty BHNT Dai-ichi Việt Nam đã được Bộ Tài chính cấp Giấy phép chấp thuận cho đợt tăng vốn thứ tám lên gần 7.700 tỷ đồng để đầu tư mở rộng hệ thống phân phối. Với việc tăng vốn này, Công ty BHNT Dai-ichi Việt Nam trở thành một trong những công ty bảo hiểm nhân thọ có mức vốn đầu tư lớn nhất thị trường, minh chứng tiềm lực tài chính vững mạnh cũng như cam kết “Gắn bó dài lâu” với khách hàng Việt Nam.<br>&nbsp;<br>");
                intent.putExtra("pathImage", R.drawable.news1);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        rvSanPham.addOnItemTouchListener(new RecyclerViewTouchListener(getActivity(), rvGioiThieu, new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
                intent.putExtra("title", "An thịnh đầu tư");
                intent.putExtra("content", "\n" +
                        "            <div style=\"text-align: justify;\"><b>Trao Gửi Niềm Tin - Đầu Tư Sáng Suốt</b><br><br>Bạn có rất nhiều các kế hoạch tài chính cho tương lai và gia đình nhưng vẫn băn khoăn làm cách nào để gia tăng tài sản  nhanh chóng bên cạnh phương thức tiết kiệm thông thường. Đặc biệt, với sự biến động không ngừng của cuộc sống cũng như thị trường tài chính, đòi hỏi bạn phải luôn linh hoạt thay đổi, sắp xếp ưu tiên cho các kế hoạch của mình.<br><br></div><div style=\"text-align: justify;\">Với An Thịnh Đầu Tư, Dai-ichi Life Việt Nam mang đến cho bạn giải pháp tài chính ưu việt kết hợp giữa đầu tư và bảo hiểm vừa giúp bạn tăng trưởng tài sản, vừa đáp ứng được nhu cầu thay đổi của bạn trong cuộc sống. Bạn toàn quyền đưa quyết định đầu tư trong hôm nay và linh hoạt thay đổi bất kỳ vào ngày sau.<br></div><font color=\"#7f7f7f\"><b><br><font color=\"#ff0000\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Một giải pháp hoàn hảo cho </font></b></font><font color=\"#ff0000\"><b>nhu cầu đầu tư và bảo vệ</b><br></font><br><div style=\"text-align: left;\">&nbsp; <img style=\"\" src=\"http://172.16.1.35/images/news/217/1497/1650/icon_1.png\">&nbsp; Tạo dựng tài sản một cách hiệu quả thông qua sự hỗ trợ đầu tư chuyên nghiệp từ công ty tài chính hàng đầu Nhật Bản.<br></div><div style=\"text-align: left;\">&nbsp; <img style=\"\" src=\"http://172.16.1.35/images/news/217/1497/1650/icon_1.png\">&nbsp; Dành cho bạn sự lựa chọn thích hợp từ 3 giải pháp đầu tư khác nhau để tăng nguồn vốn từ cổ phiếu, trái phiếu hoặc tiền gửi các tổ chức tín dụng.<br></div><div style=\"text-align: left;\">&nbsp; <img style=\"\" src=\"http://172.16.1.35/images/news/217/1497/1650/icon_1.png\">&nbsp; Khoản thưởng duy trì hợp đồng thường xuyên cho sự gắn kết vững bền.<br></div><div style=\"text-align: left;\">&nbsp; <img style=\"\" src=\"http://172.16.1.35/images/news/217/1497/1650/icon_1.png\">&nbsp; Bảo vệ toàn diện cuộc sống gia đình bạn với bảo hiểm nhân thọ và nhiều lựa chọn bảo vệ khác phòng khi bệnh hiểm nghèo, nằm viện hoặc tai nạn.<br></div><div style=\"text-align: left;\">&nbsp; &nbsp; Bạn toàn quyền chủ động trong việc linh hoạt thay đổi giải pháp đầu tư trước biến động của thị trường tài chính và cuộc sống.&nbsp;&nbsp; &nbsp;&nbsp; </div>\n" +
                        "        ");
                intent.putExtra("pathImage", R.drawable.product1);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        btnClaims.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                //check user have verify phone AND email or not
                if (CustomPref.haveVerifyEmail(getActivity()) && CustomPref.haveVerifyCellphone(getActivity())) {

                    if (CustomPref.getClaimsTemp(getActivity()) != null) {
                        showDialogClaimsTempsaved(getActivity());
                    } else {
                        Intent intent = new Intent(getActivity(), ClaimsRequesterInfoActivity.class);
                        startActivity(intent);
                    }
                } else {
                    MyCustomDialog dialog = new MyCustomDialog.Builder(getActivity())
                            .setMessage("Anh/chị cần xác nhận email và số điện thoại mới có thể tạo Yêu cầu quyền lợi bảo hiểm.")
                            .setPositiveButton(getString(R.string.confirm_ok), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(getActivity(), SettingsActivity.class);
                                    intent.putExtra(SettingsActivity.KEY_SETTINGS_TYPE, SettingsActivity.SETTING_SECURITY);
                                    startActivity(intent);
                                }
                            }).setNegativeButton("Tiếp tục", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(getActivity(), ClaimsRequesterInfoActivity.class);
                                    startActivity(intent);
                                }
                            }).create();
                    dialog.show();
                }
            }
        });
    }

    private void initYoutube(Context context) {

        String youtTubeUrl = "https://www.googleapis.com/youtube/v3/search?part=snippet&channelId=" + Constant.YOUTUBE_CHANNEL_ID + "&maxResults=25&key=" + getString(R.string.keyMap_Dev);

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, youtTubeUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    myLog.e(TAG, "Youtube Response: " + response);

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("items");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        JSONObject jsonVideoId = jsonObject1.getJSONObject("id");
                        JSONObject jsonsnippet = jsonObject1.getJSONObject("snippet");
                        JSONObject jsonObjectdefault = jsonsnippet.getJSONObject("thumbnails").getJSONObject("high");

                        VideoDetails videoDetails = new VideoDetails();

                        String videoid = jsonVideoId.optString("videoId");
                        if (!TextUtils.isEmpty(videoid)) {

                            myLog.e(TAG, "Pos " + i + " ** New Video Id " + videoid);
                            videoDetails.setURL(jsonObjectdefault.optString("url"));
                            videoDetails.setVideoName(jsonsnippet.optString("title"));
                            videoDetails.setVideoDesc(jsonsnippet.optString("description"));
                            videoDetails.setVideoId(videoid);

                            videoDetailsArrayList.add(videoDetails);
                        }
                    }
                    videoAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    myLog.printTrace(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                myLog.printTrace(error);
            }
        });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }

    /**
     * Show dialog conform claims with user
     *
     * @param context
     */
    private void showDialogClaimsTempsaved(final Context context) {

        AlertDialog.Builder alerDialog = new AlertDialog.Builder(context);
        LayoutInflater li = LayoutInflater.from(context);
        View view = li.inflate(R.layout.dialog_claims_tempsaved, null);
        alerDialog.setView(view);
        final AlertDialog dialog = alerDialog.create();
        dialog.show();

        Button btnContinue = dialog.findViewById(R.id.btnContinue);
        Button btnCreateNew = dialog.findViewById(R.id.btnCreateNew);
        RelativeLayout rllQuayLai = dialog.findViewById(R.id.rllQuayLai);

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), ClaimsRequesterInfoActivity.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });

        btnCreateNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CustomPref.saveClaimsTemp(getActivity(), null);

                Intent intent = new Intent(getActivity(), ClaimsRequesterInfoActivity.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });

        rllQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myLog.e(TAG, "onAttach");
        if (context instanceof OnFragmentInteractionListener) {
            onAddFragment = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        myLog.e(TAG, "onDetach");
    }

    @Override
    public void onResume() {
        super.onResume();
        LinearLayout lloHeader = getActivity().findViewById(R.id.lloHeader);
        if (lloHeader != null)
            lloHeader.setVisibility(View.VISIBLE);

        new CPSaveLogTask(getActivity(), Constant.CPLOG_HOME_OPEN).execute();
    }

    @Override
    public void onStop() {
        super.onStop();
        new CPSaveLogTask(getActivity(), Constant.CPLOG_HOME_CLOSE).execute();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
