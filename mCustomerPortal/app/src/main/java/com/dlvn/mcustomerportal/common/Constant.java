package com.dlvn.mcustomerportal.common;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.logging.HttpLoggingInterceptor;

public class Constant {

    /******************************************
     * MAIN URL API Services
     *****************************************/
    public static final String URL_DEV = "http://10.166.2.119:7800/iibmobile/v1/";
    public static final String URL_UAT = "https://magpcmsuat2.dai-ichi-life.com.vn/iibmobile/v1/";
    public static final String URL_PRD = "https://mobile.dai-ichi-life.com.vn/iibmobile/v1/";

    public static final String URL_FILE_DEV = "http://10.166.2.119:7800/iibmobile/v1/imagebyname?filename=";
    public static final String URL_FILE_UAT = "https://magpcmsuat2.dai-ichi-life.com.vn/iibmobile/v1/imagebyname?filename=";
    public static final String URL_FILE_PRD = "https://mobile.dai-ichi-life.com.vn/iibmobile/v1/imagebyname?filename=";

    /********************* URL PAYMENT NAPAS ************************/

    //DEV
    public static final String URL_NAPAS_NOLOGIN_DEV = "http://10.166.8.47:9000/PaymentNoLogin.aspx";
    // UAT
    public static final String URL_NAPAS_NOLOGIN_UAT = "http://khuat.dai-ichi-life.com.vn:8090/onlinepayment/PaymentNoLogin.aspx";
    public static final String URL_NAPAS_UAT = "http://khuat.dai-ichi-life.com.vn:8090/onlinepayment/mpayment.aspx";
    // PRD
    public static final String URL_NAPAS_PRD = "https://khuat.dai-ichi-life.com.vn:8090/onlinepayment/mpayment.aspx";
    // URL NAPAS RESPONSE UAT
    public static final String URL_NAPAS_RESPONSE_UAT = "http://khuat.dai-ichi-life.com.vn:8090/onlinepayment/vpc_dr.aspx?";
    // URL NAPAS RESPONSE PRD
    public static final String URL_NAPAS_RESPONSE_PRD = "https://khuat.dai-ichi-life.com.vn/onlinepayment/vpc_dr.aspx?";

    /**
     * URL API ** CHANGE WHEN PRD
     */
    public static final String URL = URL_UAT;
    public static final String URL_FILE = URL_FILE_UAT;

    // flag for print Log
    public static final boolean DEBUG = true;
    public static final HttpLoggingInterceptor.Level LOG_LEVEL = HttpLoggingInterceptor.Level.BODY;
    /**
     * END CHANGE
     */


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

    //Channel ID
    public static final String YOUTUBE_CHANNEL_ID = "UCMPzqdj3bvF8sPkoueATO8w";

    // Constant for grant permission multi choice gallery
    public static final int PERMISSION_REQUEST_CODE = 2000;
    public static final int PERMISSION_GRANTED = 2001;
    public static final int PERMISSION_DENIED = 2002;

    public static final int REQUEST_CODE_CAMERA = 2077;
    public static final int REQUEST_CODE_GALLERY = 2079;

    /**
     * ERROR INVALID TOKEN REQUEST FROM SERVER
     */
    public static final String ERROR_TOKENINVALID = "invalidtoken";

    /**
     * key intent for Nop phi/Hoan tra
     */
    public static final String NOPPHI_SOHOPDONG = "NOPPHI_SOHOPDONG";
    public static final String NOPPHI_PHIBAOHIEM = "NOPPHI_PHIBAOHIEM";
    public static final String NOPPHI_TAMUNG_DONGPHI = "NOPPHI_TAMUNG_DONGPHI";
    public static final String NOPPHI_TAMUNG = "NOPPHI_TAMUNG";
    public static final String NOPPHI_DIEMSUDUNG = "NOPPHI_DIEMSUDUNG";

    /**
     * Action for GET PRODUCT, NEWS
     */
    public static final String MASTERDATA_PRODUCTNEWS_ACTION = "ProductNews";
    public static final String MASTERDATA_PRODUCT_TYPE = "Product";
    public static final String MASTERDATA_NEWS_TYPE = "News";

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
    public static final String LOGIN_ACTION_GENERATEOTP = "GenOTP";
    public static final String LOGIN_ACTION_UnLinkAccountSocial = "UnLinkAccountSocial";//unlink social

