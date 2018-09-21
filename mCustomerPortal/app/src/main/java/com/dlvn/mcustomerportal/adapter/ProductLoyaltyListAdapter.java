package com.dlvn.mcustomerportal.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.services.model.response.CPPolicy;
import com.dlvn.mcustomerportal.services.model.response.MasterData_Category;
import com.dlvn.mcustomerportal.services.model.response.ProductLoyaltyModel;
import com.dlvn.mcustomerportal.utils.Utilities;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Adapter danh sách hợp đồng
 *
 * @author nn.tai
 * @date Dec 7, 2017
 */
public class ProductLoyaltyListAdapter extends BaseAdapter {

    List<ProductLoyaltyModel> lstData;
    Context context;

    public ProductLoyaltyListAdapter(Context c, List<ProductLoyaltyModel> data) {
        context = c;
        lstData = data;
    }

    @Override
    public int getCount() {
        return lstData.size();
    }

    @Override
    public Object getItem(int position) {
        return lstData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setData(List<ProductLoyaltyModel> data) {
        if (lstData != data)
            lstData.addAll(data);
        notifyDataSetChanged();
    }

    public void reFreshData(List<ProductLoyaltyModel> data) {
        if (lstData != data) {
            lstData.clear();
            lstData.addAll(data);
            notifyDataSetChanged();
        }
    }

    public List<ProductLoyaltyModel> getData() {
        return lstData;
    }

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ProductLoyaltyModel item = lstData.get(position);
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_product_loyalty, null);
            holder = new ViewHolder();

            holder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            holder.tvDescription = (TextView) convertView.findViewById(R.id.tvDescription);
            holder.imvProduct = (ImageView) convertView.findViewById(R.id.imvImage);

            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        if (item != null) {
            holder.tvTitle.setText(item.getStrDetail());
            holder.tvDescription.setText(Utilities.fromHtml(item.getShortDescription()));
        }

        return convertView;
    }

    private class ViewHolder {
        TextView tvTitle, tvDescription;
        ImageView imvProduct;
    }
}
