package com.dlvn.mcustomerportal.activity;

import java.text.NumberFormat;
import java.util.Locale;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.afragment.BonusProgramFragment;
import com.dlvn.mcustomerportal.afragment.ElectricBillFragment;
import com.dlvn.mcustomerportal.afragment.FundUnitPriceFragment;
import com.dlvn.mcustomerportal.afragment.HomeFragment;
import com.dlvn.mcustomerportal.afragment.InfoContractFragment;
import com.dlvn.mcustomerportal.afragment.InfoGeneralFragment;
import com.dlvn.mcustomerportal.afragment.NotificationsFragment;
import com.dlvn.mcustomerportal.afragment.PaymentPolicyFragment;
import com.dlvn.mcustomerportal.afragment.ProductInfoFragment;
import com.dlvn.mcustomerportal.afragment.SettingsFragment;
import com.dlvn.mcustomerportal.common.CustomPref;
import com.dlvn.mcustomerportal.utils.myLog;
import com.dlvn.mcustomerportal.view.MyCustomDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

	private static final String TAG = "HomeActivity";

	private NavigationView navigationView;
	private DrawerLayout drawer;
	private View navHeader;
	private ImageView imgNavHeaderBg, imgProfile;
	private TextView txtName, txtLogin, tvHopDong, tvGTHD, tvPoint;
	private Toolbar toolbar;
	private FloatingActionButton fab;
	private LinearLayout lloProfile;

	// index to identify current nav menu item
	public static int navItemIndex = 0;

	// tags used to attach the fragments
	private static final String TAG_HOME = "home";
	private static final String TAG_TTCHUNG = "InfoGeneral";
	private static final String TAG_TTSANPHAM = "InfoProduct";
	private static final String TAG_TTHOPDONG = "InfoContract";
	private static final String TAG_CTDIEMTHUONG = "BonusProgram";
	private static final String TAG_GIA_DV_QUY = "FundPrice";
	private static final String TAG_TT_TRUCTUYEN = "paymentOnlines";
	private static final String TAG_HOADONDIENTU = "electricBill";
	private static final String TAG_MANGLUOI_VP = "listOffices";
	private static final String TAG_NOTIFICATIONS = "notifications";
	private static final String TAG_SETTINGS = "settings";
	private static final String TAG_LOGOUT = "logout";
	public static String CURRENT_TAG = TAG_HOME;

	// toolbar titles respected to selected nav menu item
	private String[] activityTitles;

	// flag to load home fragment when user presses back key
	private boolean shouldLoadHomeFragOnBackPress = true;
	private Handler mHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setNavigationIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.daiichilife));
		toolbar.setTitleTextColor(Color.RED);
		setSupportActionBar(toolbar);

		// getActionBar().setIcon(R.drawable.daiichilife);

		mHandler = new Handler();

		drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		navigationView = (NavigationView) findViewById(R.id.nav_view);
		fab = (FloatingActionButton) findViewById(R.id.fab);

		// Navigation view header
		navHeader = navigationView.getHeaderView(0);
		txtName = (TextView) navHeader.findViewById(R.id.name);
		txtLogin = (TextView) navHeader.findViewById(R.id.tvLogin);
		tvHopDong = (TextView) navHeader.findViewById(R.id.tvHopDong);
		tvGTHD = (TextView) navHeader.findViewById(R.id.tvTongGiaTri);
		tvPoint = (TextView) navHeader.findViewById(R.id.tvPoint);

		imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);
		imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);
		lloProfile = (LinearLayout) navHeader.findViewById(R.id.lloProfile);

		// load toolbar titles from string resources
		activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// Snackbar.make(view, "Your own action",
				// Snackbar.LENGTH_LONG).setAction("Action", null)
				// .show();
				BonusProgramFragment fragment = new BonusProgramFragment();
				if (fragment != null) {
					FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
					fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
					fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
					fragmentTransaction.addToBackStack(CURRENT_TAG);
					fragmentTransaction.commitAllowingStateLoss();

					view.setVisibility(View.GONE);
				}
			}
		});

		txtLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
				startActivity(intent);
			}
		});

		// load nav menu header data
		loadNavHeader();

		// initializing navigation menu
		setUpNavigationView();

		if (savedInstanceState == null) {
			navItemIndex = 0;
			CURRENT_TAG = TAG_HOME;
			loadHomeFragment();
		}
	}

	/***
	 * Load navigation menu header information like background image, profile
	 * image name, website, notifications action view (dot)
	 */
	private void loadNavHeader() {
		// name, website
		if (CustomPref.haveLogin(this)) {
			txtName.setText(CustomPref.getUserName(this));
			txtLogin.setVisibility(View.GONE);
			lloProfile.setVisibility(View.VISIBLE);

			tvHopDong.setText(CustomPref.getUserContract(this) + "");
			tvGTHD.setText(NumberFormat.getNumberInstance(Locale.US).format(CustomPref.getUserAmount(this)) + " VND");
			tvPoint.setText(CustomPref.getUserPoint(this) + "");

		} else {
			txtName.setText("Guest");
			txtLogin.setText("Login");
			lloProfile.setVisibility(View.GONE);
		}

		// loading header background image
		/*
		 * Glide.with(this).load(urlNavHeaderBg).listener(new
		 * RequestListener<Drawable>() {
		 * 
		 * @Override public boolean onLoadFailed(GlideException arg0, Object
		 * arg1, Target<Drawable> arg2, boolean arg3) { //
		 * progress.setVisibility(View.GONE); myLog.E("Load Image Failed!");
		 * return false; }
		 * 
		 * @Override public boolean onResourceReady(Drawable arg0, Object arg1,
		 * Target<Drawable> arg2, DataSource arg3, boolean arg4) { //
		 * progress.setVisibility(View.GONE); myLog.E("Load Image Ready!");
		 * return false; } }).into(imgNavHeaderBg);
		 */
		// imgNavHeaderBg.setBackgroundColor(Color.parseColor("#d32f2f"));

		// Loading profile image
		/*
		 * Glide.with(this).load(R.drawable.daiichilife).thumbnail(0.5f).apply(
		 * RequestOptions.circleCropTransform()) .listener(new
		 * RequestListener<Drawable>() {
		 * 
		 * @Override public boolean onLoadFailed(GlideException arg0, Object
		 * arg1, Target<Drawable> arg2, boolean arg3) { //
		 * progress.setVisibility(View.GONE); myLog.E("Load Image Failed!");
		 * return false; }
		 * 
		 * @Override public boolean onResourceReady(Drawable arg0, Object arg1,
		 * Target<Drawable> arg2, DataSource arg3, boolean arg4) { //
		 * progress.setVisibility(View.GONE); myLog.E("Load Image Ready!");
		 * return false; } }).into(imgProfile);
		 */

		imgProfile.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (CustomPref.haveLogin(getBaseContext())) {
					Intent intent = new Intent(getBaseContext(), ProfileActivity.class);
					startActivity(intent);
				}
			}
		});

		// showing dot next to notifications label
		navigationView.getMenu().getItem(9).setActionView(R.layout.menu_dot);
	}

	/***
	 * Returns respected fragment that user selected from navigation menu
	 */
	private void loadHomeFragment() {
		// selecting appropriate nav menu item
		selectNavMenu();

		// set toolbar title
		setToolbarTitle();

		getSupportActionBar().setHomeAsUpIndicator(ResourcesCompat.getDrawable(getResources(),
				R.drawable.ico_drawer_daiichi_menu2, HomeActivity.this.getTheme()));
//		getSupportActionBar().setLogo(ResourcesCompat.getDrawable(getResources(),
//				R.drawable.ic_drawer_daiichi, HomeActivity.this.getTheme()));
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

		// if user select the current navigation menu again, don't do anything
		// just close the navigation drawer
		if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
			drawer.closeDrawers();

			// show or hide the fab button
			toggleFab();
			return;
		}

		// Sometimes, when fragment has huge data, screen seems hanging
		// when switching between navigation menus
		// So using runnable, the fragment is loaded with cross fade effect
		// This effect can be seen in GMail app
		Runnable mPendingRunnable = new Runnable() {
			@Override
			public void run() {
				myLog.E(TAG, "loadHomeFragment mPendingRunnable");
				// update the main content by replacing fragments
				Fragment fragment = getHomeFragment();
				if (fragment != null) {
					FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
					fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
					fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
					fragmentTransaction.commitAllowingStateLoss();
				}
			}
		};

		// If mPendingRunnable is not null, then add to the message queue
		if (mPendingRunnable != null) {
			mHandler.post(mPendingRunnable);
		}

		// show or hide the fab button
		toggleFab();

		// Closing drawer on item click
		drawer.closeDrawers();

		// refresh toolbar menu
		invalidateOptionsMenu();
	}

	private Fragment getHomeFragment() {
		myLog.E(TAG, "getHomeFragment navItemIndex = " + navItemIndex);
		switch (navItemIndex) {
		case 0:
			// home
			HomeFragment homeFragment = new HomeFragment();
			return homeFragment;
		case 1:
			InfoGeneralFragment infoGeneralFragment = new InfoGeneralFragment();
			return infoGeneralFragment;
		case 2:
			ProductInfoFragment productFragment = new ProductInfoFragment();
			return productFragment;
		case 3:
			InfoContractFragment infoContractFragment = new InfoContractFragment();
			return infoContractFragment;
		case 4:
			BonusProgramFragment bonusFragment = new BonusProgramFragment();
			return bonusFragment;
		case 5:
			FundUnitPriceFragment fundFragment = new FundUnitPriceFragment();
			return fundFragment;
		case 6:
			PaymentPolicyFragment paymentFragment = new PaymentPolicyFragment();
			return paymentFragment;

		case 7:
			// Hóa đơn điện tử
			ElectricBillFragment billFragment = new ElectricBillFragment();
			return billFragment;

		case 8:
			// Mạng lưới văn phòng
			// ListOfficeActivity listOffice = new ListOfficeActivity();
			return null;

		case 9:
			// notifications fragment
			NotificationsFragment notificationsFragment = new NotificationsFragment();
			return notificationsFragment;

		case 10:
			// settings fragment
			SettingsFragment settingsFragment = new SettingsFragment();
			return settingsFragment;
		case 11:
			// Logout
			MyCustomDialog.Builder builder = new MyCustomDialog.Builder(HomeActivity.this);
			builder.setMessage(getString(R.string.message_alert_logout))
					.setPositiveButton("Có", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							CustomPref.clearUserLogin(HomeActivity.this);
							CustomPref.setLogin(getBaseContext(), false);
							Intent intent = new Intent(getBaseContext(), HomeActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
							startActivity(intent);
							finish();
						}
					}).setNegativeButton("Không", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			builder.create().show();

			return null;

		default:
			return new HomeFragment();
		}
	}

	private void setToolbarTitle() {
		getSupportActionBar().setTitle(activityTitles[navItemIndex]);
	}

	private void selectNavMenu() {
		navigationView.getMenu().getItem(navItemIndex).setChecked(true);
	}

	private void setUpNavigationView() {
		// Setting Navigation View Item Selected Listener to handle the item
		// click of the navigation menu
		navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

			// This method will trigger on item Click of navigation menu
			@Override
			public boolean onNavigationItemSelected(MenuItem menuItem) {

				// Check to see which item was being clicked and perform
				// appropriate action
				switch (menuItem.getItemId()) {
				// Replacing the main content with ContentFragment Which is our
				// Inbox View;
				case R.id.home:
					navItemIndex = 0;
					CURRENT_TAG = TAG_HOME;
					break;
				case R.id.nav_infoGeneral:
					navItemIndex = 1;
					CURRENT_TAG = TAG_TTCHUNG;
					break;
				case R.id.nav_infoProduct:
					navItemIndex = 2;
					CURRENT_TAG = TAG_TTSANPHAM;
					break;
				case R.id.nav_infoContract:

					if (CustomPref.haveLogin(HomeActivity.this)) {
						navItemIndex = 3;
						CURRENT_TAG = TAG_TTHOPDONG;
						break;
					} else {
						MyCustomDialog.Builder builder = new MyCustomDialog.Builder(HomeActivity.this);
						builder.setMessage(getString(R.string.message_alert_login_to_using_function))
								.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										dialog.dismiss();
									}
								});
						builder.create().show();
						return false;
					}

				case R.id.nav_bonusProgram:

					if (CustomPref.haveLogin(HomeActivity.this)) {
						navItemIndex = 4;
						CURRENT_TAG = TAG_CTDIEMTHUONG;
						break;
					} else {
						MyCustomDialog.Builder builder = new MyCustomDialog.Builder(HomeActivity.this);
						builder.setMessage(getString(R.string.message_alert_login_to_using_function))
								.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										dialog.dismiss();
									}
								});
						builder.create().show();
						return false;
					}
				case R.id.nav_fundPrice:
					navItemIndex = 5;
					CURRENT_TAG = TAG_GIA_DV_QUY;
					break;
				case R.id.nav_payment:
					if (CustomPref.haveLogin(HomeActivity.this)) {
						navItemIndex = 6;
						CURRENT_TAG = TAG_TT_TRUCTUYEN;
						break;
					} else {
						MyCustomDialog.Builder builder = new MyCustomDialog.Builder(HomeActivity.this);
						builder.setMessage(getString(R.string.message_alert_login_to_using_function))
								.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										dialog.dismiss();
									}
								});
						builder.create().show();
						return false;
					}

				case R.id.nav_electricBill:
					if (CustomPref.haveLogin(HomeActivity.this)) {
						navItemIndex = 7;
						CURRENT_TAG = TAG_HOADONDIENTU;
						break;
					} else {
						MyCustomDialog.Builder builder = new MyCustomDialog.Builder(HomeActivity.this);
						builder.setMessage(getString(R.string.message_alert_login_to_using_function))
								.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										dialog.dismiss();
									}
								});
						builder.create().show();
						return false;
					}

				case R.id.nav_listOffice:
					navItemIndex = 8;
					CURRENT_TAG = TAG_MANGLUOI_VP;

					startActivity(new Intent(HomeActivity.this, ListOfficeActivity.class));
					drawer.closeDrawers();
					return true;

				case R.id.nav_notifications:
					navItemIndex = 9;
					CURRENT_TAG = TAG_NOTIFICATIONS;
					break;
				case R.id.nav_settings:
					navItemIndex = 10;
					CURRENT_TAG = TAG_SETTINGS;
					break;
				case R.id.nav_logout:
					navItemIndex = 11;
					CURRENT_TAG = TAG_LOGOUT;
					break;
				case R.id.nav_about_us:
					// launch new intent instead of loading fragment
					startActivity(new Intent(HomeActivity.this, AboutUsActivity.class));
					drawer.closeDrawers();
					return true;
				case R.id.nav_privacy_policy:
					// launch new intent instead of loading fragment
					startActivity(new Intent(HomeActivity.this, PrivacyPolicyActivity.class));
					drawer.closeDrawers();
					return true;
				default:
					navItemIndex = 0;
					CURRENT_TAG = TAG_HOME;
				}

				// Checking if the item is in checked state or not, if not make
				// it in checked state
				if (menuItem.isChecked()) {
					menuItem.setChecked(false);
				} else {
					menuItem.setChecked(true);
				}
				menuItem.setChecked(true);

				loadHomeFragment();

				return true;
			}
		});

		ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar,
				R.string.openDrawer, R.string.closeDrawer) {

			@Override
			public void onDrawerClosed(View drawerView) {
				// Code here will be triggered once the drawer closes as we dont
				// want anything to happen so we leave this blank
				super.onDrawerClosed(drawerView);
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				// Code here will be triggered once the drawer open as we dont
				// want anything to happen so we leave this blank
				super.onDrawerOpened(drawerView);
			}
		};

		// actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
		// actionBarDrawerToggle.setHomeAsUpIndicator(ResourcesCompat.getDrawable(getResources(),
		// R.drawable.ic_drawer_daiichi,
		// HomeActivity.this.getTheme()));

		// Setting the actionbarToggle to drawer layout
		drawer.setDrawerListener(actionBarDrawerToggle);

		// calling sync state is necessary or else your hamburger icon wont show
		// up
		actionBarDrawerToggle.syncState();
	}

	@Override
	public void onBackPressed() {
		if (drawer.isDrawerOpen(GravityCompat.START)) {
			drawer.closeDrawers();
			return;
		}

		// This code loads home fragment when back key is pressed
		// when user is in other fragment than home
		if (shouldLoadHomeFragOnBackPress) {
			// checking if user is on other navigation menu
			// rather than home
//			if (getFragmentManager().getBackStackEntryCount() > 0) {
//				getFragmentManager().popBackStackImmediate();
//				return;
//			} else {
				if (navItemIndex != 0) {
					navItemIndex = 0;
					CURRENT_TAG = TAG_HOME;
					loadHomeFragment();
					return;
				}
//			}
		}

		super.onBackPressed();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.

		// show menu only when home fragment is selected
		if (navItemIndex == 0) {
			getMenuInflater().inflate(R.menu.main, menu);
		}

		// when fragment is notifications, load the menu created for
		// notifications
		if (navItemIndex == 9) {
			getMenuInflater().inflate(R.menu.notifications, menu);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		// noinspection SimplifiableIfStatement
		if (id == R.id.action_call) {
			// Toast.makeText(getApplicationContext(), "Call user!",
			// Toast.LENGTH_LONG).show();
			startActivity(new Intent(this, ContactActivity.class));
			return true;
		}

		// user is in notifications fragment
		// and selected 'Mark all as Read'
		if (id == R.id.action_mark_all_read) {
			Toast.makeText(getApplicationContext(), "All notifications marked as read!", Toast.LENGTH_LONG).show();
		}

		// user is in notifications fragment
		// and selected 'Clear All'
		if (id == R.id.action_clear_notifications) {
			Toast.makeText(getApplicationContext(), "Clear all notifications!", Toast.LENGTH_LONG).show();
		}

		return super.onOptionsItemSelected(item);
	}

	// show or hide the fab
	private void toggleFab() {
		if (navItemIndex == 0)
			fab.show();
		else
			fab.hide();
	}

}
