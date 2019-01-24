package com.dlvn.mcustomerportal.adapter.listener;

import com.dlvn.mcustomerportal.adapter.model.HorizontalRecyclerItemModel;
import java.util.List;

public interface OnRecycleModelChangedListener {
	void onNoteListChanged(List<HorizontalRecyclerItemModel> customers);
}

