package com.dlvn.mcustomerportal.afragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.common.Constant;
import com.dlvn.mcustomerportal.services.ServicesGenerator;
import com.dlvn.mcustomerportal.services.ServicesRequest;
import com.dlvn.mcustomerportal.services.model.BaseRequest;
import com.dlvn.mcustomerportal.services.model.request.GetPriceILPRequest;
import com.dlvn.mcustomerportal.services.model.response.GetPriceILPResponse;
import com.dlvn.mcustomerportal.services.model.response.GetPriceILPResult;
import com.dlvn.mcustomerportal.services.model.response.PriceILPModel;
import com.dlvn.mcustomerportal.utils.myLog;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
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

public class FundUnitPriceFragment extends Fragment {

    private static final String TAG = "FundUnitPriceFragment2";

    View view;
    TextView tvMonth;
    ImageView imvTruoc, imvSau;
    ImageView imvTangTruong, imvPhattrien,imvBaoToan,imvThinhVuong,imvDamBao;

    ServicesRequest svRequester;
    int year, month;
    List<PriceILPModel> lsChart;
    SimpleDateFormat format;

    private LineChartView chart;
    private LineChartData data;
    private int numberOfLines = 5;
    private int maxNumberOfLines = 5;
    private int numberOfPoints = 5;

    float[][] randomNumbersTab = new float[maxNumberOfLines][numberOfPoints];
    int[] colorLine = {Color.BLUE, Color.CYAN, Color.MAGENTA, Color.YELLOW, Color.RED};

    private boolean hasAxes = true;
    private boolean hasAxesNames = true;
    private boolean hasLines = true;
    private boolean hasPoints = false;
    private ValueShape shape = ValueShape.CIRCLE;
    private boolean isFilled = false;
    private boolean hasLabels = false;
    private boolean isCubic = false;
    private boolean hasLabelForSelected = true;
    private boolean pointsHaveDifferentColor;
    private boolean hasGradientToTransparent = false;

    public FundUnitPriceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of this fragment using
     * the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PhotosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FundUnitPriceFragment newInstance(String param1, String param2) {
        FundUnitPriceFragment fragment = new FundUnitPriceFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
        svRequester = ServicesGenerator.createService(ServicesRequest.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_fundprice, container, false);

            getViews(view);
            initDatas();
            setListener();
        }

        return view;
    }

    private void getViews(View v) {
        // TODO Auto-generated method stub
        chart = (LineChartView) v.findViewById(R.id.chart);
        tvMonth = (TextView) v.findViewById(R.id.tvMonth);
        imvTruoc = v.findViewById(R.id.imvTruoc);
        imvSau = v.findViewById(R.id.imvSau);

        imvTangTruong = v.findViewById(R.id.imvTangTruong);
        imvPhattrien = v.findViewById(R.id.imvPhatTrien);
        imvBaoToan = v.findViewById(R.id.imvBaoToan);
        imvThinhVuong = v.findViewById(R.id.imvThinhvuong);
        imvDamBao = v.findViewById(R.id.imvDamBao);

        imvTangTruong.setBackgroundColor(colorLine[0]);
        imvPhattrien.setBackgroundColor(colorLine[1]);
        imvBaoToan.setBackgroundColor(colorLine[2]);
        imvThinhVuong.setBackgroundColor(colorLine[3]);
        imvDamBao.setBackgroundColor(colorLine[4]);
    }

    private void initDatas() {
        // TODO Auto-generated method stub
        format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();


        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        tvMonth.setText(month + "/" + year);

        //get fromDate - toDate for query
        Date crDate = calendar.getTime();
        String toDate = format.format(crDate);
        myLog.e(TAG, "toDate : " + toDate);

        calendar.set(Calendar.DATE, 1);
        Date date = calendar.getTime();
        String fromDate = format.format(date);
        myLog.e(TAG, "fromDate : " + fromDate);

        new GetILPPriceTask(fromDate, toDate).execute();
//        generateValues();
//        generateData();
//        chart.setViewportCalculationEnabled(false);
//        resetViewport();
    }

    private void setListener() {
        // TODO Auto-generated method stub
        chart.setOnValueTouchListener(new ValueTouchListener());

        imvTruoc.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                month--;
                if (month <= 0) {
                    month = 12;
                    year--;
                }
                tvMonth.setText(month + "/" + year);

                Calendar c = Calendar.getInstance();
                c.set(Calendar.YEAR, year);
                c.set(Calendar.MONTH, month-1);
                c.set(Calendar.DATE, 1);

                String fromDate = format.format(c.getTime());

                c.set(Calendar.DATE, c.getActualMaximum(Calendar.DAY_OF_MONTH));
                String toDate = format.format(c.getTime());

                new GetILPPriceTask(fromDate, toDate).execute();
//                reset();
//                generateValues();
//                generateData();
            }
        });

        imvSau.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                month++;
                if (month > 12) {
                    month = 1;
                    year++;
                }
                tvMonth.setText(month + "/" + year);

                Calendar c = Calendar.getInstance();
                c.set(Calendar.YEAR, year);
                c.set(Calendar.MONTH, month-1);
                c.set(Calendar.DATE, 1);

                String fromDate = format.format(c.getTime());

                c.set(Calendar.DATE, c.getActualMaximum(Calendar.DAY_OF_MONTH));
                String toDate = format.format(c.getTime());

                new GetILPPriceTask(fromDate, toDate).execute();

//                reset();
//                generateValues();
//                generateData();
            }
        });
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
        chart.setCurrentViewportWithAnimation(v);
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
//            cal.add(Calendar.DAY_OF_MONTH, 7);
            axisValues.add(new AxisValue(i).setLabel(lsChart.get(i).getSubmitDate()));
        }

        if (hasAxes) {
            Axis axisX = new Axis(axisValues);
            Axis axisY = new Axis().setHasLines(true);
            if (hasAxesNames) {
                axisX.setName("Kỳ định giá");
                axisY.setName("");
            }

            axisX.setTextColor(Color.RED);
            axisY.setTextColor(Color.RED);

            data.setAxisXBottom(axisX);
            data.setAxisYLeft(axisY);
        } else {
            data.setAxisXBottom(null);
            data.setAxisYLeft(null);
        }

        data.setBaseValue(Float.POSITIVE_INFINITY);
        chart.setLineChartData(data);
        chart.startDataAnimation();
        chart.setValueSelectionEnabled(true);
    }

    private class ValueTouchListener implements LineChartOnValueSelectListener {

        @Override
        public void onValueSelected(int lineIndex, int pointIndex, PointValue value) {
            Toast.makeText(getActivity(), "Selected: " + value, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onValueDeselected() {
            // TODO Auto-generated method stub

        }
    }

    /**
     * Get Price ILP task
     */
    private class GetILPPriceTask extends AsyncTask<Void, Void, Response<GetPriceILPResponse>> {

        String fromDate, toDate;

        public GetILPPriceTask(String from, String to) {
            fromDate = from;
            toDate = to;
        }

        @Override
        protected Response<GetPriceILPResponse> doInBackground(Void... voids) {
            Response<GetPriceILPResponse> res = null;

            try {

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
                            myLog.e(TAG, "Get Price ILP Error: " + result.getErrLog());
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // if (context instanceof OnFragmentInteractionListener) {
        // mListener = (OnFragmentInteractionListener) context;
        // } else {
        // throw new RuntimeException(context.toString()
        // + " must implement OnFragmentInteractionListener");
        // }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
