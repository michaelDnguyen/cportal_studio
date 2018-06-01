package com.dlvn.mcustomerportal.services;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @arthor nn.tai
 * @date Oct 24, 2016
 */
public class NetworkUtils {
	
	/**
	 * Check connecting network
	 * @param context
	 * @return
	 * @arthor nn.tai
	 * @date Oct 24, 2016 11:11:33 AM
	 */
	public static boolean isConnected(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		return activeNetwork != null && activeNetwork.isConnectedOrConnecting();

	}

	/**
	 * check GPS ON/OFF
	 * @param context
	 * @return
	 * @arthor nn.tai
	 * @date Oct 24, 2016 11:11:46 AM
	 */
	public static boolean checkGPS(Context context) {
		LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		// getting GPS status
		return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}
}
