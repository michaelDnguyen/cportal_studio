package com.dlvn.mcustomerportal.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.activity.prototype.DashboardActivity;
import com.dlvn.mcustomerportal.base.BaseActivity;
import com.dlvn.mcustomerportal.common.Constant;
import com.dlvn.mcustomerportal.common.CustomPref;
import com.dlvn.mcustomerportal.services.GoogleServiceGenerator;
import com.dlvn.mcustomerportal.services.GoogleServiceRequest;
import com.dlvn.mcustomerportal.services.NetworkUtils;
import com.dlvn.mcustomerportal.services.ServicesGenerator;
import com.dlvn.mcustomerportal.services.ServicesRequest;
import com.dlvn.mcustomerportal.services.model.BaseRequest;
import com.dlvn.mcustomerportal.services.model.request.CPSubmitFormRequest;
import com.dlvn.mcustomerportal.services.model.response.CPSubmitFormResponse;
import com.dlvn.mcustomerportal.services.model.response.CPSubmitFormResult;
import com.dlvn.mcustomerportal.services.model.response.SiteVerifyResponse;
import com.dlvn.mcustomerportal.utils.CPSaveLogTask;
import com.dlvn.mcustomerportal.utils.Utilities;
import com.dlvn.mcustomerportal.utils.myLog;
import com.dlvn.mcustomerportal.view.MyCustomDialog;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

public class ContactActivity extends BaseActivity {

    private static final String TAG = "ContactActivity";

    LinearLayout lloCall;
    Spinner spnLoaiLienHe;

    private TextView tvHotLine;
    ImageView imvYoutube, imvFacebook, imvMail, imvLogoDaiIChi;
    ImageView imvRecaptcha;

    Button btnBack, btnSubmit;
    EditText edtEmail, edtName, edtContent;

    ProgressBar processBar;