    public static final String LOGIN_ACTION_VERYFYCELLPHONE = "VerifyCellPhone";//verify phone
    public static final String LOGIN_ACTION_VERYFYEMAIL = "VerifyEmail";//verify email
    public static final String LOGIN_ACTION_VERYFYACCOUNT = "VerifyAccount";
    public static final String LOGIN_ACTION_UPDATEACCOUNT = "UpdateAccount";
    public static final String LOGIN_ACTION_UPDATE2FA = "UpdateTwoFA";//Update 2 factory Authen

    public static final String LOGIN_ACTION_GET_CLIENTPROFILE = "GetClientProfile";//get client profile

    public static final String LOGIN_ACTION_GET_NOTIFICATION_SETTINGS = "GetNotificationSettings";// get notifications settings

    public static final String LOGIN_Constant_UnLinkSocial = "UnLink";//unink social constant for Facebook , gmail

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
    public static final String ERR_CPLOGIN_UNLINKACCSUCC = "UNLINKACCSUCC";//Un-Link Acc success
    public static final String ERR_CPLOGIN_UNLINKACCFAIL = "UNLINKACCFAIL";//Un-Link Acc failed
    public static final String ERR_CPLOGIN_LINKNOEXIST = "LINKNOEXIST";//"Link account successfull";
    public static final String ERR_CPLOGIN_FORGOTPASSWORD = "FORGOTPASSSUCC";// Send email Forgot Password sucessfull
    public static final String ERR_CPLOGIN_FORGOTPASSFAIL = "FORGOTPASSFAIL";// Send email Forgot Password fail
    public static final String ERR_CPLOGIN_SUCCESSFUL = "SUCCESSFUL";//Request successful
    public static final String ERR_CPLOGIN_OTPINCORRECT = "OTPINCORRECT";//OTP failed
    public static final String ERR_CPLOGIN_OTPEXPIRY = "OTPEXPIRY";//OTP expired
    public static final String ERR_CPLOGIN_CLIEXISTNOACTIVE = "CLIEXISTNOACTIVE";//Register OK but not verify by OTP
    public static final String ERR_CPLOGIN_CHANGESSUCC = "CHANGESSUCC";// Change verify Phone success

    public static final long TIMER_COUNTDOWN_OTP = 180000;

    public static final String GENERATEOTP_PHONE = "CellPhone";
    public static final String GENERATEOTP_EMAIL = "Email";

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
    public static final String POINFO_ACTION_POLICY_PENDING = "PolicyPending";//Bổ sung thông tin

    /**
     * CPSaveLog status
     */
    public static final String CPLOG_HOME_OPEN = "Open_HomePage";//Open home page
    public static final String CPLOG_HOME_CLOSE = "Close_HomePage";//Open home page
    public static final String CPLOG_POLICY_OPEN = "Open_PolicyPage";
    public static final String CPLOG_POLICY_CLOSE = "Close_PolicyPage";
    public static final String CPLOG_LOYALTY_OPEN = "Open_LoyaltyPage";
    public static final String CPLOG_LOYALTY_CLOSE = "Close_LoyaltyPage";
    public static final String CPLOG_NOTIFICATIONS_OPEN = "Open_NotificationsPage";
    public static final String CPLOG_NOTIFICATIONS_CLOSE = "Close_NotificationsPage";
    public static final String CPLOG_PAYMENT_OPEN = "Open_PaymentPage";
    public static final String CPLOG_PAYMENT_CLOSE = "Close_PaymentPage";

    public static final String CPLOG_POLICYDETAIL_OPEN = "Open_PolicyDetailPage";
    public static final String CPLOG_POLICYDETAIL_CLOSE = "Close_PolicyDetailPage";
    public static final String CPLOG_SETTINGACCOUNT_OPEN = "Open_SettingAccountPage";
    public static final String CPLOG_SETTINGACCOUNT_CLOSE = "Close_SettingAccountPage";
    public static final String CPLOG_SETTINGSECURITY_OPEN = "Open_SettingSecurityPage";
    public static final String CPLOG_SETTINGSECURITY_CLOSE = "Close_SettingSecurityPage";
    public static final String CPLOG_SETTINGLINKED_OPEN = "Open_SettingLinkedPage";
    public static final String CPLOG_SETTINGLINKED_CLOSE = "Close_SettingLinkedPage";

    public static final String CPLOG_SUBMITFORM_OPEN = "Open_SubmitFormPage";
    public static final String CPLOG_SUBMITFORM_CLOSE = "Close_SubmitFormPage";
    public static final String CPLOG_TRANSHISTORY_OPEN = "Open_TransHistoryPage";
    public static final String CPLOG_TRANSHISTORY_CLOSE = "Close_TransHistoryPage";

