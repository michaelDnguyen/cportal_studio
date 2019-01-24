package com.dlvn.mcustomerportal.adapter.listener;

import com.dlvn.mcustomerportal.adapter.HorizontalRecyclerAdapter;
import com.dlvn.mcustomerportal.adapter.model.ClaimListDocModel;
import com.dlvn.mcustomerportal.adapter.model.HorizontalRecyclerItemModel;

import java.util.List;

public interface OnItemHorizontalViewClick {

    void OnItemClaimClick(ClaimListDocModel claimListDocItem, int position, HorizontalRecyclerAdapter adapter);
}
