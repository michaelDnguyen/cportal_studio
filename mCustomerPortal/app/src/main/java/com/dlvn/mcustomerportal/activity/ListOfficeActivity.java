package com.dlvn.mcustomerportal.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.SearchView;
import android.view.MotionEvent;
import android.view.WindowManager.LayoutParams;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.adapter.OfficeListAdapter;
import com.dlvn.mcustomerportal.adapter.SpinnerOfficeAdapter;
import com.dlvn.mcustomerportal.adapter.model.OfficeAddressModel;
import com.dlvn.mcustomerportal.adapter.model.SpinnerOfficeModel;
import com.dlvn.mcustomerportal.base.BaseActivity;
import com.dlvn.mcustomerportal.common.Constant;
import com.dlvn.mcustomerportal.common.CustomPref;
import com.dlvn.mcustomerportal.services.AppLocationService;
import com.dlvn.mcustomerportal.services.GMapV2Direction;
import com.dlvn.mcustomerportal.services.ServicesGenerator;
import com.dlvn.mcustomerportal.services.ServicesRequest;
import com.dlvn.mcustomerportal.services.model.BaseRequest;
import com.dlvn.mcustomerportal.services.model.request.getMapMarkerRequest;
import com.dlvn.mcustomerportal.services.model.response.getMapMarkerResponse;
import com.dlvn.mcustomerportal.services.model.response.getMapMarkerResult;
import com.dlvn.mcustomerportal.utils.Utilities;
import com.dlvn.mcustomerportal.utils.myLog;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.w3c.dom.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class ListOfficeActivity extends BaseActivity implements OnMapReadyCallback, OnMyLocationButtonClickListener, OnInfoWindowClickListener, AdapterView.OnItemSelectedListener, SearchView.OnQueryTextListener {

    private static final int MY_LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final int LOCATION_LAYER_PERMISSION_REQUEST_CODE = 2;

    GoogleMap map;
    private UiSettings mUiSettings;
    GMapV2Direction gd;

    // service location
    AppLocationService appLocationService;

    RelativeLayout rloParent;
    SearchView svSearch;
    TextView tvHuySearch;

    ImageButton ibtnBack;
    Spinner spnTitle, spnDistance;
    SpinnerOfficeAdapter adapterDistance, adapterTitle;
    SpinnerOfficeModel officeSelected, distanceSelected;

    ListView lvOffice;
    OfficeListAdapter lvAdapter;

    // location position
    LatLng llCurrent, llTarget;
    List<OfficeAddressModel> lstVanPhong, lstPhongKham;
    private boolean mLocationPermissionDenied = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_office);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //customize position of button my location
        if (mapFragment.getView() != null && mapFragment.getView().findViewById(Integer.parseInt("1")) != null) {
            @SuppressWarnings("ResourceType")
            View myLocationButton = mapFragment.getView().findViewById(0x2);

            if (myLocationButton != null && myLocationButton.getLayoutParams() instanceof RelativeLayout.LayoutParams) {
                // ZoomControl is inside of RelativeLayout
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) myLocationButton.getLayoutParams();
                // Align it to - parent BOTTOM|LEFT
                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
                params.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);

                // Update margins, set to 10dp
                final int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10,
                        getResources().getDisplayMetrics());
                params.setMargins(margin, margin, margin, margin);

                myLocationButton.setLayoutParams(params);
            }
        }

        spnTitle = (Spinner) findViewById(R.id.spnTitle);
        spnDistance = (Spinner) findViewById(R.id.spnDistance);
        rloParent = (RelativeLayout) findViewById(R.id.rloParent);
        ibtnBack = (ImageButton) findViewById(R.id.ibtnBack);
        tvHuySearch = (TextView) findViewById(R.id.tvHuySearch);

        svSearch = (SearchView) findViewById(R.id.svSearch);
