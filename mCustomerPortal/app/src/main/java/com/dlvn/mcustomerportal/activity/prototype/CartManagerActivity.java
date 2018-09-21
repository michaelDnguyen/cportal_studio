package com.dlvn.mcustomerportal.activity.prototype;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.adapter.ExpCartGroupAdapter;
import com.dlvn.mcustomerportal.adapter.listener.OnItemCheckedCartListListener;
import com.dlvn.mcustomerportal.base.BaseActivity;
import com.dlvn.mcustomerportal.common.Constant;
import com.dlvn.mcustomerportal.common.CustomPref;
import com.dlvn.mcustomerportal.services.model.request.CartItemModel;
import com.dlvn.mcustomerportal.services.model.request.CartModel;
import com.dlvn.mcustomerportal.services.model.response.MasterData_Category;
import com.dlvn.mcustomerportal.services.model.response.ProductLoyaltyModel;
import com.dlvn.mcustomerportal.utils.Utilities;
import com.dlvn.mcustomerportal.utils.myLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CartManagerActivity extends BaseActivity implements OnItemCheckedCartListListener {

    private static final String TAG = CartManagerActivity.class.getName();

    ExpandableListView lvCategory;
    TextView tvTongTien, tvPoint;
    Button btnMuaHang;

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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(getString(R.string.title_activity_cartmanager));

        getViews();
        initData();
        setListener();
    }

    private void getViews() {
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
            myLog.E(TAG, "myCart is empty");
            myCart = new CartModel();
        }

        if (myCart != null) {

            arCategory = myCart.getLsCategory();
            myLog.E(TAG, "setupReferences: " + arCategory.size());

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

            tvTongTien.setText(Utilities.formatMoneyToVND(amount));
            tvPoint.setText(Utilities.formatMoneyToPoint(amount));

            /**
             * Add data sample to Hashmap
             */
            adapter = new ExpCartGroupAdapter(this, arCategory, amount, false, this);
            lvCategory.setAdapter(adapter);
        }
    }

    private void setListener() {

        btnMuaHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartManagerActivity.this, DeliveryInputActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void OnCheckinItemCart(int groupPos, int childPos, long amount) {
        myLog.E(TAG, "OnCheckinItemCart group = " + groupPos + " - child = " + childPos + " ** amount = " + amount);
        if (amount >= 0) {
            tvTongTien.setText(Utilities.formatMoneyToVND(amount));
            tvPoint.setText(Utilities.formatMoneyToPoint(amount));
        }
    }

    @Override
    public void OnDeleteItemCart(int groupPos, int childPos) {
        myLog.E(TAG, "OnDeleteItemCart group = " + groupPos + " - child = " + childPos);
        saveMyCart(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        myLog.E(TAG, "onOptionsItemSelected");
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
                if (listSave != null)
                    if (listSave.size() > 0) {
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
}
