package com.dlvn.mcustomerportal.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.adapter.listener.OnItemCheckedCartListListener;
import com.dlvn.mcustomerportal.common.Constant;
import com.dlvn.mcustomerportal.services.model.request.CartItemModel;
import com.dlvn.mcustomerportal.services.model.response.ProductLoyaltyModel;
import com.dlvn.mcustomerportal.view.SwipeRevealLayout;
import com.dlvn.mcustomerportal.view.SwipeViewBinderHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpCartGroupAdapter extends BaseExpandableListAdapter {

    //    private final List<List<HashMap<String, String>>> childItems;
//    private List<HashMap<String, String>> parentItems;
    List<CartItemModel> lsData;

    //    private final ArrayList<HashMap<String, String>> childItems;
    private LayoutInflater inflater;
    private final SwipeViewBinderHelper binderHelper;
    private OnItemCheckedCartListListener listener;

    private Context context;
    private ProductLoyaltyModel child;
    private int count = 0;
    private long amount = 0;
    private boolean isFromMyCategoriesFragment;

    public ExpCartGroupAdapter(Context c, List<CartItemModel> lsItems, long amount, boolean isFromMyCategoriesFragment, OnItemCheckedCartListListener ln) {

//        this.parentItems = parentItems;
//        this.childItems = childItems;
        this.lsData = lsItems;
        this.context = c;
        this.isFromMyCategoriesFragment = isFromMyCategoriesFragment;
        this.listener = ln;
        this.amount = amount;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.binderHelper = new SwipeViewBinderHelper();
    }

    @Override
    public int getGroupCount() {
//        return parentItems.size();
        return lsData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        //return (childItems.get(groupPosition)).size();
        return lsData.get(groupPosition).getLsItems().size();
    }

    @Override
    public Object getGroup(int i) {
        return null;
    }

    @Override
    public Object getChild(int i, int i1) {
        return null;
    }

    @Override
    public long getGroupId(int i) {
        return 0;
    }

    @Override
    public long getChildId(int i, int i1) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, final boolean b, View convertView, ViewGroup viewGroup) {
        final ViewHolderParent viewHolderParent;
        if (convertView == null) {

            if (isFromMyCategoriesFragment) {
                convertView = inflater.inflate(R.layout.item_exp_cart_my, null);
            } else {
                convertView = inflater.inflate(R.layout.item_exp_cart_group, null);
            }
            viewHolderParent = new ViewHolderParent();

            viewHolderParent.tvMainCategoryName = convertView.findViewById(R.id.tvMainCategoryName);
            viewHolderParent.cbMainCategory = convertView.findViewById(R.id.cbMainCategory);
            viewHolderParent.ivCategory = convertView.findViewById(R.id.ivCategory);
            convertView.setTag(viewHolderParent);
        } else {
            viewHolderParent = (ViewHolderParent) convertView.getTag();
        }

        //verify state checked
        if (lsData.get(groupPosition).getIsChecked().equalsIgnoreCase(Constant.CHECK_BOX_CHECKED_TRUE)) {
            viewHolderParent.cbMainCategory.setChecked(true);
            notifyDataSetChanged();

        } else {
            viewHolderParent.cbMainCategory.setChecked(false);
            notifyDataSetChanged();
        }

        viewHolderParent.cbMainCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewHolderParent.cbMainCategory.isChecked()) {
                    lsData.get(groupPosition).setIsChecked(Constant.CHECK_BOX_CHECKED_TRUE);

                    for (int i = 0; i < lsData.get(groupPosition).getLsItems().size(); i++) {
                        lsData.get(groupPosition).getLsItems().get(i).setIsChecked(Constant.CHECK_BOX_CHECKED_TRUE);
                        amount += lsData.get(groupPosition).getLsItems().get(i).getPrice() * lsData.get(groupPosition).getLsItems().get(i).getQuantity();
                    }
                    notifyDataSetChanged();

                } else {
                    lsData.get(groupPosition).setIsChecked(Constant.CHECK_BOX_CHECKED_FALSE);
                    for (int i = 0; i < lsData.get(groupPosition).getLsItems().size(); i++) {
                        lsData.get(groupPosition).getLsItems().get(i).setIsChecked(Constant.CHECK_BOX_CHECKED_FALSE);
                        amount -= lsData.get(groupPosition).getLsItems().get(i).getPrice() * lsData.get(groupPosition).getLsItems().get(i).getQuantity();
                    }
                    notifyDataSetChanged();
                }

                listener.OnCheckinItemCart(groupPosition, -1, amount);
            }
        });

