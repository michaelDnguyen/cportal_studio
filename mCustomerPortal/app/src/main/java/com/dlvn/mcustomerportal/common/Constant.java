package com.dlvn.mcustomerportal.common;

import android.content.Context;

import com.dlvn.mcustomerportal.services.model.User;
import com.dlvn.mcustomerportal.services.model.response.ClientProfile;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.logging.HttpLoggingInterceptor;

public class Constant {

    /**************************
     * MAIN URL API Services
     *****************************************/
    public static final String URL_DEV = "http://10.166.2.119:7800/iibmobile/v1/";
    public static final String URL_UAT = "http://magpcmsuat2.dai-ichi-life.com.vn/iibmobile/v1/";
    public static final String URL_PRD = "";

    /********************* URL PAYMENT NAPAS ************************/

    //DEV
    public static final String URL_NAPAS_NOLOGIN_DEV = "http://10.166.8.47:9000/PaymentNoLogin.aspx";
    // UAT
    public static final String URL_NAPAS_NOLOGIN_UAT = "http://khuat.dai-ichi-life.com.vn:8090/onlinepayment/PaymentNoLogin.aspx";
    public static final String URL_NAPAS_UAT = "http://khuat.dai-ichi-life.com.vn:8090/onlinepayment/mpayment.aspx";
    // PRD
    public static final String URL_NAPAS_PRD = "http://khuat.dai-ichi-life.com.vn:8090/onlinepayment/mpayment.aspx";
    // URL NAPAS RESPONSE UAT
    public static final String URL_NAPAS_RESPONSE_UAT = "http://khuat.dai-ichi-life.com.vn:8090/onlinepayment/vpc_dr.aspx?";
    // URL NAPAS RESPONSE PRD
    public static final String URL_NAPAS_RESPONSE_PRD = "http://khuat.dai-ichi-life.com.vn/onlinepayment/vpc_dr.aspx?";

    // URL API
    public static final String URL = URL_UAT;
    // URL NAPAS
    public static String URL_PAYMENT = URL_NAPAS_UAT;
    //URL NAPAS NO LOGIN
    public static String URL_PAYMENT_NOLOGIN = URL_NAPAS_NOLOGIN_UAT;
    // URL NAPAS Resonse
    public static String URL_PAYMENT_RES = URL_NAPAS_RESPONSE_UAT;

    //project code
    public static final String Project_ID = "mcp";
    //acthentication String
    public static final String Project_Authentication = "094A1E47-1C6D-43F3-95BF-C1890048D390";

    // flag for print Log
    public static final boolean DEBUG = true;
    public static final HttpLoggingInterceptor.Level LOG_LEVEL = HttpLoggingInterceptor.Level.BODY;

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
    public static final String LOGIN_ACTION_LINKACCOUNTSOCIAL = "LinkAccountSocial";
    public static final String LOGIN_ACTION_LINKCLIENTID = "LinkClientID";
    public static final String LOGIN_ACTION_FORGOTPASSWORD = "ForgotPassword";
    public static final String LOGIN_ACTION_CHANGEPASSWORD = "ChangePassword";
    public static final String LOGIN_ACTION_CHECKOTP = "CheckOTP";

    /**
     * Constant ERROR Code for CPLogin
     */
    public static final String ERR_CPLOGIN_CLIEXIST = "CLIEXIST";//"ClientID/Email does exist";
    public static final String ERR_CPLOGIN_CLINOEXIST = "CLINOEXIST";//"ClientID/Email does not exist";
    public static final String ERR_CPLOGIN_CLIIDEMPTY = "CLIIDEMPTY";//"Client ID is empty";
    public static final String ERR_CPLOGIN_CLIREGFAIL = "CLIREGFAIL";//"Register fail";
    public static final String ERR_CPLOGIN_CLIREGSUCC = "CLIREGSUCC";//"Register successful";
    public static final String ERR_CPLOGIN_WRONGPASS = "WRONGPASS";//"Incorrect password";
    public static final String ERR_CPLOGIN_CHANGEPASS = "CHANGEPASS";//"Change Password"
    public static final String ERR_CPLOGIN_CHANGEPASSSUCC = "CHANGEPASSSUCC";//"Change Password sucessfull"
    public static final String ERR_CPLOGIN_CHANGEPASSFAIL = "CHANGEPASSFAIL";//"Change Password fail"
    public static final String ERR_CPLOGIN_LINKACCFAIL = "LINKACCFAIL";//"Link account not successfull";
    public static final String ERR_CPLOGIN_LINKACCSUCC = "LINKACCSUCC";//"Link account successfull";
    public static final String ERR_CPLOGIN_LINKNOEXIST = "LINKNOEXIST";//"Link account successfull";
    public static final String ERR_CPLOGIN_FORGOTPASSWORD = "FORGOTPASSSUCC";// Send email Forgot Password sucessfull
    public static final String ERR_CPLOGIN_FORGOTPASSFAIL = "FORGOTPASSFAIL";// Send email Forgot Password fail
    public static final String ERR_CPLOGIN_SUCCESSFUL = "SUCCESSFUL";//Request successful
    public static final String ERR_CPLOGIN_OTPINCORRECT = "OTPINCORRECT";//OTP failed


