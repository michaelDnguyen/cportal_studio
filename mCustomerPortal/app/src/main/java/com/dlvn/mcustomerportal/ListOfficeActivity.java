package com.dlvn.mcustomerportal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.dlvn.mcustomerportal.adapter.model.OfficeAddressModel;
import com.dlvn.mcustomerportal.base.BaseActivity;
import com.dlvn.mcustomerportal.common.Constant;
import com.dlvn.mcustomerportal.common.cPortalPref;
import com.dlvn.mcustomerportal.services.CurrentLocationService;
import com.dlvn.mcustomerportal.services.NetworkUtils;
import com.dlvn.mcustomerportal.services.ServicesGenerator;
import com.dlvn.mcustomerportal.services.ServicesRequest;
import com.dlvn.mcustomerportal.services.model.BaseRequest;
import com.dlvn.mcustomerportal.services.model.User;
import com.dlvn.mcustomerportal.services.model.request.getMapMarkerRequest;
import com.dlvn.mcustomerportal.services.model.response.getMapMarkerResponse;
import com.dlvn.mcustomerportal.services.model.response.getMapMarkerResult;
import com.dlvn.mcustomerportal.utils.Utilities;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import retrofit2.Call;
import retrofit2.Response;

public class ListOfficeActivity extends BaseActivity implements OnMapReadyCallback, OnMyLocationButtonClickListener {

	private static final int MY_LOCATION_PERMISSION_REQUEST_CODE = 1;
	private static final int LOCATION_LAYER_PERMISSION_REQUEST_CODE = 2;

	MapView mapView;
	GoogleMap map;
	private UiSettings mUiSettings;

	Button btnVanPhong, btnPhongKham;

	LatLng llCurrent, llTarget;
	List<OfficeAddressModel> lstVanPhong, lstPhongKham;
	boolean isVanPhong = true;

