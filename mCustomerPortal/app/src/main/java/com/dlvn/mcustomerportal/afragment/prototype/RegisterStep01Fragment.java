package com.dlvn.mcustomerportal.afragment.prototype;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.utils.listerner.OnRegisterFragmentListener;
import com.dlvn.mcustomerportal.view.clearable_edittext.ClearableEditText;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link com.dlvn.mcustomerportal.utils.listerner.OnRegisterFragmentListener} interface
 * to handle interaction events.
 * Use the {@link RegisterStep01Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterStep01Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static final int REGISTER_STEP_01 = 1;
    public static final int REGISTER_STEP_02 = 2;
    public static final int REGISTER_STEP_03 = 3;
    public static final int REGISTER_STEP_04 = 4;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnRegisterFragmentListener mListener;

    View v;
    TextView tvTitle;
    ClearableEditText cedtInput;
    CheckedTextView chkvShowPass;

    public RegisterStep01Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterStep01Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterStep01Fragment newInstance(String param1, String param2) {
        RegisterStep01Fragment fragment = new RegisterStep01Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public void setProcessNext(int step, Object user){


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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(v ==null) {
            v = inflater.inflate(R.layout.fragment_register_step01, container, false);

            tvTitle = (TextView) v.findViewById(R.id.tvTitle);
            cedtInput = (ClearableEditText) v.findViewById(R.id.cedtInput);
            chkvShowPass = (CheckedTextView) v.findViewById(R.id.chbShowPassword);




        }

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(int percent) {
        if (mListener != null) {
            mListener.onRegisterListener(percent);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnRegisterFragmentListener) {
            mListener = (OnRegisterFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnRegisterFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
