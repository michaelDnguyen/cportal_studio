package com.dlvn.mcustomerportal.adapter.listener;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.dlvn.mcustomerportal.utils.myLog;

public class ItemTouchHelperCallback extends ItemTouchHelper.Callback {

	private static final String TAG = "ItemTouchHelperCallback";
	private final ItemTouchHelperAdapter mAdapter;

	public static final float ALPHA_FULL = 1.0f;

	public ItemTouchHelperCallback(ItemTouchHelperAdapter adapter) {
		mAdapter = adapter;
	}

	@Override
	public boolean isLongPressDragEnabled() {
		return true;
	}

	@Override
	public boolean isItemViewSwipeEnabled() {
		return false;
	}

	@Override
	public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
		myLog.e(TAG, "getMovementFlags");
		// add LEFT & RIGHT if used GridView Layout
		final int dragFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.UP | ItemTouchHelper.DOWN;

		// change swipeFlags = 0 if used GridView layout
		// final int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
		final int swipeFlags = 0;

		return makeMovementFlags(dragFlags, swipeFlags);
	}

	@Override
	public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
		myLog.e(TAG, "onMove");
		mAdapter.onItemMove(source.getAdapterPosition(), target.getAdapterPosition());
		return true;
	}

	@Override
	public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) {
		myLog.e(TAG, "onSwiped");
		mAdapter.onItemDismiss(viewHolder.getAdapterPosition());
	}

	@Override
	public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY,
			int actionState, boolean isCurrentlyActive) {
		if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
			// Fade out the view as it is swiped out of the parent's bounds
			final float alpha = ALPHA_FULL - Math.abs(dX) / (float) viewHolder.itemView.getWidth();
			viewHolder.itemView.setAlpha(alpha);
			viewHolder.itemView.setTranslationX(dX);
		} else {
			super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
		}
	}

	@Override
	public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
		myLog.e(TAG, "onSelectedChanged");
		if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
			if (viewHolder instanceof ItemTouchHelperViewHolder) {
				ItemTouchHelperViewHolder itemViewHolder = (ItemTouchHelperViewHolder) viewHolder;
				itemViewHolder.onItemSelected();
			}
		}

		super.onSelectedChanged(viewHolder, actionState);
	}

	@Override
	public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
		myLog.e(TAG, "onClearView");
		super.clearView(recyclerView, viewHolder);
		if (viewHolder instanceof ItemTouchHelperViewHolder) {
			ItemTouchHelperViewHolder itemViewHolder = (ItemTouchHelperViewHolder) viewHolder;
			itemViewHolder.onItemClear();
		}
	}

}
