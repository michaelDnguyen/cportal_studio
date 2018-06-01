package com.dlvn.mcustomerportal.afragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.dlvn.mcustomerportal.R;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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

public class FundUnitPriceFragment extends Fragment {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;

	private OnFragmentInteractionListener mListener;

	View view;

	TextView tvMonth, tvTruoc, tvSau;

	int year, month;

	private LineChartView chart;
	private LineChartData data;
	private int numberOfLines = 3;
	private int maxNumberOfLines = 4;
	private int numberOfPoints = 4;

	float[][] randomNumbersTab = new float[maxNumberOfLines][numberOfPoints];
	int[] colorLine = { Color.BLUE, Color.CYAN, Color.MAGENTA };

	private boolean hasAxes = true;
	private boolean hasAxesNames = true;
	private boolean hasLines = true;
	private boolean hasPoints = true;
	private ValueShape shape = ValueShape.CIRCLE;
	private boolean isFilled = false;
	private boolean hasLabels = false;
	private boolean isCubic = false;
	private boolean hasLabelForSelected = false;
	private boolean pointsHaveDifferentColor;
	private boolean hasGradientToTransparent = false;

	public FundUnitPriceFragment() {
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 *
	 * @param param1
	 *            Parameter 1.
	 * @param param2
	 *            Parameter 2.
	 * @return A new instance of fragment PhotosFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static FundUnitPriceFragment newInstance(String param1, String param2) {
		FundUnitPriceFragment fragment = new FundUnitPriceFragment();
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
		tvTruoc = (TextView) v.findViewById(R.id.tvTruoc);
		tvSau = (TextView) v.findViewById(R.id.tvSau);
	}

	private void initDatas() {
		// TODO Auto-generated method stub
		Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH) + 1;
		tvMonth.setText(month + "/" + year);

		generateValues();

		generateData();

		// Disable viewport recalculations, see toggleCubic() method for more
		// info.
		chart.setViewportCalculationEnabled(false);

		resetViewport();
	}

	private void setListener() {
		// TODO Auto-generated method stub
		chart.setOnValueTouchListener(new ValueTouchListener());

		tvTruoc.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				month--;
				if (month <= 0) {
					month = 12;
					year--;
				}
				tvMonth.setText(month + "/" + year);

				reset();
				generateValues();
				generateData();
			}
		});

		tvSau.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				month++;
				if (month > 12) {
					month = 1;
					year++;
				}
				tvMonth.setText(month + "/" + year);

				reset();
				generateValues();
				generateData();
			}
		});
	}

	private void generateValues() {
		for (int i = 0; i < maxNumberOfLines; ++i) {
			for (int j = 0; j < numberOfPoints; ++j) {
				randomNumbersTab[i][j] = (float) Math.random() * 7000f + 10000;
			}
		}
	}

	private void reset() {
		numberOfLines = 3;

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
		v.bottom = 10000;
		v.top = 17000;
		v.left = 0;
		v.right = numberOfPoints - 1;
		chart.setMaximumViewport(v);
		chart.setCurrentViewport(v);
	}

	private void generateData() {

		List<Line> lines = new ArrayList<Line>();
		for (int i = 0; i < numberOfLines; ++i) {

			List<PointValue> values = new ArrayList<PointValue>();
			for (int j = 0; j < numberOfPoints; ++j) {
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

		for (int i = 0; i < 4; i++) {
			int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
			axisValues.add(new AxisValue(i).setLabel(fmt.format(cal.getTime())));
			cal.add(Calendar.DAY_OF_MONTH, 7);
		}

		if (hasAxes) {
			Axis axisX = new Axis(axisValues);
			Axis axisY = new Axis().setHasLines(true);
			if (hasAxesNames) {
				axisX.setName("Kỳ định giá");
				axisY.setName("Giá đơn vị");
			}
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
	 * Adds lines to data, after that data should be set again with
	 * {@link LineChartView#setLineChartData(LineChartData)}. Last 4th line has
	 * non-monotonically x values.
	 */
	private void addLineToData() {
		if (data.getLines().size() >= maxNumberOfLines) {
			Toast.makeText(getActivity(), "Samples app uses max 4 lines!", Toast.LENGTH_SHORT).show();
			return;
		} else {
			++numberOfLines;
		}

		generateData();
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

	// TODO: Rename method, update argument and hook method into UI event
	public void onButtonPressed(Uri uri) {
		if (mListener != null) {
			mListener.onFragmentInteraction(uri);
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
		mListener = null;
	}

	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated to
	 * the activity and potentially other fragments contained in that activity.
	 * <p>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	public interface OnFragmentInteractionListener {
		// TODO: Update argument type and name
		void onFragmentInteraction(Uri uri);
	}

}
