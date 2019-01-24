package com.dlvn.mcustomerportal.afragment;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.activity.HomeActivity;
import com.dlvn.mcustomerportal.activity.ListOfficeActivity;
import com.dlvn.mcustomerportal.activity.prototype.NotificationDetailActivity;
import com.dlvn.mcustomerportal.adapter.NotificationListAdapter;
import com.dlvn.mcustomerportal.adapter.model.NotificationModel;
import com.dlvn.mcustomerportal.common.Constant;
import com.dlvn.mcustomerportal.common.CustomPref;
import com.dlvn.mcustomerportal.services.ServicesGenerator;
import com.dlvn.mcustomerportal.services.ServicesRequest;
import com.dlvn.mcustomerportal.services.model.BaseRequest;
import com.dlvn.mcustomerportal.services.model.request.CPSubmitFormRequest;
import com.dlvn.mcustomerportal.services.model.request.GetClientProfileByCLIIDRequest;
import com.dlvn.mcustomerportal.services.model.response.CPSubmitFormResponse;
import com.dlvn.mcustomerportal.services.model.response.CPSubmitFormResult;
import com.dlvn.mcustomerportal.services.model.response.ClientProfile;
import com.dlvn.mcustomerportal.services.model.response.GetClientProfileByCLIIDResponse;
import com.dlvn.mcustomerportal.services.model.response.GetClientProfileByCLIIDResult;
import com.dlvn.mcustomerportal.utils.CPSaveLogTask;
import com.dlvn.mcustomerportal.utils.Utilities;
import com.dlvn.mcustomerportal.utils.myLog;
import com.dlvn.mcustomerportal.view.MyCustomDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Response;

public class NotificationsFragment extends Fragment {

    ListView lvNotification;
    TextView tvNotNoti;
    SwipeRefreshLayout swipeContainer;
    Button btnNoti, btnMailbox;

    View view;

    NotificationListAdapter adapter;
    List<NotificationModel> lstNotification, lstMailbox;
    String dateGet = "1900-01-01";
    SimpleDateFormat format;

    ServicesRequest svRequester;

    public NotificationsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of this fragment using
     * the provided parameters.
     *
     * @return A new instance of fragment NotificationsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotificationsFragment newInstance(String param1, String param2) {
        NotificationsFragment fragment = new NotificationsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
        svRequester = ServicesGenerator.createService(ServicesRequest.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_notifications, container, false);

            swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
            lvNotification = (ListView) view.findViewById(R.id.lvNotification);
            btnNoti = view.findViewById(R.id.btnNotification);
            btnMailbox = view.findViewById(R.id.btnMailbox);

            tvNotNoti = (TextView) view.findViewById(R.id.tvNotNoti);

            initData();
            setListener();
        }

