package com.dlvn.mcustomerportal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.adapter.model.ContractDetailModel;
import com.dlvn.mcustomerportal.adapter.model.HomeItemModel;
import com.dlvn.mcustomerportal.utils.DateUtils;
import com.dlvn.mcustomerportal.utils.Utilities;
import com.dlvn.mcustomerportal.view.holder.ExpViewHolder;

import java.util.ArrayList;
import java.util.List;

public class ExpParentLevelAdapter extends BaseExpandableListAdapter {

    private static final String TAG = "Level-PARENT";
    private Context context;

    //list data
    private List<ContractDetailModel> lstData;

    //array child listview
//    ChildLevelExpandableListView arrSecondLevelList[];

    //contructor
    public ExpParentLevelAdapter(Context context) {
        this.context = context;
        this.lstData = new ArrayList<ContractDetailModel>();
//        arrSecondLevelList = new ChildLevelExpandableListView[0];
    }

    public ExpParentLevelAdapter(Context context, List<ContractDetailModel> data) {
        this.context = context;
        this.lstData = data;
//        arrSecondLevelList = new ChildLevelExpandableListView[lstData.size()];
    }

    @Override
    public Object getChild(int arg0, int arg1) {
        return lstData.get(arg0).getLstDetail().get(arg1);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
                             ViewGroup parent) {

        HomeItemModel item = lstData.get(groupPosition).getLstValue().get(childPosition);
        ExpViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_exp_child, null);

            holder = new ExpViewHolder();

            holder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            holder.tvContent = (TextView) convertView.findViewById(R.id.tvValue);
//            // icon
//            holder.imvIcon = (ImageView) convertView.findViewById(R.id.imvIconExpand);
            convertView.setTag(holder);
        } else
            holder = (ExpViewHolder) convertView.getTag();

        /**
         * Chỉnh sửa format dữ liệu từ server
         */
        if (item != null) {
            if (item.get_title() != null)
                holder.tvTitle.setText(item.get_title());
            if (item.get_content() != null) {

                //Chỉnh sửa ngày tháng
                if (item.get_title().equals("Ngày sinh") || item.get_title().equals("Ngày đáo hạn") || item.get_title().equals("Ngày bắt đầu quyền lợi bảo hiểm")
                        || item.get_title().equals("Ngày bắt đầu hiệu lực hợp đồng")) {
                    holder.tvContent.setText(DateUtils.parseDateForMCP(item.get_content().trim()));
                }

                //Chỉnh sửa giới tính
                else if (item.get_title().equals("Giới tính")) {
                    if (item.get_content().equals("F"))
                        holder.tvContent.setText("Nữ");
                    else
                        holder.tvContent.setText("Nam");
                }

                //Chỉnh sửa format của tiền tệ
                else if (item.get_title().equals("Số tiền bảo hiểm") || item.get_title().equals("Phí bảo hiểm cơ bản định kỳ") || item.get_title().equals("Phí bảo hiểm dụ tính đóng")
                        || item.get_title().equals("Giá trị tài khoản hợp đồng") || item.get_title().equals("Tổng phí bảo hiểm đã được phân bổ") || item.get_title().equals("Quyền lợi nhận lãi từ kết qủa đầu tư của Quỹ liên kết chung")
                        || item.get_title().equals("Quyền lợi thưởng duy trì hợp đồng") || item.get_title().equals("Các khoản chi phí bảo hiểm rủi ro và quản lý hợp đồng đã khấu trừ")
                        || item.get_title().equals("Các khoản tiền đã rút từ Giá trị tài khoản hợp đồng") || item.get_title().equals("Tổng các khoản tạm ứng từ giá trị hoàn lại và chi phí tạm ứng")
                        || item.get_title().equals("Phí bảo hiểm định kỳ") || item.get_title().equals("Phí bảo hiểm dự tính đóng") || item.get_title().equals("Tổng phí bảo hiểm đã đóng")
                        || item.get_title().equals("Phí bảo hiểm cơ bản") || item.get_title().equals("Phí bảo hiểm đóng thêm") || item.get_title().equals("Chi phí rút một phần giá trị tài khoản hợp đồng")) {
                    holder.tvContent.setText(Utilities.formatMoneyToVND(item.get_content().trim()));
                }

                //Thêm dấu % cho tỉ lệ
                else if (item.get_title().equals("Tỉ lệ")) {
                    holder.tvContent.setText(item.get_content().trim() + "%");
                } else holder.tvContent.setText(item.get_content().trim());
            }
        }

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return lstData.get(groupPosition).getLstValue().size();

        /* return 1 để khỏi bị lặp Item con level 2 */
//        return 1;
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
        ContractDetailModel item = lstData.get(groupPosition);
        ExpViewHolder prHolder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_exp_parent, null);

            prHolder = new ExpViewHolder();
            // name
            prHolder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
//            // value
//            prHolder.value = (TextView) convertView.findViewById(R.id.tvValue);
//            // icon
            prHolder.imvIcon = (ImageView) convertView.findViewById(R.id.imvIconExpand);
            convertView.setTag(prHolder);
        } else
            prHolder = (ExpViewHolder) convertView.getTag();

        //fill data
        if (item.getTitle() != null) {
            prHolder.tvTitle.setText(item.getTitle());
//            prHolder.value.setText(item.getValue());
        }

        //if flag expand true ->set icon expand list
        if (isExpanded)
            prHolder.imvIcon.setImageResource(R.drawable.ico_arrow_down);
        else
            prHolder.imvIcon.setImageResource(R.drawable.ico_arrow_right_grey);

//        ((ExpandableListView) parent).expandGroup(groupPosition, true);
//
//        if (item.getLstItem().size() > 0)
//            prHolder.imvIcon.setVisibility(View.VISIBLE);
//        else
//            prHolder.imvIcon.setVisibility(View.INVISIBLE);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        // TODO Auto-generated method stub
        super.onGroupExpanded(groupPosition);
    }

    public void reFreshData(List<ContractDetailModel> data) {
        if (lstData != data) {
            lstData.clear();
            lstData.addAll(data);
            notifyDataSetChanged();
        }
    }
}
