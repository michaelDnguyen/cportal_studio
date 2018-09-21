package com.dlvn.mcustomerportal.common;

import android.content.Context;
import android.text.TextUtils;

import com.dlvn.mcustomerportal.services.model.request.CartModel;
import com.dlvn.mcustomerportal.services.model.response.ClientProfile;
import com.dlvn.mcustomerportal.utils.CustomPreferences;
import com.dlvn.mcustomerportal.utils.myLog;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class CustomPref {

    public static final String haveLOGIN = "haveLogin";

    public static final String HAVE_AUTH_WITH_FINGER = "HAVE_AUTH_WITH_FINGER";
    public static final String FINGER_AUTHENTICATE_KEY = "FINGER_AUTHENTICATE_KEY";

    public static final String isRefreshing = "isRefreshing";

    public static final String STORAGE_SHOPPING_CART = "STORAGE_SHOPPING_CART";

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
    public static void saveToken(Context context, String token) {
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

            if (!TextUtils.isEmpty(user.getLoginName())) {
                CustomPreferences.putString(context, ClientProfile.USER_NAME, user.getLoginName());
                CustomPreferences.putString(context, ClientProfile.USER_ID, user.getClientID());

            } else {
                CustomPreferences.putString(context, ClientProfile.USER_NAME, user.getClientID());
                CustomPreferences.putString(context, ClientProfile.USER_ID, user.getClientID());
            }
            CustomPreferences.putString(context, ClientProfile.PASSWORD, user.getPassword());
            CustomPreferences.putString(context, ClientProfile.TOKEN_LOGIN, user.getaPIToken());

            CustomPreferences.putString(context, ClientProfile.FULLNAME, user.getFullName());
            CustomPreferences.putString(context, ClientProfile.GENDER, user.getGender());
            CustomPreferences.putString(context, ClientProfile.ADDRESS, user.getAddress());
            CustomPreferences.putString(context, ClientProfile.EMAIL, user.getEmail());
            CustomPreferences.putString(context, ClientProfile.PHONE_NUMBER, user.getCellPhone());
            CustomPreferences.putString(context, ClientProfile.FACEBOOK, user.getLinkFaceBook());
            CustomPreferences.putString(context, ClientProfile.GOOGLE, user.getLinkGmail());

            CustomPreferences.putString(context, ClientProfile.DAYOFBIRTH, user.getDOB());
            CustomPreferences.putString(context, ClientProfile.USER_POID, user.getPOID());

//        CustomPreferences.putInt(context, ClientProfile.USER_POINT, user.getPoint());
//        CustomPreferences.putInt(context, ClientProfile.USER_CONTRACT, user.getNumberContract());
//        CustomPreferences.putLong(context, ClientProfile.USER_AMOUNT, user.getAmountContract());
//        CustomPreferences.putString(context, ClientProfile.USER_PROPOSAL, user.getProposalNo());
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
            CustomPreferences.clear(context, ClientProfile.USER_NAME);
            CustomPreferences.clear(context, ClientProfile.USER_ID);
            CustomPreferences.clear(context, ClientProfile.PASSWORD);
            CustomPreferences.clear(context, ClientProfile.TOKEN_LOGIN);

            CustomPreferences.clear(context, ClientProfile.FULLNAME);
            CustomPreferences.clear(context, ClientProfile.GENDER);
            CustomPreferences.clear(context, ClientProfile.ADDRESS);
            CustomPreferences.clear(context, ClientProfile.EMAIL);
            CustomPreferences.clear(context, ClientProfile.FACEBOOK);
            CustomPreferences.clear(context, ClientProfile.GOOGLE);
            CustomPreferences.clear(context, ClientProfile.PHONE_NUMBER);
            CustomPreferences.clear(context, ClientProfile.DAYOFBIRTH);
            CustomPreferences.clear(context, ClientProfile.USER_POID);
            CustomPreferences.clear(context, ClientProfile.USER_POINT);
            CustomPreferences.clear(context, ClientProfile.USER_RANK);
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

    public static String getFacebookID(Context context) {
        return CustomPreferences.getString(context, ClientProfile.FACEBOOK, "");
    }

    public static String getGoogleID(Context context) {
        return CustomPreferences.getString(context, ClientProfile.GOOGLE, "");
    }

    public static String getPhoneNumber(Context context) {
        return CustomPreferences.getString(context, ClientProfile.PHONE_NUMBER, "");
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
                myLog.E(context.getClass().getName(), "save Cart json = " + json);
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
        myLog.E(context.getClass().getName(), "load cart json = " + json);

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
}
