package com.dlvn.mcustomerportal.common;

import com.google.android.gms.maps.model.LatLng;

import okhttp3.logging.HttpLoggingInterceptor;

public class Constant {

	/**************************
	 * MAIN URL API Services
	 *****************************************/
	public static final String URL_DEV = "http://10.166.2.119:7800/iibmobile/v1/";
	public static final String URL_UAT = "http://magpcmsuat2.dai-ichi-life.com.vn/iibmobile/v1/";
	public static final String URL_PRD = "";

	/********************* URL PAYMENT NAPAS ************************/
	// UAT
	public static final String URL_NAPAS_UAT = "http://khuat.dai-ichi-life.com.vn:8090/onlinepayment/mpayment.aspx";
	// PRD
	public static final String URL_NAPAS_PRD = "http://khuat.dai-ichi-life.com.vn:8090/onlinepayment/mpayment.aspx";
	// URL NAPAS RESPONSE UAT
	public static final String URL_NAPAS_RESPONSE_UAT = "http://khuat.dai-ichi-life.com.vn/onlinepayment/vpc_dr.aspx?";
	// URL NAPAS RESPONSE PRD
	public static final String URL_NAPAS_RESPONSE_PRD = "http://khuat.dai-ichi-life.com.vn/onlinepayment/vpc_dr.aspx?";

	// URL API
	public static final String URL = URL_DEV;
	// URL NAPAS
	public static String URL_PAYMENT = URL_NAPAS_UAT;
	// URL NAPAS Resonse
	public static String URL_PAYMENT_RES = URL_NAPAS_RESPONSE_UAT;

	//project code
	public static final String Project_ID = "mcp";
	//acthentication String
	public static final String Project_Authentication = "094A1E47-1C6D-43F3-95BF-C1890048D390";

	// flag for print Log
	public static final boolean DEBUG = true;
	public static final HttpLoggingInterceptor.Level LOG_LEVEL =  HttpLoggingInterceptor.Level.BODY;

	// Constant for grant permission multi choice gallery
	public static final int PERMISSION_REQUEST_CODE = 2000;
	public static final int PERMISSION_GRANTED = 2001;
	public static final int PERMISSION_DENIED = 2002;

	/**
	 * key intent for Nop phi/Hoan tra
	 */
	public static final String NOPPHI_SOHOPDONG = "NOPPHI_SOHOPDONG";
	public static final String NOPPHI_PHIBAOHIEM = "NOPPHI_PHIBAOHIEM";
	public static final String NOPPHI_TAMUNG_DONGPHI = "NOPPHI_TAMUNG_DONGPHI";
	public static final String NOPPHI_TAMUNG = "NOPPHI_TAMUNG";
	public static final String NOPPHI_DIEMSUDUNG = "NOPPHI_DIEMSUDUNG";

	/**
	 * Constant text for check action login
	 */
	public static final String LOGIN_ACTION_CHECKACCOUNT = "CheckAccount";
	public static final String LOGIN_ACTION_CPLOGIN = "CPLOGIN";
	public static final String LOGIN_ACTION_REGISTERACCOUNT = "RegisterAccount";
	public static final String LOGIN_ACTION_LINKACCOUNT = "LinkAccountSocial";
	public static final String LOGIN_ACTION_LINKCLIENTID = "LinkClientID";
    public static final String LOGIN_ACTION_FORGOTPASSWORD = "ForgotPassword";
	public static final String LOGIN_ACTION_CHANGEPASSWORD = "ChangePassword";

