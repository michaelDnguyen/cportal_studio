package com.dlvn.mcustomerportal.afragment.prototype;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.common.Constant;
import com.dlvn.mcustomerportal.common.CustomPref;
import com.dlvn.mcustomerportal.services.ServicesGenerator;
import com.dlvn.mcustomerportal.services.ServicesRequest;
import com.dlvn.mcustomerportal.services.model.BaseRequest;
import com.dlvn.mcustomerportal.services.model.request.CPSubmitFormRequest;
import com.dlvn.mcustomerportal.services.model.response.CPSubmitFormResponse;
import com.dlvn.mcustomerportal.services.model.response.CPSubmitFormResult;
import com.dlvn.mcustomerportal.utils.Utilities;
import com.dlvn.mcustomerportal.utils.myLog;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingNotificationFragment extends Fragment {

    private static final String TAG = "SettingNotificationFragment";

    View view;
    SwitchCompat swChangeAcc, swChangePolicy, swEventHoliday, swUsingLoyalty, swNews;
    SwitchCompat swChangeAcc2, swChangePolicy2, swUsingLoyalty2, swNews2;

    ServicesRequest svRequester;

    public SettingNotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_setting_notification, container, false);

            initViews(view);
            setListener();
        }
        return view;
    }

    private void initViews(View v) {
        svRequester = ServicesGenerator.createService(ServicesRequest.class);

        swChangeAcc = v.findViewById(R.id.swChangeAcc);
        swChangePolicy = v.findViewById(R.id.swChangePolicy);
        swEventHoliday = v.findViewById(R.id.swEventHoliday);
        swUsingLoyalty = v.findViewById(R.id.swUsingLoyalty);
        swNews = v.findViewById(R.id.swNews);

        swChangeAcc2 = v.findViewById(R.id.swChangeAcc2);
        swChangePolicy2 = v.findViewById(R.id.swChangePolicy2);
        swUsingLoyalty2 = v.findViewById(R.id.swUsingLoyalty2);
        swNews2 = v.findViewById(R.id.swNews2);
    }

    private void setListener() {
        swChangeAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (swChangeAcc.isChecked())
                    new doUpdateSettingsTask(getActivity(), Constant.ACTION_NOTIFICATION_SETTINGS, Constant.ACTION_ACTIVE, Constant.MSG_CODE_NOTIFY_ACCOUNT).execute();
                else
                    new doUpdateSettingsTask(getActivity(), Constant.ACTION_NOTIFICATION_SETTINGS, Constant.ACTION_NON_ACTIVE, Constant.MSG_CODE_NOTIFY_ACCOUNT).execute();
            }
        });

        swChangePolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (swChangePolicy.isChecked())
                    new doUpdateSettingsTask(getActivity(), Constant.ACTION_NOTIFICATION_SETTINGS, Constant.ACTION_ACTIVE, Constant.MSG_CODE_NOTIFY_POLICY).execute();
                else
                    new doUpdateSettingsTask(getActivity(), Constant.ACTION_NOTIFICATION_SETTINGS, Constant.ACTION_NON_ACTIVE, Constant.MSG_CODE_NOTIFY_POLICY).execute();
            }
        });

        swEventHoliday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (swEventHoliday.isChecked())
                    new doUpdateSettingsTask(getActivity(), Constant.ACTION_NOTIFICATION_SETTINGS, Constant.ACTION_ACTIVE, Constant.MSG_CODE_NOTIFY_EVENT_DOB).execute();
                else
                    new doUpdateSettingsTask(getActivity(), Constant.ACTION_NOTIFICATION_SETTINGS, Constant.ACTION_NON_ACTIVE, Constant.MSG_CODE_NOTIFY_EVENT_DOB).execute();
            }
        });

        swUsingLoyalty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (swUsingLoyalty.isChecked())
                    new doUpdateSettingsTask(getActivity(), Constant.ACTION_NOTIFICATION_SETTINGS, Constant.ACTION_ACTIVE, Constant.MSG_CODE_NOTIFY_LOYALTYPOINT).execute();
                else
                    new doUpdateSettingsTask(getActivity(), Constant.ACTION_NOTIFICATION_SETTINGS, Constant.ACTION_NON_ACTIVE, Constant.MSG_CODE_NOTIFY_LOYALTYPOINT).execute();
            }
        });

        swNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (swNews.isChecked())
                    new doUpdateSettingsTask(getActivity(), Constant.ACTION_NOTIFICATION_SETTINGS, Constant.ACTION_ACTIVE, Constant.MSG_CODE_NOTIFY_NEWS).execute();
                else
                    new doUpdateSettingsTask(getActivity(), Constant.ACTION_NOTIFICATION_SETTINGS, Constant.ACTION_NON_ACTIVE, Constant.MSG_CODE_NOTIFY_NEWS).execute();
            }
        });

        swChangeAcc2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (swChangeAcc2.isChecked())
                    new doUpdateSettingsTask(getActivity(), Constant.ACTION_NOTIFICATION_SETTINGS, Constant.ACTION_ACTIVE, Constant.MSG_CODE_SMS_ACCOUNT).execute();
                else
                    new doUpdateSettingsTask(getActivity(), Constant.ACTION_NOTIFICATION_SETTINGS, Constant.ACTION_NON_ACTIVE, Constant.MSG_CODE_SMS_ACCOUNT).execute();
            }
        });

        swChangePolicy2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (swChangePolicy2.isChecked())
                    new doUpdateSettingsTask(getActivity(), Constant.ACTION_NOTIFICATION_SETTINGS, Constant.ACTION_ACTIVE, Constant.MSG_CODE_SMS_POLICY).execute();
                else
                    new doUpdateSettingsTask(getActivity(), Constant.ACTION_NOTIFICATION_SETTINGS, Constant.ACTION_NON_ACTIVE, Constant.MSG_CODE_SMS_POLICY).execute();
            }
        });

        swUsingLoyalty2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (swUsingLoyalty2.isChecked())
                    new doUpdateSettingsTask(getActivity(), Constant.ACTION_NOTIFICATION_SETTINGS, Constant.ACTION_ACTIVE, Constant.MSG_CODE_SMS_LOYALTYPOINT).execute();
                else
                    new doUpdateSettingsTask(getActivity(), Constant.ACTION_NOTIFICATION_SETTINGS, Constant.ACTION_NON_ACTIVE, Constant.MSG_CODE_SMS_LOYALTYPOINT).execute();
            }
        });

        swNews2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (swNews2.isChecked())
                    new doUpdateSettingsTask(getActivity(), Constant.ACTION_NOTIFICATION_SETTINGS, Constant.ACTION_ACTIVE, Constant.MSG_CODE_SMS_NEWS).execute();
                else
                    new doUpdateSettingsTask(getActivity(), Constant.ACTION_NOTIFICATION_SETTINGS, Constant.ACTION_NON_ACTIVE, Constant.MSG_CODE_SMS_NEWS).execute();
            }
        });
    }

    /**
     * send Update Settings to server
     *
     * @modify 16/10/2018 by nn.tai
     */
    private class doUpdateSettingsTask extends AsyncTask<Void, Void, Response<CPSubmitFormResponse>> {

        Context context;
        String action, active, messageCode;

        public doUpdateSettingsTask(Context context, String Action, String Active, String messageCode) {
            this.context = context;
            this.action = Action;
            this.active = Active;
            this.messageCode = messageCode;
        }

        @Override
        protected Response<CPSubmitFormResponse> doInBackground(Void... voids) {
            Response<CPSubmitFormResponse> res = null;

            try {
                CPSubmitFormRequest data = new CPSubmitFormRequest();

                if (!TextUtils.isEmpty(CustomPref.getUserName(context)))
                    data.setUserLogin(CustomPref.getUserName(context));
                else
                    data.setUserLogin(CustomPref.getUserID(context));
                data.setClientID(CustomPref.getUserID(context));

                data.setAPIToken(CustomPref.getAPIToken(context));
                data.setDeviceId(Utilities.getDeviceID(context));
                data.setOS(Utilities.getDeviceOS());
                data.setProject(Constant.Project_ID);
                data.setAuthentication(Constant.Project_Authentication);

                data.setAction(action);
                data.setActive(active);
                data.setMessageCode(messageCode);

                BaseRequest request = new BaseRequest();
                request.setJsonDataInput(data);

                Call<CPSubmitFormResponse> call = svRequester.SubmitFormContact(request);
                res = call.execute();

            } catch (IOException e) {
                myLog.printTrace(e);
            }

            return res;
        }

        @Override
        protected void onPostExecute(Response<CPSubmitFormResponse> response) {
            super.onPostExecute(response);

            if (response != null)
                if (response.isSuccessful()) {
                    CPSubmitFormResponse rp = response.body();
                    if (rp != null) {
                        CPSubmitFormResult result = rp.getResponse();
                        if (result != null) {

                            if (result.getResult().equalsIgnoreCase("true")) {


                            } else {

                                myLog.e(TAG, "CPSubmit Settings Not Success " + result.getErrLog());
                            }
                        }
                    }
                } else {
                    myLog.e(TAG, "CPSubmit Settings Request Failed " + response.errorBody());
                }
        }
    }
}