        return view;
    }

    private void initData() {

        format = new SimpleDateFormat("yyyy-mm-dd");
        Calendar cal = Calendar.getInstance();
        dateGet = format.format(cal.getTime());

        btnNoti.setSelected(true);
        btnMailbox.setSelected(false);

        lstNotification = new ArrayList<>();
        lstMailbox = new ArrayList<>();

        if (adapter == null)
            adapter = new NotificationListAdapter(getActivity(), R.layout.item_notification, lstNotification);
        lvNotification.setAdapter(adapter);

        swipeContainer.setRefreshing(false);

        new GetNotificationTask(getActivity(), Constant.ACTION_NOTIFICATION).execute();
    }

    private void initDataNotification() {
        if (lstNotification.size() > 0) {
            adapter.reFreshData(lstNotification);
            lvNotification.setVisibility(View.VISIBLE);
            tvNotNoti.setVisibility(View.GONE);
        } else {
            lvNotification.setVisibility(View.GONE);
            tvNotNoti.setVisibility(View.VISIBLE);
            new GetNotificationTask(getActivity(), Constant.ACTION_NOTIFICATION).execute();
        }
        swipeContainer.setRefreshing(false);

    }

    private void initMailbox() {
        if (lstMailbox.size() > 0) {
            adapter.reFreshData(lstMailbox);
            lvNotification.setVisibility(View.VISIBLE);
            tvNotNoti.setVisibility(View.GONE);
        } else {
            lvNotification.setVisibility(View.GONE);
            tvNotNoti.setVisibility(View.VISIBLE);
            new GetNotificationTask(getActivity(), Constant.ACTION_MAILBOX).execute();
        }
        swipeContainer.setRefreshing(false);
    }

    private void setListener() {
        swipeContainer.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh() {
                // TODO Auto-generated method stub
                if (btnNoti.isSelected())
                    new GetNotificationTask(getActivity(), Constant.ACTION_NOTIFICATION).execute();
                else
                    new GetNotificationTask(getActivity(), Constant.ACTION_MAILBOX).execute();
            }
        });

        btnNoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnNoti.setSelected(true);
                btnMailbox.setSelected(false);
                initDataNotification();
            }
        });

        btnMailbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnNoti.setSelected(false);
                btnMailbox.setSelected(true);
                initMailbox();
            }
        });

        lvNotification.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                NotificationModel mo = (NotificationModel) adapterView.getAdapter().getItem(i);
                if (mo != null) {
                    Intent intent = new Intent(getActivity(), NotificationDetailActivity.class);
                    intent.putExtra(NotificationDetailActivity.INT_DETAILS_INFO, mo);
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * Task get Mailbox, Notificaltion
     */
    private class GetNotificationTask extends AsyncTask<Void, Void, Response<GetClientProfileByCLIIDResponse>> {

        Context context;
        String action;

        public GetNotificationTask(Context c, String act) {
            this.context = c;
            this.action = act;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            swipeContainer.setRefreshing(true);
        }

        @Override
        protected Response<GetClientProfileByCLIIDResponse> doInBackground(Void... voids) {
            Response<GetClientProfileByCLIIDResponse> response = null;

            try {
                GetClientProfileByCLIIDRequest data = new GetClientProfileByCLIIDRequest();
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

                data.setEmail(CustomPref.getEmail(context));
                data.setAction(action);
                data.setCreatedDate(dateGet);

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
            swipeContainer.setRefreshing(false);

            if (response != null)
                if (response.isSuccessful()) {
                    GetClientProfileByCLIIDResponse rp = response.body();
                    if (rp != null) {
                        GetClientProfileByCLIIDResult result = rp.getResponse();
                        if (result != null) {
                            if (result.getResult().equalsIgnoreCase("true")) {
                                if (result.getClientProfile() != null) {
                                    List<ClientProfile> lst = result.getClientProfile();
                                    if (action.equalsIgnoreCase(Constant.ACTION_NOTIFICATION)) {

                                        if (lstNotification.size() > 0)
                                            lstNotification.clear();

                                        for (int i = 0; i < lst.size(); i++) {
                                            lstNotification.add(new NotificationModel(lst.get(i).getSubject(), lst.get(i).getContent(), lst.get(i).getCreatedDate(), false));
                                        }

                                        adapter.setData(lstNotification);
                                        if (lstNotification.size() > 0) {
                                            lvNotification.setVisibility(View.VISIBLE);
                                            tvNotNoti.setVisibility(View.GONE);
                                        } else {
                                            lvNotification.setVisibility(View.GONE);
                                            tvNotNoti.setVisibility(View.VISIBLE);
                                        }

                                    } else if (action.equalsIgnoreCase(Constant.ACTION_MAILBOX)) {
                                        lstMailbox = new ArrayList<>();

                                        if (lstMailbox.size() > 0)
                                            lstMailbox.clear();

                                        for (int i = 0; i < lst.size(); i++) {
                                            lstMailbox.add(new NotificationModel(lst.get(i).getSubject(), lst.get(i).getContent(), lst.get(i).getCreatedDate(), false));
                                        }
                                        adapter.setData(lstMailbox);
                                        if (lstMailbox.size() > 0) {
                                            lvNotification.setVisibility(View.VISIBLE);
                                            tvNotNoti.setVisibility(View.GONE);
                                        } else {
                                            lvNotification.setVisibility(View.GONE);
                                            tvNotNoti.setVisibility(View.VISIBLE);
                                        }
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
    public void onAttach(Context context) {
        super.onAttach(context);
        // if (context instanceof OnFragmentInteractionListener) {
        // mListener = (OnFragmentInteractionListener) context;
        // } else {
        // throw new RuntimeException(context.toString()
        // + " must implement OnFragmentInteractionListener");
        // }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();
        new CPSaveLogTask(getActivity(), Constant.CPLOG_NOTIFICATIONS_OPEN).execute();
    }

    @Override
    public void onStop() {
        super.onStop();
        new CPSaveLogTask(getActivity(), Constant.CPLOG_NOTIFICATIONS_CLOSE).execute();
    }
}
