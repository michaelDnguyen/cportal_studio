package com.dlvn.mcustomerportal.afragment;

import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.activity.PaymentResultActivity;
import com.dlvn.mcustomerportal.activity.WebNapasActivity;
import com.dlvn.mcustomerportal.activity.WebTutorialActivity;
import com.dlvn.mcustomerportal.activity.prototype.DashboardActivity;
import com.dlvn.mcustomerportal.adapter.HomePagerAdapter;
import com.dlvn.mcustomerportal.adapter.model.HomeItemModel;
import com.dlvn.mcustomerportal.adapter.model.HomePageItemModel;
import com.dlvn.mcustomerportal.common.Constant;
import com.dlvn.mcustomerportal.common.CustomPref;
import com.dlvn.mcustomerportal.services.ServicesGenerator;
import com.dlvn.mcustomerportal.services.ServicesRequest;
import com.dlvn.mcustomerportal.services.model.BaseRequest;
import com.dlvn.mcustomerportal.services.model.request.GetPriceILPRequest;
import com.dlvn.mcustomerportal.services.model.response.GetPriceILPResponse;
import com.dlvn.mcustomerportal.services.model.response.GetPriceILPResult;
import com.dlvn.mcustomerportal.services.model.response.PriceILPModel;
import com.dlvn.mcustomerportal.utils.Utilities;
import com.dlvn.mcustomerportal.utils.listerner.OnFragmentInteractionListener;
import com.dlvn.mcustomerportal.utils.myLog;
import com.dlvn.mcustomerportal.view.MyCustomDialog;
import com.dlvn.mcustomerportal.view.indicator.CirclePageIndicator;
import com.dlvn.mcustomerportal.view.indicator.ScrollerViewPager;
import com.dlvn.mcustomerportal.view.indicator.SpringIndicator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;
import retrofit2.Call;
import retrofit2.Response;

