package com.dlvn.mcustomerportal.afragment;

import java.util.ArrayList;
import java.util.List;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.adapter.ElectricBillListAdapter;
import com.dlvn.mcustomerportal.adapter.model.ElectricBillModel;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class ElectricBillFragment extends Fragment {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;

	private OnFragmentInteractionListener mListener;

	View view;

	ListView lvData;
	TextView tvNoData;
	SwipeRefreshLayout swipeContainer;
	
	ElectricBillListAdapter adapter;
	List<ElectricBillModel> lstData;

	public ElectricBillFragment() {
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
	public static ElectricBillFragment newInstance(String param1, String param2) {
		ElectricBillFragment fragment = new ElectricBillFragment();
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
			view = inflater.inflate(R.layout.fragment_electricbill, container, false);

			getView(view);
			initDatas();
			setListener();
		}

		return view;
	}

	private void getView(View v) {
		// TODO Auto-generated method stub
		lvData = (ListView) v.findViewById(R.id.lvData);
		lvData.setDividerHeight(10);
		
		tvNoData = (TextView) v.findViewById(R.id.tvNoData);
		swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
	}

	private void initDatas() {
		// TODO Auto-generated method stub
		if(lstData == null)
			lstData = new ArrayList<>();
		
		lstData.add(new ElectricBillModel("0987873","00869752", "Công ty TNHH An Thịnh Phát", "1,500,000,000", "14/02/2017", "", ""));
		lstData.add(new ElectricBillModel("0845626","00369856", "Công ty TNHH An Thịnh Phát", "1,200,000,000", "16/02/2016", "", ""));
		lstData.add(new ElectricBillModel("0756398","00869752", "Công ty TNHH An Thịnh Phát", "900,000,000", "17/02/2015", "", ""));
		lstData.add(new ElectricBillModel("0658471","00369856", "Công ty TNHH An Thịnh Phát", "750,000,000", "20/02/2014", "", ""));
		
		if(adapter == null)
			adapter = new ElectricBillListAdapter(getActivity(), lstData);
		lvData.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		
		if(lstData.size() > 0)
			tvNoData.setVisibility(View.GONE);
		else
			tvNoData.setVisibility(View.VISIBLE);
	}

	private void setListener() {
		// TODO Auto-generated method stub
		swipeContainer.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				swipeContainer.setRefreshing(false);
			}
		});
		
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
