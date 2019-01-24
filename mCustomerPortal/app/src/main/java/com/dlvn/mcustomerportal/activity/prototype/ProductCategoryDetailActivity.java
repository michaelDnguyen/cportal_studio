package com.dlvn.mcustomerportal.activity.prototype;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.ActionMenuView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.base.BaseActivity;
import com.dlvn.mcustomerportal.common.CustomPref;
import com.dlvn.mcustomerportal.services.model.request.CartItemModel;
import com.dlvn.mcustomerportal.services.model.request.CartModel;
import com.dlvn.mcustomerportal.services.model.response.MasterData_Category;
import com.dlvn.mcustomerportal.services.model.response.ProductLoyaltyModel;
import com.dlvn.mcustomerportal.utils.Utilities;
import com.dlvn.mcustomerportal.utils.myLog;
import com.dlvn.mcustomerportal.view.CountDrawable;
import com.dlvn.mcustomerportal.view.MyCustomDialog;

import java.util.ArrayList;
import java.util.List;

public class ProductCategoryDetailActivity extends BaseActivity {
    private static final String TAG = ProductCategoryDetailActivity.class.getName();

    public static final String INT_CATEGORY_PRODUCT_DETAIL = "category_product_detail";

    LinearLayout lloBack;
    ImageView imvProduct;
    TextView tvTitle, tvDescription;
    Button btnMuaHang;
    ActionMenuView toolbar_menu;

    List<ProductLoyaltyModel> lsProduct;
    ProductLoyaltyModel productItem;
    MasterData_Category category;

