package com.dlvn.mcustomerportal.afragment.prototype;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.activity.TransactionDetailActivity;
import com.dlvn.mcustomerportal.adapter.TransactionListAdapter;
import com.dlvn.mcustomerportal.adapter.model.TransactionModel;
import com.dlvn.mcustomerportal.common.CustomPref;
import com.dlvn.mcustomerportal.services.NetworkUtils;
import com.dlvn.mcustomerportal.services.ServicesGenerator;
import com.dlvn.mcustomerportal.services.ServicesRequest;
import com.dlvn.mcustomerportal.services.model.request.HistoryPaymentRequest;
import com.dlvn.mcustomerportal.services.model.response.HistoryPaymentResponse;
import com.dlvn.mcustomerportal.services.model.response.HistoryPaymentResult;
import com.dlvn.mcustomerportal.utils.myLog;
import com.dlvn.mcustomerportal.view.MyCustomDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class SettingTransHistoryFragment extends Fragment {

    View view;

    SwipeRefreshLayout swipeLayout;
    ListView lvTransaction;
    Spinner spnTinhTrang;
    Button btnTimKiem;
    TextView tvTuNgay, tvDenNgay, tvNoData;

    TransactionListAdapter adapter;
    List<TransactionModel> lstData;

    SimpleDateFormat dateFormat;
    Calendar fromDate, toDate;

    ServicesRequest svRequester;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        svRequester = ServicesGenerator.createService(ServicesRequest.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_settings_trans_history, container, false);

            getViews(view);
            initData();
            setListener();

        }
        return view;
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    private void getViews(View view) {
        // TODO Auto-generated method stub
        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);

        lvTransaction = view.findViewById(R.id.lvTransaction);
        lvTransaction.setDivider(getResources().getDrawable(R.color.medium_grey));
        lvTransaction.setDividerHeight(1);

        spnTinhTrang = view.findViewById(R.id.spnTinhTrang);

        btnTimKiem = view.findViewById(R.id.btnTimkiem);
        tvTuNgay = view.findViewById(R.id.tvTuNgay);
        tvDenNgay = view.findViewById(R.id.tvDenNgay);
        tvNoData = view.findViewById(R.id.tvNoData);
    }

    private void initData() {
        // TODO Auto-generated method stub
        List<String> list = new ArrayList<String>();
        list.add("Tất cả");
        list.add("Hoàn tất");
        list.add("Không thành công");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,
                list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnTinhTrang.setAdapter(dataAdapter);

        if (lstData == null)
            lstData = new ArrayList<>();

        if (adapter == null)
            adapter = new TransactionListAdapter(getActivity(), lstData);
        lvTransaction.setAdapter(adapter);

        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        fromDate = Calendar.getInstance();
        toDate = Calendar.getInstance();

        if (NetworkUtils.isConnectedHaveDialog(getActivity()))
            new GetHistoryPaymentTask().execute();
    }

    private void setListener() {
        // TODO Auto-generated method stub
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                // TODO Auto-generated method stub
                new GetHistoryPaymentTask().execute();
            }
        });

        btnTimKiem.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // initDataSearch();
            }
        });

        tvTuNgay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                int year = fromDate.get(Calendar.YEAR);
                int month = fromDate.get(Calendar.MONTH);
                int day = fromDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(getActivity(),
                        android.R.style.Theme_Material_Light_Dialog_Alert, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // TODO Auto-generated method stub
                        fromDate.set(year, month, dayOfMonth);
                        tvTuNgay.setText(dateFormat.format(fromDate.getTime()));
                    }
                }, year, month, day);
                dialog.show();
            }
        });

        tvDenNgay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                int year = toDate.get(Calendar.YEAR);
                int month = toDate.get(Calendar.MONTH);
                int day = toDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(getActivity(),
                        android.R.style.Theme_Material_Light_Dialog_Alert, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // TODO Auto-generated method stub
                        toDate.set(year, month, dayOfMonth);
                        tvDenNgay.setText(dateFormat.format(toDate.getTime()));
                    }
                }, year, month, day);
                dialog.show();
            }
        });

        lvTransaction.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                TransactionModel item = (TransactionModel) parent.getAdapter().getItem(position);
                if (item != null) {
                    Intent intent = new Intent(getActivity(), TransactionDetailActivity.class);
                    intent.putExtra(TransactionDetailActivity.INT_TRANS_MODEL, item);
                    startActivity(intent);
                }
            }
        });
    }

    private class GetHistoryPaymentTask extends AsyncTask<Void, Void, Response<HistoryPaymentResponse>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            swipeLayout.setRefreshing(true);
        }

        @Override
        protected Response<HistoryPaymentResponse> doInBackground(Void... voids) {
            Response<HistoryPaymentResponse> response = null;

            HistoryPaymentRequest request = new HistoryPaymentRequest();

            try {
                request.setClientID(CustomPref.getUserID(getActivity()));

                Call<HistoryPaymentResponse> call = svRequester.GetHistoryPayment(request);
                response = call.execute();
            } catch (Exception e) {
                myLog.printTrace(e);
            }

            return response;
        }

        @Override
        protected void onPostExecute(Response<HistoryPaymentResponse> res) {
            super.onPostExecute(res);
            swipeLayout.setRefreshing(false);

            if (res.isSuccessful()) {
                HistoryPaymentResponse response = res.body();
                if (response != null) {
                    HistoryPaymentResult result = response.getResponse();
                    if (result != null && result.getResult().equals("true")) {

                        lstData = result.getListHistory();
                        if (lstData != null) {

                            tvNoData.setVisibility(View.GONE);
                            swipeLayout.setVisibility(View.VISIBLE);

                            if (adapter == null)
                                adapter = new TransactionListAdapter(getActivity(), lstData);
                            else
                                adapter.reFreshData(lstData);
                        }
                    } else {

                        tvNoData.setVisibility(View.VISIBLE);
                        swipeLayout.setVisibility(View.GONE);

//                        MyCustomDialog dialog = new MyCustomDialog.Builder(getActivity())
//                                .setMessage(getString(R.string.alert_data_not_found))
//                                .setPositiveButton(getString(R.string.confirm_ok), new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        dialog.dismiss();
//                                    }
//                                }).create();
//                        dialog.show();
                    }
                }
            } else {
                MyCustomDialog dialog = new MyCustomDialog.Builder(getActivity())
                        .setMessage(getString(R.string.alert_error_request_server))
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
