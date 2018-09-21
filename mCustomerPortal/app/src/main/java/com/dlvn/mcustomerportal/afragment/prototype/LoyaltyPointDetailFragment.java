package com.dlvn.mcustomerportal.afragment.prototype;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.adapter.LoyaltyPointPagerAdapter;
import com.dlvn.mcustomerportal.common.CustomPref;
import com.dlvn.mcustomerportal.utils.Utilities;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoyaltyPointDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class LoyaltyPointDetailFragment extends Fragment {
    LinearLayout lloHeader;
    private ViewPager viewPager;
    private LoyaltyPointPagerAdapter adapter;
    TextView tvDiemLon, tvDiemNho, tvRank, tenKhachHang;

    View view;
    ImageView leftArrow, rightArrow;

    private OnFragmentInteractionListener mListener;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LoyaltyPointDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_loyalty_point_detail, container, false);
        setView();

        tvDiemLon.setText(Utilities.formatMoneyToVND(String.valueOf(CustomPref.getUserPoint(getActivity()) * 1000)).replaceAll("VND", ""));
        tvDiemNho.setText(String.valueOf(CustomPref.getUserPoint(getActivity())));
        tvRank.setText(CustomPref.getUserRank(getActivity()));
        tenKhachHang.setText(CustomPref.getFullName(getActivity()));

        adapter = new LoyaltyPointPagerAdapter(getActivity());
        viewPager.setAdapter(adapter);
        setListener();
        return view;
    }

    /**
     * set views
     */
    private void setView() {
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        leftArrow = (ImageView) view.findViewById(R.id.image_view_left_arrow);
        rightArrow = (ImageView) view.findViewById(R.id.image_view_right_arrow);
        lloHeader = (LinearLayout) getActivity().findViewById(R.id.lloHeader);
        //TODO: find view for tvDiemLon tvDiemNho
        tvDiemLon = (TextView) view.findViewById(R.id.tvDiemLon);
        tvDiemNho = (TextView) view.findViewById(R.id.tvDiemNho);
        tvRank = view.findViewById(R.id.tvRank);
        tenKhachHang = view.findViewById(R.id.tenKhachHang);
    }

    /**
     * set listeners
     */
    private void setListener() {
        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getItem(-1) >= 0)
                    viewPager.setCurrentItem(getItem(-1), true);
                else
                    Toast.makeText(getContext(), "Đã đến vị trí đầu trang", Toast.LENGTH_SHORT).show();
            }
        });

        rightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: nên tính toán dựa theo chiều dài của dữ liệu khi có dữ liệu hoàn chỉnh
                if (getItem(1) <= 4)
                    viewPager.setCurrentItem(getItem(+1), true);
                else
                    Toast.makeText(getContext(), "Đã đến vị trí cuối trang", Toast.LENGTH_SHORT).show();
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
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onStart() {
        /**
         * TODO: Hide Header of Dashboard Activity
         */
        if (lloHeader != null)
            lloHeader.setVisibility(View.GONE);
        super.onStart();
    }

    @Override
    public void onPause() {
        /**
         * TODO: Show Header of Dashboard Activity
         */
        lloHeader.setVisibility(View.VISIBLE);
        super.onPause();
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
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
