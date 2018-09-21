package com.dlvn.mcustomerportal.afragment.prototype;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.common.CustomPref;

/**
 * A placeholder fragment containing a simple view.
 */
public class SettingLinkFragment extends Fragment {

    View v;
    ImageView imvAvatar, imvAddAvatar;
    TextView tvFullName, tvUserName, tvGender, tvPoints, tvRank;

    public SettingLinkFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (v == null) {
            v = inflater.inflate(R.layout.fragment_settings_security, container, false);

            imvAvatar = (ImageView) v.findViewById(R.id.imvAvatar);
            imvAddAvatar = (ImageView) v.findViewById(R.id.imvAddAvatar);

            tvFullName = (TextView) v.findViewById(R.id.tvFullname);
            tvUserName = (TextView) v.findViewById(R.id.tvUserName);
            tvGender = (TextView) v.findViewById(R.id.tvGender);
            tvPoints = (TextView) v.findViewById(R.id.tvPoints);
            tvRank = (TextView) v.findViewById(R.id.tvRank);

            initDatas();

        }

        return v;
    }

    private void initDatas() {
        if (CustomPref.haveLogin(getActivity())) {
            tvFullName.setText(CustomPref.getFullName(getActivity()));
            tvUserName.setText(CustomPref.getFullName(getActivity()));
            tvGender.setText(CustomPref.getGender(getActivity()));
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
