package com.dlvn.mcustomerportal.afragment.prototype;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.activity.prototype.DashboardActivity;
import com.dlvn.mcustomerportal.services.model.response.PaymentDetailModel;
import com.dlvn.mcustomerportal.utils.listerner.OnFragmentInteractionListener;
import com.dlvn.mcustomerportal.view.MyCustomDialog;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlterationFragment extends Fragment {

    private static final String TAG = "AlterationFragment";

    private static final String POINFO = "PO-Info";
    private static final String LIINFO = "LI-Info";
    private static final String BENEINFO = "Bene-Info";

    View view;
    Button btnTiepTuc;
    CheckedTextView ctvUpdatePO, ctvUpdateLI, ctvUpdateBEN;
    LinearLayout lloBack;

    PaymentDetailModel POProfile, LIProfile, BeneProfile;

    OnFragmentInteractionListener onAddFragment;

    public static AlterationFragment newInstance(PaymentDetailModel po, PaymentDetailModel li, PaymentDetailModel bene) {
        AlterationFragment fragment = new AlterationFragment();
        Bundle args = new Bundle();
        args.putParcelable(POINFO, po);
        args.putParcelable(LIINFO, li);
        args.putParcelable(BENEINFO, bene);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            POProfile = getArguments().getParcelable(POINFO);
            LIProfile = getArguments().getParcelable(LIINFO);
            BeneProfile = getArguments().getParcelable(BENEINFO);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_alteration, container, false);

            lloBack = view.findViewById(R.id.lloBack);
            lloBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().onBackPressed();
                }
            });

            ctvUpdatePO = view.findViewById(R.id.ctvCapNhatBMBH);
            ctvUpdateLI = view.findViewById(R.id.ctvCapNhatNDBH);
            ctvUpdateBEN = view.findViewById(R.id.ctvCapNhatNTH);

            ctvUpdatePO.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ctvUpdatePO.isChecked())
                        ctvUpdatePO.setChecked(false);
                    else {
                        if (POProfile != null)
                            ctvUpdatePO.setChecked(true);
                        else {
                            MyCustomDialog dialog = new MyCustomDialog.Builder(getActivity())
                                    .setMessage("Không tìm thấy thông tin của người được chọn!")
                                    .setPositiveButton(getString(R.string.confirm_ok), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }).create();
                            dialog.show();
                        }
                    }
                }
            });

            ctvUpdateLI.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ctvUpdateLI.isChecked())
                        ctvUpdateLI.setChecked(false);
                    else {
                        if (LIProfile != null)
                            ctvUpdateLI.setChecked(true);
                        else {
                            MyCustomDialog dialog = new MyCustomDialog.Builder(getActivity())
                                    .setMessage("Không tìm thấy thông tin của người được chọn!")
                                    .setPositiveButton(getString(R.string.confirm_ok), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }).create();
                            dialog.show();
                        }
                    }
                }
            });

            ctvUpdateBEN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ctvUpdateBEN.isChecked())
                        ctvUpdateBEN.setChecked(false);
                    else {
                        if (BeneProfile != null)
                            ctvUpdateBEN.setChecked(true);
                        else {
                            MyCustomDialog dialog = new MyCustomDialog.Builder(getActivity())
                                    .setMessage("Không tìm thấy thông tin của người được chọn!")
                                    .setPositiveButton(getString(R.string.confirm_ok), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }).create();
                            dialog.show();
                        }
                    }
                }
            });

            btnTiepTuc = view.findViewById(R.id.btnTiepTuc);
            btnTiepTuc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    boolean isPO = ctvUpdatePO.isChecked();
                    boolean isLI = ctvUpdateLI.isChecked();
                    boolean isBene = ctvUpdateBEN.isChecked();

                    if (!isPO && !isLI && !isBene) {
                        MyCustomDialog dialog = new MyCustomDialog.Builder(getActivity())
                                .setMessage(getString(R.string.alert_alteration_choice_option))
                                .setPositiveButton(getString(R.string.confirm_ok), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).create();
                        dialog.show();
                    } else {

                        ArrayList<PaymentDetailModel> lsModels = new ArrayList<>();
                        if (isPO & POProfile != null) {
                            lsModels.add(POProfile);
                        }

                        if (isLI && LIProfile != null) {
                            lsModels.add(LIProfile);
                        }

                        if (isBene && BeneProfile != null) {
                            lsModels.add(BeneProfile);
                        }

                        AlterationPOInfoFragment fragment = AlterationPOInfoFragment.newInstance(lsModels);
                        onAddFragment.onFragmentAddstackListener(DashboardActivity.TAB_CONTRACT, fragment, true);
                    }
                }
            });
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        LinearLayout lloHeader = getActivity().findViewById(R.id.lloHeader);
        if (lloHeader != null)
            lloHeader.setVisibility(View.GONE);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            onAddFragment = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
}
