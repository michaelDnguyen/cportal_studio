package com.dlvn.mcustomerportal.common;

import com.dlvn.mcustomerportal.services.model.User;
import com.dlvn.mcustomerportal.utils.CustomPreferences;

import android.content.Context;

public class cPortalPref {

	public static final String haveLOGIN = "haveLogin";

	public static final String isRefreshing = "isRefreshing";

	/**
	 * Kiem tra user da login chua
	 * 
	 * @param context
	 * @return
	 * @arthor nn.tai
	 * @date Oct 26, 2016 9:43:39 AM
	 */
	public static boolean haveLogin(Context context) {
		return CustomPreferences.getBoolean(context, haveLOGIN, false);
	}

	/**
	 * Save tinh trang Login cua user
	 * 
	 * @param context
	 * @param haveLogin
	 * @arthor nn.tai
	 * @date Oct 26, 2016 9:45:47 AM
	 */
	public static void setLogin(Context context, boolean haveLogin) {
		CustomPreferences.putBoolean(context, haveLOGIN, haveLogin);
	}

	/**
	 * Save thong tin user login lai
	 * 
	 * @param context
	 * @param user
	 * @arthor nn.tai
	 * @date Oct 26, 2016 9:46:05 AM
	 */
	public static void saveUserLogin(Context context, User user) {
		CustomPreferences.putString(context, User.USER_NAME, user.getUserName());
		CustomPreferences.putString(context, User.USER_ID, user.getUserID());
		CustomPreferences.putString(context, User.PASSWORD, user.getPassword());
		CustomPreferences.putString(context, User.TOKEN_LOGIN, user.getAPIToken());
		
		CustomPreferences.putInt(context, User.USER_POINT, user.getPoint());
		CustomPreferences.putInt(context, User.USER_CONTRACT, user.getNumberContract());
		CustomPreferences.putLong(context, User.USER_AMOUNT, user.getAmountContract());
		CustomPreferences.putString(context, User.USER_PROPOSAL, user.getProposalNo());
	}

	/**
	 * clear UserLogin
	 * 
	 * @param context
	 * @param user
	 * @arthor nn.tai
	 * @date Nov 8, 2016 11:14:05 AM
	 */
	public static void clearUserLogin(Context context) {
		CustomPreferences.clear(context, User.USER_NAME);
		// CustomPreferences.clear(context, User.USER_ID);
		CustomPreferences.clear(context, User.PASSWORD);
		CustomPreferences.clear(context, User.TOKEN_LOGIN);
	}

	/**
	 * Lay user name Login
	 * 
	 * @param context
	 * @return
	 * @arthor nn.tai
	 * @date Oct 26, 2016 9:46:18 AM
	 */
	public static String getUserName(Context context) {
		return CustomPreferences.getString(context, User.USER_NAME, "");
	}

	/**
	 * Lay user id login
	 * 
	 * @param context
	 * @return
	 * @arthor nn.tai
	 * @date Oct 26, 2016 9:46:32 AM
	 */
	public static String getUserID(Context context) {
		return CustomPreferences.getString(context, User.USER_ID, "");
	}

	public static String getPassword(Context context) {
		return CustomPreferences.getString(context, User.PASSWORD, "");
	}
	
	public static String getUserProposal(Context context) {
		return CustomPreferences.getString(context, User.USER_PROPOSAL, "");
	}
	
	public static Long getUserAmount(Context context) {
		return CustomPreferences.getLong(context, User.USER_AMOUNT, 0);
	}
	
	public static int getUserContract(Context context) {
		return CustomPreferences.getInt(context, User.USER_CONTRACT, 0);
	}
	
	public static int getUserPoint(Context context) {
		return CustomPreferences.getInt(context, User.USER_POINT, 0);
	}
	

	/**
	 * Lay API token login
	 * 
	 * @param context
	 * @return
	 * @arthor nn.tai
	 * @date Oct 26, 2016 9:46:52 AM
	 */
	public static String getAPIToken(Context context) {
		return CustomPreferences.getString(context, User.TOKEN_LOGIN, "");
	}

	/**
	 * check refreshing
	 * 
	 * @param context
	 * @return
	 * @arthor nn.tai
	 * @date Nov 10, 2016 11:20:00 AM
	 */
	public static boolean isRefreshing(Context context) {
		return CustomPreferences.getBoolean(context, isRefreshing, false);
	}

	/**
	 * set refreshing
	 * 
	 * @param context
	 * @param isRefresh
	 * @arthor nn.tai
	 * @date Nov 10, 2016 11:20:21 AM
	 */
	public static void setRefreshing(Context context, boolean isRefresh) {
		CustomPreferences.putBoolean(context, isRefreshing, isRefresh);
	}

}