	private CurrentLocationService currentLocationService;
	private boolean mLocationPermissionDenied = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_office);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		currentLocationService = new CurrentLocationService(this);

		getViews();
		initDatas();
		setListener();
	}

	private void getViews() {
		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);

		btnVanPhong = (Button) findViewById(R.id.btnVanPhong);
		btnPhongKham = (Button) findViewById(R.id.btnPhongKham);
	}

	private void initDatas() {
		// check network & GPS
		if (!NetworkUtils.isConnected(this))
			Utilities.showSettingsAlertConnection(this);

		initVanPhong();
		initPhongKham();

		btnVanPhong.setSelected(true);

	}

	private void initVanPhong() {
		if (lstVanPhong == null)
			lstVanPhong = new ArrayList<>();

		// get List office from api
		if (lstVanPhong.size() > 0)
			drawMarKerLocation(lstVanPhong, Constant.OFFICE_TYPE);
		else
			doGetMapMarker(Constant.OFFICE_TYPE);

	}

	private void initPhongKham() {
		if (lstPhongKham == null)
			lstPhongKham = new ArrayList<>();

		// get List medic from api
		if (lstPhongKham.size() > 0)
			drawMarKerLocation(lstPhongKham, Constant.MEDIC_TYPE);
		else
			doGetMapMarker(Constant.MEDIC_TYPE);

	}

	private void getCurrentLocation() {
		// Detected current location
		Location location = currentLocationService.getLocation(LocationManager.GPS_PROVIDER);
		if (location == null)
			location = currentLocationService.getLocation(LocationManager.NETWORK_PROVIDER);

		// Lay dc toa do hien tai, show map
		if (location != null) {
			double latitude = location.getLatitude();
			double longitude = location.getLongitude();
			llCurrent = new LatLng(latitude, longitude);
		} else {
			// Ko lay dc thi lay toa do mac dinh
			llCurrent = Constant.defaultLocation;

			// Open service location in Settings
			final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
				Utilities.showSettingsAlertGPS(ListOfficeActivity.this);
			}
		}
	}

	private void setListener() {
		// TODO Auto-generated method stub
		btnVanPhong.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.setSelected(true);
				btnPhongKham.setSelected(false);
				if (!isVanPhong) {
					initVanPhong();
					isVanPhong = true;
				}
			}
		});

		btnPhongKham.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.setSelected(true);
				btnVanPhong.setSelected(false);
				if (isVanPhong) {
					initPhongKham();
					isVanPhong = false;
				}
			}
		});
	}

	@Override
	public void onMapReady(GoogleMap agr0) {

		agr0.getUiSettings().setMyLocationButtonEnabled(true);
		// agr0.getUiSettings().setCompassEnabled(true);
		// agr0.getUiSettings().setZoomControlsEnabled(true);
		// agr0.getUiSettings().setMapToolbarEnabled(true);
		agr0.setMyLocationEnabled(true);
		map = agr0;

		getCurrentLocation();
		initVanPhong();
		map.setOnMyLocationButtonClickListener(this);
	}

	@Override
	public boolean onMyLocationButtonClick() {
		getCurrentLocation();

		CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(llCurrent, 15);
		map.animateCamera(cameraUpdate);
		return false;
	}

	/**
	 * Draw list point location in map
	 * 
	 * @author nn.tai
	 * @date Dec 21, 2017
	 * @param data
	 * @param officeType
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
								.icon(BitmapDescriptorFactory.fromResource(R.drawable.ico_office_marker)));
					else
						map.addMarker(new MarkerOptions()
								.position(new LatLng(Double.parseDouble(item.getLat()),
										Double.parseDouble(item.getLng())))
								.title(item.getName())
								.icon(BitmapDescriptorFactory.fromResource(R.drawable.ico_medic_marker)));
				}
			}

			map.addMarker(new MarkerOptions().position(llCurrent).title("You are here.")
					.icon(BitmapDescriptorFactory.fromResource(R.drawable.ico_mylocation_marker)));
			// zoom camera len
			CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(llCurrent, 17);
			map.animateCamera(cameraUpdate);
		}
	}

	/**
	 * get List point in map from APi
	 * 
	 * @author nn.tai
	 * @date Dec 21, 2017
	 * @param officeType:
	 *            office or medic
	 */
	private void doGetMapMarker(final String officeType) {

		new AsyncTask<Void, Void, Response<getMapMarkerResponse>>() {

			@Override
			protected Response<getMapMarkerResponse> doInBackground(Void... params) {

				ServicesRequest service = ServicesGenerator.createService(ListOfficeActivity.this,
						ServicesRequest.class);
				Response<getMapMarkerResponse> response = null;

//				User user = new User();
//				user = cPortalPref.getPassword(ListOfficeActivity.this);
				
				getMapMarkerRequest data = new getMapMarkerRequest();
				data.setAgentId(cPortalPref.getUserID(ListOfficeActivity.this));
				data.setPassword(cPortalPref.getPassword(ListOfficeActivity.this));
				data.setDeviceId(Utilities.getDeviceID(ListOfficeActivity.this));
				data.setDeviceName(Utilities.getDeviceName());
				data.setAPIToken(cPortalPref.getAPIToken(ListOfficeActivity.this));
				data.setLat("10.794834");
				data.setLng("106.676285");
				data.setTypeOffice(officeType);

				BaseRequest base = new BaseRequest();
				base.setJsonDataInput(data);
				Call<getMapMarkerResponse> call = service.getMapMarker(base);
				try {

					response = call.execute();

				} catch (IOException e) {
					e.printStackTrace();
				}

				return response;
			}

			@Override
			protected void onPostExecute(Response<getMapMarkerResponse> result) {
				if (result != null && result.isSuccessful()) {
					getMapMarkerResponse response = result.body();
					if (response != null) {
						getMapMarkerResult rs = response.getGetMapMarkerResult();
						if (rs != null)
							if (rs.getResult().equals("true")) {
								if (rs.getDtProposal().size() > 0) {

									if (officeType.equals(Constant.OFFICE_TYPE)) {
										lstVanPhong.addAll(rs.getDtProposal());
										drawMarKerLocation(lstVanPhong, officeType);
									} else {
										lstPhongKham.addAll(rs.getDtProposal());
										drawMarKerLocation(lstPhongKham, officeType);
									}
								}
							}
					}
				}
			};

		}.execute();
	}

	/**
	 * Request location permission
	 * 
	 * @author nn.tai
	 * @date Dec 21, 2017
	 * @param requestCode
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
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		// noinspection SimplifiableIfStatement
		if (id == android.R.id.home) {
			// finish the activity
			onBackPressed();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

}
