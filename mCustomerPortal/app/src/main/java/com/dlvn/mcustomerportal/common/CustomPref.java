package com.dlvn.mcustomerportal.common;

import android.content.Context;
import android.text.TextUtils;

import com.dlvn.mcustomerportal.database.DataClient;
import com.dlvn.mcustomerportal.services.model.claims.ClaimsFromRequest;
import com.dlvn.mcustomerportal.services.model.request.CartModel;
import com.dlvn.mcustomerportal.services.model.response.ClientProfile;
import com.dlvn.mcustomerportal.utils.CustomPreferences;
import com.dlvn.mcustomerportal.utils.Utilities;
import com.dlvn.mcustomerportal.utils.myLog;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class CustomPref {

    public static final String haveLOGIN = "haveLogin";

    public static final String HAVE_AUTH_WITH_FINGER = "HAVE_AUTH_WITH_FINGER";
    public static final String FINGER_AUTHENTICATE_KEY = "FINGER_AUTHENTICATE_KEY";

    public static final String isRefreshing = "isRefreshing";

    public static final String STORAGE_SHOPPING_CART = "STORAGE_SHOPPING_CART";

    public static final String STORAGE_CLAIMS_TEMPSAVED = "STORAGE_CLAIMS_TEMPSAVED";

    public static final String FIREBASE_INSTANCE_TOKEN = "FIREBASE_INSTANCE_TOKEN";

    public static final String TIME_GENERATE_OTP = "TIME_GENERATE_OTP";
    public static final String TYPE_GENERATE_OTP = "TYPE_GENERATE_OTP";


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
     * Save token for request API
     *
     * @param context
     * @param token
     */
    public static void saveAPIToken(Context context, String token) {
        myLog.e("Save APIToken into Preferences");
        CustomPreferences.putString(context, ClientProfile.TOKEN_LOGIN, token);
    }

    /**
     * Save point of client into Pref
     *
     * @param context
     * @param point
     */
    public static void saveUserPoint(Context context, float point) {
        CustomPreferences.putFloat(context, ClientProfile.USER_POINT, point);
    }

    /**
     * @param context
     * @param rank
     */
    public static void saveUserRank(Context context, String rank) {
        CustomPreferences.putString(context, ClientProfile.USER_RANK, rank);
    }


    /**
     * Save thong tin user login lai
     *
     * @param context
     * @param user
     * @arthor nn.tai
     * @date Oct 26, 2016 9:46:05 AM
     */
    public static boolean saveUserLogin(Context context, ClientProfile user) {
        try {

            if (!user.getClientID().equalsIgnoreCase(getUserID(context)))
                setAuthFinger(context, false);

//            if (!TextUtils.isEmpty(user.getLoginName())) {
            CustomPreferences.putString(context, ClientProfile.USER_NAME, user.getLoginName());
            CustomPreferences.putString(context, ClientProfile.USER_ID, user.getClientID());
//            } else {
//                CustomPreferences.putString(context, ClientProfile.USER_NAME, user.getClientID());
//                CustomPreferences.putString(context, ClientProfile.USER_ID, user.getClientID());
//            }

            CustomPreferences.putString(context, ClientProfile.PASSWORD, user.getPassword());
            CustomPreferences.putString(context, ClientProfile.TOKEN_LOGIN, user.getaPIToken());

            CustomPreferences.putString(context, ClientProfile.FULLNAME, user.getFullName());
            CustomPreferences.putString(context, ClientProfile.GENDER, user.getGender());
            CustomPreferences.putString(context, ClientProfile.ADDRESS, user.getAddress());

            if (!TextUtils.isEmpty(user.getEmail()))
                CustomPreferences.putString(context, ClientProfile.EMAIL, user.getEmail());
            else if (Utilities.validateEmail(user.getLoginName()))
                CustomPreferences.putString(context, ClientProfile.EMAIL, user.getLoginName());

            CustomPreferences.putString(context, ClientProfile.PHONE_NUMBER, user.getCellPhone());
            CustomPreferences.putString(context, ClientProfile.FACEBOOK, user.getLinkFaceBook());
            CustomPreferences.putString(context, ClientProfile.GOOGLE, user.getLinkGmail());
            CustomPreferences.putString(context, ClientProfile.USER_PHOTO, user.getProfilePhoto());

            CustomPreferences.putString(context, ClientProfile.DAYOFBIRTH, user.getDOB());
            CustomPreferences.putString(context, ClientProfile.USER_POID, user.getPOID());

            CustomPreferences.putString(context, ClientProfile.VERYFY_CELLPHONE, user.getVerifyCellPhone());
            CustomPreferences.putString(context, ClientProfile.VERYFY_EMAIL, user.getVerifyEmail());
            CustomPreferences.putString(context, ClientProfile.VERYFY_TWOFA, user.getVerifyTwoFA());

        } catch (Exception e) {
            myLog.printTrace(e);
            return false;
        }
        return true;
    }

    /**
     * get thong tin user login lai
     *
     * @param context
     * @arthor nn.tai
     * @date Oct 26, 2016 9:46:05 AM
     */
    public static ClientProfile getUserLogin(Context context) {
        ClientProfile user = new ClientProfile();
        try {
            user.setLoginName(getUserName(context));
            user.setClientID(getUserID(context));
            user.setPassword(getPassword(context));
            user.setaPIToken(getAPIToken(context));
            user.setFullName(getFullName(context));
            user.setGender(getGender(context));
            user.setEmail(getEmail(context));
            user.setCellPhone(getPhoneNumber(context));
            user.setLinkFaceBook(getFacebookID(context));
            user.setLinkGmail(getGoogleID(context));
            user.setAddress(CustomPreferences.getString(context, ClientProfile.ADDRESS, ""));
            user.setDOB(CustomPreferences.getString(context, ClientProfile.DAYOFBIRTH, ""));
            user.setPOID(CustomPreferences.getString(context, ClientProfile.USER_POID, ""));
            user.setProfilePhoto(getUserPhoto(context));

            user.setVerifyCellPhone(CustomPreferences.getString(context, ClientProfile.VERYFY_CELLPHONE, "0"));
            user.setVerifyEmail(CustomPreferences.getString(context, ClientProfile.VERYFY_EMAIL, "0"));
            user.setVerifyTwoFA(CustomPreferences.getString(context, ClientProfile.VERYFY_TWOFA, "0"));

        } catch (Exception e) {
            myLog.printTrace(e);
        }
        return user;
    }

    /**
     * clear UserLogin
     *
     * @param context
     * @arthor nn.tai
     * @date Nov 8, 2016 11:14:05 AM
     */
    public static void clearUserLogin(Context context) {
        try {
            //delete all data
            DataClient.getInstance(context).getAppDatabase().clearAllTables();

            CustomPreferences.remove(context, ClientProfile.USER_NAME);
            CustomPreferences.remove(context, ClientProfile.USER_ID);
            CustomPreferences.remove(context, ClientProfile.PASSWORD);
            CustomPreferences.remove(context, ClientProfile.TOKEN_LOGIN);

            CustomPreferences.remove(context, ClientProfile.FULLNAME);
            CustomPreferences.remove(context, ClientProfile.GENDER);
            CustomPreferences.remove(context, ClientProfile.ADDRESS);
            CustomPreferences.remove(context, ClientProfile.EMAIL);
            CustomPreferences.remove(context, ClientProfile.FACEBOOK);
            CustomPreferences.remove(context, ClientProfile.GOOGLE);
            CustomPreferences.remove(context, ClientProfile.PHONE_NUMBER);
            CustomPreferences.remove(context, ClientProfile.DAYOFBIRTH);
            CustomPreferences.remove(context, ClientProfile.USER_POID);
            CustomPreferences.remove(context, ClientProfile.USER_POINT);
            CustomPreferences.remove(context, ClientProfile.USER_RANK);
            CustomPreferences.remove(context, ClientProfile.USER_PHOTO);

            CustomPreferences.remove(context, ClientProfile.VERYFY_CELLPHONE);
            CustomPreferences.remove(context, ClientProfile.VERYFY_EMAIL);
            CustomPreferences.remove(context, ClientProfile.VERYFY_TWOFA);
        } catch (Exception e) {
            myLog.printTrace(e);
        }
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
        return CustomPreferences.getString(context, ClientProfile.USER_NAME, "");
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
        return CustomPreferences.getString(context, ClientProfile.USER_ID, "");
    }

    public static String getPassword(Context context) {
        return CustomPreferences.getString(context, ClientProfile.PASSWORD, "");
    }

    public static Long getUserAmount(Context context) {
        return CustomPreferences.getLong(context, ClientProfile.USER_AMOUNT, 0);
    }

    public static float getUserPoint(Context context) {
        return CustomPreferences.getFloat(context, ClientProfile.USER_POINT, 0);
    }

    public static String getUserRank(Context context) {
        return CustomPreferences.getString(context, ClientProfile.USER_RANK, "");
    }

    public static String getFullName(Context context) {
        return CustomPreferences.getString(context, ClientProfile.FULLNAME, "");
    }

    public static String getGender(Context context) {
        return CustomPreferences.getString(context, ClientProfile.GENDER, "");
    }

    public static String getEmail(Context context) {
        return CustomPreferences.getString(context, ClientProfile.EMAIL, "");
    }

    public static void setEmail(Context context, String email) {
        CustomPreferences.putString(context, ClientProfile.EMAIL, email);
    }

    public static String getFacebookID(Context context) {
        return CustomPreferences.getString(context, ClientProfile.FACEBOOK, "");
    }

    public static void setFacebookID(Context context, String facebookID) {
        CustomPreferences.putString(context, ClientProfile.FACEBOOK, facebookID);
    }

    public static String getGoogleID(Context context) {
        return CustomPreferences.getString(context, ClientProfile.GOOGLE, "");
    }

    public static void setGoogleID(Context context, String googleID) {
        CustomPreferences.putString(context, ClientProfile.GOOGLE, googleID);
    }

    public static String getPhoneNumber(Context context) {
        return CustomPreferences.getString(context, ClientProfile.PHONE_NUMBER, "");
    }

    public static void setPhoneNumber(Context context, String phone) {
        CustomPreferences.putString(context, ClientProfile.PHONE_NUMBER, phone);
    }

    public static String getUserPhoto(Context context) {
        return CustomPreferences.getString(context, ClientProfile.USER_PHOTO, "");
    }

    public static void setUserPhoto(Context context, String path) {
        CustomPreferences.putString(context, ClientProfile.USER_PHOTO, path);
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
        return CustomPreferences.getString(context, ClientProfile.TOKEN_LOGIN, "");
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

    /**
     * Save Shopping Cart to Preferences by json
     *
     * @param context
     * @param cart
     * @return
     */
    public static boolean saveShoppingCart(Context context, CartModel cart) {

        Gson gson = new Gson();
        try {
            if (cart != null) {
                String json = gson.toJson(cart);
                myLog.e(context.getClass().getName(), "save Cart json = " + json);
                CustomPreferences.putString(context, STORAGE_SHOPPING_CART, json);
            }
        } catch (Exception e) {
            myLog.printTrace(e);
            return false;
        }
        return true;
    }

    /**
     * Get Shopping Cart from Preferences by json
     *
     * @param context
     * @return
     */
    public static CartModel getShoppingCart(Context context) {
        Gson gson = new Gson();
        CartModel container = new CartModel();

        String json = CustomPreferences.getString(context, STORAGE_SHOPPING_CART, "{}");
        myLog.e(context.getClass().getName(), "load cart json = " + json);

        try {
            container = gson.fromJson(json, CartModel.class);
        } catch (JsonSyntaxException e) {
            myLog.printTrace(e);
        }

        return container;
    }

    /**
     * @param context
     * @return
     * @author nn.tai
     * Check Preferences have use Finger Print to authen app
     */
    public static boolean haveAuthFinger(Context context) {
        return CustomPreferences.getBoolean(context, HAVE_AUTH_WITH_FINGER, false);
    }

    /**
     * @param context
     * @param haveFinger : true OR false
     * @author nn.tai
     */
    public static void setAuthFinger(Context context, boolean haveFinger) {
        CustomPreferences.putBoolean(context, HAVE_AUTH_WITH_FINGER, haveFinger);
    }

    /**
     * @param context
     * @return
     * @author nn.tai
     * @modify 11/09/2018
     */
    public static String getAuthFingerKey(Context context) {
        return CustomPreferences.getString(context, FINGER_AUTHENTICATE_KEY, "");
    }

    public static void setAuthFingerKey(Context context, String authenKey) {
        CustomPreferences.putString(context, FINGER_AUTHENTICATE_KEY, authenKey);
    }

    /**
     * @param context
     * @return
     * @author nn.tai
     * @modify 24/09/2018
     */
    public static String getFirebaseToken(Context context) {
        return CustomPreferences.getString(context, FIREBASE_INSTANCE_TOKEN, "");
    }

    public static void setFirebaseToken(Context context, String token) {
        CustomPreferences.putString(context, FIREBASE_INSTANCE_TOKEN, token);
    }

    /**
     * Verify email
     * Value: 0 - not verify; 1- verify
     *
     * @param context
     * @return
     */
    public static boolean haveVerifyEmail(Context context) {
        return CustomPreferences.getString(context, ClientProfile.VERYFY_EMAIL, "0").equalsIgnoreCase("1");
    }

    public static void setVerifyEmail(Context context, String verify) {
        CustomPreferences.putString(context, ClientProfile.VERYFY_EMAIL, verify);
    }

    /**
     * Verify cellphone
     * Value : 0 - not verify *** 1 - verify
     *
     * @param context
     * @return
     */
    public static boolean haveVerifyCellphone(Context context) {
        return CustomPreferences.getString(context, ClientProfile.VERYFY_CELLPHONE, "0").equalsIgnoreCase("1");
    }

    public static void setVerifyCellphone(Context context, String verify) {
        CustomPreferences.putString(context, ClientProfile.VERYFY_CELLPHONE, verify);
    }

    /**
     * Verify 2 factory authenticates
     *
     * @param context
     * @return
     */
    public static boolean haveVerifyTwoFA(Context context) {
        return CustomPreferences.getString(context, ClientProfile.VERYFY_TWOFA, "0").equalsIgnoreCase("1");
    }

    public static void setVerifyTwoFA(Context context, String verify) {
        CustomPreferences.putString(context, ClientProfile.VERYFY_TWOFA, verify);
    }

    /**
     * @param context : get time generate OTP
     * @return
     */
    public static long getTimeGenerateOTP(Context context) {
        return CustomPreferences.getLong(context, TIME_GENERATE_OTP, 0);
    }

    public static void setTimeGenerateOTP(Context context, long time) {
        CustomPreferences.putLong(context, TIME_GENERATE_OTP, time);
    }

    /**
     * @param context : get type generate OTP
     * @return
     */
    public static long getTypeGenerateOTP(Context context) {
        return CustomPreferences.getLong(context, TYPE_GENERATE_OTP, 0);
    }

    public static void setTypeGenerateOTP(Context context, long type) {
        CustomPreferences.putLong(context, TYPE_GENERATE_OTP, type);
    }

    /**
     * Save tempsaved items claims submiss yet
     *
     * @param context
     * @param claims
     * @return
     */
    public static boolean saveClaimsTemp(Context context, ClaimsFromRequest claims) {

        Gson gson = new Gson();
        try {
            if (claims != null) {
                String json = gson.toJson(claims);
                myLog.e(context.getClass().getName(), "save Claims json = " + json);
                CustomPreferences.putString(context, STORAGE_CLAIMS_TEMPSAVED, json);
            } else {
                myLog.e(context.getClass().getName(), "save Claims json = null");
                CustomPreferences.putString(context, STORAGE_CLAIMS_TEMPSAVED, null);
            }
        } catch (Exception e) {
            myLog.printTrace(e);
            return false;
        }
        return true;
    }

    /**
     * get Claims Tempsaved from Preference
     *
     * @param context
     * @return
     */
    public static ClaimsFromRequest getClaimsTemp(Context context) {
        Gson gson = new Gson();
        ClaimsFromRequest container = null;

        String json = CustomPreferences.getString(context, STORAGE_CLAIMS_TEMPSAVED, null);
        myLog.e(context.getClass().getName(), "load Claims json = " + json);

        try {
            container = gson.fromJson(json, ClaimsFromRequest.class);
        } catch (JsonSyntaxException e) {
            myLog.printTrace(e);
        }
        return container;
    }
}
