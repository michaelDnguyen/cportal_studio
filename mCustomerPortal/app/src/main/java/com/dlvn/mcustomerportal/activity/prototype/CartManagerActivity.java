package com.dlvn.mcustomerportal.activity.prototype;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.adapter.ExpCartGroupAdapter;
import com.dlvn.mcustomerportal.adapter.listener.OnItemCheckedCartListListener;
import com.dlvn.mcustomerportal.base.BaseActivity;
import com.dlvn.mcustomerportal.common.Constant;
import com.dlvn.mcustomerportal.common.CustomPref;
import com.dlvn.mcustomerportal.services.model.request.CartItemModel;
import com.dlvn.mcustomerportal.services.model.request.CartModel;
import com.dlvn.mcustomerportal.services.model.response.ProductLoyaltyModel;
import com.dlvn.mcustomerportal.utils.Utilities;
import com.dlvn.mcustomerportal.utils.myLog;
import com.dlvn.mcustomerportal.view.MyCustomDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CartManagerActivity extends BaseActivity implements OnItemCheckedCartListListener {

    private static final String TAG = CartManagerActivity.class.getName();

    LinearLayout lloBack;

    ExpandableListView lvCategory;
    TextView tvTongTien, tvPoint;
    Button btnMuaHang;
    float totalAmount = 0;

    private List<CartItemModel> arCategory;
    private List<ProductLoyaltyModel> arProducts;
    private List<ArrayList<ProductLoyaltyModel>> arProductFinal;

    private List<HashMap<String, String>> parentItems;
    private List<List<HashMap<String, String>>> childItems;
    private ExpCartGroupAdapter adapter;

    CartModel myCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_manager);
        setTitle("");

        getViews();
        initData();
        setListener();
    }

    private void getViews() {
        lloBack = findViewById(R.id.lloBack);

        lvCategory = findViewById(R.id.lvCategory);
        tvTongTien = findViewById(R.id.tvTongTien);
        tvPoint = findViewById(R.id.tvPoint);
        btnMuaHang = findViewById(R.id.btnMuaHang);
    }

    private void initData() {

        arCategory = new ArrayList<>();
        arProducts = new ArrayList<>();
        parentItems = new ArrayList<>();
        childItems = new ArrayList<>();

        myCart = CustomPref.getShoppingCart(this);
        if (myCart == null) {
            myLog.e(TAG, "myCart is empty");
            myCart = new CartModel();
        }

        if (myCart != null) {

            arCategory = myCart.getLsCategory();
            myLog.e(TAG, "setupReferences: " + arCategory.size());

            for (CartItemModel item : arCategory) {
                if (item.getLsItems().size() <= 0)
                    arCategory.remove(item);
            }

            long amount = 0;
            for (CartItemModel item : arCategory) {
                List<ProductLoyaltyModel> ls = item.getLsItems();
                if (ls != null)
                    for (ProductLoyaltyModel mo : ls) {
                        if (mo.getIsChecked().equalsIgnoreCase(Constant.CHECK_BOX_CHECKED_TRUE))
                            amount += mo.getPrice() * mo.getQuantity();
                    }
            }

            totalAmount = amount;
            tvTongTien.setText(Utilities.formatMoneyToVND(amount));
            tvPoint.setText(Utilities.formatMoneyToPoint(amount) + " điểm.");

            /**
             * Add data sample to Hashmap
             */
//            setUpHashMap();
            adapter = new ExpCartGroupAdapter(this, arCategory, amount, false, this);
            lvCategory.setAdapter(adapter);
        }
    }

    /**
     * Sample load listview by Hashmap
     */
    private void setUpHashMap() {
        arProducts = new ArrayList<>();
        parentItems = new ArrayList<>();
        childItems = new ArrayList<>();

        CartItemModel dataItem = new CartItemModel();
        dataItem.setCategory("1");
        dataItem.setCategoryName("Adventure");

        for (int i = 1; i < 3; i++) {

            ProductLoyaltyModel subCategoryItem = new ProductLoyaltyModel();
            subCategoryItem.setCategory(String.valueOf(i));
            subCategoryItem.setIsChecked(Constant.CHECK_BOX_CHECKED_TRUE);
            subCategoryItem.setQuantity(2);
            subCategoryItem.setPrice(10000);
            subCategoryItem.setShortDescription("Adventure: " + i);
            arProducts.add(subCategoryItem);
        }
        dataItem.setLsItems(arProducts);
        arCategory.add(dataItem);

        dataItem = new CartItemModel();
        dataItem.setCategory("2");
        dataItem.setCategoryName("Art");
        arProducts = new ArrayList<>();
        for (int j = 1; j < 3; j++) {

            ProductLoyaltyModel subCategoryItem = new ProductLoyaltyModel();
            subCategoryItem.setCategory(String.valueOf(j));
            subCategoryItem.setIsChecked(Constant.CHECK_BOX_CHECKED_TRUE);
            subCategoryItem.setQuantity(3);
            subCategoryItem.setPrice(20000);
            subCategoryItem.setShortDescription("Art: " + j);
            arProducts.add(subCategoryItem);
        }
        dataItem.setLsItems(arProducts);
        arCategory.add(dataItem);

        dataItem = new CartItemModel();
        dataItem.setCategory("3");
        dataItem.setCategoryName("Cooking");
        arProducts = new ArrayList<>();
        for (int k = 1; k < 3; k++) {

            ProductLoyaltyModel subCategoryItem = new ProductLoyaltyModel();
            subCategoryItem.setCategory(String.valueOf(k));
            subCategoryItem.setIsChecked(Constant.CHECK_BOX_CHECKED_TRUE);
            subCategoryItem.setQuantity(1);
            subCategoryItem.setPrice(30000);
            subCategoryItem.setShortDescription("Cooking: " + k);
            arProducts.add(subCategoryItem);
        }

        dataItem.setLsItems(arProducts);
        arCategory.add(dataItem);


        for (CartItemModel data : arCategory) {
            ArrayList<HashMap<String, String>> childArrayList = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> mapParent = new HashMap<String, String>();

            mapParent.put(Constant.Parameter.CATEGORY_ID, data.getCategory());
            mapParent.put(Constant.Parameter.CATEGORY_NAME, data.getCategoryName());

            int countIsChecked = 0;
            for (ProductLoyaltyModel subCategoryItem : data.getLsItems()) {

                HashMap<String, String> mapChild = new HashMap<String, String>();
                mapChild.put(Constant.Parameter.SUB_ID, subCategoryItem.getProductID());
                mapChild.put(Constant.Parameter.SUB_CATEGORY_NAME, subCategoryItem.getShortDescription());
                mapChild.put(Constant.Parameter.CATEGORY_ID, subCategoryItem.getCategory());
                mapChild.put(Constant.Parameter.IS_CHECKED, subCategoryItem.getIsChecked());
                mapChild.put(Constant.Parameter.CATEGORY_PRICE, String.valueOf(subCategoryItem.getPrice()));
                mapChild.put(Constant.Parameter.CATEGORY_QUANTITY, String.valueOf(subCategoryItem.getQuantity()));

                if (subCategoryItem.getIsChecked().equalsIgnoreCase(Constant.CHECK_BOX_CHECKED_TRUE)) {

                    countIsChecked++;
                }
                childArrayList.add(mapChild);
            }

            if (countIsChecked == data.getLsItems().size()) {
                data.setIsChecked(Constant.CHECK_BOX_CHECKED_TRUE);
            } else {
                data.setIsChecked(Constant.CHECK_BOX_CHECKED_FALSE);
            }

            mapParent.put(Constant.Parameter.IS_CHECKED, data.getIsChecked());
            childItems.add(childArrayList);
            parentItems.add(mapParent);
        }

        Constant.parentItems = parentItems;
        Constant.childItems = childItems;
//        adapter = new ExpCartGroupAdapterBk(this, parentItems, childItems, false,this);
//        lvCategory.setAdapter(adapter);

    }

    private void setListener() {

        btnMuaHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (totalAmount <= CustomPref.getUserPoint(CartManagerActivity.this) * 1000) {
                    Intent intent = new Intent(CartManagerActivity.this, DeliveryInputActivity.class);
                    startActivity(intent);
                } else {
                    MyCustomDialog dialog = new MyCustomDialog.Builder(CartManagerActivity.this)
                            .setTitle(getString(R.string.title_alert))
                            .setMessage(getString(R.string.alert_notenough_for_checkout))
                            .setPositiveButton(getString(R.string.confirm_ok), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).create();
                    dialog.show();
                }
            }
        });

        lloBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void OnCheckinItemCart(int groupPos, int childPos, long amount) {
        myLog.e(TAG, "OnCheckinItemCart group = " + groupPos + " - child = " + childPos + " ** amount = " + amount);
        if (amount >= 0) {
            totalAmount = amount;
            tvTongTien.setText(Utilities.formatMoneyToVND(amount));
            tvPoint.setText(Utilities.formatMoneyToPoint(amount) + " điểm.");
        }
    }

    @Override
    public void OnDeleteItemCart(int groupPos, int childPos, long amount) {
        myLog.e(TAG, "OnDeleteItemCart group = " + groupPos + " - child = " + childPos + " ** amount = " + amount);
        if (amount >= 0) {
            totalAmount = amount;
            tvTongTien.setText(Utilities.formatMoneyToVND(amount));
            tvPoint.setText(Utilities.formatMoneyToPoint(amount) + " điểm.");
        }
        saveMyCart(this);
    }

    @Override
    public void OnItemCartChange(int groupPos, int childPos, long amount) {
        myLog.e(TAG, "OnItemCartChange group = " + groupPos + " - child = " + childPos + " ** amount = " + amount);
        if (amount >= 0) {
            totalAmount = amount;
            tvTongTien.setText(Utilities.formatMoneyToVND(amount));
            tvPoint.setText(Utilities.formatMoneyToPoint(amount) + " điểm.");
        }
        saveMyCart(this);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        myLog.e(TAG, "onOptionsItemSelected");
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        saveMyCart(this);
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void saveMyCart(final Context context) {

        new AsyncTask<Void, Void, Void>() {

            private ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(context);
                progressDialog.setTitle("Đang lưu...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
            }

            @Override
            protected Void doInBackground(Void... voids) {

                //Save change in my Cart
                //get list cart from adapter
                List<CartItemModel> listSave = null;
                if (adapter != null)
                    listSave = adapter.getLsData();

                //save my Cart
                if (listSave != null) {
                    myCart.setLsCategory(listSave);
                    CustomPref.saveShoppingCart(context, myCart);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (progressDialog != null)
                    progressDialog.dismiss();
            }
        }.execute();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        myLog.e("dispatchTouchEvent");
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