    CartModel myCart;
    CartItemModel myCartCategory;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_category_detail);

        getViews();
        initData();
        setListener();
    }

    private void getViews() {
        lloBack = findViewById(R.id.lloBack);

        imvProduct = findViewById(R.id.imvProduct);
        tvTitle = findViewById(R.id.tvTitle);
        tvDescription = findViewById(R.id.tvDescription);
        btnMuaHang = findViewById(R.id.btnMuaHang);

        toolbar_menu = findViewById(R.id.toolbar_menu);
        toolbar_menu.setVisibility(View.VISIBLE);
        getMenuInflater().inflate(R.menu.menu_cart, toolbar_menu.getMenu());
        toolbar_menu.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        toolbar_menu.setOnMenuItemClickListener(new ActionMenuView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.ic_group:
                        Intent intent = new Intent(ProductCategoryDetailActivity.this, CartManagerActivity.class);
                        saveMyCart(intent);
                        return true;

                    default:
                        break;
                }
                return false;
            }
        });
    }

    private void initData() {
        if (getIntent().getExtras() != null)
            if (getIntent().getExtras().containsKey(INT_CATEGORY_PRODUCT_DETAIL))
                productItem = getIntent().getParcelableExtra(INT_CATEGORY_PRODUCT_DETAIL);
        if (getIntent().getExtras() != null)
            if (getIntent().getExtras().containsKey(ProductByCategoryActivity.INT_CATEGORY_PRODUCT))
                category = getIntent().getParcelableExtra(ProductByCategoryActivity.INT_CATEGORY_PRODUCT);

        if (productItem != null) {
            setTitle(productItem.getStrDetail());
            productItem.setQuantity(1);
            tvTitle.setText(productItem.getStrDetail());
            tvDescription.setText(Utilities.fromHtml(productItem.getFullDescription()));
        }
    }

    private void setListener() {

        btnMuaHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productItem != null) {

                    if (productItem.getPrice() <= CustomPref.getUserPoint(ProductCategoryDetailActivity.this) * 1000) {
                        if (lsProduct.contains(productItem))
                            productItem.setQuantity(productItem.getQuantity() + 1);
                        else {
                            lsProduct.add(productItem);
                        }
                        count++;
                        if (toolbar_menu != null)
                            setCount(ProductCategoryDetailActivity.this, toolbar_menu.getMenu(), count + "");
//                        invalidateOptionsMenu();
                    } else {
                        MyCustomDialog dialog = new MyCustomDialog.Builder(ProductCategoryDetailActivity.this)
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
    public boolean onCreateOptionsMenu(Menu menu) {
        myLog.e(TAG, "onCreateOptionsMenu");
        getMenuInflater().inflate(R.menu.menu_cart, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        myLog.e(TAG, "onOptionsItemSelected");
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.ic_group:
                //Todo: set action click on cart --> screen checkout
                Intent intent = new Intent(ProductCategoryDetailActivity.this, CartManagerActivity.class);
                saveMyCart(intent);
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        myLog.e(TAG, "onPrepareOptionsMenu");

        setCount(this, menu, count + "");
        myLog.e(TAG, "count = " + count);

        return super.onPrepareOptionsMenu(menu);
    }

    public void setCount(Context context, Menu menu, String count) {
        MenuItem menuItem = menu.findItem(R.id.ic_group);
        LayerDrawable icon = (LayerDrawable) menuItem.getIcon();

        CountDrawable badge;

        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_group_count);
        if (reuse != null && reuse instanceof CountDrawable) {
            badge = (CountDrawable) reuse;
        } else {
            badge = new CountDrawable(context);
        }

        badge.setCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_group_count, badge);
    }

    /**
     * Load myCart from Preferences
     */
    private void loadMyCart() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {

                myCart = CustomPref.getShoppingCart(ProductCategoryDetailActivity.this);

                if (lsProduct != null)
                    lsProduct.clear();
                if (myCartCategory != null)
                    myCartCategory.setLsItems(lsProduct);

                if (myCart != null) {
                    if (myCart.getLsCategory() != null) {

                        //enhance myCart: remove all category empty
                        for (int a = 0; a < myCart.getLsCategory().size(); a++) {
                            if (myCart.getLsCategory().get(a).getLsItems().size() <= 0) {
                                myCart.getLsCategory().remove(a);
                                a--;
                            }
                        }

                        //find cart category same current category product
                        for (int i = 0; i < myCart.getLsCategory().size(); i++)
                            if (myCart.getLsCategory().get(i).getCategory().equals(category.getPRODUCTCATEGORYCD())) {
                                myCartCategory = myCart.getLsCategory().get(i);
                                myLog.e(TAG, "Load myCart container myCategory");
                                break;
                            }
                    }
                }

                if (myCartCategory != null) {
                    if (myCartCategory.getLsItems() != null) {
                        lsProduct = myCartCategory.getLsItems();
                        myLog.e(TAG, "Load mycategory container list product");
                    }
                } else {
                    myLog.e(TAG, "Load mycategory is null");
                    myCartCategory = new CartItemModel();
                    myCartCategory.setCategory(category.getPRODUCTCATEGORYCD());
                    myCartCategory.setCategoryName(category.getProductTitle());
                }

                if (lsProduct == null) {
                    lsProduct = new ArrayList<>();
                    myLog.e(TAG, "Load list product is null");
                }

                if (myCart != null) {
                    count = Utilities.countItemInCart(myCart.getLsCategory());
                    myLog.e(TAG, "count = " + count);
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (myCart != null) {
//                    invalidateOptionsMenu();
                    if (toolbar_menu != null)
                        setCount(ProductCategoryDetailActivity.this, toolbar_menu.getMenu(), count + "");
                }
            }
        }.execute();
    }

    private void saveMyCart(final Intent intent) {

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {

                //save my Cart
                if (lsProduct != null & lsProduct.size() > 0) {
                    if (myCartCategory != null) {
                        myCartCategory.setLsItems(lsProduct);
                    }

                    if (myCart == null)
                        myCart = new CartModel();

                    if (myCart.getLsCategory() != null) {

                        List<CartItemModel> lst = myCart.getLsCategory();

                        //find position of category in myCart for replace
                        int pos = -1;
                        for (int i = 0; i < lst.size(); i++)
                            if (lst.get(i).getCategory().equals(myCartCategory.getCategory())) {
                                pos = i;
                                break;
                            }

                        myLog.e(TAG, "Save myCart  container category at " + pos);
                        if (pos >= 0)
                            lst.set(pos, myCartCategory);
                        else
                            lst.add(myCartCategory);

                        myCart.setLsCategory(lst);

                    } else {
                        List<CartItemModel> ls = new ArrayList<>();
                        ls.add(myCartCategory);
                        myCart.setLsCategory(ls);
                    }

                    CustomPref.saveShoppingCart(ProductCategoryDetailActivity.this, myCart);
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (intent != null)
                    startActivity(intent);
            }
        }.execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        myLog.e(TAG, "onResume");
        loadMyCart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        myLog.e(TAG, "onStop");
        saveMyCart(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myLog.e(TAG, "onDestroy");
    }
}
