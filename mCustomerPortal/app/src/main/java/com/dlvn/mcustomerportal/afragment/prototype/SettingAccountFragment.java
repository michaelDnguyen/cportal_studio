package com.dlvn.mcustomerportal.afragment.prototype;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.common.Constant;
import com.dlvn.mcustomerportal.common.CustomPref;
import com.dlvn.mcustomerportal.services.ServicesGenerator;
import com.dlvn.mcustomerportal.services.ServicesRequest;
import com.dlvn.mcustomerportal.services.model.BaseRequest;
import com.dlvn.mcustomerportal.services.model.request.GetPointByCLIIDRequest;
import com.dlvn.mcustomerportal.services.model.response.GetPointByCLIIDResponse;
import com.dlvn.mcustomerportal.services.model.response.GetPointByCLIIDResult;
import com.dlvn.mcustomerportal.utils.Utilities;
import com.dlvn.mcustomerportal.utils.myLog;
import com.dlvn.mcustomerportal.view.MyCustomDialog;

import retrofit2.Call;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class SettingAccountFragment extends Fragment {

    View v;
    ImageView imvAvatar, imvAddAvatar;
    TextView tvFullName, tvUserName, tvUserID, tvGender, tvPoints, tvRank;

    ServicesRequest svRequester;

    public SettingAccountFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        svRequester = ServicesGenerator.createService(ServicesRequest.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (v == null) {
            v = inflater.inflate(R.layout.fragment_settings_account, container, false);

            imvAvatar = (ImageView) v.findViewById(R.id.imvAvatar);
            imvAddAvatar = (ImageView) v.findViewById(R.id.imvAddAvatar);

            tvFullName = (TextView) v.findViewById(R.id.tvFullname);
            tvUserName = (TextView) v.findViewById(R.id.tvUserName);
            tvUserID = v.findViewById(R.id.tvUserID);
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
            tvUserID.setText(CustomPref.getUserID(getActivity()));

            if (CustomPref.getGender(getActivity()).equals("F"))
                tvGender.setText("Nữ");
            else
                tvGender.setText("Nam");

            tvPoints.setText(CustomPref.getUserPoint(getActivity()) + "");
            tvRank.setText(CustomPref.getUserRank(getActivity()));
        }

        new getClientPointTask(getActivity()).execute();
    }

    /**
     * Task call API get client point By CLient ID
     */
    public class getClientPointTask extends AsyncTask<Void, Void, Response<GetPointByCLIIDResponse>> {
        Context context;

        public getClientPointTask(Context c) {
            context = c;
        }

        @Override
        protected Response<GetPointByCLIIDResponse> doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            Response<GetPointByCLIIDResponse> result = null;
            try {
                GetPointByCLIIDRequest data = new GetPointByCLIIDRequest();

                data.setClientID(CustomPref.getUserID(context));
                data.setAPIToken(CustomPref.getAPIToken(context));
                data.setDeviceID(Utilities.getDeviceID(context));
                data.setOS(Utilities.getDeviceName() + "-" + Utilities.getVersion());
                data.setProject(Constant.Project_ID);
                data.setAuthentication(Constant.Project_Authentication);
                data.setUserLogin(CustomPref.getUserName(context));

                BaseRequest request = new BaseRequest();
                request.setJsonDataInput(data);

                Call<GetPointByCLIIDResponse> call = svRequester.GetPointByCLIID(request);
                result = call.execute();

            } catch (Exception e) {
                myLog.printTrace(e);
                return null;
            }

            // TODO: register the new account here.
            return result;
        }

        @Override
        protected void onPostExecute(final Response<GetPointByCLIIDResponse> success) {

            if (success != null) {
                try {
                    if (success.isSuccessful()) {
                        GetPointByCLIIDResponse response = success.body();
                        if (response != null)
                            if (response.getResponse() != null) {
                                GetPointByCLIIDResult result = response.getResponse();
                                if (result != null) {

                                    if (result.getResult() != null && result.getResult().equals("false")) {
                                        //If account not exits --> link to register
                                        myLog.E("SettingAccountFragment", "Get Point: " + result.getErrLog());

                                        if (result.getNewAPIToken().equalsIgnoreCase("invalidtoken")) {
                                            Utilities.processLoginAgain(context, getString(R.string.message_alert_relogin));
                                        }

                                    } else if (result.getResult() != null && result.getResult().equals("true")) {

                                        //Save Token
                                        if (!TextUtils.isEmpty(result.getNewAPIToken()))
                                            CustomPref.saveToken(context, result.getNewAPIToken());

                                        if (result.getPoint() != null) {
                                            String point = result.getPoint();
                                            String rank = result.getClassPO();

                                            CustomPref.saveUserPoint(getActivity(), Float.parseFloat(point) / 1000);
                                            CustomPref.saveUserRank(getActivity(), rank);
                                            tvPoints.setText(CustomPref.getUserPoint(getActivity()) + "");
                                            tvRank.setText(CustomPref.getUserRank(getActivity()));
                                        }
                                    }
                                }
                            }
                    }
                } catch (Exception e) {
                    myLog.printTrace(e);
                }
            } else {
                MyCustomDialog dialog = new MyCustomDialog.Builder(context).setMessage("Không kết nối được server!").setTitle("Lỗi mạng").setPositiveButton(getString(R.string.confirm_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
                dialog.show();
            }
        }

        @Override
        protected void onCancelled() {
            this.cancel(true);
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