//        svSearch.onActionViewExpanded(); //new Added line
        svSearch.setIconifiedByDefault(false);
        svSearch.setQueryHint("Nhập văn phòng hoặc địa chỉ");

        if (!svSearch.isFocused()) {
            svSearch.clearFocus();
        }

        lvOffice = (ListView) findViewById(R.id.lvOffice);

        initData();
        setListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Utilities.hideSoftInputKeyboard(ListOfficeActivity.this);
    }

    private void initData() {
        // TODO Auto-generated method stub

        //init Title & distance
        List<SpinnerOfficeModel> spnArray = new ArrayList<>();
        spnArray.add(new SpinnerOfficeModel("Văn phòng", Constant.OFFICE_TYPE));
        spnArray.add(new SpinnerOfficeModel("Phòng khám", Constant.MEDIC_TYPE));

        adapterTitle = new SpinnerOfficeAdapter(this, spnArray, 18.0f); //selected item will look like a spinner set from XML
        spnTitle.setAdapter(adapterTitle);
        spnTitle.setOnItemSelectedListener(this);
        spnTitle.setSelection(0);
        officeSelected = (SpinnerOfficeModel) spnTitle.getSelectedItem();

        List<SpinnerOfficeModel> spnArray2 = new ArrayList<>();
        spnArray2.add(new SpinnerOfficeModel("1 km", "1"));
        spnArray2.add(new SpinnerOfficeModel("3 km", "3"));
        spnArray2.add(new SpinnerOfficeModel("5 km", "5"));
        spnArray2.add(new SpinnerOfficeModel("10 km", "10"));
        spnArray2.add(new SpinnerOfficeModel("15 km", "15"));
        spnArray2.add(new SpinnerOfficeModel("20 km", "20"));

        adapterDistance = new SpinnerOfficeAdapter(this, spnArray2, 15.0f); //selected item will look like a spinner set from XML
        spnDistance.setAdapter(adapterDistance);
        spnDistance.setOnItemSelectedListener(this);
        spnDistance.setSelection(0);
        distanceSelected = (SpinnerOfficeModel) spnDistance.getSelectedItem();

        appLocationService = new AppLocationService(this);
    }

    private void setListener() {
        // TODO Auto-generated method stub

        svSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                svSearch.setIconified(false);
                lvOffice.setVisibility(View.VISIBLE);
                tvHuySearch.setVisibility(View.VISIBLE);
            }
        });
        svSearch.setOnQueryTextListener(this);

        tvHuySearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                lvOffice.setVisibility(View.GONE);
                v.setVisibility(View.GONE);
                svSearch.setIconified(true);
            }
        });

        ibtnBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lvOffice.getVisibility() == View.VISIBLE)
                    lvOffice.setVisibility(View.GONE);
                else
                    onBackPressed();
            }
        });

        lvOffice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OfficeAddressModel item = (OfficeAddressModel) parent.getItemAtPosition(position);
                if (item != null && map != null) {
                    myLog.E("ListView Item click " + item.getName());
                    try {
                        lvOffice.setVisibility(View.GONE);
                        llTarget = new LatLng(Double.parseDouble(item.getLat()), Double.parseDouble(item.getLng()));

                        //Calculate zoom level from distance in maps
//                        float zoomlevel = (float) (19 - Math.log(Integer.parseInt(distanceSelected.getValue()) * 5.508));
                        float zoomlevel = getZoomLevel(Integer.parseInt(distanceSelected.getValue()));

                        myLog.E("Zoom level for " + distanceSelected.getTitle() + " = " + zoomlevel);
                        // zoom camera and animate to current location
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(llTarget, zoomlevel);
                        map.animateCamera(cameraUpdate);
                        tvHuySearch.setVisibility(View.GONE);
                    } catch (NumberFormatException e) {
                        myLog.printTrace(e);
                    }
                }
            }
        });
    }

    public void getCurrentLocation() {

        // Detected current location
        Location location = appLocationService.getLocation(LocationManager.GPS_PROVIDER);
        if (location == null)
            location = appLocationService.getLocation(LocationManager.NETWORK_PROVIDER);

        if (map != null) {
            map.clear();
            // Lay dc toa do hien tai, show map
            if (location != null) {

                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                llCurrent = new LatLng(latitude, longitude);
                llTarget = llCurrent;
                Utilities.showMap(map, llCurrent, "Vị trí hiện tại", true);
            } else {
                // Ko lay dc thi lay toa do mac dinh
                llCurrent = new LatLng(Constant.LAT_DLVN, Constant.LONG_DLVN);
                llTarget = llCurrent;
                Utilities.showMap(map, llCurrent, "Dai-ichi-Life Việt Nam", true);

                // Open service location
                final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    Utilities.showSettingsAlertGPS(this);
                }
            }

            if (officeSelected.getValue().equals(Constant.OFFICE_TYPE))
                initVanPhong();
            else
                initPhongKham();
        }
    }

    @Override
    public void onMapReady(GoogleMap arg0) {
        myLog.E("onMapReady");
        map = arg0;

        // setUp map
        map.setMyLocationEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.getUiSettings().setCompassEnabled(true);

        map.setOnMyLocationButtonClickListener(this);
        map.setOnInfoWindowClickListener(this);

        // get current position
        getCurrentLocation();

        //init list office
        if (officeSelected.getValue().equals(Constant.OFFICE_TYPE)) {
            if (lstVanPhong.size() > 0)
                lvAdapter = new OfficeListAdapter(ListOfficeActivity.this, lstVanPhong);
        } else if (officeSelected.getValue().equals(Constant.MEDIC_TYPE)) {
            if (lstPhongKham.size() > 0)
                lvAdapter = new OfficeListAdapter(ListOfficeActivity.this, lstPhongKham);
        }
        lvOffice.setAdapter(lvAdapter);

        // get List Medic
        if (lstPhongKham == null)
            lstPhongKham = new ArrayList<>();

        if (lstPhongKham.size() <= 0)
            doGetMapMarker(Constant.MEDIC_TYPE, "", distanceSelected.getValue());

        try {
            //initial object
            gd = new GMapV2Direction(this);
            //set listener
            gd.setOnDirectionResponseListener(new GMapV2Direction.OnDirectionResponseListener() {

                @Override
                public void onResponse(String status, Document doc, GMapV2Direction gd) {
                    if (doc != null) {

                        map.clear();
                        if (officeSelected.getValue().equals(Constant.OFFICE_TYPE))
                            initVanPhong();
                        else
                            initPhongKham();

                        //get danh sach toa do đường đi
                        ArrayList<LatLng> directionPoint = gd.getDirection(doc);
                        //setup đường line sẽ vẽ
                        PolylineOptions rectLine = new PolylineOptions().width(7).color(Color.RED);

                        for (int i = 0; i < directionPoint.size(); i++) {
                            rectLine.add(directionPoint.get(i));
                        }
                        //apply line vào map
                        map.addPolyline(rectLine);
                    } else
                        Toast.makeText(ListOfficeActivity.this, "Không lấy được vị trí của khách hàng!", Toast.LENGTH_LONG)
                                .show();
                }
            });
        } catch (Exception e) {
            myLog.printTrace(e);
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        getCurrentLocation();
        return true;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        // TODO Auto-generated method stub
        showDialogDetailMarkerPoint(ListOfficeActivity.this, marker);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = (Spinner) parent;
        Utilities.hideSoftInputKeyboard(ListOfficeActivity.this);

        if (spinner.getId() == R.id.spnTitle) {
            SpinnerOfficeModel item = (SpinnerOfficeModel) parent.getItemAtPosition(position);
            myLog.E("Choice in Spinner Title: " + item.getValue());
            if (item != null) {
                officeSelected = item;
                if (item.getValue().equals(Constant.OFFICE_TYPE)) {
                    initVanPhong();
                } else if (item.getValue().equals(Constant.MEDIC_TYPE)) {
                    initPhongKham();
                }
            }
        } else if (spinner.getId() == R.id.spnDistance) {

            SpinnerOfficeModel item = (SpinnerOfficeModel) parent.getItemAtPosition(position);
            myLog.E("Choice in Spinner Distance: " + item.getValue());
            if (item != null) {
                distanceSelected = item;
                if (!TextUtils.isEmpty(svSearch.getQuery().toString())) {

//                    float zoomlevel = (float) (19 - Math.log(Integer.parseInt(distanceSelected.getValue()) * 5.508));
                    float zoomlevel = getZoomLevel(Integer.parseInt(distanceSelected.getValue()));
                    myLog.E("Zoom level for " + distanceSelected.getTitle() + " = " + zoomlevel);

                    // zoom camera and animate to current location
                    if (llTarget != null) {
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(llTarget, zoomlevel);
                        map.animateCamera(cameraUpdate);
                    } else {
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(llCurrent, zoomlevel);
                        map.animateCamera(cameraUpdate);
                    }
                } else {
                    doGetMapMarker(officeSelected.getValue(), "", distanceSelected.getValue());
                }
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        myLog.E("Office Medic search text = " + query);
        if (TextUtils.isEmpty(query))
            query = "";
        doGetMapMarker(officeSelected.getValue(), query, distanceSelected.getValue());
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        svSearch.setIconified(false);
        lvOffice.setVisibility(View.VISIBLE);
        tvHuySearch.setVisibility(View.VISIBLE);
        return false;
    }

    private void initVanPhong() {
        if (lstVanPhong == null)
            lstVanPhong = new ArrayList<>();

        // get List office from api
        if (lstVanPhong.size() > 0)
            drawMarKerLocation(lstVanPhong, officeSelected.getValue());
        else
            doGetMapMarker(officeSelected.getValue(), "", distanceSelected.getValue());

    }

    private void initPhongKham() {
        if (lstPhongKham == null)
            lstPhongKham = new ArrayList<>();

        // get List medic from api
        if (lstPhongKham.size() > 0)
            drawMarKerLocation(lstPhongKham, officeSelected.getValue());
        else
            doGetMapMarker(officeSelected.getValue(), "", distanceSelected.getValue());

    }

    /**
     * Draw list point location in map
     *
     * @param data
     * @param officeType
     * @author nn.tai
     * @date Dec 21, 2017
     */
    private void drawMarKerLocation(List<OfficeAddressModel> data, String officeType) {

        if (map != null) {
            map.clear();

            if (data != null) {
                for (OfficeAddressModel item : data) {
                    if (officeType.equals(Constant.OFFICE_TYPE))
                        map.addMarker(new MarkerOptions()
                                .position(new LatLng(Double.parseDouble(item.getLat()),
                                        Double.parseDouble(item.getLng())))
                                .title(item.getName())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ico_office_marker))).setTag(item);
                    else
                        map.addMarker(new MarkerOptions()
                                .position(new LatLng(Double.parseDouble(item.getLat()),
                                        Double.parseDouble(item.getLng())))
                                .title(item.getName())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ico_medic_marker))).setTag(item);
                }
            }

            map.addMarker(new MarkerOptions().position(llCurrent).title(getString(R.string.text_current_location))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ico_mylocation_marker))).setTag(null);

            //Calculate zoom level from distance in maps
