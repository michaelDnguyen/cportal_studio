package com.dlvn.mcustomerportal.adapter;

import java.util.List;

import com.dlvn.mcustomerportal.activity.ContractDetailActivity;
import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.activity.WebTutorialActivity;
import com.dlvn.mcustomerportal.adapter.model.ContractModel;
import com.dlvn.mcustomerportal.adapter.model.HomePageItemModel;
import com.dlvn.mcustomerportal.common.Constant;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class HomePagerAdapter extends PagerAdapter {

    List<HomePageItemModel> data;
    Context context;
    LayoutInflater inflater;

    public HomePagerAdapter(Context c, List<HomePageItemModel> lst) {
        super();
        context = c;
        data = lst;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // get views
        View v = inflater.inflate(R.layout.item_page_product, null);
        final HomePageItemModel item = data.get(position);
        ViewHolder holder = new ViewHolder();

        holder.tvMaHopDong = (TextView) v.findViewById(R.id.tvMaHopDong);
        holder.tvTenSanPham = (TextView) v.findViewById(R.id.tvTenSanPham);
        holder.tvTongPhiDaDong = (TextView) v.findViewById(R.id.tvTongPhiDaDong);
        holder.tvPhiCoBan = (TextView) v.findViewById(R.id.tvPhiCoBan);
        holder.tvNgayDongPhi = (TextView) v.findViewById(R.id.tvNgayDongPhi);
        holder.tvNgayGiaHan = (TextView) v.findViewById(R.id.tvNgayGiaHan);
        holder.tvSoNgayConLai = (TextView) v.findViewById(R.id.tvNgayConLai);
        holder.btnChiTiet = (Button) v.findViewById(R.id.btnChiTiet);

        // fill data
        if (item != null) {
            holder.tvMaHopDong.setText(item.getMaHopDong());
            holder.tvTenSanPham.setText(item.getTenSanPham());
            holder.tvTongPhiDaDong.setText(item.getTongPhiDaDong());
            holder.tvPhiCoBan.setText(item.getPhiCoBan());
            holder.tvNgayDongPhi.setText("Ngày thu phí " + item.getNgayDongPhi());
            holder.tvNgayGiaHan.setText(item.getNgayGiaHan());
            holder.tvSoNgayConLai.setText("Còn " + item.getNgayConlai() + " ngày");

            ((ViewGroup)holder.tvTenSanPham.getParent()).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, WebTutorialActivity.class);
                    intent.putExtra(Constant.INT_KEY_WEB_URL, Constant.CONST_URL_PRODUCT);
                    intent.putExtra(Constant.INT_KEY_WEB_URL_TITLE, "Sản phẩm");
                    context.startActivity(intent);
                }
            });

            holder.btnChiTiet.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                }
            });
        }

        container.addView(v);
        return v;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        // TODO Auto-generated method stub
        super.finishUpdate(container);
    }

    @Override
    public void startUpdate(ViewGroup container) {
        // TODO Auto-generated method stub
        super.startUpdate(container);
    }

    private class ViewHolder {
        TextView tvMaHopDong, tvTenSanPham, tvTongPhiDaDong, tvPhiCoBan, tvNgayDongPhi, tvNgayGiaHan, tvSoNgayConLai;
        Button btnChiTiet;
    }
}
