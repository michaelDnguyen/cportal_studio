package com.dlvn.mcustomerportal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.adapter.model.SingleChoiceModel;

import java.util.List;

/**
 * adapter single choice with checker icon
 */
public class SingleChoiceListAdapter extends ArrayAdapter<SingleChoiceModel> {

    List<SingleChoiceModel> data;
    Context context;
    LayoutInflater inflater;

    public SingleChoiceListAdapter(Context context, int resource, List<SingleChoiceModel> objects) {
        super(context, resource, objects);
        this.context = context;
        this.data = objects;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public SingleChoiceModel getItem(int position) {
        return data.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        SingleChoiceModel item = data.get(position);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_list_singlechoice, null);
            holder = new ViewHolder();
            holder.tvName = convertView.findViewById(R.id.tvName);
            holder.tvSubName = convertView.findViewById(R.id.tvSubName);
            holder.lloContent = convertView.findViewById(R.id.lloContent);
            holder.imvSelected = convertView.findViewById(R.id.imvSelected);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        if (item != null) {
            holder.tvName.setText(item.getName());
            holder.tvSubName.setText(item.getSubname());

            if (item.isSelected())
                holder.imvSelected.setPressed(true);
            else
                holder.imvSelected.setPressed(false);
        }

        return convertView;
    }

    /**
     * set checker status at position
     * @param position
     */
    public void setSelectedAtPosition(int position) {
        if (position >= 0) {
            if (data.get(position).isSelected())
                data.get(position).setSelected(false);
            else {
                clearSelected();
                data.get(position).setSelected(true);
            }
        }
        notifyDataSetChanged();
    }

    /**
     * get item selected
     * @return
     */
    public SingleChoiceModel getSelectionItem() {
        for (int i = 0; i < data.size(); i++)
            if (data.get(i).isSelected())
                return data.get(i);

        return null;
    }

    public void clearSelected() {
        if (data != null) {
            for (int i = 0; i < data.size(); i++)
                if (data.get(i).isSelected())
                    data.get(i).setSelected(false);
        }
    }

    private class ViewHolder {
        TextView tvName, tvSubName;
        LinearLayout lloContent;
        ImageView imvSelected;
    }

}
