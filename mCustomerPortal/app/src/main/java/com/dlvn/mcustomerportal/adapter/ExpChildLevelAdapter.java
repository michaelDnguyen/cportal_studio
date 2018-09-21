package com.dlvn.mcustomerportal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.adapter.model.HomeItemModel;
import com.dlvn.mcustomerportal.view.ChildLevelExpandableListView;
import com.dlvn.mcustomerportal.view.holder.ExpViewHolder;

import java.util.ArrayList;
import java.util.List;

public class ExpChildLevelAdapter extends BaseExpandableListAdapter {

    private static final String TAG = "SECOND LVL";
    private Context context;

    //list data
    private List<HomeItemModel> lstData;
    //array child view
    ChildLevelExpandableListView arrSecondLevelList[];

    //contructor
    public ExpChildLevelAdapter(Context context) {
        this.context = context;
        this.lstData = new ArrayList<HomeItemModel>();
        arrSecondLevelList = new ChildLevelExpandableListView[0];
    }

    public ExpChildLevelAdapter(Context context, List<HomeItemModel> data) {
        this.context = context;
        this.lstData = data;
        arrSecondLevelList = new ChildLevelExpandableListView[data.size()];
    }

    @Override
    public Object getGroup(int groupPosition) {
        return lstData.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return lstData.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        //get data item
        HomeItemModel item = lstData.get(groupPosition);
        ExpViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_exp_child, null);

            holder = new ExpViewHolder();
            // name
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
//            // value
            holder.tvContent = (TextView) convertView.findViewById(R.id.tvValue);
//            // icon
//            holder.imvIcon = (ImageView) convertView.findViewById(R.id.imvIconExpand);
            convertView.setTag(holder);
        } else
            holder = (ExpViewHolder) convertView.getTag();

        if (item != null) {
            holder.tvTitle.setText(item.get_title());
            holder.tvContent.setText(item.get_content());
        }

        // Xét có or ko có icon xổ xuống
//        if (isExpanded)
//            holder.imvIcon.setImageResource(R.drawable.ico_collapse);
//        else
//            holder.imvIcon.setImageResource(R.drawable.ico_expand);
//
//        // xét có con OR ko để tô màu nền
//        if (item.getLstItem().size() > 0) {
//            holder.imvIcon.setVisibility(View.VISIBLE);
//        } else {
//            holder.imvIcon.setVisibility(View.INVISIBLE);
//        }

        return convertView;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
                             ViewGroup parent) {

//        CPPolicyInfo item = lstData.get(groupPosition).getLstObj().get(childPosition);

        ExpViewHolder holder;
//        if (convertView == null) {
//            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            convertView = inflater.inflate(R.layout.row_third, null);
//
//            holder = new ExpViewHolder();
//            // name
//            holder.title = (TextView) convertView.findViewById(R.id.tvTitle);
//            // value
//            holder.value = (TextView) convertView.findViewById(R.id.tvValue);
//            // icon
//            convertView.setTag(holder);
//        } else
//            holder = (PayslipViewHolder) convertView.getTag();
//
//        if (item != null) {
//            holder.title.setText(item.getName());
//            holder.value.setText(item.getValue());
//        }

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
//        return lstData.get(groupPosition).getLstObj().size();
		return 0;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