    /**
     * Constant Action Code for Request Get Policy Info
     */
    public static final String POINFO_ACTION_POLICYDETAIL = "PolicyDetail";//Get Chi Tiet Hop Dong
    public static final String POINFO_ACTION_POLICYPRODUCT = "PolicyProduct";//Get Thong tin San Pham
    public static final String POINFO_ACTION_POLICYPAYMENT = "PolicyPayment";//Get Thông tin Thanh toan hợp đồng
    public static final String POINFO_ACTION_POLICYANN = "PolicyAnn";//Get Gia tri Hop Dong
    public static final String POINFO_ACTION_POLICYBENE = "PolicyBene";//Get Nguoi thu huong
    public static final String POINFO_ACTION_POLICYLIFEINSURED = "PolicyLifeInsured";//Get Thông tin người được bảo hiểm chinh
    public static final String POINFO_ACTION_POLICYAGENT = "PolicyAgent";//Get Tu Van Vien Hop Dong
    public static final String POINFO_ACTION_POLICY_CLIENT = "PolicyClient";//Truy vấn thông tin hợp đồng ko cần Login

    /**
     * Constant for master data
     */
    public static final String MASTER_DATA_TYPE_CATEGORY = "category";
    public static final String MASTER_DATA_TYPE_CITY_DISTRICT = "city_district";
    public static final String MASTER_DATA_TYPE_ORDER_STATUS = "order_status";
    public static final String MASTER_DATA_TYPE_SHIPPING_STATUS = "shipping_status";

    /**
     * Key INTENT for transport data from Intent to Intent
     */
    public static final String INTENT_USER_DATA = "USER_DATA";

    /**
     * Số đt bp chăm soc khách hàng Dai-Ichi_life
     */
    public static final String PHONE_CUSTOMER_SERVICE = "02838100888";

    //save pref map and location
    public final static String KEY_ADDRESS_MAP = "map_address";
    public static final Double LAT_DLVN = 10.7948801;
    public static final Double LONG_DLVN = 106.6763111;

    public final static String MODE_DRIVING = "driving";
    public final static String MODE_WALKING = "walking";

    // 05/04/2018: SR07433 - Apply DLVN, GA & Medic Offices to Google Map on mAGP
    public final static String OFFICE_TYPE = "DLVN Office";
    public final static String MEDIC_TYPE = "Medic";

    /**
     * Contact Action
     */
    public static final String ACTION_CONTACT = "ContactForm";

    /**
     * Default location Dai-Ichi_Life VN Head Office
     */
    public static final LatLng defaultLocation = new LatLng(10.794908, 106.676367);

    /**
     * Constant value for Expandable ListView Cart by category
     */
    public static final String CHECK_BOX_CHECKED_TRUE = "YES";
    public static final String CHECK_BOX_CHECKED_FALSE = "NO";

    public static List<List<HashMap<String, String>>> childItems = new ArrayList<>();
    public static List<HashMap<String, String>> parentItems = new ArrayList<>();

    public class Parameter {
        public static final String IS_CHECKED = "is_checked";
        public static final String SUB_CATEGORY_NAME = "sub_category_name";
        public static final String CATEGORY_NAME = "category_name";
        public static final String CATEGORY_ID = "category_id";
        public static final String SUB_ID = "sub_id";
    }
    /* End value constant for Expandable ListView */

    /**
     * Intent key for policy No
     */
    public static final String INT_KEY_POLICY_NO = "KEY_POLICY_NO";

    /**
     * Intent key for Loyalty Point
     */
    public static final String INT_USEPOINT_TYPE = "INT_USEPOINT_TYPE";
    public static final String INT_USEPOINT_CATEGORY = "INT_USEPOINT_CATEGORY";
    public static final String INT_USEPOINT_USER_POINTDON = "INT_USEPOINT_USER_POINTDON";

    /**
     * Intent & Constant for WEB Home Page
     */
    public static final String INT_KEY_WEB_URL = "KEY_WEB_URL";
    public static final String INT_KEY_WEB_URL_TITLE = "KEY_WEB_URL_TITLE";
    public static final String CONST_URL_NEWS = "https://www.dai-ichi-life.com.vn/vi-VN/hoat-dong-cong-dong/306";
    public static final String CONST_URL_PRODUCT = "https://www.dai-ichi-life.com.vn/vi-VN/san-pham/329/";

    /**
     * OTP length
     */
    public static final int OTP_LENGTH = 6;

    /**
     * model containt info Payment NAPAS
     */
    //key request napas payment
    public static final int REQUEST_NAPAS_PAYMENT = 3886;

    public static final String KEY_NAPAS_PAYMENT_MODEL = "KEY_NAPAS_PAYMENT_MODEL";
    //contain transaction id after payment result
    public static final String KEY_NAPAS_PAYMENT_TRANSID = "KEY_NAPAS_PAYMENT_TRANSID";
    //key url payment request
    public static final String KEY_NAPAS_PAYMENT_URL = "NAPAS_PAYMENT_URL";

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
     *
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

    public static final String CATEGORY_FEEBASICOFPOL = "3";

    public enum Category_PCPCode {
        PCP_001("Hàng hóa giao theo địa chỉ", "001", ""), PCP_002("Hàng hóa giao online", "002", ""), PCP_003("Đổi điểm nộp phí/Hoàn trả tạm ứng", "003",
                ""), PCP_004("Tặng điểm", "004", ""), PCP_005("Quà tặng DLVN", "005", "");

        private String stringName;
        private String stringValue;
        private String ID;

        private Category_PCPCode(String toString, String value, String toID) {
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

        public static Category_PCPCode fromValue(String value) {
            for (Category_PCPCode mo : values()) {
                if (mo.stringValue.equals(value)) {
                    return mo;
                }
            }
            return PCP_001;
        }

        public static Category_PCPCode fromName(String name) {
            for (Category_PCPCode mo : values()) {
                if (mo.stringName.equals(name)) {
                    return mo;
                }
            }
            return PCP_001;
        }
    }

}
