package com.dlvn.mcustomerportal.activity.prototype;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.activity.ContactActivity;
import com.dlvn.mcustomerportal.afragment.HomeFragment;
import com.dlvn.mcustomerportal.afragment.LoyaltyProgramFragment;
import com.dlvn.mcustomerportal.afragment.Home2Fragment;
import com.dlvn.mcustomerportal.afragment.InfoContractFragment;
import com.dlvn.mcustomerportal.afragment.NotificationsFragment;
import com.dlvn.mcustomerportal.afragment.SettingsFragment;
import com.dlvn.mcustomerportal.base.BaseActivity;
import com.dlvn.mcustomerportal.common.Constant;
import com.dlvn.mcustomerportal.common.CustomPref;
import com.dlvn.mcustomerportal.services.NetworkUtils;
import com.dlvn.mcustomerportal.services.ServicesGenerator;
import com.dlvn.mcustomerportal.services.ServicesRequest;
import com.dlvn.mcustomerportal.services.model.BaseRequest;
import com.dlvn.mcustomerportal.services.model.request.GetPointByCLIIDRequest;
import com.dlvn.mcustomerportal.services.model.response.GetPointByCLIIDResponse;
import com.dlvn.mcustomerportal.services.model.response.GetPointByCLIIDResult;
import com.dlvn.mcustomerportal.utils.Utilities;
import com.dlvn.mcustomerportal.utils.listerner.OnFragmentInteractionListener;
import com.dlvn.mcustomerportal.utils.myLog;
import com.dlvn.mcustomerportal.view.BottomNavigationBehavior;
import com.dlvn.mcustomerportal.view.MyCustomDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;

import retrofit2.Call;
import retrofit2.Response;

public class DashboardActivity extends BaseActivity implements OnFragmentInteractionListener {

    private static final String TAG = "DashboardActivity";
    /**
     * The code used when requesting permissions
     */
    private static final int PERMISSIONS_REQUEST = 1234;

    private TextView tvHotLine;
    ImageView imvLogoDaiIChi, imvYoutube, imvFacebook, imvMail;

    ServicesRequest svRequester;

    private HashMap<String, Stack<Fragment>> mStacks;
    private String mCurrentTab;

    public static final String TAB_HOME = "tab_home";
    public static final String TAB_CONTRACT = "tab_contract";
    public static final String TAB_FUNDBONUS = "tab_fundbonus";
    public static final String TAB_NOTIFICATIONS = "tab_notifications";
    public static final String TAB_OTHER = "tab_other";
    LinearLayout lloHeader;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    selectedTab(TAB_HOME);
                    return true;
                case R.id.navigation_contract:
                    if (CustomPref.haveLogin(DashboardActivity.this)) {
//                        if (!TextUtils.isEmpty(CustomPref.getUserID(DashboardActivity.this))) {
                        selectedTab(TAB_CONTRACT);
                        return true;
//                        } else {
//                            Utilities.showDialogAlertLoginDLVNAcc(DashboardActivity.this);
//                            return false;
//                        }
                    } else {
                        Utilities.showDialogAlertLoginNormal(DashboardActivity.this);
                        return false;
                    }

                case R.id.navigation_fundbonus:
                    if (CustomPref.haveLogin(DashboardActivity.this)) {
                        if (!TextUtils.isEmpty(CustomPref.getUserID(DashboardActivity.this))) {
                            selectedTab(TAB_FUNDBONUS);
                            return true;
                        } else {
                            Utilities.showDialogAlertLoginDLVNAcc(DashboardActivity.this);
                            return false;
                        }
                    } else {
                        Utilities.showDialogAlertLoginNormal(DashboardActivity.this);
                        return false;
                    }
                case R.id.navigation_notifications:
                    if (CustomPref.haveLogin(DashboardActivity.this)) {
                        if (!TextUtils.isEmpty(CustomPref.getUserID(DashboardActivity.this))) {
                            selectedTab(TAB_NOTIFICATIONS);
                            return true;
                        } else {
                            Utilities.showDialogAlertLoginDLVNAcc(DashboardActivity.this);
                            return false;
                        }
                    } else {
                        Utilities.showDialogAlertLoginNormal(DashboardActivity.this);
                        return false;
                    }
                case R.id.navigation_other:
                    selectedTab(TAB_OTHER);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        svRequester = ServicesGenerator.createService(ServicesRequest.class);
        if (FirebaseInstanceId.getInstance() != null) {
            myLog.e(TAG, " tokem from Firebase = " + FirebaseInstanceId.getInstance().getToken());
        }

