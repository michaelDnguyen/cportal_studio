package com.dlvn.mcustomerportal.afragment;

import java.util.ArrayList;
import java.util.List;

import com.dlvn.mcustomerportal.ContractDetailActivity;
import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.adapter.ContractListAdapter;
import com.dlvn.mcustomerportal.adapter.model.ContractModel;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Thông tin hợp đồng bảo hiểm: gồm hợp đồng nhóm và hợp đồng cá nhân
 * 
 * @author nn.tai
 * @date Nov 7, 2017
 */
public class InfoContractFragment extends Fragment {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;

	View view;

	Button btnHDCaNhan, btnHDNhom;
	ListView lvData;
	TextView tvNoData;
	SwipeRefreshLayout swipeContainer;

	ContractListAdapter canhanAdapter, nhomAdapter;
	boolean isCanhan = true;

	private OnFragmentInteractionListener mListener;

	public InfoContractFragment() {
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
	 * @return A new instance of fragment MoviesFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static InfoContractFragment newInstance(String param1, String param2) {
		InfoContractFragment fragment = new InfoContractFragment();
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
			view = inflater.inflate(R.layout.fragment_info_contract, container, false);
			getViews(view);
			initDatas();
			setListener();

		}
		return view;
	}

	private void getViews(View v) {
		btnHDCaNhan = (Button) v.findViewById(R.id.btnHDCaNhan);
		btnHDNhom = (Button) v.findViewById(R.id.btnHDNhom);

		tvNoData = (TextView) v.findViewById(R.id.tvNoData);
		tvNoData.setVisibility(View.GONE);

		swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);

		lvData = (ListView) v.findViewById(R.id.lvDataCaNhan);
		lvData.setDividerHeight(10);
	}

	private void initDatas() {

		initCanhan();

		btnHDCaNhan.setSelected(true);
		lvData.setAdapter(canhanAdapter);
		// lvDataNhom.setAdapter(nhomAdapter);
	}

	private void initCanhan() {
		List<ContractModel> lst = new ArrayList<>();
		lst.add(new ContractModel("000048526", "An Tâm Hưng Thịnh", true, 80000000, "12/05/2009", "12/10/2025"));
		lst.add(new ContractModel("000098574", "An Thịnh Đầu Tư", true, 15000000, "12/05/2009", "12/10/2025"));
		lst.add(new ContractModel("000015423", "An Phúc Hưng Thịnh", true, 30000000, "12/05/2009", "12/10/2025"));
		lst.add(new ContractModel("000085632", "An Nhàn Hưu Trí", false, 100000000, "12/05/2009", "12/10/2025"));
		lst.add(new ContractModel("000075412", "Chăm Sóc Sức Khỏe", false, 120000000, "12/05/2009", "12/10/2025"));
		if (canhanAdapter == null)
			canhanAdapter = new ContractListAdapter(getActivity(), lst);
		else
			canhanAdapter.reFreshData(lst);
	}
	
	private void initNhom(){
		List<ContractModel> lst2 = new ArrayList<>();
		lst2.add(new ContractModel("000047856", "Chăm sóc sức khỏe", true, 80000000, "12/05/2009", "12/10/2025"));
		lst2.add(new ContractModel("000056458", "An Thịnh Đầu Tư", true, 15000000, "12/05/2009", "12/10/2025"));
		lst2.add(new ContractModel("000068563", "An Tâm Hưng Thịnh", false, 30000000, "12/05/2009", "12/10/2025"));
		lst2.add(new ContractModel("000014536", "An Nhàn Hưu Trí", true, 100000000, "12/05/2009", "12/10/2025"));
		lst2.add(new ContractModel("000036547", "An Lạc Hưng Thịnh", false, 120000000, "12/05/2009", "12/10/2025"));
		nhomAdapter = new ContractListAdapter(getActivity(), lst2);
		if (canhanAdapter == null)
			canhanAdapter = new ContractListAdapter(getActivity(), lst2);
		else 
			canhanAdapter.reFreshData(lst2);
	}

	private void setListener() {
		btnHDCaNhan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.setSelected(true);
				btnHDNhom.setSelected(false);
				if (!isCanhan) {
					initCanhan();
					isCanhan = true;
				}
			}
		});

		btnHDNhom.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.setSelected(true);
				btnHDCaNhan.setSelected(false);
				if (isCanhan) {
					initNhom();
					isCanhan = false;
				}
			}
		});

		swipeContainer.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				swipeContainer.setRefreshing(false);
			}
		});
		
		lvData.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ContractModel item = (ContractModel) parent.getAdapter().getItem(position);
				
				Intent intent = new Intent(getActivity(), ContractDetailActivity.class);
				intent.putExtra("CONTRACT_DETAIL", item);
				startActivity(intent);
				
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
