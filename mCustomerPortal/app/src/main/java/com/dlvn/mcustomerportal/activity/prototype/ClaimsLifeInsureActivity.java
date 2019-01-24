package com.dlvn.mcustomerportal.activity.prototype;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSeekBar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.adapter.SingleListAdapter;
import com.dlvn.mcustomerportal.adapter.model.NotificationModel;
import com.dlvn.mcustomerportal.adapter.model.SingleChoiceModel;
import com.dlvn.mcustomerportal.base.BaseActivity;
import com.dlvn.mcustomerportal.common.Constant;
import com.dlvn.mcustomerportal.common.CustomPref;
import com.dlvn.mcustomerportal.services.ServicesGenerator;
import com.dlvn.mcustomerportal.services.ServicesRequest;
import com.dlvn.mcustomerportal.services.model.BaseRequest;
import com.dlvn.mcustomerportal.services.model.claims.ClaimsFromRequest;
import com.dlvn.mcustomerportal.services.model.request.GetClientProfileByCLIIDRequest;
import com.dlvn.mcustomerportal.services.model.response.ClientProfile;
import com.dlvn.mcustomerportal.services.model.response.GetClientProfileByCLIIDResponse;
import com.dlvn.mcustomerportal.services.model.response.GetClientProfileByCLIIDResult;
import com.dlvn.mcustomerportal.utils.Utilities;
import com.dlvn.mcustomerportal.utils.myLog;
import com.dlvn.mcustomerportal.view.MyCustomDialog;
import com.dlvn.mcustomerportal.view.clearable_edittext.ClearableEditText;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class ClaimsLifeInsureActivity extends BaseActivity {

    private static final String TAG = "ClaimsLifeInsureActivity";

    LinearLayout lloBack, lloLifeInsure, lloDetail;
    TextView tvFullName, tvCMND, tvClaimsDate, tvNgayCapCMND;
    ClearableEditText cedtNoiCapCMND, cedtAlias, cedtPermanentAddress, cedtOccuspation, cedtWorkPlace, cedtMedicalInsurance, cedtRegisPlace;

    ListView lvData;
    SingleListAdapter adapter;
    List<SingleChoiceModel> lsData;

    AppCompatSeekBar sbStep;
    Button btnCapNhat;
    TextView tvStep;

    SingleChoiceModel lifeInsureSelected = null;
    boolean isDetail = false;
    ServicesRequest svRequester;
    ClaimsFromRequest claimsFromRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claims_lifeinsured);

        svRequester = ServicesGenerator.createService(ServicesRequest.class);

        lloBack = findViewById(R.id.lloBack);
        lloBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDetail) {
                    lloDetail.setVisibility(View.GONE);
                    lloLifeInsure.setVisibility(View.VISIBLE);
                    isDetail = false;
                } else
                    onBackPressed();
            }
        });

        getViews();
        initData();
        setListener();
    }

    private void getViews() {
        lloLifeInsure = findViewById(R.id.lloLifeInsure);
        lloDetail = findViewById(R.id.lloDetail);
        lloDetail.setVisibility(View.GONE);

        tvFullName = findViewById(R.id.tvFullName);
        tvCMND = findViewById(R.id.tvCMND);
        tvClaimsDate = findViewById(R.id.tvClaimsDate);
        tvNgayCapCMND = findViewById(R.id.tvNgayCapCMND);

        cedtNoiCapCMND = findViewById(R.id.cedtNoiCapCMND);
        cedtAlias = findViewById(R.id.cedtAlias);
        cedtPermanentAddress = findViewById(R.id.cedtPermanentAddress);
        cedtOccuspation = findViewById(R.id.cedtOccuspation);
        cedtWorkPlace = findViewById(R.id.cedtWorkPlace);
        cedtMedicalInsurance = findViewById(R.id.cedtMedicalInsurance);
        cedtRegisPlace = findViewById(R.id.cedtRegisPlace);

        tvStep = findViewById(R.id.tvStep);
        sbStep = findViewById(R.id.sbStep);
        sbStep.setThumb(null);
        sbStep.setEnabled(false);
        sbStep.setMax(4);
        sbStep.setProgress(1);

        btnCapNhat = findViewById(R.id.btnCapNhat);
        lvData = findViewById(R.id.lvData);
    }

    private void initData() {

        claimsFromRequest = CustomPref.getClaimsTemp(this);
        if (claimsFromRequest == null) {
            MyCustomDialog dialog = new MyCustomDialog.Builder(this)
                    .setMessage(getString(R.string.alert_claims_no_requester_info))
                    .setPositiveButton(getString(R.string.confirm_ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            btnCapNhat.setActivated(false);
                            dialog.dismiss();
                        }
                    }).create();
            dialog.show();
        }

        if (!TextUtils.isEmpty(claimsFromRequest.getLiClientID())) {
            lifeInsureSelected = new SingleChoiceModel(0, claimsFromRequest.getLiClientID(), claimsFromRequest.getLIFullName(), claimsFromRequest.getLIiDNum(), false);
            isDetail = true;

            viewDetail();
        } else
            //get list Life Insured
            new GetLifeInsuredListTask(this).execute();
    }

    private void setListener() {
        tvClaimsDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                DatePickerDialog dialog = new DatePickerDialog(ClaimsLifeInsureActivity.this,
                        android.R.style.Theme_Material_Light_Dialog_Alert, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        cal.set(year, month, dayOfMonth);
                        tvClaimsDate.setText(dateFormat.format(cal.getTime()));
                    }
                }, year, month, day);
                dialog.show();
            }
        });

        tvNgayCapCMND.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                DatePickerDialog dialog = new DatePickerDialog(ClaimsLifeInsureActivity.this,
                        android.R.style.Theme_Material_Light_Dialog_Alert, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        cal.set(year, month, dayOfMonth);
                        tvNgayCapCMND.setText(dateFormat.format(cal.getTime()));
                    }
                }, year, month, day);
                dialog.show();
            }
        });

        btnCapNhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDetail && lifeInsureSelected != null) {

                    claimsFromRequest.setLiClientID(lifeInsureSelected.getCode());
                    claimsFromRequest.setLifeInsuredID(lifeInsureSelected.getCode());
                    claimsFromRequest.setLIFullName(lifeInsureSelected.getName());
                    claimsFromRequest.setLIiDNum(lifeInsureSelected.getSubname());

                    claimsFromRequest.setAliasName(cedtAlias.getText().toString());
                    claimsFromRequest.setPermanentAddress(cedtPermanentAddress.getText().toString());
                    claimsFromRequest.setOccupation(cedtOccuspation.getText().toString());
                    claimsFromRequest.setWorkPlace(cedtWorkPlace.getText().toString());
                    claimsFromRequest.setMedicInsurance(cedtMedicalInsurance.getText().toString());
                    claimsFromRequest.setRegistrationPlace(cedtRegisPlace.getText().toString());

                    //save temp claims
                    CustomPref.saveClaimsTemp(ClaimsLifeInsureActivity.this, claimsFromRequest);

                    Intent intent = new Intent(ClaimsLifeInsureActivity.this, ClaimsInsuranceBenefitActivity.class);
//                    intent.putExtra(Constant.CLAIMS_INTKEY_MAIN_REQUEST, claimsFromRequest);
                    startActivity(intent);
                }
            }
        });

        lvData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                lifeInsureSelected = adapter.getItem(position);
                isDetail = true;
                viewDetail();
            }
        });
    }

    private void viewDetail() {
        if (lifeInsureSelected != null) {
            lloDetail.setVisibility(View.VISIBLE);
            lloLifeInsure.setVisibility(View.GONE);

            tvFullName.setText(lifeInsureSelected.getName().trim());
            tvCMND.setText(lifeInsureSelected.getSubname().trim());

//            tvNgayCapCMND.setText(claimsFromRequest.getDateIssueOfIDNum());
//            cedtNoiCapCMND.setText(claimsFromRequest.getPlaceIssueOfIDNum());
            cedtAlias.setText(claimsFromRequest.getAliasName());
            cedtOccuspation.setText(claimsFromRequest.getOccupation());
            cedtWorkPlace.setText(claimsFromRequest.getWorkPlace());
            cedtPermanentAddress.setText(claimsFromRequest.getPermanentAddress());
            cedtMedicalInsurance.setText(claimsFromRequest.getMedicInsurance());
            cedtRegisPlace.setText(claimsFromRequest.getRegistrationPlace());
        }
    }

    /**
     * Task get LifeInsuredList
     */
    private class GetLifeInsuredListTask extends AsyncTask<Void, Void, Response<GetClientProfileByCLIIDResponse>> {

        Context context;
        ProgressDialog process;

        public GetLifeInsuredListTask(Context c) {
            this.context = c;
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (process == null)
                process = new ProgressDialog(context);
            process.setIndeterminate(false);
            process.setIndeterminateDrawable(getDrawable(R.drawable.dialog_progress_cercle));
            process.show();
        }

        @Override
        protected Response<GetClientProfileByCLIIDResponse> doInBackground(Void... voids) {
            Response<GetClientProfileByCLIIDResponse> response = null;

            try {
                GetClientProfileByCLIIDRequest data = new GetClientProfileByCLIIDRequest();

                if (TextUtils.isEmpty(CustomPref.getUserName(context)))
                    data.setUserLogin(CustomPref.getUserID(context));
                else
                    data.setUserLogin(CustomPref.getUserName(context));
                data.setClientID(CustomPref.getUserID(context));
                data.setAPIToken(CustomPref.getAPIToken(context));
                data.setDeviceId(Utilities.getDeviceID(context));
                data.setOS(Utilities.getDeviceOS());
                data.setProject(Constant.Project_ID);
                data.setAuthentication(Constant.Project_Authentication);

                data.setAction(Constant.CLAIMS_ACTION_LIFEINSURED_LIST);

                BaseRequest request = new BaseRequest();
                request.setJsonDataInput(data);

                Call<GetClientProfileByCLIIDResponse> call = svRequester.GetClientProfileByCLIID(request);
                response = call.execute();

            } catch (IOException e) {
                myLog.printTrace(e);
            }

            return response;
        }

        @Override
        protected void onPostExecute(Response<GetClientProfileByCLIIDResponse> response) {
            super.onPostExecute(response);
            process.dismiss();

            if (response != null)
                if (response.isSuccessful()) {
                    GetClientProfileByCLIIDResponse rp = response.body();
                    if (rp != null) {
                        GetClientProfileByCLIIDResult result = rp.getResponse();
                        if (result != null) {
                            if (result.getResult().equalsIgnoreCase("true")) {
                                if (result.getClientProfile() != null) {
                                    List<ClientProfile> lst = result.getClientProfile();
                                    if (lst != null) {
                                        lsData = new ArrayList<>();
                                        for (int i = 0; i < lst.size(); i++) {
                                            lsData.add(new SingleChoiceModel(0, lst.get(i).getClientID(), lst.get(i).getFullName(), lst.get(i).getPOID(), false));
                                        }

                                        adapter = new SingleListAdapter(context, 0, lsData);
                                        lvData.setAdapter(adapter);
                                    }
                                }
                            } else {
                                if (result.getNewAPIToken().equalsIgnoreCase(Constant.ERROR_TOKENINVALID)) {
                                    Utilities.processLoginAgain(context, getString(R.string.message_alert_relogin));
                                } else {
                                    MyCustomDialog dialog = new MyCustomDialog.Builder(context)
                                            .setMessage("Yêu cầu đã được gửi không thành công! Xin vui lòng thử lại.")
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
                    }
                }
        }

    }

    @Override
    public void onBackPressed() {
        if (isDetail) {
            lloDetail.setVisibility(View.GONE);
            lloLifeInsure.setVisibility(View.VISIBLE);
            isDetail = false;
        } else
            super.onBackPressed();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View v = getCurrentFocus();
        if (v != null && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE)
                && v instanceof EditText) {
            int scrcoords[] = new int[2];
            v.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + v.getLeft() - scrcoords[0];
            float y = ev.getRawY() + v.getTop() - scrcoords[1];

            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom())
                Utilities.hideSoftInputKeyboard(this, v);
        }

        return super.dispatchTouchEvent(ev);
    }
}
