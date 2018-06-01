package com.dlvn.mcustomerportal.afragment;

import java.util.ArrayList;
import java.util.List;

import com.dlvn.mcustomerportal.ProductDetailActivity;
import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.adapter.HomeListAdapter;
import com.dlvn.mcustomerportal.adapter.ProductListAdapter;
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

public class ProductInfoFragment extends Fragment {
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

	ProductListAdapter rvAdapter;
	List<HomeItemModel> lstData;

	public ProductInfoFragment() {
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
	public static ProductInfoFragment newInstance(String param1, String param2) {
		ProductInfoFragment fragment = new ProductInfoFragment();
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

			initData();
			setListener();
		}

		return view;
	}

	private void getViews(View v) {
		rvContent = (RecyclerView) v.findViewById(R.id.rvContent);
		RecyclerView.LayoutManager layout = new RecyclerSmoothLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,
				false);
		rvContent.setLayoutManager(layout);

		DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvContent.getContext());
		rvContent.addItemDecoration(dividerItemDecoration);

	}

	private void initData() {
		lstData = new ArrayList<HomeItemModel>();

		lstData.add(new HomeItemModel("AN TÂM HƯNG THỊNH",
				"Một giải pháp tài chính tối ưu, vừa cung cấp cho bạn sự bảo vệ toàn diện, vừa giúp bạn đầu tư hiệu quả và thật linh hoạt.",
				"https://www.dai-ichi-life.com.vn/images/news/217/1497/an-tam-hung-thinh-danhsac.png"));
		lstData.add(new HomeItemModel("AN PHÚC HƯNG THỊNH",
				"Một giải pháp tài chính toàn diện, mang đến sự bảo vệ trước những rủi ro trong cuộc sống và đầu tư sinh lợi hiệu quả với lãi suất theo sát mức lãi suất của thị trường tài chính.",
				"https://www.dai-ichi-life.com.vn/images/news/212/1492/1645/An-tam_phuc-hinh_1.png"));
		lstData.add(new HomeItemModel("AN THỊNH ĐẦU TƯ",
				"Là giải pháp ưu việt kết hợp giữa bảo hiểm và đầu tư mang đến cơ hội gia tăng giá trị tài sản với nhiều danh mục đầu tư khác nhau và linh hoạt trước mọi thay đổi của cuộc sống.",
				"https://www.dai-ichi-life.com.vn/images/news/209/1745/atdt-iconweb.jpg"));
		lstData.add(new HomeItemModel("AN TÂM SỨC KHỎE",
				"An tâm tận hưởng cuộc sống, không còn mối lo tài chính trước những rủi ro do bệnh tật.",
				"https://www.dai-ichi-life.com.vn/images/news/154/410/467/an-tam-suc-khoe_1.png"));
		lstData.add(new HomeItemModel("AN NHÀN HƯU TRÍ",
				"Giải pháp hưu trí cung cấp thu nhập đều đặn khi về hưu và sự bảo vệ toàn diện trong suốt thời gian tích lũy giúp bạn có được sự độc lập về tài chính và yên tâm trước mọi rủi ro trong cuộc sống.",
				"https://www.dai-ichi-life.com.vn/images/news/233/1513/anht_1.PNG"));
		lstData.add(new HomeItemModel("HƯNG NGHIỆP HƯU TRÍ",
				"Nguồn động lực gắn kết doanh nghiệp và nhân viên cùng phát triển bền vững",
				"https://www.dai-ichi-life.com.vn/images/news/237/1517/hnht_outside.PNG"));
		lstData.add(new HomeItemModel("BẢO HIỂM HỖ TRỢ CHI PHÍ CHỮA TRỊ BỆNH NAN Y",
				"Để ứng phó kịp thời trước những căn bệnh nan y, giúp người bệnh an tâm điều trị, vượt qua bệnh tật và tiếp tục cuộc sống bình an bên những người thân yêu",
				"https://www.dai-ichi-life.com.vn/images/news/42/554/bh_hotro-chiphi-chua-tri-benh-nan-y_ds.png"));
		lstData.add(new HomeItemModel("BẢO HIỂM HỖ TRỢ ĐÓNG PHÍ BỆNH HIỂM NGHÈO",
				"Giúp an tâm điều trị mà không phải lo lắng về kế hoạch tài chính cho người thân",
				"https://www.dai-ichi-life.com.vn/images/news/52/564/bh-hotro-dongphi-benh-hiemngheo_ds.png"));
		lstData.add(new HomeItemModel("BẢO HIỂM BỆNH HIỂM NGHÈO",
				"Tăng cường khả năng tài chính nhằm đối phó với các căn bệnh hiểm nghèo. ",
				"https://www.dai-ichi-life.com.vn/images/news/33/545/bhbenhhiemngheo.jpg"));
		lstData.add(new HomeItemModel("BẢO HIỂM HỖ TRỢ CHI PHÍ SINH HOẠT",
				"Nguồn tài chính giúp duy trì chi phí sinh hoạt tối thiểu của gia đình trong thời gian điều trị thương tật.",
				"https://www.dai-ichi-life.com.vn/images/news/34/546/billcover.jpg"));
		lstData.add(new HomeItemModel("BẢO HIỂM HỖ TRỢ ĐÓNG PHÍ",
				"Kế hoạch tài chính cho tương lai của những người thương yêu của bạn sẽ luôn được bảo đảm, ngay cả khi có những rủi ro không mong đợi xảy ra.",
				"https://www.dai-ichi-life.com.vn/images/news/55/567/bh_hotro-dongphi_ds.png"));
		lstData.add(new HomeItemModel("BẢO HIỂM TỪ BỎ THU PHÍ",
				"Ngay cả khi có những rủi ro không mong đợi xảy ra, kế hoạch tài chính cho tương lai của những người thương yêu của bạn sẽ luôn được bảo đảm.",
				"https://www.dai-ichi-life.com.vn/images/news/72/584/tubothuphi.jpg"));
		lstData.add(new HomeItemModel("BẢO HIỂM CHĂM SÓC SỨC KHỎE",
				"Sản phẩm bảo hiểm y tế giúp bạn chăm sóc sức khỏe cho gia đình, nhẹ nỗi lo về chi phí y tế để an tâm vui sống bên cạnh những người thân yêu.",
				"https://www.dai-ichi-life.com.vn/images/news/165/1701/healthcare.jpg"));
		lstData.add(new HomeItemModel("BẢO HIỂM BỆNH HIỂM NGHÈO CAO CẤP TOÀN DIỆN",
				"Bảo vệ bạn và gia đình trước 88 Bệnh hiểm nghèo ở nhiều giai đoạn khác nhau.",
				"https://www.dai-ichi-life.com.vn/images/news/168/1960/ci88.jpg"));

		rvAdapter = new ProductListAdapter(getActivity(), lstData);
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

						Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
						intent.putExtra("title", lstData.get(position).get_title());
						intent.putExtra("content", lstData.get(position).get_content());
						intent.putExtra("pathImage", lstData.get(position).getImgPath());
						startActivity(intent);
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
