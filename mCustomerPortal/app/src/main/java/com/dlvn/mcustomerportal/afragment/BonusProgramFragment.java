package com.dlvn.mcustomerportal.afragment;

import java.util.ArrayList;
import java.util.List;

import com.dlvn.mcustomerportal.activity.Bonus_NopPhiBH_Step01_Activity;
import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.adapter.BonusListAdapter;
import com.dlvn.mcustomerportal.adapter.listener.RecyclerViewClickListener;
import com.dlvn.mcustomerportal.adapter.listener.RecyclerViewTouchListener;
import com.dlvn.mcustomerportal.adapter.model.BonusItemModel;
import com.dlvn.mcustomerportal.view.DividerItemDecoration;
import com.dlvn.mcustomerportal.view.RecyclerSmoothGridLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BonusProgramFragment extends Fragment {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;

	private OnFragmentInteractionListener mListener;

	View view;
	RecyclerView rvContent;

	BonusListAdapter rvAdapter;
	List<BonusItemModel> lstData;

	public BonusProgramFragment() {
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
	public static BonusProgramFragment newInstance(String param1, String param2) {
		BonusProgramFragment fragment = new BonusProgramFragment();
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
			view = inflater.inflate(R.layout.fragment_bonus_program, container, false);
			getViews(view);

			initData();
			setListener();
		}

		return view;
	}

	private void getViews(View v) {
		rvContent = (RecyclerView) v.findViewById(R.id.rvContent);
		RecyclerView.LayoutManager layout = new RecyclerSmoothGridLayoutManager(getActivity(), 2);
		rvContent.setLayoutManager(layout);

		DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvContent.getContext());
		rvContent.addItemDecoration(dividerItemDecoration);
	}

	private void initData() {
		lstData = new ArrayList<BonusItemModel>();

		lstData.add(new BonusItemModel("Nộp phí bảo hiểm / Hoàn trả tạm ứng",
				"Quý khách có thể sử dụng tiền thưởng để nộp phí bảo hiểm định kỳ hoặc hoàn trả tạm ứng cho hợp đồng bảo hiểm của Quý khách.",
				R.drawable.nop_phi_bao_hiem));
		lstData.add(new BonusItemModel("Nhận Thẻ quà tặng",
				"Quý khách có thể sử dụng tiền thưởng để nhận Thẻ quà tặng và chuyển tiền thưởng vào tài khoản Thẻ để trải nghiệm các tiện ích ưu đãi của thẻ trả trước đồng thương hiệu Dai-ichi Life Việt Nam & HDBank được phát hành bởi HDBank dành cho khách hàng của Dai-ichi Life Việt Nam.",
				R.drawable.nhan_the_qua_tang));
		lstData.add(new BonusItemModel("Nạp Thẻ điện thoại",
				"Quý khách có thể sử dụng tiền thưởng để nạp tiền cho các thuê bao di động trả trước của mạng Vinaphone, Mobifone và Viettel.",
				R.drawable.nap_the_dien_thoai));
		lstData.add(new BonusItemModel("Nhận Phiếu mua hàng",
				"Quý khách có thể sử dụng tiền thưởng để nhận Phiếu mua hàng để mua sắm tại các siêu thị hàng đầu Việt Nam hiện nay là Co.opMart và Big C.",
				R.drawable.nhan_phieu_mua_hang));
		lstData.add(new BonusItemModel("Nhận Phiếu kiểm tra y tế",
				"Quý khách có thể sử dụng Phiếu kiểm tra y tế do Dai-ichi Life Việt Nam phát hành để kiểm tra sức khỏe tại các cơ sở y tế ở các địa chỉ này.",
				R.drawable.phieu_kiem_tra_y_te));
		lstData.add(new BonusItemModel("Tặng điểm",
				"Quý khách có thể sử dụng tiền thưởng để tặng điểm thưởng tích lũy của Quý khách cho người khác là Bên mua bảo hiểm có hợp đồng đang có hiệu lực với Dai-ichi Life Việt Nam. ",
				R.drawable.tang_diem));
		lstData.add(new BonusItemModel("Quá trình sử dụng điểm thưởng", "", R.drawable.su_dung_diem));
		lstData.add(new BonusItemModel("Quá trình tích lũy điểm thưởng", "", R.drawable.tich_luy_diem));

		rvAdapter = new BonusListAdapter(getActivity(), lstData);
		rvContent.setAdapter(rvAdapter);
	}

	private void setListener() {
		rvContent.addOnItemTouchListener(
				new RecyclerViewTouchListener(getActivity(), rvContent, new RecyclerViewClickListener() {

					@Override
					public void onLongClick(View view, int position) {

					}

					@Override
					public void onClick(View view, int position) {
						startActivity(new Intent(getActivity(), Bonus_NopPhiBH_Step01_Activity.class));
					}
				}));
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
