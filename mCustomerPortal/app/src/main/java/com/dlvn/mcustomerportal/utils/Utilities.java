package com.dlvn.mcustomerportal.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.base.CPortalApplication;
import com.dlvn.mcustomerportal.services.model.request.CartItemModel;
import com.dlvn.mcustomerportal.services.model.response.MasterData_Category;
import com.dlvn.mcustomerportal.services.model.response.ProductLoyaltyModel;
import com.dlvn.mcustomerportal.view.MyCustomDialog;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * @arthor nn.tai
 * @date Oct 3, 2016
 */
public class Utilities {

    /**
     * set Height cho listview item, sau khi set xong sẽ ko cần scrollbar cho
     * listview
     *
     * @param listView
     * @param padding  - padding between items
     * @arthor nn.tai
     * @date Oct 20, 2016 9:43:01 AM
     */
    public static void setListViewHeightBasedOnChildren(ListView listView, int padding) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.EXACTLY);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, null, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight() + padding;
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    /**
     * set Height cho expand listview item, sau khi set xong sẽ ko cần scrollbar
     * cho Expand listview
     *
     * @param listView
     * @param group
     * @arthor nn.tai
     * @date Oct 20, 2016 9:44:53 AM
     */
    public static void setExpListViewHeightForAllChild(ExpandableListView listView, int group) {
        ExpandableListAdapter listAdapter = (ExpandableListAdapter) listView.getExpandableListAdapter();
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.EXACTLY);
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            View groupItem = listAdapter.getGroupView(i, false, null, listView);
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

            totalHeight += groupItem.getMeasuredHeight();

            if (((listView.isGroupExpanded(i)) && (i != group)) || ((!listView.isGroupExpanded(i)) && (i == group))) {
                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                    View listItem = listAdapter.getChildView(i, j, false, null, listView);
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

                    totalHeight += listItem.getMeasuredHeight();

                }
            }
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int height = totalHeight + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        if (height < 10)
            height = 200;
        params.height = height;
        listView.setLayoutParams(params);
        listView.requestLayout();

    }

    /**
     * get File name from Path to file
     *
     * @param path
     * @return
     * @arthor nn.tai
     * @date Oct 20, 2016 9:45:22 AM
     */
    public static String getFileNameFromPath(String path) {
        String result = "";
        String[] s = path.split("/");
        if (s.length > 0)
            result = s[s.length - 1];
        return result;
    }

    /**
     * Hide keyboard has show when focus view
     *
     * @param c
     * @param view
     * @arthor nn.tai
     * @date Oct 20, 2016 9:45:56 AM
     */
    public static void hideSoftInputKeyboard(Context c, View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void hideSoftInputKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    /**
     * Get version OS of device
     *
     * @return
     * @arthor nn.tai
     * @date Oct 25, 2016 3:09:54 PM
     */
    public static String getVersion() {
        String version = String.valueOf(Build.VERSION.SDK_INT);

        String code_release = Build.VERSION.RELEASE;
        if (TextUtils.isEmpty(code_release)) {
            return "No version";
        }

        return "Android-" + code_release;

//        if (version == null || version.isEmpty()) {
//            return "No version";
//        }
//
//        if (version.equals("25"))
//            return "Android7.1";
//        else if (version.equals("24"))
//            return "Android7.0";
//        else if (version.equals("23"))
//            return "Android6.0";
//        else if (version.equals("22"))
//            return "Android5.1";
//        else if (version.equals("21"))
//            return "Android5.0";
//        else if (version.equals("20"))
//            return "Android4.4W";
//        else if (version.equals("19"))
//            return "Android4.4";
//        else if (version.equals("18"))
//            return "Android4.3";
//        else if (version.equals("17"))
//            return "Android4.2";
//        else if (version.equals("16"))
//            return "Android4.1";
//        else if (version.equals("15") || version.equals("14"))
//            return "Android4.0";
//        return version;
    }

    /**
     * get Device Name
     *
     * @return
     * @arthor nn.tai
     * @date Oct 25, 2016 3:12:25 PM
     */
    public static String getDeviceName() {
        try {
            String manufacturer = Build.MANUFACTURER;
            String model = Build.MODEL;

            if (model.contains("Unknown") || manufacturer.contains("Unknown")) {
                return "No find device";
            }

            if (model.startsWith(manufacturer)) {
                return capitalize(model);
            } else {
                return capitalize(manufacturer) + "_" + model;
            }
        } catch (Exception ex) {
            return "No find device";
        }
    }

    /**
     * get Device ID of device
     *
     * @param context
     * @return
     * @arthor nn.tai
     * @date Oct 25, 2016 3:43:50 PM
     */
    public static String getDeviceID(Context context) {
        return Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
    }

    public static String capitalize(String s) {
        if (TextUtils.isEmpty(s)) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    public static String getApplicationHashKey(Context ctx) {
        try {
            PackageInfo info = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String sig = Base64.encodeToString(md.digest(), Base64.DEFAULT).trim();
                if (sig.trim().length() > 0) {
                    return sig;
                }
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
        return "";
    }

    /**
     * @param c
     * @return
     * @author nn.tai
     */
    public static String[] getRequiredPermissions(Context c) {
        String[] permissions = null;
        try {
            permissions = c.getPackageManager().getPackageInfo(c.getPackageName(),
                    PackageManager.GET_PERMISSIONS).requestedPermissions;
        } catch (PackageManager.NameNotFoundException e) {
            myLog.printTrace(e);
        }
        if (permissions == null) {
            return new String[0];
        } else {
            return permissions.clone();
        }
    }


    /**
     * Format number by Locate US
     *
     * @param number
     * @return
     * @arthor nn.tai
     * @date Nov 29, 2016 2:25:15 PM
     */
    public static String formatNumberbyLocate(String number) {
        if (TextUtils.isEmpty(number))
            return "0";
        NumberFormat df = NumberFormat.getNumberInstance(Locale.US);
        try {
            double num = Double.parseDouble(number);
            return df.format(num);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return number;
        }
    }

    /**
     * delete all file and folder in directory in path
     *
     * @param path
     * @arthor nn.tai
     * @date Nov 29, 2016 2:24:44 PM
     */
    public static void deleteAllFileInFolder(String path) {
        if (!TextUtils.isEmpty(path)) {
            File dir = new File(path);
            myLog.W("delete " + dir.getAbsolutePath());
            if (dir.isDirectory()) {
                for (File file : dir.listFiles())
                    deleteAllFileInFolder(file.getAbsolutePath());
            } else if (!dir.delete()) {
                new FileNotFoundException("Failed to delete file: " + dir);
            }
            dir.delete();
        }
    }

    /**
     * process case No INTERNET or NO SERVICE, send user to login screen
     *
     * @param context
     * @param message
     * @arthor nn.tai
     * @date Dec 13, 2016 9:25:00 AM
     */
    public static void processLoginAgain(final Context context, final String message) {
        ((Activity) context).runOnUiThread(new Runnable() {

            @Override
            public void run() {
                try {
                    MyCustomDialog dialog = new MyCustomDialog.Builder(context)
                            .setTitle("Thông báo")
                            .setMessage(message)
                            .setPositiveButton(context.getString(R.string.confirm_ok), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    CPortalApplication.getInstance().reLogin();
                                }
                            }).create();
                    dialog.show();

//                    DialogUtils.showAlertDialogWithCallback(context, message, new DialogInterface.OnClickListener() {
//
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//
//                        }
//                    });
                } catch (Exception e) {
                    myLog.printTrace(e);
                }
            }
        });
    }

    /**
     * Process finish Activity current (Detail Act) to gome Summary Act
     *
     * @param context
     * @param message
     * @arthor nn.tai
     * @date Dec 22, 2016 5:56:25 PM
     */
    public static void processHomeAgain(final Context context, final String message) {
        ((Activity) context).runOnUiThread(new Runnable() {

            @Override
            public void run() {
                try {
                    DialogUtils.showAlertDialogWithCallback(context, message, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            ((Activity) context).finish();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Get location on google map by address
     *
     * @param context
     * @param strAddress
     * @return
     */
    public static LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context, Locale.getDefault());
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return p1;
    }

    /**
     * Display in Google map view a point with lat,long and address
     *
     * @param map
     * @param point
     * @param address
     */
    public static void showMap(GoogleMap map, LatLng point, String address) {
        if (point != null) {
            // create marker
            MarkerOptions marker = new MarkerOptions().position(point).title(address);
            // adding marker
            map.addMarker(marker);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 15.0f));
            // Zoom in, animating the camera.
            map.animateCamera(CameraUpdateFactory.zoomTo(17), 2000, null);
        }
    }

    public static void showMap(GoogleMap map, LatLng point, String address, boolean isMind) {
        if (point != null) {
            // create marker
            MarkerOptions marker = new MarkerOptions().position(point).title(address);
            if (isMind)
                marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ico_mylocation_marker));

            // adding marker
            map.addMarker(marker);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 15.0f));
            // Zoom in, animating the camera.
            map.animateCamera(CameraUpdateFactory.zoomTo(17), 2000, null);
        }
    }


    /**
     * Display Setting alert about enable Location Provider, Ex: GPS
     *
     * @param context
     */
    public static void showSettingsAlertGPS(final Context context) {
        MyCustomDialog.Builder builder = new MyCustomDialog.Builder(context);
        builder.setMessage("Anh/chị chưa mở GPS, vui lòng vào Setting để mở GPS!")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        context.startActivity(intent);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    /**
     * Show settings storage
     *
     * @param context
     * @modify Nov 24, 2017
     * @arthor nn.tai
     */
    public static void showSettingsAlertStorage(final Context context) {
        MyCustomDialog.Builder builder = new MyCustomDialog.Builder(context);
        builder.setMessage(
                "Bộ nhớ trong trên thiết bị anh chị sắp hết dung lượng. Vui lòng kiểm tra và gỡ những ứng dụng không cần thiết để có thêm dung lượng lưu trữ.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(Settings.ACTION_INTERNAL_STORAGE_SETTINGS);
                        context.startActivity(intent);
                    }
                });
        builder.create().show();
    }

    public static void showSettingsAlertConnection(final Context context) {
        MyCustomDialog.Builder builder = new MyCustomDialog.Builder(context);
        builder.setMessage("Anh/chị chưa bật kết nối Internet, vui lòng vào Setting để mở kết nối Internet!")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                        context.startActivity(intent);
                    }
                });
        builder.create().show();
    }

    public static void showAlert(final Context context, String message) {
        if (!((Activity) context).isFinishing()) {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            alertDialog.setMessage(message);
            alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alertDialog.show();
        }
    }

    /**
     * Open app call phone number
     *
     * @param context
     * @param number
     */
    public static void actionCallPhoneNumber(Context context, String number) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
        context.startActivity(intent);
    }

    /**
     * Open mail app
     *
     * @param context
     */
    public static void actionOpenMailApp(Context context) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_APP_EMAIL);
        context.startActivity(intent);
    }

    /**
     * Check a string contain all number
     *
     * @param s
     * @return
     */
    public static boolean checkStringAllNumber(String s) {
        return s.matches(".*\\d+.*");
    }

    /**
     * Valid input email syntax
     *
     * @param email
     * @return
     */
    public static boolean validateEmail(String email) {
        //TODO: Replace this with your own logic
        boolean rs = true;
        if (!email.contains("@"))
            rs = false;

        if (email.length() > 255)
            rs = false;

        return rs;
    }

    /**
     * Check lenght of password
     *
     * @param password
     * @return
     */
    public static boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return (password.length() >= 8 && password.length() <= 32);
    }

    /**
     * Check lenght of phone number
     *
     * @param phone
     * @return
     */
    public static boolean isPhoneNumberValid(String phone) {
        //TODO: Replace this with your own logic
        return (phone.length() >= 8 && phone.length() <= 15);
    }

    /**
     * Valid a string contain special character
     *
     * @param str
     * @return
     */
    public static boolean validateSpecialCharacter(String str) {
        Pattern regex = Pattern.compile("[$&+,:;=\\\\?#|/'<>.^*()%!-]");
//		Pattern regex = Pattern.compile("[$&+,:;=\\\\?@#|/'<>.^*()%!-]");
        return regex.matcher(str).find();
    }

    /**
     * Requests the fine location permission. If a rationale with an additional
     * explanation should be shown to the user, displays a dialog that triggers
     * the request.
     */
    public static void requestPermission(AppCompatActivity activity, int requestId, String permission,
                                         boolean finishActivity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
            // Display a dialog with rationale.
            Utilities.RationaleDialog.newInstance(requestId, finishActivity).show(activity.getSupportFragmentManager(),
                    "dialog");
        } else {
            // Location permission has not been granted yet, request it.
            ActivityCompat.requestPermissions(activity, new String[]{permission}, requestId);

        }
    }

    /**
     * Checks if the result contains a {@link PackageManager#PERMISSION_GRANTED}
     * result for a permission from a runtime permissions request.
     *
     * @see android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback
     */
    public static boolean isPermissionGranted(String[] grantPermissions, int[] grantResults, String permission) {
        for (int i = 0; i < grantPermissions.length; i++) {
            if (permission.equals(grantPermissions[i])) {
                return grantResults[i] == PackageManager.PERMISSION_GRANTED;
            }
        }
        return false;
    }

    /**
     * A dialog that displays a permission denied message.
     */
    public static class PermissionDeniedDialog extends DialogFragment {

        private static final String ARGUMENT_FINISH_ACTIVITY = "finish";

        private boolean mFinishActivity = false;

        /**
         * Creates a new instance of this dialog and optionally finishes the
         * calling Activity when the 'Ok' button is clicked.
         */
        public static PermissionDeniedDialog newInstance(boolean finishActivity) {
            Bundle arguments = new Bundle();
            arguments.putBoolean(ARGUMENT_FINISH_ACTIVITY, finishActivity);

            PermissionDeniedDialog dialog = new PermissionDeniedDialog();
            dialog.setArguments(arguments);
            return dialog;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            mFinishActivity = getArguments().getBoolean(ARGUMENT_FINISH_ACTIVITY);

            return new AlertDialog.Builder(getActivity()).setMessage("Không được phân quyền xác định vị trí.")
                    .setPositiveButton(android.R.string.ok, null).create();
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            super.onDismiss(dialog);
            if (mFinishActivity) {
                Toast.makeText(getActivity(), "Cần cấp quyền để xác định vị trí.", Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        }
    }

    /**
     * A dialog that explains the use of the location permission and requests
     * the necessary permission.
     * <p>
     * The activity should implement
     * {@link android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback}
     * to handle permit or denial of this permission request.
     */
    public static class RationaleDialog extends DialogFragment {

        private static final String ARGUMENT_PERMISSION_REQUEST_CODE = "requestCode";

        private static final String ARGUMENT_FINISH_ACTIVITY = "finish";

        private boolean mFinishActivity = false;

        /**
         * Creates a new instance of a dialog displaying the rationale for the
         * use of the location permission.
         * <p>
         * The permission is requested after clicking 'ok'.
         *
         * @param requestCode    Id of the request that is used to request the permission.
         *                       It is returned to the
         *                       {@link android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback}.
         * @param finishActivity Whether the calling Activity should be finished if the
         *                       dialog is cancelled.
         */
        public static RationaleDialog newInstance(int requestCode, boolean finishActivity) {
            Bundle arguments = new Bundle();
            arguments.putInt(ARGUMENT_PERMISSION_REQUEST_CODE, requestCode);
            arguments.putBoolean(ARGUMENT_FINISH_ACTIVITY, finishActivity);
            RationaleDialog dialog = new RationaleDialog();
            dialog.setArguments(arguments);
            return dialog;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Bundle arguments = getArguments();
            final int requestCode = arguments.getInt(ARGUMENT_PERMISSION_REQUEST_CODE);
            mFinishActivity = arguments.getBoolean(ARGUMENT_FINISH_ACTIVITY);

            return new AlertDialog.Builder(getActivity()).setMessage("Cấp quyền xác định vị trí.")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // After click on Ok, request the permission.
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, requestCode);
                            // Do not finish the Activity while requesting
                            // permission.
                            mFinishActivity = false;
                        }
                    }).setNegativeButton(android.R.string.cancel, null).create();
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            super.onDismiss(dialog);
            if (mFinishActivity) {
                Toast.makeText(getActivity(), "Cần cấp quyền xác định vị trí", Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        }
    }

    /**
     * method to reformat the input money for mCP
     *
     * @param number
     * @return money + "%"
     */
    public static String formatMoneyToVND(String number) {
        String result = "";
        if (number == null || number.equals("0") || number.equals(""))
            return "0";
        try {
            DecimalFormat formatter = new DecimalFormat("#,###,###");
            result = formatter.format(Double.parseDouble(number));
            return result;
        } catch (Exception ex) {
            myLog.printTrace(ex);
            return "0";
        }
    }

    /**
     * method to reformat the input money for mCP
     *
     * @return money + "%"
     */
    public static String formatMoneyToVND(long number) {
        String result = "";
        if (number == 0)
            return "0";
        try {
            DecimalFormat formatter = new DecimalFormat("#,###,###");
            result = formatter.format(Double.parseDouble(number + ""))+" VNĐ";
            return result;
        } catch (Exception ex) {
            myLog.printTrace(ex);
            return "0";
        }
    }

    public static String formatMoneyToPoint(long str) {
        double value = (double) str / 1000;
        DecimalFormat formatter = new DecimalFormat("#,###,###.#");
        return formatter.format(Double.parseDouble(value + "")) + " điểm.";
    }


    /**
     * @param html
     * @return
     * @author nn.tai
     * control function parse Html by version OS
     */
    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(html);
        }
    }

    /**
     * format distance to km
     *
     * @param distance
     * @return
     * @arthor nn.tai
     * @modify Mar 18, 2018
     */
    public static final String FormatDistance(Double distance) {
        String result = "";
        if (distance == 0.0)
            return "0";
        try {
            result = String.format("%.2f", distance);
            return result;
        } catch (Exception ex) {
            return "";
        }
    }

    /**
     * @param cart
     * @return
     * @author nn.tai - count total item in Cart list
     */
    public static int countItemInCart(List<CartItemModel> cart) {
        int count = 0;
        if (cart != null)
            for (CartItemModel item : cart) {
                for (ProductLoyaltyModel mo : item.getLsItems())
                    count += mo.getQuantity();
            }

        return count;
    }

    public static List<ProductLoyaltyModel> addProductToCart(ProductLoyaltyModel item, List<ProductLoyaltyModel> lsCart) {
        boolean isExist = false;
        try {
            for (int i = 0; i < lsCart.size(); i++) {
                if (lsCart.get(i).getProductID().equals(item.getProductID())) {
                    isExist = true;
                    int quantity = lsCart.get(i).getQuantity();
                    lsCart.get(i).setQuantity(quantity + item.getQuantity());
                }
            }

            if (!isExist)
                lsCart.add(item);
        } catch (Exception e) {
            myLog.printTrace(e);
        }
        return lsCart;
    }
}