    public static final String CPLOG_CLAIMREQUEST_OPEN = "Open_ClaimRequestPage";
    public static final String CPLOG_CLAIMREQUEST_CLOSE = "Close_ClaimRequestPage";
    public static final String CPLOG_CLAIMLIST_OPEN = "Open_ClaimListPage";
    public static final String CPLOG_CLAIMLIST_CLOSE = "Close_ClaimListPage";
    public static final String CPLOG_CLAIMDETAIL_OPEN = "Open_ClaimDetailPage";
    public static final String CPLOG_CLAIMDETAIL_CLOSE = "Close_ClaimDetailPage";

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
     * Số đt bp chăm soc khách hàng Dai-ichi_life
     */
    public static final String PHONE_CUSTOMER_SERVICE = "02838100888";

    public static final String XML_SCHEMA_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

    //save pref map and location
    public final static String KEY_ADDRESS_MAP = "map_address";
    public static final Double LAT_DLVN = 10.7948801;
    public static final Double LONG_DLVN = 106.6763111;

    public final static String MODE_DRIVING = "driving";
    public final static String MODE_WALKING = "walking";

    /**
     * FCM key intent send message
     */
    public final static String FCM_DESTINATION_ID = "fcm-destination-id";
    public final static String FCM_DESTINATION_CATEGORY = "fcm-destination-category";

    // 05/04/2018: SR07433 - Apply DLVN, GA & Medic Offices to Google Map on mAGP
    public final static String OFFICE_TYPE = "DLVN Office";
    public final static String MEDIC_TYPE = "Medic";

    /**
     * Point Account process
     */
    public final static String POINTACCOUNT_INGEN = "INGEN";
    public final static String POINTACCOUNT_DEGEN = "DEOD";

    /**
     * Contact Action
     */
    public static final String ACTION_CONTACT = "ContactForm";
    public static final String ACTION_MAILBOX = "MailBox";
    public static final String ACTION_NOTIFICATION = "Notification";
    public static final String ACTION_ALTERATIONFORM = "AlterationForm";

    public static final String ACTION_NOTIFICATION_SETTINGS = "NotificationSettings";

    public static final String ACTION_ACTIVE = "1";
    public static final String ACTION_NON_ACTIVE = "0";

    public static final String MSG_CODE_NOTIFY_ACCOUNT = "NOTIFY_ACCOUNT";
    public static final String MSG_CODE_NOTIFY_POLICY = "NOTIFY_POLICY";
    public static final String MSG_CODE_NOTIFY_EVENT_DOB = "NOTIFY_EVENT_DOB";
    public static final String MSG_CODE_NOTIFY_EVENT_NOEL = "NOTIFY_EVENT_NOEL";
    public static final String MSG_CODE_NOTIFY_EVENT_NEWYEAR = "NOTIFY_EVENT_NEWYEAR";
    public static final String MSG_CODE_NOTIFY_LOYALTYPOINT = "NOTIFY_LOYALTYPOINT";
    public static final String MSG_CODE_NOTIFY_NEWS = "NOTIFY_NEWS";

    public static final String MSG_CODE_SMS_ACCOUNT = "SMS_ACCOUNT";
    public static final String MSG_CODE_SMS_POLICY = "SMS_POLICY";
    public static final String MSG_CODE_SMS_LOYALTYPOINT = "SMS_LOYALTYPOINT";
    public static final String MSG_CODE_SMS_NEWS = "SMS_NEWS";

    public static final String MSG_CODE_EMAIL_ACCOUNT = "EMAIL_ACCOUNT";
    public static final String MSG_CODE_EMAIL_POLICY = "EMAIL_POLICY";
    public static final String MSG_CODE_EMAIL_LOYALTYPOINT = "EMAIL_LOYALTYPOINT";
    public static final String MSG_CODE_EMAIL_NEWS = "EMAIL_NEWS";

