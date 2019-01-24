package com.dlvn.mcustomerportal.afragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.activity.ProductListActivity;
import com.dlvn.mcustomerportal.common.Constant;
import com.dlvn.mcustomerportal.utils.CPSaveLogTask;
import com.dlvn.mcustomerportal.utils.listerner.OnFragmentInteractionListener;
import com.dlvn.mcustomerportal.utils.myLog;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    OnFragmentInteractionListener onAddFragment;

    View view;
    Button btnWelcome, btnNews, btnVideo, btnProduct, btnService, btnPayment, btnFacebook, btnYoutube;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_home, container, false);
            getViews(view);
            initData();
            setListener();

        }
        return view;
    }

    private void getViews(View v) {
        btnWelcome = v.findViewById(R.id.btnWelcome);
        btnNews = v.findViewById(R.id.btnNews);
        btnVideo = v.findViewById(R.id.btnVideo);
        btnProduct = v.findViewById(R.id.btnProduct);
        btnService = v.findViewById(R.id.btnService);
        btnPayment = v.findViewById(R.id.btnPayment);
        btnFacebook = v.findViewById(R.id.btnFacebook);
        btnYoutube = v.findViewById(R.id.btnYoutube);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (displayMetrics.widthPixels - 60) / 2, displayMetrics);
        int width = (int) (displayMetrics.widthPixels - 60 * displayMetrics.density) / 2;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, width);
        params.setMargins(10, 0, 10, 0);

        btnWelcome.setLayoutParams(params);
        btnNews.setLayoutParams(params);
        btnVideo.setLayoutParams(params);
        btnProduct.setLayoutParams(params);
        btnService.setLayoutParams(params);
        btnPayment.setLayoutParams(params);
        btnFacebook.setLayoutParams(params);
        btnYoutube.setLayoutParams(params);
    }

    private void initData() {

    }

    private void setListener() {
        btnNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProductListActivity.class);
                intent.putExtra(ProductListActivity.INT_MASTERDATA_TYPE, Constant.MASTERDATA_NEWS_TYPE);
                startActivity(intent);
            }
        });

        btnProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), ProductListActivity.class);
                intent.putExtra(ProductListActivity.INT_MASTERDATA_TYPE, Constant.MASTERDATA_PRODUCT_TYPE);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myLog.e(TAG, "onAttach");
        if (context instanceof OnFragmentInteractionListener) {
            onAddFragment = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        myLog.e(TAG, "onDetach");
    }

    @Override
    public void onResume() {
        super.onResume();
        LinearLayout lloHeader = getActivity().findViewById(R.id.lloHeader);
        if (lloHeader != null)
            lloHeader.setVisibility(View.VISIBLE);

        new CPSaveLogTask(getActivity(), Constant.CPLOG_HOME_OPEN).execute();
    }

    @Override
    public void onStop() {
        super.onStop();
        new CPSaveLogTask(getActivity(), Constant.CPLOG_HOME_CLOSE).execute();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
