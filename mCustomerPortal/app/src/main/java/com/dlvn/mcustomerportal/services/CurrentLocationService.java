package com.dlvn.mcustomerportal.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class CurrentLocationService extends Service implements LocationListener {

	private static final String TAG = "CurrentLocationService";
	protected LocationManager locationManager;
	Location location;
	Context context;

	private static final long MIN_DISTANCE_FOR_UPDATE = 10;
	private static final long MIN_TIME_FOR_UPDATE = 1000 * 60 * 2;

	// contructor
	public CurrentLocationService(Context context) {
		this.context = context;
		locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
	}

	/**
	 * get Location by provider
	 * 
	 * @arthor nn.tai
	 * @param provider
	 * @return
	 */
	public Location getLocation(String provider) {
		if (locationManager.isProviderEnabled(provider)) {
			locationManager.requestLocationUpdates(provider, MIN_TIME_FOR_UPDATE, MIN_DISTANCE_FOR_UPDATE, this);
			if (locationManager != null) {
				location = locationManager.getLastKnownLocation(provider);
				return location;
			}
		}
		return null;
	}

	/**
	 * check location services
	 * 
	 * @arthor nn.tai
	 * @return
	 */
	public boolean checkLocation() {
		boolean gps_enabled = false;
		boolean network_enabled = false;

		try {
			gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		try {
			network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		if (!gps_enabled && !network_enabled) {
			// notify user
			Toast.makeText(context, "Location not Enable", Toast.LENGTH_LONG).show();
			return false;
		}
		return true;
	}

	@Override
	public void onLocationChanged(Location location) {
		Log.e(TAG, "onLocationChanged " + location);
	}

	@Override
	public void onProviderDisabled(String provider) {
		Log.e(TAG, "onProviderDisabled " + provider);
	}

	@Override
	public void onProviderEnabled(String provider) {
		Log.e(TAG, "onProviderEnabled " + provider);
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		Log.e(TAG, "onStatusChanged = " + provider + " ** " + status + " ** " + extras.toString());
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

}
