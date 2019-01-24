package com.dlvn.mcustomerportal.adapter.listener;

public interface OnItemCheckedCartListListener {

    public void OnCheckinItemCart(int groupPos, int childPos, long amount);

    public void OnDeleteItemCart(int groupPos, int childPos, long amount);

    public void OnItemCartChange(int groupPos, int childPos, long amount);
}
