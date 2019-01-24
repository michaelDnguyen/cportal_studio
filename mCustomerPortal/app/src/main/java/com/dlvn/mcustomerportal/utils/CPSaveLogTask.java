package com.dlvn.mcustomerportal.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.dlvn.mcustomerportal.common.Constant;
import com.dlvn.mcustomerportal.common.CustomPref;
import com.dlvn.mcustomerportal.services.ServicesGenerator;
import com.dlvn.mcustomerportal.services.ServicesRequest;
import com.dlvn.mcustomerportal.services.model.BaseRequest;
import com.dlvn.mcustomerportal.services.model.request.CPSaveLogRequest;
import com.dlvn.mcustomerportal.services.model.response.GetTaxInvoiceResponse;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Save log to server for Tracking
 *
 * @author nn.tai
 * @modify 22-10-2018
 */
public class CPSaveLogTask extends AsyncTask<Void, Void, Response<GetTaxInvoiceResponse>> {

    private static final String TAG = "SaveLogTask";

    Context context;
    ServicesRequest svRequester;
    String status;

    public CPSaveLogTask(Context context, String status) {
        this.context = context;
        this.status = status;
        svRequester = ServicesGenerator.createService(ServicesRequest.class);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Response<GetTaxInvoiceResponse> doInBackground(Void... voids) {
        Response<GetTaxInvoiceResponse> res = null;

        if (!TextUtils.isEmpty(CustomPref.getUserID(context))) {
            try {
                CPSaveLogRequest data = new CPSaveLogRequest();

                data.setDeviceToken(CustomPref.getFirebaseToken(context));
                data.setDeviceID(Utilities.getDeviceID(context));
                data.setOS(Utilities.getDeviceOS());
                data.setProject(Constant.Project_ID);
                data.setAuthentication(Constant.Project_Authentication);
                data.setAPIToken(CustomPref.getAPIToken(context));

                data.setClientID(CustomPref.getUserID(context));
                if (!TextUtils.isEmpty(CustomPref.getUserName(context)))
                    data.setUserLogin(CustomPref.getUserName(context));
                else
                    data.setUserLogin(CustomPref.getUserID(context));
                data.setFunction(status);

                BaseRequest request = new BaseRequest();
                request.setJsonDataInput(data);

                Call<GetTaxInvoiceResponse> call = svRequester.CPSaveLog(request);
                res = call.execute();

            } catch (Exception e) {
                myLog.printTrace(e);
                return null;
            }
        }

        return res;
    }

    @Override
    protected void onPostExecute(Response<GetTaxInvoiceResponse> res) {
        super.onPostExecute(res);

        if (res != null) {
            if (res.isSuccessful()) {
                myLog.e(TAG, "send Log success!");
            }
        } else
            myLog.e(TAG, "send Log failed!");
    }
}
