package com.dlvn.mcustomerportal.afragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.bumptech.glide.Glide;
import com.dlvn.mcustomerportal.activity.NewsActivity;
import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.adapter.HomePagerAdapter;
import com.dlvn.mcustomerportal.adapter.model.HomeItemModel;
import com.dlvn.mcustomerportal.adapter.model.HomePageItemModel;
import com.dlvn.mcustomerportal.common.cPortalPref;
import com.dlvn.mcustomerportal.utils.myLog;
import com.dlvn.mcustomerportal.view.indicator.CirclePageIndicator;
import com.dlvn.mcustomerportal.view.indicator.ScrollerViewPager;
import com.dlvn.mcustomerportal.view.indicator.SpringIndicator;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * 
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

	private OnFragmentInteractionListener mListener;

	View view;
	ImageView imvAds;

	LinearLayout lloHopDong, lloTransaction;
	RelativeLayout rloThongTinChung, rloTinTuc;
	ScrollerViewPager viewPager;
	SpringIndicator springIndicator;
	CirclePageIndicator circleIndicator;
	HomePagerAdapter pagerAdapter;

	TextView tvWelcome, tvDescription;
	TextView tvtitleTinTuc, tvcontentTinTuc;

	List<HomeItemModel> lstData;
	List<HomePageItemModel> lstPagerData;

	// Chart
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
	 * @param param1
	 *            Parameter 1.
	 * @param param2
	 *            Parameter 2.
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
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		myLog.E(TAG, "onCreateView");

		if (view == null) {
			view = inflater.inflate(R.layout.fragment_home, container, false);

			getViews(view);
			initData();
			setListener();
		}
		return view;
	}

	/**
	 * get view from layout
	 * 
	 * @author nn.tai
	 * @date Dec 14, 2017
	 * @param view
	 */
	private void getViews(View view) {

		rloThongTinChung = (RelativeLayout) view.findViewById(R.id.rloThongTinChung);
		rloTinTuc = (RelativeLayout) view.findViewById(R.id.rloTinTuc);
		tvtitleTinTuc = (TextView) view.findViewById(R.id.tvtitleTinTuc);
		tvcontentTinTuc = (TextView) view.findViewById(R.id.tvcontentTinTuc);

		lloHopDong = (LinearLayout) view.findViewById(R.id.lloHopDong);
		lloTransaction = (LinearLayout) view.findViewById(R.id.lloTransaction);

		viewPager = (ScrollerViewPager) view.findViewById(R.id.view_pager);
		// springIndicator = (SpringIndicator)
		// view.findViewById(R.id.indicator);
		circleIndicator = (CirclePageIndicator) view.findViewById(R.id.indicator);

		tvWelcome = (TextView) view.findViewById(R.id.tvWelcome);
		tvDescription = (TextView) view.findViewById(R.id.tvDescription);

		imvAds = (ImageView) view.findViewById(R.id.imv_ads);
		Glide.with(this).load(R.drawable.daiichii_ads).into(imvAds);

		chart = (LineChartView) view.findViewById(R.id.chart);
	}

	/**
	 * init data to views
	 * 
	 * @author nn.tai
	 * @date Dec 14, 2017
	 */
	private void initData() {

		if (cPortalPref.haveLogin(getActivity())) {
			tvWelcome.setText("Chào mừng " + cPortalPref.getUserName(getActivity()));
			tvDescription.setText(getActivity().getString(R.string.home_welcome_user));
			lloHopDong.setVisibility(View.VISIBLE);
			lloTransaction.setVisibility(View.VISIBLE);
		} else {
			tvWelcome.setText("Chào mừng Quý khách");
			tvDescription.setText(getActivity().getString(R.string.home_welcome_guest));
			lloHopDong.setVisibility(View.GONE);
			lloTransaction.setVisibility(View.GONE);
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

		// chart
		generateValues();
		generateData();
		// Disable viewport recalculations, see toggleCubic() method for more
		// info.
		chart.setViewportCalculationEnabled(false);
		resetViewport();
	}

	/**
	 * function set listener for view
	 * 
	 * @author nn.tai
	 * @date Dec 14, 2017
	 */
	private void setListener() {
		rloThongTinChung.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				InfoGeneralFragment fragment = new InfoGeneralFragment();
				if (fragment != null) {
					FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
					fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
					fragmentTransaction.replace(R.id.frame, fragment, fragment.getClass().getName());
					fragmentTransaction.commitAllowingStateLoss();
				}
			}
		});

		rloTinTuc.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(getActivity(), NewsActivity.class));
			}
		});

		tvtitleTinTuc.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(getActivity(), NewsActivity.class));
			}
		});

		tvcontentTinTuc.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(getActivity(), NewsActivity.class));
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

	// TODO: Rename method, update argument and hook method into UI event
	public void onButtonPressed(Uri uri) {
		if (mListener != null) {
			mListener.onFragmentInteraction(uri);
		}
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		myLog.E(TAG, "onAttach");
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
		myLog.E(TAG, "onDetach");
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
