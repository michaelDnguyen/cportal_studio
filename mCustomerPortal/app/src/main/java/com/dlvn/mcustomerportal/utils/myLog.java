package com.dlvn.mcustomerportal.utils;

import com.dlvn.mcustomerportal.common.Constant;

import android.util.Log;

public class myLog {

	private static final String TAG = "CPortal";

	public static void E(String tag, String mess) {
		if (Constant.DEBUG)
			Log.e(tag, mess);
	}

	public static void E(String mess) {
		if (Constant.DEBUG)
			Log.e(TAG, mess);
	}

	public static void D(String tag, String mess) {
		if (Constant.DEBUG)
			Log.d(tag, mess);
	}

	public static void D(String mess) {
		if (Constant.DEBUG)
			Log.d(TAG, mess);
	}

	public static void W(String tag, String mess) {
		if (Constant.DEBUG)
			Log.w(tag, mess);
	}

	public static void W(String mess) {
		if (Constant.DEBUG)
			Log.w(TAG, mess);
	}

	public static void I(String tag, String mess) {
		if (Constant.DEBUG)
			Log.i(tag, mess);
	}

	public static void I(String mess) {
		if (Constant.DEBUG)
			Log.i(TAG, mess);
	}
}