    /**
     * Claims Intent key put extract
     */
    //put parcelable form request for claims
    public static final String CLAIMS_INTKEY_MAIN_REQUEST = "CLAIMS_INTKEY_MAIN_REQUEST";
    // put extract id of claims
    public static final String CLAIMS_INTKEY_CLAIMS_ID = "CLAIMS_INTKEY_CLAIMS_ID";
    public static final String CLAIMS_INTKEY_CLAIMS_SUPPLEMENT_ID = "CLAIMS_INTKEY_CLAIMS_SUPPLEMENT_ID";
    //claims ID của thằng cha cần tạo Bổ Sung(Supplement)
    public static final String CLAIMS_INTKEY_SUPPLEMENT_SUBMISSIONID_PARENT = "CLAIMS_INTKEY_SUPPLEMENT_SUBMISSIONID_PARENT";

    /**
     * Action for Claims
     */
    public static final String CLAIMS_ACTION_LIFEINSURED_LIST = "LifeInsuredList";
    public static final String CLAIMS_ACTION_LIFEINSURED_BENEFIT = "LifeInsuredBenefit";
    public static final String CLAIMS_ACTION_REQUEST = "ClaimForm_Requester";
    public static final String CLAIMS_ACTION_HOLD_REQUEST = "ClaimHold";

    public static final String CLAIMS_ACTION_CHECKHOLD = "CheckHold";
    public static final String CLAIMS_ACTION_CHECK_CLAIM_DUPLICATE = "CheckClaimDuplicate";

    /**
     * Action for Sync Claims, ussing with API CPGetClientProfileByCLIID
     */
    public static final String CLAIMS_ACTION_SYNCLISTCLAIMS = "SyncListClaim";
    public static final String CLAIMS_ACTION_SYNCCLAIMS = "SyncClaim";

    /**
     * Claims Doc Process ID
     */
    public static final String CLAIMS_DOCPROCESS_ID = "OTC";

    /**
     * Claims DocType Question Câu Hỏi Mở
     */
    public static final String CLAIMS_DOCTYPE_QUESTION = "Question";

    /**
     * Claims Additional case Missing Document
     */
    public static final String CLAIMS_DOCPTYPE_MISSING = "Missing";

    /**
     * Claims Type for Supplement
     */
    public static final String CLAIMS_TYPE_SUPPLEMENT_ID = "Additional";
    public static final String CLAIMS_TYPE_SUPPLEMENT_NAME = "Bổ sung yêu cầu quyền lợi BH";

    /**
     * Thông tin lý do quyền lợi làm claim
     */
    public static final String CLAIMS_REASON_ACCIDENT = "Accident";
    public static final String CLAIMS_REASON_ILLNESS = "Illness";

    /**
     * Thông tin điều trị
     */
    public static final String CLAIMS_TREATMENT_DENTAL = "Dental";
    public static final String CLAIMS_TREATMENT_DRUG = "Drug";
    public static final String CLAIMS_TREATMENT_INPATIENT = "Inpatient";
    public static final String CLAIMS_TREATMENT_OUTPATIENT = "Outpatient";

    public enum TreatmentType {
        ClaimDetails("Chi tiết quyền lợi", "ClaimDetails", ""), HealthCare("Chăm sóc sức khỏe", "HealthCare", ""), Accident("Tai nạn", "Accident",
                ""), IllnessProgression("diễn biến bệnh", "IllnessProgression", ""), IllnessTreatment("cách điều trị", "IllnessTreatment", ""),
        IllnessCurrently("Bệnh", "IllnessCurrently", ""), Death("Tử vong", "Death", "");

        private String stringName;
        private String stringValue;
        private String ID;

        private TreatmentType(String toString, String value, String toID) {
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

        public static TreatmentType fromValue(String value) {
            for (TreatmentType mo : values()) {
                if (mo.stringValue.equals(value)) {
                    return mo;
                }
            }
            return ClaimDetails;
        }
    }

    /**
     * Action tax invice
     */
    public static final String ACTION_TAXINVOICE_LIST = "TaxInvoiceList";
    public static final String ACTION_TAXINVOICE_DETAIL = "TaxInvoiceDetail";

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
        public static final String CATEGORY_QUANTITY = "category_quantity";
        public static final String CATEGORY_PRICE = "category_price";
        public static final String SUB_ID = "sub_id";
    }
    /* End value constant for Expandable ListView */

    /**
     * Intent key for policy No
     */
    public static final String INT_KEY_POLICY_NO = "KEY_POLICY_NO";
    public static final String INT_KEY_PAYMENT_MODEL = "KEY_PAYMENT_MODEL";

    /**
     * intent key for point account
     */
    public static final String INT_KEY_POINT_ACCOUNT = "KEY_POINT_ACCOUNT";