/**
 * @author nn.tai
 * @date Dec 21, 2017
 */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TAG = "HomeFragment";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    OnFragmentInteractionListener onAddFragment;

    View view;
    ImageView imvAds, imv1, imv2;

    LinearLayout lloHopDong, lloTransaction, llo01, llo02;
    RelativeLayout rloThongTinChung, rloTinTuc;
    ScrollerViewPager viewPager;
    SpringIndicator springIndicator;
    CirclePageIndicator circleIndicator;
    HomePagerAdapter pagerAdapter;

    TextView tvWelcome, tvDescription;
    TextView tvtitleTinTuc, tvcontentTinTuc;

    /**
     * Youtube
     */
    RelativeLayout rlYoutube;
    WebView webView;

    List<HomeItemModel> lstData;
    List<HomePageItemModel> lstPagerData;

    ServicesRequest svRequester;

    // Chart
    List<PriceILPModel> lsChart;
    int year, month;

    private LineChartView chart;
    private LineChartData data;
    private int numberOfLines = 5;
    private int maxNumberOfLines = 5;
    private int numberOfPoints = 5;

    float[][] randomNumbersTab = new float[maxNumberOfLines][numberOfPoints];
    int[] colorLine = {Color.BLUE, Color.CYAN, Color.MAGENTA,Color.RED, Color.GREEN};

    private boolean hasAxes = true;
    private boolean hasAxesNames = true;
    private boolean hasLines = true;
    private boolean hasPoints = false;
    private ValueShape shape = ValueShape.CIRCLE;
    private boolean isFilled = false;
    private boolean hasLabels = false;
    private boolean isCubic = false;
    private boolean hasLabelForSelected = false;
    private boolean pointsHaveDifferentColor;
    private boolean hasGradientToTransparent = false;

    public HomeFragment() {
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
    public static HomeFragment newInstance(String param1, String param2) {
        myLog.E(TAG, "newInstance");

        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myLog.E(TAG, "onCreate");
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        svRequester = ServicesGenerator.createService(ServicesRequest.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myLog.E(TAG, "onCreateView");

        if (view == null) {
//			view = inflater.inflate(R.layout.fragment_home, container, false);
            view = inflater.inflate(R.layout.testing_homefragment, container, false);

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

//        rloThongTinChung = (RelativeLayout) view.findViewById(R.id.rloThongTinChung);
//        rloTinTuc = (RelativeLayout) view.findViewById(R.id.rloTinTuc);
//        tvtitleTinTuc = (TextView) view.findViewById(R.id.tvtitleTinTuc);
//        tvcontentTinTuc = (TextView) view.findViewById(R.id.tvcontentTinTuc);

        lloHopDong = (LinearLayout) view.findViewById(R.id.lloHopDong);
        lloHopDong.setVisibility(View.GONE);
//        lloTransaction = (LinearLayout) view.findViewById(R.id.lloTransaction);

        viewPager = (ScrollerViewPager) view.findViewById(R.id.view_pager);
        viewPager.setVisibility(View.GONE);
        // springIndicator = (SpringIndicator)
        // view.findViewById(R.id.indicator);
        circleIndicator = (CirclePageIndicator) view.findViewById(R.id.indicator);
        circleIndicator.setVisibility(View.GONE);

//        tvWelcome = (TextView) view.findViewById(R.id.tvWelcome);
//        tvDescription = (TextView) view.findViewById(R.id.tvDescription);
//
//        imvAds = (ImageView) view.findViewById(R.id.imv_ads);
//        Glide.with(this).load(R.drawable.daiichii_ads).into(imvAds);

        imv1 = view.findViewById(R.id.imv1);
        Glide.with(getActivity()).load(R.drawable.huan_chuong).into(imv1);
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

        imv2 = view.findViewById(R.id.imv2);
        Glide.with(getActivity()).load(R.drawable.an_tam_hung_thinh_2).into(imv2);
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


        chart = view.findViewById(R.id.chart);
        chart.setVisibility(View.GONE);

        /**
         * youtube
         */
        rlYoutube = view.findViewById(R.id.rlYoutube);
        webView = view.findViewById(R.id.webView);
    }

    /**
     * init data to views
     *
     * @author nn.tai
     * @date Dec 14, 2017
     */
    private void initData() {

        if (CustomPref.haveLogin(getActivity())) {
//            tvWelcome.setText("Chào mừng " + CustomPref.getUserName(getActivity()));
//            tvDescription.setText(getActivity().getString(R.string.home_welcome_user));
            lloHopDong.setVisibility(View.VISIBLE);
//            lloTransaction.setVisibility(View.VISIBLE);
        } else {
//            tvWelcome.setText("Chào mừng Quý khách");
//            tvDescription.setText(getActivity().getString(R.string.home_welcome_guest));
            lloHopDong.setVisibility(View.GONE);
//            lloTransaction.setVisibility(View.GONE);
        }

        // init viewpager
        lstPagerData = new ArrayList<>();
        lstPagerData.add(new HomePageItemModel("00078941", "An Phúc Hưng Thịnh", "236,500,000", "3,200,000",
                "23/12/2017", "23/01/2018", "39"));
        lstPagerData.add(new HomePageItemModel("001034198", "An Thịnh Đầu Tư", "688,105,024", "36,215,500",
                "23/10/2017", "22/12/2017", "8"));
        lstPagerData.add(new HomePageItemModel("001036845", "An Tâm Hưng Thịnh", "56,331,000", "2,814,400",
                "02/11/2017", "01/01/2018", "18"));

        pagerAdapter = new HomePagerAdapter(getActivity(), lstPagerData);
        viewPager.setAdapter(pagerAdapter);
        viewPager.fixScrollSpeed();

        // just set viewPager
        circleIndicator.setViewPager(viewPager);

//        // chart
//        generateValues();
//        generateData();
//         Disable viewport recalculations, see toggleCubic() method for more
//         info.
//        chart.setViewportCalculationEnabled(false);
//        resetViewport();
//        new GetILPPriceTask().execute();

        /**
         * Youtube
         */
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadData("<html>\n" +
                "<head>  \n" +
                "  </head>  \n" +
                "  <body>\n" +
                "<div>\n" +
                "  <p><iframe src=\"https://www.youtube.com/embed/p6ZGRqh9d-c\" width=\"contain\" height=\"contain\" frameborder=\"0\" allowfullscreen=\"allowfullscreen\"></iframe></p>\n" +
                "    </div>    \n" +
                "  </body>\n" +
                "</html>","text/html","UTF-8");
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                myLog.E("shouldOverrideUrlLoading " + url);
                view.loadUrl(url);

                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                myLog.E("onPageStarted " + url);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                myLog.E("onPageFinish " + url);
                super.onPageFinished(view, url);
            }

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                myLog.E("shouldInterceptRequest " + request.getUrl().toString());
                return super.shouldInterceptRequest(view, request);
            }

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                myLog.E("onReceivedError " + request.getUrl().toString());
                super.onReceivedError(view, request, error);
            }

            @Override
            public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
                myLog.E("onReceivedSslError = " + error.getUrl());
                myLog.E("onReceivedSslError code = " + error.getPrimaryError());

                MyCustomDialog.Builder builder = new MyCustomDialog.Builder(getActivity());
                builder.setMessage("Notification error ssl certificate invalid. Do you continue ?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                handler.proceed();
                            }
                        }).setNegativeButton("Hủy", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handler.cancel();
                    }
                });
                builder.create().show();
            }
        });

    }

    /**
     * function set listener for view
     *
     * @author nn.tai
     * @date Dec 14, 2017
     */
    private void setListener() {

        chart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                FundUnitPriceFragment fragment = new FundUnitPriceFragment();
                onAddFragment.onFragmentAddstackListener(DashboardActivity.TAB_HOME, fragment, true);

            }
        });

        rlYoutube.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = "p6ZGRqh9d-c";

                Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
                Intent webIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://www.youtube.com/channel/" + id + "/videos"));
                try {
                    startActivity(appIntent);
                } catch (ActivityNotFoundException ex) {
                    startActivity(webIntent);
                }
            }
        });

