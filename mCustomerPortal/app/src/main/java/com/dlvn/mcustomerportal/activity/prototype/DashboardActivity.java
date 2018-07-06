package com.dlvn.mcustomerportal.activity.prototype;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.afragment.BonusProgramFragment;
import com.dlvn.mcustomerportal.afragment.HomeFragment;
import com.dlvn.mcustomerportal.afragment.InfoContractFragment;
import com.dlvn.mcustomerportal.afragment.NotificationsFragment;
import com.dlvn.mcustomerportal.afragment.SettingsFragment;
import com.dlvn.mcustomerportal.base.BaseActivity;
import com.dlvn.mcustomerportal.utils.Utilities;
import com.dlvn.mcustomerportal.utils.myLog;

import java.util.HashMap;
import java.util.Stack;

public class DashboardActivity extends BaseActivity {

    private TextView mTextMessage;

    private HashMap<String, Stack<Fragment>> mStacks;
    private String mCurrentTab;

    public static final String TAB_HOME = "tab_home";
    public static final String TAB_CONTRACT = "tab_contract";
    public static final String TAB_FUNDBONUS = "tab_fundbonus";
    public static final String TAB_NOTIFICATIONS = "tab_notifications";
    public static final String TAB_OTHER = "tab_other";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    selectedTab(TAB_HOME);
                    return true;
                case R.id.navigation_contract:
                    selectedTab(TAB_CONTRACT);
                    return true;
                case R.id.navigation_fundbonus:
                    selectedTab(TAB_FUNDBONUS);
                    return true;
                case R.id.navigation_notifications:
                    selectedTab(TAB_NOTIFICATIONS);
                    return true;
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

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //init stacks for Fragment
        mStacks = new HashMap<String, Stack<Fragment>>();
        mStacks.put(TAB_HOME, new Stack<Fragment>());
        mStacks.put(TAB_CONTRACT, new Stack<Fragment>());
        mStacks.put(TAB_FUNDBONUS, new Stack<Fragment>());
        mStacks.put(TAB_NOTIFICATIONS, new Stack<Fragment>());
        mStacks.put(TAB_OTHER, new Stack<Fragment>());


        selectedTab(TAB_HOME);
        //get Hash key of App
        try {
            myLog.E("App hashKey = " + Utilities.getApplicationHashKey(DashboardActivity.this));
        } catch (Exception e) {
            myLog.printTrace(e);
        }

    }

    private void gotoFragment(Fragment selectedFragment) {
        myLog.E("gotoFragment");
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_container, selectedFragment);
        fragmentTransaction.commit();
    }

    private void selectedTab(String tabId) {
        myLog.E("selectedTab");
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
                pushFragments(tabId, new BonusProgramFragment(), true);
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
        myLog.E("pushFragment");
        if (shouldAdd)
            mStacks.get(tag).push(fragment);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(R.id.main_container, fragment);
        ft.commit();
    }

    public void popFragments() {
        myLog.E("popFragment");
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
    public void onBackPressed() {
        if (mStacks != null)
            if (mStacks.get(mCurrentTab).size() == 1) {
                // We are already showing first fragment of current tab, so when back pressed, we will finish this activity..
                finish();
                return;
            } else
                finish();
        else
            finish();

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
}