	/**
	 * Constant ERROR Code for CPLogin
	 */
	public static final String	ERR_CPLOGIN_CLIEXIST = "CLIEXIST";//"ClientID/Email does exist";
	public static final String	ERR_CPLOGIN_CLINOEXIST = "CLINOEXIST";//"ClientID/Email does not exist";
	public static final String	ERR_CPLOGIN_CLIIDEMPTY = "CLIIDEMPTY";//"Client ID is empty";
	public static final String	ERR_CPLOGIN_CLIREGFAIL = "CLIREGFAIL";//"Register fail";
	public static final String	ERR_CPLOGIN_CLIREGSUCC = "CLIREGSUCC";//"Register successful";
	public static final String	ERR_CPLOGIN_WRONGPASS = "WRONGPASS";//"Incorrect password";
    public static final String	ERR_CPLOGIN_CHANGEPASS = "CHANGEPASS";//"Change Password"
	public static final String	ERR_CPLOGIN_CHANGEPASSSUCC = "CHANGEPASSSUCC";//"Change Password sucessfull"
	public static final String	ERR_CPLOGIN_CHANGEPASSFAIL = "CHANGEPASSFAIL";//"Change Password fail"
	public static final String	ERR_CPLOGIN_LINKACCFAIL = "LINKACCFAIL";//"Link account not successfull";
	public static final String	ERR_CPLOGIN_LINKACCSUCC = "LINKACCSUCC";//"Link account successfull";
    public static final String ERR_CPLOGIN_FORGOTPASSWORD = "FORGOTPASSSUCC";// Send email Forgot Password sucessfull
    public static final String ERR_CPLOGIN_FORGOTPASSFAIL = "FORGOTPASSFAIL";// Send email Forgot Password fail


	/**
	 * Key INTENT for transport data from Intent to Intent
	 */
	public static final String INTENT_USER_DATA = "USER_DATA";

	/**
	 * Số đt bp chăm soc khách hàng Dai-Ichi_life
	 */
	public static final String PHONE_CUSTOMER_SERVICE = "02838100888";

	/**
	 * Type of office
	 */
	public static final String OFFICE_TYPE = "DLVN Office";
	public static final String MEDIC_TYPE = "Medic";

	/**
	 * Default location Dai-Ichi_Life VN Head Office
	 */
	public static final LatLng defaultLocation = new LatLng(10.794908, 106.676367);

	/**
	 * enum các kênh thanh toán được hỗ trợ
	 * 
	 * @author nn.tai
	 * @modify Nov 21, 2017
	 */
	public enum PayChannel {
		CASH("Tiền mặt", "Cash", ""), BANK("Ngân Hàng", "Bank", ""), NAPAS("Napas/Thẻ nội địa", "Napas",
				""), MPOS("mPOS", "mPos", "");

		private String stringName;
		private String stringValue;
		private String ID;

		private PayChannel(String toString, String value, String toID) {
			stringName = toString;
			stringValue = value;
			ID = toID;
		}

		@Override
		public String toString() {
			return stringName;
		}

		public String getStringValue() {
			return stringValue;
		}

		public String getID() {
			return ID;
		}

		public static PayChannel fromValue(String value) {
			for (PayChannel mo : values()) {
				if (mo.stringValue.equals(value)) {
					return mo;
				}
			}
			return CASH;
		}
	}


	/**
	 * enum Định kỳ đóng phí
	 * @author nn.tai
	 * @date Jan 3, 2018
	 */
	public enum FeeFrequency {
		MONTHLY("Tháng", "Monthly", ""), QUARTERLY("Quý", "Quarterly", ""), HALF_YEARLY("Nửa năm", "Half-Yearly",
				""), YEARLY("Năm", "Yearly", "");

		private String stringName;
		private String stringValue;
		private String ID;

		private FeeFrequency(String toString, String value, String toID) {
			stringName = toString;
			stringValue = value;
			ID = toID;
		}

		@Override
		public String toString() {
			return stringName;
		}

		public String getStringValue() {
			return stringValue;
		}

		public String getID() {
			return ID;
		}

		public static FeeFrequency fromValue(String value) {
			for (FeeFrequency mo : values()) {
				if (mo.stringValue.equals(value)) {
					return mo;
				}
			}
			return YEARLY;
		}
		
		public static FeeFrequency fromName(String name) {
			for (FeeFrequency mo : values()) {
				if (mo.stringName.equals(name)) {
					return mo;
				}
			}
			return YEARLY;
		}
	}
}