        /**
         * FCM get token
         */
        // Get token
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            myLog.e(TAG, "getInstanceId failed " + task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        if (!TextUtils.isEmpty(token)) {
                            CustomPref.setFirebaseToken(DashboardActivity.this, token);
                        } else
                            CustomPref.setFirebaseToken(DashboardActivity.this, null);
                        // Log and toast
                        myLog.e(TAG, "FCM Registration token = " + token);
                    }
                });

        // check notification push page
        try {
            if (getIntent().getExtras() != null) {
                if (getIntent().getExtras().containsKey(Constant.FCM_DESTINATION_ID)) {
                    int code = getIntent().getExtras().getInt(Constant.FCM_DESTINATION_ID);
                    myLog.e(TAG, "Notification Code = " + code);
                    Intent myintent = null;
                    switch (code) {
                        case 1:
                            break;
                        case 2:
                            break;
                        case 3:
                            break;
                        case 4:
                            myintent = new Intent(this, ClaimsHistoryActivity.class);

                            break;
                        case 5:
                            break;
                        case 6:
                            break;
                        case 7:
                            break;

                        default:
                            myintent = new Intent();
                            break;
                    }
                    startActivity(myintent);
                }
            }
        } catch (Exception e) {
            myLog.printTrace(e);
        }

        //TODO: declare lloHeader
        lloHeader = findViewById(R.id.lloHeader);

        final BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
//        Utilities.disableShiftMode(navigation);

//        BottomNavigationMenuView menuView = (BottomNavigationMenuView) navigation.getChildAt(0);
//        if (menuView.getChildCount() == 5) {
//            final View iconView = menuView.getChildAt(2).findViewById(android.support.design.R.id.icon);
//            final ViewGroup.LayoutParams layoutParams = iconView.getLayoutParams();
//            final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
//            // set your height here
//            layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32, displayMetrics);
//            // set your width here
//            layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32, displayMetrics);
//            iconView.setLayoutParams(layoutParams);
//        }

        // attaching bottom sheet behaviour - hide / show on scroll
