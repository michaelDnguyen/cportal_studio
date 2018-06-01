package com.dlvn.mcustomerportal.base;

import com.dlvn.mcustomerportal.LoginActivity;
import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.common.cPortalPref;
import com.dlvn.mcustomerportal.utils.Utilities;

import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

public class CPortalApplication extends Application {

	public static CPortalApplication mInstance;

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
	}

	public static synchronized CPortalApplication getInstance() {
		return mInstance;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		super.onTerminate();
	}

	@Override
	public void onTrimMemory(int level) {
		switch (level) {
		case ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN:
			/*
			 * Release any UI objects that currently hold memory. The user
			 * interface has moved to the background.
			 */
			break;
		case ComponentCallbacks2.TRIM_MEMORY_RUNNING_MODERATE:
		case ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW:
		case ComponentCallbacks2.TRIM_MEMORY_RUNNING_CRITICAL:
			/*
			 * Release any memory that your app doesn't need to run. The device
			 * is running low on memory while the app is running. The event
			 * raised indicates the severity of the memory-related event. If the
			 * event is TRIM_MEMORY_RUNNING_CRITICAL, then the system will begin
			 * killing background processes.
			 */
			break;
		case ComponentCallbacks2.TRIM_MEMORY_BACKGROUND:
		case ComponentCallbacks2.TRIM_MEMORY_MODERATE:
		case ComponentCallbacks2.TRIM_MEMORY_COMPLETE:
			/*
			 * Release as much memory as the process can. The app is on the LRU
			 * list and the system is running low on memory. The event raised
			 * indicates where the app sits within the LRU list. If the event is
			 * TRIM_MEMORY_COMPLETE, the process will be one of the first to be
			 * terminated.
			 */
			// new
			// AlertDialog.Builder(this).setTitle(R.string.low_memory).setMessage(R.string.low_memory_message).create()
			// .show();
			Toast.makeText(this, R.string.low_memory_message, 2000).show();
			break;
		default:
			/*
			 * Release any non-critical data structures. The app received an
			 * unrecognized memory level value from the system. Treat this as a
			 * generic low-memory message.
			 */
			break;
		}
	}

	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		super.onLowMemory();
	}

	/**
	 * @modify Nov 6, 2017
	 * @description Logout app and delete data
	 */
	public void logout() {
		// xoa data
		cPortalPref.clearUserLogin(this);
		cPortalPref.setLogin(this, false);
		Utilities.deleteAllFileInFolder(getExternalFilesDir(null).getAbsolutePath());

		Intent intent = new Intent(this, LoginActivity.class);
		intent.setFlags(
				Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

}