//        rloThongTinChung.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                InfoGeneralFragment fragment = new InfoGeneralFragment();
//                if (fragment != null) {
//                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//                    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
//                    fragmentTransaction.replace(R.id.frame, fragment, fragment.getClass().getName());
//                    fragmentTransaction.commitAllowingStateLoss();
//                }
//            }
//        });

//        rloTinTuc.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                startActivity(new Intent(getActivity(), NewsActivity.class));
//            }
//        });

//        tvtitleTinTuc.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                startActivity(new Intent(getActivity(), NewsActivity.class));
//            }
//        });

//        tvcontentTinTuc.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                startActivity(new Intent(getActivity(), NewsActivity.class));
//            }
//        });
    }

    private void generateValues() {
        for (int j = 0; j < lsChart.size(); ++j) {
            randomNumbersTab[0][j] = Float.parseFloat(lsChart.get(j).getILPF1());
            randomNumbersTab[1][j] = Float.parseFloat(lsChart.get(j).getILPF2());
            randomNumbersTab[2][j] = Float.parseFloat(lsChart.get(j).getILPF3());
            randomNumbersTab[3][j] = Float.parseFloat(lsChart.get(j).getILPF4());
            randomNumbersTab[4][j] = Float.parseFloat(lsChart.get(j).getILPF5());
        }
    }

    private void reset() {
        numberOfLines = 5;

        hasAxes = true;
        hasAxesNames = true;
        hasLines = true;
        hasPoints = true;
        shape = ValueShape.CIRCLE;
        isFilled = false;
        hasLabels = false;
        isCubic = false;
        hasLabelForSelected = false;
        pointsHaveDifferentColor = false;

        chart.setValueSelectionEnabled(hasLabelForSelected);
        resetViewport();
    }

    private void resetViewport() {
        // Reset viewport height range to (0,100)
        final Viewport v = new Viewport(chart.getMaximumViewport());
        v.bottom = 9000;
        v.top = 11000;
        v.left = 0;
        v.right = lsChart.size();
        chart.setMaximumViewport(v);
        chart.setCurrentViewport(v);
    }

    private void generateData() {

        List<Line> lines = new ArrayList<Line>();
        for (int i = 0; i < numberOfLines; ++i) {

            List<PointValue> values = new ArrayList<PointValue>();
            for (int j = 0; j < lsChart.size(); ++j) {
                values.add(new PointValue(j, randomNumbersTab[i][j]));
            }

            Line line = new Line(values);
            line.setColor(colorLine[i]);
            line.setShape(shape);
            line.setCubic(isCubic);
            line.setFilled(isFilled);
            line.setHasLabels(hasLabels);
            line.setHasLabelsOnlyForSelected(hasLabelForSelected);
            line.setHasLines(hasLines);
            line.setHasPoints(hasPoints);
            if (pointsHaveDifferentColor) {
                line.setPointColor(ChartUtils.COLORS[(i + 1) % ChartUtils.COLORS.length]);
            }
            lines.add(line);
        }

        data = new LineChartData(lines);

        List<AxisValue> axisValues = new ArrayList<AxisValue>();

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        int month = cal.get(Calendar.MONTH);
        SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");

        for (int i = 0; i < lsChart.size(); i++) {

//            int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
//            axisValues.add(new AxisValue(i).setLabel(fmt.format(cal.getTime())));

            axisValues.add(new AxisValue(i).setLabel(lsChart.get(i).getSubmitDate()));
//            cal.add(Calendar.DAY_OF_MONTH, 7);
        }

        if (hasAxes) {
            Axis axisX = new Axis(axisValues);
            Axis axisY = new Axis().setHasLines(true);
            if (hasAxesNames) {
                axisX.setName("Kỳ định giá");
                axisY.setName("");
            }
            axisX.setTextColor(Color.RED);
            axisX.setTextSize(14);
            axisY.setTextColor(Color.RED);
            axisY.setTextSize(14);

            data.setAxisXBottom(axisX);
            data.setAxisYLeft(axisY);
        } else {
            data.setAxisXBottom(null);
            data.setAxisYLeft(null);
        }

        data.setBaseValue(Float.NEGATIVE_INFINITY);
        chart.setLineChartData(data);
    }

    /**
     * Get Price ILP task
     */
    private class GetILPPriceTask extends AsyncTask<Void, Void, Response<GetPriceILPResponse>> {

        @Override
        protected Response<GetPriceILPResponse> doInBackground(Void... voids) {
            Response<GetPriceILPResponse> res = null;

            try {
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

                //get fromDate - toDate for query
                Date crDate = calendar.getTime();
                String toDate = format.format(crDate);
                myLog.E(TAG, "toDate : " + toDate);

                calendar.add(Calendar.MONTH, -3);
                Date date = calendar.getTime();
                String fromDate = format.format(date);
                myLog.E(TAG, "fromDate : " + fromDate);

                GetPriceILPRequest data = new GetPriceILPRequest();
                data.setProject(Constant.Project_ID);
                data.setFromDate(fromDate);
                data.setToDate(toDate);

                BaseRequest request = new BaseRequest();
                request.setJsonDataInput(data);
                Call<GetPriceILPResponse> call = svRequester.GetPriceILP(request);
                res = call.execute();
            } catch (Exception e) {
                myLog.printTrace(e);
            }
            return res;
        }

        @Override
        protected void onPostExecute(Response<GetPriceILPResponse> res) {
            super.onPostExecute(res);
            if (res.isSuccessful()) {
                GetPriceILPResponse response = res.body();
                if (response != null) {
                    GetPriceILPResult result = response.getResponse();
                    if (result != null) {
                        if (result.getResult().equalsIgnoreCase("true")) {
                            lsChart = result.getDtProposal();
                            if (lsChart != null) {
                                generateValues();
                                generateData();
                                chart.setViewportCalculationEnabled(false);
                                resetViewport();
                            }
                        } else {
                            myLog.E(TAG, "Get Price ILP Error: " + result.getErrLog());
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myLog.E(TAG, "onAttach");
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
        myLog.E(TAG, "onDetach");
    }
}
