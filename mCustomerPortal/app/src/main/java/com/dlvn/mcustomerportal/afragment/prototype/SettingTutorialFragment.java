package com.dlvn.mcustomerportal.afragment.prototype;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dlvn.mcustomerportal.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingTutorialFragment extends Fragment {

    private static final String TAG = "SettingTutorialFragment";

    View view;

    public SettingTutorialFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(view == null){
            view = inflater.inflate(R.layout.fragment_setting_tutorial, container, false);



        }
        return view;
    }
}
