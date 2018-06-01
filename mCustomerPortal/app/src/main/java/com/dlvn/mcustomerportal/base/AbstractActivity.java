package com.dlvn.mcustomerportal.base;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

public abstract class AbstractActivity extends AppCompatActivity  {
	private static final String TAG = "AbstractActivity";

	protected abstract View getContentView();

	protected abstract BaseFragment getContentFragment();

	protected FragmentManager mFragmentManager;
	protected RelativeLayout container;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mFragmentManager = getFragmentManager();

	}

	protected View getView(int idLayout) {
		return getLayoutInflater().inflate(idLayout, container, false);
	}

	protected void initView() {
		View view = getContentView();
		BaseFragment fragment = getContentFragment();
		if (view != null)
			container.addView(view);
		else if (fragment != null) {
//			pushFragment(fragment, fragment.isAddToBackStack());
		}

	}

//	protected void pushFragment(BaseFragment baseFragment,
//			boolean isAddToBackStack) {
//		if (isAddToBackStack)
//			mFragmentManager
//					.beginTransaction()
//					.replace(R.id.container, baseFragment,
//							baseFragment.getTagFragment())
//					.addToBackStack(baseFragment.getTagFragment())
//					.commitAllowingStateLoss();
//		else
//			mFragmentManager
//					.beginTransaction()
//					.replace(R.id.container, baseFragment,
//							baseFragment.getTagFragment())
//					.commitAllowingStateLoss();
//	}

	void setTitle(String title) {
		// mToolbar.setTitle(title);
		getSupportActionBar().setTitle(title);
	}

//	protected GoogleApiClient mGoogleApiClient;

//	@Override
//	public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//		android.util.Log.d(getClass().getName(), "onConnectionFailed:"
//				+ connectionResult);
//	}
//
//	public synchronized void buildGoogleApiLocationClient(
//			GoogleApiClient.ConnectionCallbacks callbacks,
//			GoogleApiClient.OnConnectionFailedListener listener) {
//		mGoogleApiClient = new GoogleApiClient.Builder(this)
//				.addConnectionCallbacks(callbacks)
//				.addOnConnectionFailedListener(listener)
//				.addApi(LocationServices.API).build();
//	}
//
//	public void connectGoogleAPIClient() {
//		if (mGoogleApiClient != null) {
//			mGoogleApiClient.connect();
//		}
//	}
//
//	public void disConnectGoogleAPIClient() {
//		if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
//			mGoogleApiClient.disconnect();
//		}
//	}
//
//	public boolean checkGPS() {
//        if (!NetworkUtils.checkGPS(this)) {
//            PublicFunction.showDialog(this, getString(R.string.enable_gps_login),
//                    (dialog, which) -> {
//                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                        startActivity(intent);
//                    },
//                    ((dialog1, which1) -> dialog1.dismiss()));
//            return false;
//        }
//        return true;
//    }
//
//	public Location getLocation() {
//		if (mGoogleApiClient != null) {
//			Log.i(TAG, "getLocation: != null");
//			return LocationServices.FusedLocationApi
//					.getLastLocation(mGoogleApiClient);
//		}
//		return null;
//	}
	
}
