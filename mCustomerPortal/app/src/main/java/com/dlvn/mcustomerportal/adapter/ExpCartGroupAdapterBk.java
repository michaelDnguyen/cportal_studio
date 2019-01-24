package com.dlvn.mcustomerportal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.adapter.listener.OnItemCheckedCartListListener;
import com.dlvn.mcustomerportal.common.Constant;

import java.util.HashMap;
import java.util.List;

public class ExpCartGroupAdapterBk extends BaseExpandableListAdapter {

    private final List<List<HashMap<String, String>>> childItems;
    private List<HashMap<String, String>> parentItems;
    //    private final ArrayList<HashMap<String, String>> childItems;
    private LayoutInflater inflater;
    private Context context;

    private HashMap<String, String> child;
    private int count = 0;
    private boolean isFromMyCategoriesFragment;
    private long amount = 0;

    private OnItemCheckedCartListListener listener;

    public ExpCartGroupAdapterBk(Context c, List<HashMap<String, String>> parentItems,
                                 List<List<HashMap<String, String>>> childItems, boolean isFromMyCategoriesFragment, OnItemCheckedCartListListener ln) {

        this.parentItems = parentItems;
        this.childItems = childItems;
        this.context = c;
        this.isFromMyCategoriesFragment = isFromMyCategoriesFragment;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.listener = ln;
    }

    @Override
    public int getGroupCount() {
        return parentItems.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return (childItems.get(groupPosition)).size();
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
        if (parentItems.get(groupPosition).get(Constant.Parameter.IS_CHECKED).equalsIgnoreCase(Constant.CHECK_BOX_CHECKED_TRUE)) {
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
                    parentItems.get(groupPosition).put(Constant.Parameter.IS_CHECKED, Constant.CHECK_BOX_CHECKED_TRUE);

                    for (int i = 0; i < childItems.get(groupPosition).size(); i++) {
                        childItems.get(groupPosition).get(i).put(Constant.Parameter.IS_CHECKED, Constant.CHECK_BOX_CHECKED_TRUE);
                    }
                    notifyDataSetChanged();

                } else {
                    parentItems.get(groupPosition).put(Constant.Parameter.IS_CHECKED, Constant.CHECK_BOX_CHECKED_FALSE);
                    for (int i = 0; i < childItems.get(groupPosition).size(); i++) {
                        childItems.get(groupPosition).get(i).put(Constant.Parameter.IS_CHECKED, Constant.CHECK_BOX_CHECKED_FALSE);
                    }
                    notifyDataSetChanged();
                }
            }
        });

        Constant.childItems = childItems;
        Constant.parentItems = parentItems;

        viewHolderParent.tvMainCategoryName.setText(parentItems.get(groupPosition).get(Constant.Parameter.CATEGORY_NAME));

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, final boolean b, View convertView, ViewGroup viewGroup) {

        final ViewHolderChild viewHolderChild;
        child = childItems.get(groupPosition).get(childPosition);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_exp_cart_child, null);
            viewHolderChild = new ViewHolderChild();

            viewHolderChild.tvSubCategoryName = convertView.findViewById(R.id.tvSubCategoryName);
            viewHolderChild.edtQuantity = convertView.findViewById(R.id.edtQuantity);
            viewHolderChild.cbSubCategory = convertView.findViewById(R.id.cbSubCategory);
            viewHolderChild.ibtnAdd = convertView.findViewById(R.id.ibtnAdd);

            viewHolderChild.ibtnRemove = convertView.findViewById(R.id.ibtnRemove);
            viewHolderChild.viewDivider = convertView.findViewById(R.id.viewDivider);

            convertView.setTag(viewHolderChild);
        } else {
            viewHolderChild = (ViewHolderChild) convertView.getTag();
        }

        if (childItems.get(groupPosition).get(childPosition).get(Constant.Parameter.IS_CHECKED).equalsIgnoreCase(Constant.CHECK_BOX_CHECKED_TRUE)) {
            viewHolderChild.cbSubCategory.setChecked(true);
            notifyDataSetChanged();
        } else {
            viewHolderChild.cbSubCategory.setChecked(false);
            notifyDataSetChanged();
        }

        viewHolderChild.tvSubCategoryName.setText(child.get(Constant.Parameter.SUB_CATEGORY_NAME));
        viewHolderChild.edtQuantity.setText(child.get(Constant.Parameter.CATEGORY_QUANTITY));

        viewHolderChild.cbSubCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (viewHolderChild.cbSubCategory.isChecked()) {
                    count = 0;
                    childItems.get(groupPosition).get(childPosition).put(Constant.Parameter.IS_CHECKED, Constant.CHECK_BOX_CHECKED_TRUE);
                    notifyDataSetChanged();
                } else {
                    count = 0;
                    childItems.get(groupPosition).get(childPosition).put(Constant.Parameter.IS_CHECKED, Constant.CHECK_BOX_CHECKED_FALSE);
                    notifyDataSetChanged();
                }

                for (int i = 0; i < childItems.get(groupPosition).size(); i++) {
                    if (childItems.get(groupPosition).get(i).get(Constant.Parameter.IS_CHECKED).equalsIgnoreCase(Constant.CHECK_BOX_CHECKED_TRUE)) {
                        count++;
                    }
                }
                if (count == childItems.get(groupPosition).size()) {
                    parentItems.get(groupPosition).put(Constant.Parameter.IS_CHECKED, Constant.CHECK_BOX_CHECKED_TRUE);
                    notifyDataSetChanged();
                } else {
                    parentItems.get(groupPosition).put(Constant.Parameter.IS_CHECKED, Constant.CHECK_BOX_CHECKED_FALSE);
                    notifyDataSetChanged();
                }

                Constant.childItems = childItems;
                Constant.parentItems = parentItems;
            }
        });

        viewHolderChild.ibtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = Integer.parseInt(child.get(Constant.Parameter.CATEGORY_QUANTITY));
                if (quantity > 0)
                    quantity += 1;
                viewHolderChild.edtQuantity.setText(String.valueOf(quantity));

                child.put(Constant.Parameter.CATEGORY_QUANTITY,String.valueOf(quantity));

                amount += Integer.parseInt(child.get(Constant.Parameter.CATEGORY_PRICE));
                listener.OnItemCartChange(groupPosition,childPosition,amount);
                notifyDataSetChanged();
            }
        });

        viewHolderChild.ibtnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = Integer.parseInt(child.get(Constant.Parameter.CATEGORY_QUANTITY));
                if (quantity > 1) {
                    quantity -= 1;
                    viewHolderChild.edtQuantity.setText(String.valueOf(quantity));

                    child.put(Constant.Parameter.CATEGORY_QUANTITY,String.valueOf(quantity));
                    amount -= Integer.parseInt(child.get(Constant.Parameter.CATEGORY_PRICE));
                    listener.OnItemCartChange(groupPosition,childPosition,amount);
                } else {

                    childItems.get(groupPosition).remove(childPosition);
                    listener.OnDeleteItemCart(groupPosition, childPosition, amount);
                }
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

    private class ViewHolderParent {

        TextView tvMainCategoryName;
        CheckBox cbMainCategory;
        ImageView ivCategory;
    }

    private class ViewHolderChild {

        TextView tvSubCategoryName;
        EditText edtQuantity;
        CheckBox cbSubCategory;
        ImageButton ibtnRemove, ibtnAdd;
        View viewDivider;
    }


}