//        Constant.childItems = childItems;
//        Constant.parentItems = parentItems;

        viewHolderParent.tvMainCategoryName.setText(lsData.get(groupPosition).getCategoryName());
        ((ExpandableListView) viewGroup).expandGroup(groupPosition, true);

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, final boolean b, View convertView, ViewGroup viewGroup) {

        final ViewHolderChild viewHolderChild;
        child = lsData.get(groupPosition).getLsItems().get(childPosition);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_exp_cart_child, null);
            viewHolderChild = new ViewHolderChild();

            viewHolderChild.tvSubCategoryName = convertView.findViewById(R.id.tvSubCategoryName);
            viewHolderChild.tvQuantity = convertView.findViewById(R.id.tvQuantity);
            viewHolderChild.cbSubCategory = convertView.findViewById(R.id.cbSubCategory);
            viewHolderChild.viewDivider = convertView.findViewById(R.id.viewDivider);

            viewHolderChild.deleteView = convertView.findViewById(R.id.delete_layout);
            viewHolderChild.swipeLayout = convertView.findViewById(R.id.swipe_layout);

            convertView.setTag(viewHolderChild);
        } else {
            viewHolderChild = (ViewHolderChild) convertView.getTag();
        }

        if (lsData.get(groupPosition).getLsItems().get(childPosition).getIsChecked().equalsIgnoreCase(Constant.CHECK_BOX_CHECKED_TRUE)) {
            viewHolderChild.cbSubCategory.setChecked(true);
            notifyDataSetChanged();
        } else {
            viewHolderChild.cbSubCategory.setChecked(false);
            notifyDataSetChanged();
        }

        viewHolderChild.tvSubCategoryName.setText(child.getStrDetail());
        viewHolderChild.tvQuantity.setText("Số lượng: " + child.getQuantity());
        binderHelper.bind(viewHolderChild.swipeLayout, child.getProductID());

        viewHolderChild.cbSubCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewHolderChild.cbSubCategory.isChecked()) {
                    count = 0;
                    lsData.get(groupPosition).getLsItems().get(childPosition).setIsChecked(Constant.CHECK_BOX_CHECKED_TRUE);
                    amount += lsData.get(groupPosition).getLsItems().get(childPosition).getPrice() * lsData.get(groupPosition).getLsItems().get(childPosition).getQuantity();
                    notifyDataSetChanged();
                } else {
                    count = 0;
                    lsData.get(groupPosition).getLsItems().get(childPosition).setIsChecked(Constant.CHECK_BOX_CHECKED_FALSE);
                    amount -= lsData.get(groupPosition).getLsItems().get(childPosition).getPrice() * lsData.get(groupPosition).getLsItems().get(childPosition).getQuantity();
                    notifyDataSetChanged();
                }

                for (int i = 0; i < lsData.get(groupPosition).getLsItems().size(); i++) {
                    if (lsData.get(groupPosition).getLsItems().get(i).getIsChecked().equalsIgnoreCase(Constant.CHECK_BOX_CHECKED_TRUE)) {
                        count++;
                    }
                }
                if (count == lsData.get(groupPosition).getLsItems().size()) {
                    lsData.get(groupPosition).setIsChecked(Constant.CHECK_BOX_CHECKED_TRUE);
                    notifyDataSetChanged();
                } else {
                    lsData.get(groupPosition).setIsChecked(Constant.CHECK_BOX_CHECKED_FALSE);
                    notifyDataSetChanged();
                }

                listener.OnCheckinItemCart(groupPosition, childPosition, amount);
//                Constant.childItems = childItems;
//                Constant.parentItems = parentItems;
            }
        });

        viewHolderChild.deleteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //delete item
                lsData.get(groupPosition).getLsItems().remove(childPosition);
                listener.OnDeleteItemCart(groupPosition, childPosition);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }

    public List<CartItemModel> getLsData() {
        return lsData;
    }

    private class ViewHolderParent {

        TextView tvMainCategoryName;
        CheckBox cbMainCategory;
        ImageView ivCategory;
    }

    private class ViewHolderChild {

        TextView tvSubCategoryName, tvQuantity;
        CheckBox cbSubCategory;
        View viewDivider;
        View deleteView;
        SwipeRevealLayout swipeLayout;
    }
}