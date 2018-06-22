package com.dlvn.mcustomerportal.afragment;

import java.util.ArrayList;
import java.util.List;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.activity.WebTutorialActivity;
import com.dlvn.mcustomerportal.adapter.HomeListAdapter;
import com.dlvn.mcustomerportal.adapter.listener.RecyclerViewClickListener;
import com.dlvn.mcustomerportal.adapter.listener.RecyclerViewTouchListener;
import com.dlvn.mcustomerportal.adapter.model.HomeItemModel;
import com.dlvn.mcustomerportal.view.DividerItemDecoration;
import com.dlvn.mcustomerportal.view.RecyclerSmoothLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class InfoGeneralFragment extends Fragment {
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
	HomeListAdapter rvAdapter;

	List<HomeItemModel> lstData;

	public InfoGeneralFragment() {
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
	public static InfoGeneralFragment newInstance(String param1, String param2) {
		InfoGeneralFragment fragment = new InfoGeneralFragment();
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
			view = inflater.inflate(R.layout.fragment_info_general, container, false);

			getViews(view);
			initDatas();
			setListener();
		}

		return view;
	}

	private void getViews(View v) {
		// TODO Auto-generated method stub
		rvContent = (RecyclerView) view.findViewById(R.id.rvContent);
		RecyclerView.LayoutManager layout = new RecyclerSmoothLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,
				false);
		rvContent.setLayoutManager(layout);

		DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvContent.getContext());
		rvContent.addItemDecoration(dividerItemDecoration);
	}

	private void initDatas() {
		// TODO Auto-generated method stub
		lstData = new ArrayList<HomeItemModel>();

		lstData.add(new HomeItemModel("HƯỚNG DẪN SỬ DỤNG CỔNG THÔNG TIN DỊCH VỤ KHÁCH HÀNG",
				"Hướng dẫn sử dụng các chức năng của cổng thông tin khách hàng như đăng nhập, thay đổi mật khẩu, sửa đổi thông tin, thông tin hợp đồng, chương trình điểm thưởng... ",
				"http://baohiemtuonglai.vn/wp-content/uploads/2017/06/tuyen-nhan-vien-kinh-doanh-luong-cao-2.jpg"));
		lstData.add(new HomeItemModel("HƯỚNG DẪN NỘP PHÍ BẢO HIỂM ĐỊNH KỲ",
				"Để hợp đồng bảo hiểm được duy trì hiệu lực liên tục nhằm đảm bảo các quyền lợi bảo hiểm của mình, Quý khách vui lòng nộp phí bảo hiểm định kỳ đúng hạn và có thể lựa chọn...",
				"https://www.baohiem-dai-ichi-life.com/wp-content/uploads/2016/05/3phut-tu-van-bao-hiem-nhan-tho-dai-ichi-life-nhat-ban12.png"));
		lstData.add(new HomeItemModel("CÁC LOẠI BIỂU MẪU",
				"Cung cấp các loại biểu mẫu cho khách hàng như đăng kí dịch vụ SMS, yêu cầu sử dụng điểm thưởng, giải quyết quyền lợi bảo hiểm, thanh toán quyền lợi hợp đồng bảo hiểm...",
				"https://dai-ichi-life.com.vn/images/news/165/1701/attribute/74/Outpatient-Healthcare.jpg"));
		lstData.add(new HomeItemModel("Ý KIẾN KHÁCH HÀNG",
				"Với mục tiêu phục vụ khách hàng ngày càng tốt hơn, chúng tôi rất vui lòng tiếp nhận các ý kiến đóng ghóp của quý khách hàng thông qua việc thu thập ý kiến đóng ghóp qua Cổng thông tin khách hàng.",
				"https://thue.today/media/images/section/brand/168706156912166_dai-ichi-life-viet-nam-cover.png"));
		lstData.add(new HomeItemModel("KHẢO SÁT",
				"Nhằm nâng cao và hoàn thiện hơn nữa chất lượng phục vụ khách hàng, Quý khách vui lòng dành thời gian trả lời ĐẦY ĐỦ nội dung 20 câu hỏi được nêu trong bảng khảo sát dưới đây, ở mức độ chính xác cao nhất có thể. ",
				"https://viecoi.vn/jobs/jobfullview/userdata/jobs/5273/241216-002.jpg"));

		lstData.add(new HomeItemModel("GIỚI THIỆU TIÊU CHÍ ĐIỂM THƯỞNG",
				"Điểm thưởng sẽ được cập nhật tự động vào tài khoản điểm thưởng của Quý khách sau khi đáp ứng các tiêu chí thưởng điểm của Chương trình, trong đó 1 (một) điểm thưởng tương đương với 1.000 (một nghìn) đồng. ",
				"https://www.baohiem-dai-ichi-life.com/wp-content/uploads/2016/05/3phut-tu-van-bao-hiem-nhan-tho-dai-ichi-life-nhat-ban12.png"));
		lstData.add(new HomeItemModel("HƯỚNG DẪN SỬ DỤNG ĐIỂM THƯỞNG",
				"Quý khách có thể sử dụng hoặc tích lũy điểm thưởng của mình để sử dụng các dịch vụ được  Dai-ichi Life Việt Nam cung cấp sau đây:",
				"https://dai-ichi-life.com.vn/images/news/165/1701/attribute/74/Outpatient-Healthcare.jpg"));

		rvAdapter = new HomeListAdapter(getActivity(), lstData);
		rvContent.setAdapter(rvAdapter);
	}

	private void setListener() {
		// TODO Auto-generated method stub
		rvContent.addOnItemTouchListener(
				new RecyclerViewTouchListener(getActivity(), rvContent, new RecyclerViewClickListener() {

					@Override
					public void onLongClick(View view, int position) {

					}

					@Override
					public void onClick(View view, int position) {
						startActivity(new Intent(getActivity(), WebTutorialActivity.class));
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