    ServicesRequest svRequester;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        getViews();
        initDatas();
        setListener();
    }

    private void getViews() {
//		// TODO Auto-generated method stub
//		lloCall = (LinearLayout) findViewById(R.id.lloCall);
//		spnLoaiLienHe = (Spinner) findViewById(R.id.spnLoaiLienHe);

        btnBack = findViewById(R.id.btnBack);
        btnSubmit = findViewById(R.id.btnSubmit);

        edtEmail = findViewById(R.id.edtEmail);
        edtName = findViewById(R.id.edtName);
        edtContent = findViewById(R.id.edtContent);

        imvRecaptcha = findViewById(R.id.imvRecaptcha);
        /**
         * Action for youtube, facebook, mail
         */
        tvHotLine = findViewById(R.id.tvHotLine);
        imvYoutube = findViewById(R.id.imvYoutube);
        imvFacebook = findViewById(R.id.imvFacebook);
        imvMail = findViewById(R.id.imvMail);
        imvLogoDaiIChi = findViewById(R.id.imvLogoDaiIChi);


        tvHotLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyCustomDialog dialog = new MyCustomDialog.Builder(ContactActivity.this)
                        .setMessage(getString(R.string.message_conform_call_customer_service))
                        .setPositiveButton(getString(R.string.confirm_yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Utilities.actionCallPhoneNumber(ContactActivity.this, Constant.PHONE_CUSTOMER_SERVICE);
                            }
                        }).setNegativeButton(getString(R.string.confirm_no), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create();
                dialog.show();
            }
        });
        imvMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilities.actionOpenMailApp(ContactActivity.this);
            }
        });

        imvYoutube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilities.actionOpenYoutubeApp(ContactActivity.this);
            }
        });

        imvFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilities.actionOpenFacebookApp(ContactActivity.this);
            }
        });

        imvLogoDaiIChi.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactActivity.this, DashboardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

    }

    private void initDatas() {
//		// TODO Auto-generated method stub
//		List<String> list = new ArrayList<String>();
//		list.add("Yêu cầu thu phí");
//		list.add("Yêu cầu thay đổi địa chỉ");
//		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
//		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		spnLoaiLienHe.setAdapter(dataAdapter);

        if (CustomPref.haveLogin(this)) {
            edtEmail.setText(CustomPref.getEmail(this));
            edtName.setText(CustomPref.getFullName(this));
            edtContent.requestFocus();
        }

        svRequester = ServicesGenerator.createService(ServicesRequest.class);
    }

    private void setListener() {
        // TODO Auto-generated method stub
//		lloCall.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				Utilities.actionCallPhoneNumber(ContactActivity.this, Constant.PHONE_CUSTOMER_SERVICE);
//			}
//		});

        btnSubmit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString();
                String name = edtName.getText().toString();
                String content = edtContent.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    edtEmail.setError("Bạn cần nhập địa chỉ email");
                    edtEmail.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(name)) {
                    edtName.setError("Bạn cần nhập tên yêu cầu");
                    edtName.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(content)) {
                    edtContent.setError("Bạn cần điền nội dung yêu cầu");
                    edtContent.requestFocus();
                    return;
                }

                if (NetworkUtils.isConnectedHaveDialog(ContactActivity.this))
                    verifyWithReCaptcha(email, name, content);
            }
        });

        btnBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private class doSubmitForm extends AsyncTask<Void, Void, Response<CPSubmitFormResponse>> {

        Context context;
        String email, name, content;

        public doSubmitForm(Context c, String mail, String name, String ct) {
            this.context = c;
            this.email = mail;
            this.name = name;
            this.content = ct;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (processBar == null)
                processBar = new ProgressBar(context, null, android.R.attr.progressBarStyleSmall);
            processBar.setIndeterminate(true);
            processBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Response<CPSubmitFormResponse> doInBackground(Void... voids) {
            Response<CPSubmitFormResponse> response = null;

            try {
                CPSubmitFormRequest data = new CPSubmitFormRequest();

                data.setUserLogin(CustomPref.getUserID(context));

                data.setClientID(CustomPref.getUserID(context));
                data.setAPIToken(CustomPref.getAPIToken(context));
                data.setDeviceId(Utilities.getDeviceID(context));
                data.setOS(Utilities.getDeviceOS());
                data.setProject(Constant.Project_ID);
                data.setAuthentication(Constant.Project_Authentication);

                data.setEmail(email);
                data.setContactContent(content);
                data.setContactType(name);
                data.setAction(Constant.ACTION_CONTACT);


                BaseRequest request = new BaseRequest();
                request.setJsonDataInput(data);

                Call<CPSubmitFormResponse> call = svRequester.SubmitFormContact(request);
                response = call.execute();

            } catch (IOException e) {
                myLog.printTrace(e);
            }

            return response;
        }

        @Override
        protected void onPostExecute(Response<CPSubmitFormResponse> response) {
            super.onPostExecute(response);
            processBar.setVisibility(View.GONE);
            if (response != null)
                if (response.isSuccessful()) {
                    CPSubmitFormResponse rp = response.body();
                    if (rp != null) {
                        CPSubmitFormResult result = rp.getResponse();
                        if (result != null) {
                            if (result.getResult().equalsIgnoreCase("true")) {
                                MyCustomDialog dialog = new MyCustomDialog.Builder(context)
                                        .setTitle("Thông báo")
                                        .setMessage("Yêu cầu đã được gửi thành công!")
                                        .setPositiveButton(getString(R.string.confirm_ok), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        }).create();
                                dialog.show();

                            } else {

                                if (result.getNewAPIToken().equalsIgnoreCase(Constant.ERROR_TOKENINVALID)) {
                                    Utilities.processLoginAgain(context, getString(R.string.message_alert_relogin));
                                } else {
                                    MyCustomDialog dialog = new MyCustomDialog.Builder(context)
                                            .setTitle("Thông báo")
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
                } else {

                }
        }
    }

    private void verifyWithReCaptcha(final String email, final String name, final String content) {
        myLog.e(TAG, "verifyWithReCaptcha");
        SafetyNet.getClient(this).verifyWithRecaptcha(getString(R.string.api_sitekey_recaptcha))
                .addOnSuccessListener(this, new OnSuccessListener<SafetyNetApi.RecaptchaTokenResponse>() {
                    @Override
                    public void onSuccess(SafetyNetApi.RecaptchaTokenResponse response) {
                        if (!response.getTokenResult().isEmpty()) {
                            myLog.e(TAG, "Token result " + response.getTokenResult());
                            getSiteVerifyReCaptCha(ContactActivity.this, response.getTokenResult(), email, name, content);
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (e instanceof ApiException) {
                            ApiException apiException = (ApiException) e;
                            myLog.e(TAG, "Error message: " +
                                    CommonStatusCodes.getStatusCodeString(apiException.getStatusCode()));
                        } else {
                            myLog.e(TAG, "Unknown type of error: " + e.getMessage());
                        }
                    }
                });
    }

    private void getSiteVerifyReCaptCha(final Context c, final String responseCaptcha, final String email, final String name, final String content) {
        new AsyncTask<Void, Void, Response<SiteVerifyResponse>>() {

            @Override
            protected Response<SiteVerifyResponse> doInBackground(Void... voids) {

                GoogleServiceGenerator svgenerate = new GoogleServiceGenerator();
                GoogleServiceRequest service = svgenerate.createService(c, GoogleServiceRequest.class);

                Call<SiteVerifyResponse> call = service.getSiteVerifyReCaptCha2(getString(R.string.api_secrectkey_recaptcha), responseCaptcha);
                Response<SiteVerifyResponse> response = null;
                try {
                    response = call.execute();
                } catch (IOException e) {
                    myLog.printTrace(e);
                }
                return response;
            }

            @Override
            protected void onPostExecute(Response<SiteVerifyResponse> result) {
                super.onPostExecute(result);
                if (result != null) {
                    if (result.isSuccessful()) {
                        SiteVerifyResponse rs = result.body();
                        if (rs != null) {
                            if (rs.getSuccess()) {
                                new doSubmitForm(ContactActivity.this, email, name, content).execute();
                            } else {
                                MyCustomDialog dialog = new MyCustomDialog.Builder(c)
                                        .setMessage(getString(R.string.alert_message_verify_captcha))
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
        }.execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        new CPSaveLogTask(this, Constant.CPLOG_SUBMITFORM_OPEN).execute();
    }

    @Override
    protected void onStop() {
        super.onStop();
        new CPSaveLogTask(this, Constant.CPLOG_SUBMITFORM_CLOSE).execute();
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