//        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) navigation.getLayoutParams();
//        layoutParams.setBehavior(new BottomNavigationBehavior());

        //init stacks for Fragment
        mStacks = new HashMap<String, Stack<Fragment>>();
        mStacks.put(TAB_HOME, new Stack<Fragment>());
        mStacks.put(TAB_CONTRACT, new Stack<Fragment>());
        mStacks.put(TAB_FUNDBONUS, new Stack<Fragment>());
        mStacks.put(TAB_NOTIFICATIONS, new Stack<Fragment>());
        mStacks.put(TAB_OTHER, new Stack<Fragment>());

        //default open Home Page
        selectedTab(TAB_HOME);
        //get Hash key of App
        try {
            myLog.e("App hashKey = " + Utilities.getApplicationHashKey(DashboardActivity.this));
        } catch (Exception e) {
            myLog.printTrace(e);
        }

        //check permission required
        if (Build.VERSION.SDK_INT >= 23) {
            checkPermissions();
        }

        //Nếu có login & có DLVN account thì getPoint sẵn
        if (CustomPref.haveLogin(this))
            if (!TextUtils.isEmpty(CustomPref.getUserID(this)))
                if (NetworkUtils.isConnected(this))
                    new getClientPointTask(this).execute();

        /**
         * Action for youtube,facebook,mail
         */
        tvHotLine = findViewById(R.id.tvHotLine);
        imvLogoDaiIChi = findViewById(R.id.imvLogoDaiIChi);
        imvYoutube = findViewById(R.id.imvYoutube);
        imvFacebook = findViewById(R.id.imvFacebook);
        imvMail = findViewById(R.id.imvMail);

        tvHotLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyCustomDialog dialog = new MyCustomDialog.Builder(DashboardActivity.this)
                        .setMessage(getString(R.string.message_conform_call_customer_service))
                        .setPositiveButton(getString(R.string.confirm_yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Utilities.actionCallPhoneNumber(DashboardActivity.this, Constant.PHONE_CUSTOMER_SERVICE);
                            }
                        }).setNegativeButton(getString(R.string.confirm_no), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create();
                dialog.show();
            }
        });
        imvMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, ContactActivity.class));
            }
        });

        imvYoutube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilities.actionOpenYoutubeApp(DashboardActivity.this);
            }
        });

        imvFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilities.actionOpenFacebookApp(DashboardActivity.this);
            }
        });

        imvLogoDaiIChi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mCurrentTab.equals(TAB_HOME)) {
                    selectedTab(TAB_HOME);
                    navigation.setSelectedItemId(R.id.navigation_home);
                }
            }
        });


        /**
         * Check if user not have email OR Phone, open screen request input it
         */
        if (CustomPref.haveLogin(this))
            if (TextUtils.isEmpty(CustomPref.getEmail(this)) || TextUtils.isEmpty(CustomPref.getPhoneNumber(this))) {
                Intent intent = new Intent(this, SettingsActivity.class);
                intent.putExtra(SettingsActivity.KEY_SETTINGS_TYPE, SettingsActivity.SETTING_UPDATE_PROFILE);
                startActivity(intent);
            }
    }

    private void gotoFragment(Fragment selectedFragment) {
        myLog.e("gotoFragment");
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_container, selectedFragment);
        fragmentTransaction.commit();
    }

    private void selectedTab(String tabId) {
        myLog.e("selectedTab " + tabId);
        mCurrentTab = tabId;

        if (mStacks.get(tabId).size() == 0) {
            /*
             *    First time this tab is selected. So add first fragment of that tab.
             *    Dont need animation, so that argument is false.
             *    We are adding a new fragment which is not present in stack. So add to stack is true.
             */
            if (tabId.equals(TAB_HOME)) {
                pushFragments(tabId, new HomeFragment(), true);
            } else if (tabId.equals(TAB_CONTRACT)) {
                pushFragments(tabId, new InfoContractFragment(), true);
            } else if (tabId.equals(TAB_FUNDBONUS)) {
                pushFragments(tabId, new LoyaltyProgramFragment(), true);
            } else if (tabId.equals(TAB_NOTIFICATIONS)) {
                pushFragments(tabId, new NotificationsFragment(), true);
            } else if (tabId.equals(TAB_OTHER)) {
                pushFragments(tabId, new SettingsFragment(), true);
            }
        } else {
            /*
             *    We are switching tabs, and target tab is already has atleast one fragment.
             *    No need of animation, no need of stack pushing. Just show the target fragment
             */
            pushFragments(tabId, mStacks.get(tabId).lastElement(), false);
        }
    }

    public void pushFragments(String tag, Fragment fragment, boolean shouldAdd) {
        myLog.e("pushFragment");
        if (shouldAdd)
            mStacks.get(tag).push(fragment);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(R.id.main_container, fragment, fragment.getTag());
        ft.addToBackStack(fragment.getTag());
        ft.commit();
    }

    public void popFragments() {
        myLog.e("popFragment");
        /*
         *    Select the second last fragment in current tab's stack..
         *    which will be shown after the fragment transaction given below
         */
        Fragment fragment = mStacks.get(mCurrentTab).elementAt(mStacks.get(mCurrentTab).size() - 2);

        /*pop current fragment from stack.. */
        mStacks.get(mCurrentTab).pop();

        /* We have the target fragment in hand.. Just show it.. Show a standard navigation animation*/
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(R.id.main_container, fragment);
        ft.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        myLog.e("Change " + newConfig);
    }

    @Override
    public void onBackPressed() {
        if (mStacks != null)
            if (mStacks.get(mCurrentTab).size() == 1) {
                // We are already showing first fragment of current tab, so when back pressed, we will finish this activity..
                finish();
                return;
            }
//            else
//                finish();
//        else
//            finish();

        /* Goto previous fragment in navigation stack of this tab */
        popFragments();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View v = getCurrentFocus();
        if (v != null && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE)
                && v instanceof EditText) {
            int scrcoords[] = new int[2];
            v.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + v.getLeft() - scrcoords[0];
            float y = ev.getRawY() + v.getTop() - scrcoords[1];

            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom())
                Utilities.hideSoftInputKeyboard(this, v);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onFragmentAddstackListener(String tagId, Fragment frag, boolean shouldAdd) {
        pushFragments(tagId, frag, shouldAdd);
    }

    /**
     * Check if the required permissions have been granted, and
     * {@link #requestPermissions(String[], int)}.
     */
    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermissions() {
        String[] ungrantedPermissions = requiredPermissionsStillNeeded();
        if (ungrantedPermissions.length != 0) {
            requestPermissions(ungrantedPermissions, PERMISSIONS_REQUEST);
        }
    }

    /**
     * Convert the array of required permissions to a {@link } to remove
     * redundant elements. Then remove already granted permissions, and return
     * an array of ungranted permissions.
     */
    @TargetApi(Build.VERSION_CODES.M)
    private String[] requiredPermissionsStillNeeded() {

        Set<String> permissions = new HashSet<String>();
        String[] lsPermission = Utilities.getRequiredPermissions(DashboardActivity.this);
        myLog.e("Permission Required " + lsPermission.toString());
        for (String permission : lsPermission) {
            permissions.add(permission);
        }
        for (Iterator<String> i = permissions.iterator(); i.hasNext(); ) {
            String permission = i.next();
            if (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
                myLog.e(DashboardActivity.class.getSimpleName(),
                        "Permission: " + permission + " already granted.");
                i.remove();
            } else {
                myLog.e(DashboardActivity.class.getSimpleName(),
                        "Permission: " + permission + " not yet granted.");
            }
        }
        return permissions.toArray(new String[permissions.size()]);
    }

    /**
     * See if we now have all of the required dangerous permissions. Otherwise,
     * tell the user that they cannot continue without granting the permissions,
     * and then request the permissions again.
     */
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST) {
//            checkPermissions();
            myLog.e("String[]permission " + permissions + " -- Result = " + grantResults);
        }
    }

    /**
     * Task call API get client point By CLient ID in Background
     */
    public class getClientPointTask extends AsyncTask<Void, Void, Response<GetPointByCLIIDResponse>> {
        Context context;

        public getClientPointTask(Context c) {
            context = c;
        }

        @Override
        protected Response<GetPointByCLIIDResponse> doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            Response<GetPointByCLIIDResponse> result = null;
            try {
                GetPointByCLIIDRequest data = new GetPointByCLIIDRequest();

                data.setClientID(CustomPref.getUserID(context));
                data.setAPIToken(CustomPref.getAPIToken(context));
                data.setDeviceID(Utilities.getDeviceID(context));
                data.setOS(Utilities.getDeviceOS());
                data.setProject(Constant.Project_ID);
                data.setAuthentication(Constant.Project_Authentication);
                data.setUserLogin(CustomPref.getUserName(context));

                BaseRequest request = new BaseRequest();
                request.setJsonDataInput(data);

                Call<GetPointByCLIIDResponse> call = svRequester.GetPointByCLIID(request);
                result = call.execute();

            } catch (Exception e) {
                myLog.printTrace(e);
                return null;
            }

            // TODO: register the new account here.
            return result;
        }

        @Override
        protected void onPostExecute(final Response<GetPointByCLIIDResponse> success) {
            if (success != null) {
                try {
                    if (success.isSuccessful()) {
                        GetPointByCLIIDResponse response = success.body();
                        if (response != null)
                            if (response.getResponse() != null) {
                                GetPointByCLIIDResult result = response.getResponse();
                                if (result != null) {

                                    if (result.getResult() != null && result.getResult().equals("false")) {
                                        //If account not exits --> link to register
                                        myLog.e("LoyaltyProgramFragment", "Get Point: " + result.getErrLog());

//                                        if (result.getNewAPIToken().equalsIgnoreCase("invalidtoken")) {
//                                            Utilities.processLoginAgain(context, getString(R.string.message_alert_relogin));
//                                        }

                                    } else if (result.getResult() != null && result.getResult().equals("true")) {

                                        //Save Token
                                        if (!TextUtils.isEmpty(result.getNewAPIToken()))
                                            CustomPref.saveAPIToken(context, result.getNewAPIToken());

                                        if (result.getPoint() != null) {
                                            String point = result.getPoint();
                                            String rank = result.getClassPO();
                                            CustomPref.saveUserPoint(context, Float.parseFloat(result.getPoint()) / 1000);
                                            CustomPref.saveUserRank(context, rank);
                                        }
                                    }
                                }
                            }
                    }
                } catch (Exception e) {
                    myLog.printTrace(e);
                }
            }
        }
    }
}
