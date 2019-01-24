package com.dlvn.mcustomerportal.afragment.prototype;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.ActionMenuView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dlvn.mcustomerportal.BuildConfig;
import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.activity.prototype.SettingsActivity;
import com.dlvn.mcustomerportal.common.Constant;
import com.dlvn.mcustomerportal.common.CustomPref;
import com.dlvn.mcustomerportal.services.ServicesGenerator;
import com.dlvn.mcustomerportal.services.ServicesRequest;
import com.dlvn.mcustomerportal.services.model.BaseRequest;
import com.dlvn.mcustomerportal.services.model.request.GetPointByCLIIDRequest;
import com.dlvn.mcustomerportal.services.model.response.ClientProfile;
import com.dlvn.mcustomerportal.services.model.response.GetPointByCLIIDResponse;
import com.dlvn.mcustomerportal.services.model.response.GetPointByCLIIDResult;
import com.dlvn.mcustomerportal.utils.CPSaveLogTask;
import com.dlvn.mcustomerportal.utils.Utilities;
import com.dlvn.mcustomerportal.utils.myLog;
import com.dlvn.mcustomerportal.view.MyCustomDialog;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class SettingAccountFragment extends Fragment {

    View v;
    ImageView imvAvatar, imvAddAvatar;
    TextView tvFullName, tvUserName, tvUserID, tvGender, tvPoints, tvRank;

    ActionMenuView actionMenu;
    TextView tvEdit;

    ServicesRequest svRequester;
    Uri pathCache = null;

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

            imvAvatar = v.findViewById(R.id.imvAvatar);
            imvAddAvatar = v.findViewById(R.id.imvAddAvatar);

            tvFullName = v.findViewById(R.id.tvFullname);
            tvUserName = v.findViewById(R.id.tvUserName);
            tvUserID = v.findViewById(R.id.tvUserID);
            tvGender = v.findViewById(R.id.tvGender);
            tvPoints = v.findViewById(R.id.tvPoints);
            tvRank = v.findViewById(R.id.tvRank);

            actionMenu = getActivity().findViewById(R.id.toolbar_menu);
            if (actionMenu != null) {
            }
            tvEdit = getActivity().findViewById(R.id.tvEdit);
            if (tvEdit != null)
                tvEdit.setVisibility(View.VISIBLE);

            initDatas();
        }

        return v;
    }

    private void initDatas() {
        if (CustomPref.haveLogin(getActivity())) {
            tvFullName.setText(CustomPref.getFullName(getActivity()));
            tvUserName.setText(CustomPref.getFullName(getActivity()));
            if (!TextUtils.isEmpty(CustomPref.getUserID(getActivity()))) {
                tvUserID.setText(CustomPref.getUserID(getActivity()));
                new getClientPointTask(getActivity()).execute();
            } else
                tvUserID.setText(CustomPref.getUserName(getActivity()));

            if (CustomPref.getGender(getActivity()).equals("F"))
                tvGender.setText("Nữ");
            else
                tvGender.setText("Nam");

            tvPoints.setText(CustomPref.getUserPoint(getActivity()) + "");

            String rank = CustomPref.getUserRank(getActivity());
            if (rank.equalsIgnoreCase("NORANK") || rank.equalsIgnoreCase("NOVIP"))
                tvRank.setText("");
            else
                tvRank.setText(rank);

            myLog.e("User Avatar = " + CustomPref.getUserPhoto(getActivity()));
            if (!TextUtils.isEmpty(CustomPref.getUserPhoto(getActivity()))) {
                Glide.with(getActivity()).load(CustomPref.getUserPhoto(getActivity())).apply(RequestOptions.circleCropTransform()).into(imvAvatar);
            }
        }

        imvAddAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogPickImage(getActivity());
            }
        });

        if (tvEdit != null)
            tvEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClientProfile user = CustomPref.getUserLogin(getActivity());

                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    UpdateAccountFragment fragment = UpdateAccountFragment.newInstance(user, false);
                    fragmentTransaction.replace(R.id.main_container, fragment);
                    fragmentTransaction.commit();

                }
            });
    }

    private void showDialogPickImage(final Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_pick_image);
        dialog.setCanceledOnTouchOutside(true);

        Button btnCamera = dialog.findViewById(R.id.btnCamera);
        Button btnGallery = dialog.findViewById(R.id.btnGallery);
        RelativeLayout rllQuayLai = dialog.findViewById(R.id.rllQuayLai);

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String file = Calendar.getInstance().getTimeInMillis() + ".jpg";
                File newfile = null;
                try {
                    newfile = new File(getActivity().getExternalCacheDir().getPath() + "/" + file);
                    newfile.createNewFile();
                } catch (IOException e) {
                    myLog.printTrace(e);
                }
                pathCache = FileProvider.getUriForFile(getActivity(),
                        BuildConfig.APPLICATION_ID + ".provider", newfile);

                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                takePicture.putExtra(MediaStore.EXTRA_OUTPUT, pathCache);
                startActivityForResult(takePicture, Constant.REQUEST_CODE_CAMERA);
                dialog.dismiss();
            }
        });

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, Constant.REQUEST_CODE_GALLERY);
                dialog.dismiss();
            }
        });

        rllQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constant.REQUEST_CODE_CAMERA:
                if (resultCode == Activity.RESULT_OK) {
                    if (pathCache != null) {
                        Glide.with(getActivity()).load(pathCache).apply(RequestOptions.circleCropTransform()).into(imvAvatar);
                        CustomPref.setUserPhoto(getActivity(), pathCache.toString());
                    }
                }

                break;
            case Constant.REQUEST_CODE_GALLERY:
                if (resultCode == Activity.RESULT_OK) {
                    Uri selectedImage = data.getData();
                    if (selectedImage != null) {
                        Glide.with(getActivity()).load(selectedImage).apply(RequestOptions.circleCropTransform()).into(imvAvatar);
                        CustomPref.setUserPhoto(getActivity(), selectedImage.toString());
                    }
                }
                break;
        }
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
                if (!TextUtils.isEmpty(CustomPref.getUserName(context)))
                    data.setUserLogin(CustomPref.getUserName(context));
                else
                    data.setUserLogin(CustomPref.getUserID(context));

                data.setAPIToken(CustomPref.getAPIToken(context));
                data.setDeviceID(Utilities.getDeviceID(context));
                data.setOS(Utilities.getDeviceOS());
                data.setProject(Constant.Project_ID);
                data.setAuthentication(Constant.Project_Authentication);

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
                                        myLog.e("SettingAccountFragment", "Get Point: " + result.getErrLog());

                                        if (result.getNewAPIToken().equalsIgnoreCase(Constant.ERROR_TOKENINVALID)) {
                                            Utilities.processLoginAgain(context, getString(R.string.message_alert_relogin));
                                        }

                                    } else if (result.getResult() != null && result.getResult().equals("true")) {

                                        //Save Token
                                        if (!TextUtils.isEmpty(result.getNewAPIToken()))
                                            CustomPref.saveAPIToken(context, result.getNewAPIToken());

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
        new CPSaveLogTask(getActivity(), Constant.CPLOG_SETTINGACCOUNT_OPEN).execute();
    }

    @Override
    public void onStop() {
        super.onStop();
        new CPSaveLogTask(getActivity(), Constant.CPLOG_SETTINGACCOUNT_CLOSE).execute();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
