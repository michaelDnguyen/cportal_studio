package com.dlvn.mcustomerportal.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class DividerItemDecoration extends RecyclerView.ItemDecoration {

	private int verticalSpaceHeight = 0;

	private static final int[] ATTRS = new int[] { android.R.attr.listDivider };

	private Drawable divider;
	private Context context;

	public DividerItemDecoration(Context c, int verticalSpaceHeight, int resId) {
		context = c;
		if (verticalSpaceHeight != 0)
			this.verticalSpaceHeight = verticalSpaceHeight;
		if (resId != 0)
			divider = ContextCompat.getDrawable(context, resId);
	}

	public DividerItemDecoration(Context c) {
		context = c;
		final TypedArray styledAttributes = context.obtainStyledAttributes(ATTRS);
		divider = styledAttributes.getDrawable(0);
		styledAttributes.recycle();
	}

	@Override
	public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
		outRect.bottom = verticalSpaceHeight;
	}

	@Override
	public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
		int left = parent.getPaddingLeft();
		int right = parent.getWidth() - parent.getPaddingRight();

		int childCount = parent.getChildCount();
		for (int i = 0; i < childCount; i++) {
			View child = parent.getChildAt(i);

			RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

			int top = child.getBottom() + params.bottomMargin;
			int bottom = top + divider.getIntrinsicHeight();

			divider.setBounds(left, top, right, bottom);
			divider.draw(c);
		}
	}

}