    /**
     * Constant for ID Product Fee
     */
    public static final String CONTS_FEE_SNDRY_AMT_CODE = "18";
    public static final String CONTS_FEE_APL_CODE = "19";
    public static final String CONTS_FEE_OPL_CODE = "20";

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

    /**
     * Enum value for Alteration
     */
    public enum AlterationType {
        PO("Thông tin Bên mua bảo hiểm", "PO", ""), LI("Thông tin Người được bảo hiểm", "LI", ""), BENE("Thông tin Người thụ hưởng", "BEN",
                "");

        private String stringName;
        private String stringValue;
        private String ID;

        private AlterationType(String toString, String value, String toID) {
            stringName = toString;
            stringValue = value;
            ID = toID;
        }

        @Override
        public String toString() {
            return stringName;
        }

        public String toValue() {
            return stringValue;
        }

        public String getID() {
            return ID;
        }

        public static AlterationType fromValue(String value) {
            for (AlterationType mo : values()) {
                if (mo.stringValue.equals(value)) {
                    return mo;
                }
            }
            return PO;
        }
    }

    // enum cho status của một bộ hồ sơ yêu cầu bảo hiểm - proposal
    public enum StatusSubmit {
        CHUAGUI("Chưa gửi", "CHUA_GUI"), DAGUI("Đã gửi", "DA_GUI"), DANGCHODUYET("Đang chờ duyệt", "DANG_CHO_DUYET"), DANGXULY("Đang xử lý", "DANG_XU_LY"), CHOBOSUNG("Chờ bổ sung Thông tin", "CHO_BO_SUNG"), CHONOP_HSG("Chờ nộp hồ sơ gốc", "CHO_NOP_HO_SO_GOC"), TAMDINHCHI("Tạm đình chỉ", "TAM_DINH_CHI"),
        CHAPNHANTHANHTOAN("Chấp nhận thanh toán", "CHAP_NHAN_THANH_TOAN"), TUCHOITHANHTOAN("Từ chối thanh toán", "TU_CHOI_THANH_TOAN"),
        HOANTAT("Hoàn tất", "HOAN_TAT"), TUCHOIGIAIQUYET("Từ chối giải quyết", "TU_CHOI_GIAI_QUYET");

        private String stringName;
        private String stringValue;

        private StatusSubmit(String toString, String value) {
            stringName = toString;
            stringValue = value;
        }

        @Override
        public String toString() {
            return stringName;
        }

        public String getStringValue() {
            return stringValue;
        }

        public static StatusSubmit fromValue(String value) {
            for (StatusSubmit color : values()) {
                if (color.stringValue.equals(value)) {
                    return color;
                }
            }
            return CHUAGUI;
        }
    }

    // enum status của document trong bo hồ sơ
    public enum StatusDoc {
        DANG_TAO_HO_SO("Đang tạo hồ sơ", "DANG_TAO_HO_SO"),

        CHUA_DU_GIAY_TO("Chưa đủ giấy tờ", "CHUA_DU_GIAY_TO"), CHUA_NOP_HO_SO("Chưa nộp hồ sơ",
                "CHUA_NOP_HO_SO"), CHO_BO_SUNG("Chờ bổ sung", "CHO_BO_SUNG"), NOP_HO_SO_THAT_BAI("Nộp hồ sơ thất bại",
                "NOP_HO_SO_THAT_BAI"), NOP_BO_SUNG_THAT_BAI("Nộp bổ sung thất bại",
                "NOP_BO_SUNG_THAT_BAI"), DANG_NOP_HO_SO("Đang nộp hồ sơ",
                "DANG_NOP_HO_SO"), DANG_NOP_BO_SUNG("Đang nộp bổ sung",
                "DANG_NOP_BO_SUNG"), DA_NOP_HO_SO("Đã nộp hồ sơ",
                "DA_NOP_HO_SO"), DA_BO_SUNG("Đã bổ sung",
                "DA_BO_SUNG"), TU_CHOI_BOI_PS("Từ chối bởi phòng phát hành HĐ",
                "TU_CHOI_BOI_PS");

        private String stringName;
        private String stringValue;

        private StatusDoc(String toString, String value) {
            stringName = toString;
            stringValue = value;
        }

        @Override
        public String toString() {
            return stringName;
        }

        public String getStringValue() {
            return stringValue;
        }

        public static StatusDoc fromValue(String value) {
            for (StatusDoc color : values()) {
                if (color.stringValue.equals(value)) {
                    return color;
                }
            }
            return DANG_TAO_HO_SO;
        }
    }

}
