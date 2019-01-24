package com.dlvn.mcustomerportal.view.indicator;

import java.lang.reflect.Field;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.dlvn.mcustomerportal.utils.myLog;

public class ScrollerViewPager extends ViewPager {

    private static final String TAG = ScrollerViewPager.class.getSimpleName();

    private int duration = 1000;
    View mCurrentView;

    public ScrollerViewPager(Context context) {
        super(context);
    }

    public ScrollerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void fixScrollSpeed() {
        fixScrollSpeed(duration);
    }

    public void fixScrollSpeed(int duration) {
        this.duration = duration;
        setScrollSpeedUsingRefection(duration);
    }

    private void setScrollSpeedUsingRefection(int duration) {
        try {
            Field localField = ViewPager.class.getDeclaredField("mScroller");
            localField.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(getContext(), new DecelerateInterpolator(1.5F));
            scroller.setDuration(duration);
            localField.set(this, scroller);
            return;
        } catch (IllegalAccessException localIllegalAccessException) {
        } catch (IllegalArgumentException localIllegalArgumentException) {
        } catch (NoSuchFieldException localNoSuchFieldException) {
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "onInterceptTouchEvent in IllegalArgumentException");
            return false;
        }
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mCurrentView == null) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        int height = 0, width = 0;
        mCurrentView.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        int h = mCurrentView.getMeasuredHeight();
//        int w = mCurrentView.getMeasuredHeight();
        if (h > height) height = h;
//        if (w != width) width = w;
        myLog.e(TAG,"heightMeasure = " + height);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
//        widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void measureCurrentView(View currentView) {
        mCurrentView = currentView;
        requestLayout();
    }

    public int measureFragment(View view) {
        if (view == null)
            return 0;

        view.measure(0, 0);
        return view.getMeasuredHeight();
    }
}
