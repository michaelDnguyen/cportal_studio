package com.dlvn.mcustomerportal.afragment;

import java.util.ArrayList;
import java.util.List;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.adapter.NotificationListAdapter;
import com.dlvn.mcustomerportal.adapter.model.NotificationModel;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class NotificationsFragment extends Fragment {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;

	private OnFragmentInteractionListener mListener;

	ListView lvNotification;
	TextView tvNotNoti;
	SwipeRefreshLayout swipeContainer;

	View view;

	NotificationListAdapter adapter;
	List<NotificationModel> lstData;

	public NotificationsFragment() {
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
	 * @return A new instance of fragment NotificationsFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static NotificationsFragment newInstance(String param1, String param2) {
		NotificationsFragment fragment = new NotificationsFragment();
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
			view = inflater.inflate(R.layout.fragment_notifications, container, false);

			swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
			lvNotification = (ListView) view.findViewById(R.id.lvNotification);
			
			tvNotNoti = (TextView) view.findViewById(R.id.tvNotNoti);

			initData();
			setListener();
		}

		return view;
	}

	private void initData() {

		if (lstData == null)
			lstData = new ArrayList<>();

		lstData.add(
				new NotificationModel("Thông báo gửi tặng điểm thưởng thành công tới hợp đồng 00045213", "25/12/2017", true));
		lstData.add(new NotificationModel("BẢN TIN DAI-ICHI LIFE VIỆT NAM Số 11 năm 2017", "03/11/2017", true));
		lstData.add(new NotificationModel("Thông báo đến hạn đóng phí hợp đồng 00084562", "25/10/2017", false));
		lstData.add(new NotificationModel("Thông báo mua thẻ cào thành công", "12/10/2017", false));
		lstData.add(new NotificationModel("BẢN TIN DAI-ICHI LIFE VIỆT NAM Số 11 năm 2017", "03/10/2017", false));
		lstData.add(new NotificationModel("Thông báo đổi phiếu mua hàng siêu thị thành công", "23/09/2017", false));
		lstData.add(new NotificationModel("BẢN TIN DAI-ICHI LIFE VIỆT NAM Số 11 năm 2017", "03/09/2017", false));

		if (adapter == null)
			adapter = new NotificationListAdapter(getActivity(), R.layout.item_notification, lstData);
		lvNotification.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		
		swipeContainer.setRefreshing(false);
	}

	private void setListener() {
		swipeContainer.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				initData();
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