//          float zoomlevel = (float) (19 - Math.log(Integer.parseInt(distanceSelected.getValue()) * 5.508));
            float zoomlevel = getZoomLevel(Integer.parseInt(distanceSelected.getValue()));

            myLog.E("Zoom level for " + distanceSelected.getTitle() + " = " + zoomlevel);

            // zoom camera and animate to current location
            if (llTarget != null) {
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(llTarget, zoomlevel);
                map.animateCamera(cameraUpdate);
            } else {
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(llCurrent, zoomlevel);
                map.animateCamera(cameraUpdate);
            }
        }
    }

    /**
     * @param distamce: kilometer
     * @return
     */
    private float getZoomLevel(int distamce) {
        float zoomlevel = 0.0f;

        //distance đổi ra meters, lấy 1/2 ra bán kính.
        double radius = (distamce * 1000) / 500;

        //zoomlevel = (float) (16 - Math.log(distamce * 1000 * 5.508));
        zoomlevel = (float) (17 - Math.log(radius) / Math.log(2));

        return zoomlevel;
    }

    /**
     * get List point in map from APi
     *
     * @param officeType: office or medic
     * @author nn.tai
     * @date Dec 21, 2017
     */
    private void doGetMapMarker(final String officeType, final String search, final String distance) {

        new AsyncTask<Void, Void, Response<getMapMarkerResponse>>() {

            ProgressDialog process = null;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if (!TextUtils.isEmpty(search)) {
                    process = new ProgressDialog(ListOfficeActivity.this);
                    process.setMessage("Đang tìm...");
                    process.setCanceledOnTouchOutside(false);
                    process.show();
                }
            }

            @Override
            protected Response<getMapMarkerResponse> doInBackground(Void... params) {

                ServicesRequest service = ServicesGenerator.createService(ListOfficeActivity.this, ServicesRequest.class);
                Response<getMapMarkerResponse> response = null;
                // User user = new User();
                // user = cPortalPref.getPassword(ListOfficeActivity.this);

                getMapMarkerRequest data = new getMapMarkerRequest();
                data.setAgentId(CustomPref.getUserID(ListOfficeActivity.this));
                data.setPassword(CustomPref.getPassword(ListOfficeActivity.this));
                data.setDeviceName(Utilities.getDeviceName() + "-" + Utilities.getVersion());
                data.setAPIToken(CustomPref.getAPIToken(ListOfficeActivity.this));
                data.setLat(llCurrent.latitude + "");
                data.setLng(llCurrent.longitude + "");
                data.setTypeOffice(officeType);
                data.setSearch(search);
                data.setDistance(distance);

//                data.setProject(Constant.Project_ID);
//                data.setAuthentication(Constant.Project_Authentication);

                BaseRequest base = new BaseRequest();
                base.setJsonDataInput(data);
                Call<getMapMarkerResponse> call = service.getMapMarker(base);
                try {
                    response = call.execute();
                } catch (IOException e) {
                    myLog.printTrace(e);
                }
                return response;
            }

            @Override
            protected void onPostExecute(Response<getMapMarkerResponse> result) {

                if (process != null && process.isShowing())
                    process.dismiss();
                Utilities.hideSoftInputKeyboard(ListOfficeActivity.this);

                if (result != null && result.isSuccessful()) {
                    getMapMarkerResponse response = result.body();
                    if (response != null) {
                        getMapMarkerResult rs = response.getGetMapMarkerResult();
                        if (rs != null)
                            if (rs.getResult().equals("true")) {
                                if (rs.getDtProposal().size() > 0) {

                                    if (lvAdapter == null)
                                        lvAdapter = new OfficeListAdapter(ListOfficeActivity.this, rs.getDtProposal());
                                    else
                                        lvAdapter.reFreshData(rs.getDtProposal());
                                    lvOffice.setAdapter(lvAdapter);

                                    if (!TextUtils.isEmpty(search)) {
                                        lvOffice.setVisibility(View.VISIBLE);
                                        tvHuySearch.setVisibility(View.VISIBLE);
                                    }


                                    if (officeType.equals(Constant.OFFICE_TYPE)) {
                                        lstVanPhong.clear();
                                        lstVanPhong.addAll(rs.getDtProposal());
                                        if (officeSelected.getValue().equals(Constant.OFFICE_TYPE))
                                            drawMarKerLocation(lstVanPhong, officeType);
                                    } else {
                                        lstPhongKham.clear();
                                        lstPhongKham.addAll(rs.getDtProposal());
                                        if (officeSelected.getValue().equals(Constant.MEDIC_TYPE))
                                            drawMarKerLocation(lstPhongKham, officeType);
                                    }
                                } else {
                                    if (!TextUtils.isEmpty(search)) {
                                        Toast.makeText(ListOfficeActivity.this, R.string.message_warning_no_location_searched, Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                    }
                }
            }
        }.execute();
    }

    /**
     * Dialog show marker point detail
     *
     * @modify Dec 26, 2017
     * @arthor nn.tai
     */
    public void showDialogDetailMarkerPoint(final Context context, Marker marker) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        dialog.setContentView(R.layout.dialog_marker_detail);
        dialog.setCanceledOnTouchOutside(true);

        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.BOTTOM;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);

        TextView tvTitle, tvAddress, tvHome, tvMobile, tvFax, tvDistance;
        tvTitle = (TextView) dialog.findViewById(R.id.tvTitle);
        tvAddress = (TextView) dialog.findViewById(R.id.tvAddress);
        tvHome = (TextView) dialog.findViewById(R.id.tvHomePhone);
        tvMobile = (TextView) dialog.findViewById(R.id.tvMobile);
        tvFax = (TextView) dialog.findViewById(R.id.tvFax);
        tvDistance = (TextView) dialog.findViewById(R.id.tvDistance);

        ImageView imvCallHome = (ImageView) dialog.findViewById(R.id.imvCallHome);
        ImageView imvCallPhone = (ImageView) dialog.findViewById(R.id.imvCallPhone);

        ImageView imvDirection = (ImageView) dialog.findViewById(R.id.imv_Direction);

        final OfficeAddressModel item = (OfficeAddressModel) marker.getTag();

        if (item != null) {

            tvTitle.setText(item.getName());

            if (!TextUtils.isEmpty(item.getAddress()))
                tvAddress.setText(item.getAddress());
            else
                ((View) tvAddress.getParent()).setVisibility(View.GONE);

            if (!TextUtils.isEmpty(item.getPhone()))
                tvHome.setText(item.getPhone());
            else
                ((View) tvHome.getParent()).setVisibility(View.GONE);

            if (!TextUtils.isEmpty(item.getMobile()))
                tvMobile.setText(item.getMobile());
            else
                ((View) tvMobile.getParent()).setVisibility(View.GONE);

            if (!TextUtils.isEmpty(item.getFax()))
                tvFax.setText(item.getFax());
            else
                ((View) tvFax.getParent()).setVisibility(View.GONE);

            if (!TextUtils.isEmpty(item.getDistance() + ""))
                tvDistance.setText(String.format("%.2f", item.getDistance()) + " km");
            else
                ((View) tvDistance.getParent()).setVisibility(View.GONE);

            llTarget = new LatLng(Double.parseDouble(item.getLat()), Double.parseDouble(item.getLng()));
            imvDirection.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (llCurrent != null && llTarget != null) {
                        //get direction in map
                        gd.getDocument(llCurrent, llTarget, Constant.MODE_DRIVING);
                    }
                }
            });

            imvCallHome.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (item.getPhone().contains("/"))
                            Utilities.actionCallPhoneNumber(context, item.getPhone().substring(0, item.getPhone().indexOf("/")).replace("(", "").replace(")", ""));
                        else
                            Utilities.actionCallPhoneNumber(context, item.getPhone().replace("(", "").replace(")", ""));
                    } catch (Exception e) {
                        Toast.makeText(ListOfficeActivity.this, R.string.message_cantnot_call_phone, Toast.LENGTH_LONG).show();
                        myLog.printTrace(e);
                    }
                }
            });

            imvCallPhone.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (item.getMobile().contains("/"))
                            Utilities.actionCallPhoneNumber(context, item.getMobile().substring(0, item.getMobile().indexOf("/")).replace("(", "").replace(")", ""));
                        else
                            Utilities.actionCallPhoneNumber(context, item.getMobile().replace("(", "").replace(")", ""));
                    } catch (Exception e) {
                        Toast.makeText(ListOfficeActivity.this, R.string.message_cantnot_call_phone, Toast.LENGTH_LONG).show();
                        myLog.printTrace(e);
                    }
                }
            });
        }

        if (!((Activity) context).isFinishing())
            dialog.show();
    }

    /**
     * Request location permission
     *
     * @param requestCode
     * @author nn.tai
     * @date Dec 21, 2017
     */
    public void requestLocationPermission(int requestCode) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Display a dialog with rationale.
            Utilities.RationaleDialog.newInstance(requestCode, false).show(getSupportFragmentManager(), "dialog");
        } else {
            // Location permission has not been granted yet, request it.
            Utilities.requestPermission(this, requestCode, android.Manifest.permission.ACCESS_FINE_LOCATION, false);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == MY_LOCATION_PERMISSION_REQUEST_CODE) {
            // Enable the My Location button if the permission has been granted.
            if (Utilities.isPermissionGranted(permissions, grantResults,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                mUiSettings.setMyLocationButtonEnabled(true);
            } else {
                mLocationPermissionDenied = true;
            }

        } else if (requestCode == LOCATION_LAYER_PERMISSION_REQUEST_CODE) {
            // Enable the My Location layer if the permission has been granted.
            if (Utilities.isPermissionGranted(permissions, grantResults,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                map.setMyLocationEnabled(true);
            } else {
                mLocationPermissionDenied = true;
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View v = getCurrentFocus();
        if (v != null && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE)
                && (v instanceof EditText || v instanceof SearchView)) {
            int scrcoords[] = new int[2];
            v.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + v.getLeft() - scrcoords[0];
            float y = ev.getRawY() + v.getTop() - scrcoords[1];

            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom())
                Utilities.hideSoftInputKeyboard(this);
        }
        return super.dispatchTouchEvent(ev);
    }
}
