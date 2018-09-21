package com.dlvn.mcustomerportal.services;

import android.content.Context;
import android.content.DialogInterface;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.view.MyCustomDialog;

/**
 * @arthor nn.tai
 * @date Oct 24, 2016
 */
public class NetworkUtils {

    /**
     * Check connecting network
     *
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

    public static boolean isConnectedHaveDialog(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnect = false;

        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            isConnect = true;
        } else {
            MyCustomDialog dialog = new MyCustomDialog.Builder(context)
                    .setMessage(context.getString(R.string.error_no_internet))
                    .setPositiveButton(context.getString(R.string.confirm_ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create();
            dialog.show();
        }

        return isConnect;
    }

    /**
     * check GPS ON/OFF
     *
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
