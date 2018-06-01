package com.dlvn.mcustomerportal.view;

import android.content.Context;
import android.graphics.PointF;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

public class RecyclerSmoothLayoutManager extends LinearLayoutManager {
	private static final float MILLISECONDS_PER_INCH = 50f;
	private Context mContext;

	public RecyclerSmoothLayoutManager(Context context) {
		super(context);
		mContext = context;
	}

	public RecyclerSmoothLayoutManager(Context context, int orientation, boolean reserveLayout) {
		super(context, orientation, reserveLayout);
		mContext = context;
	}

	public RecyclerSmoothLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		mContext = context;
	}

	@Override
	public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, final int position) {

		LinearSmoothScroller smoothScroller = new LinearSmoothScroller(mContext) {

			// This controls the direction in which smoothScroll looks
			// for your view
			@Override
			public PointF computeScrollVectorForPosition(int targetPosition) {
				return RecyclerSmoothLayoutManager.this.computeScrollVectorForPosition(targetPosition);
			}

			// This returns the milliseconds it takes to
			// scroll one pixel.
			@Override
			protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
				return MILLISECONDS_PER_INCH / displayMetrics.densityDpi;
			}
		};

		smoothScroller.setTargetPosition(position);
		startSmoothScroll(smoothScroller);
	}
}
